package com.example.labourmangement.Contractor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.labourmangement.Adapter.AppliedJobsAdapter;
import com.example.labourmangement.Adapter.JobAdapter;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Labour.JobStatusApproval;
import com.example.labourmangement.R;
import com.example.labourmangement.model.AppliedJobsModel;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AppliedJobs extends AppCompatActivity implements JobAdapter.OnItemClickListener  {
    RecyclerView recyclerViewjobs;
    RecyclerView.LayoutManager layoutManager;
    private static final String TAG = AppliedJobs.class.getSimpleName();
    List<AppliedJobsModel> joblist;
    //ProgressDialog progressDialog;
    CustomLoader loader;
    SessionManagerContractor sessionManagerContractor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_jobs);
       /* getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().setTitle("Posted Jobs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        recyclerViewjobs = (RecyclerView) findViewById(R.id.recycleViewjobs);
        recyclerViewjobs.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerViewjobs.setLayoutManager(layoutManager);

        joblist = new ArrayList<AppliedJobsModel>();
        sessionManagerContractor=new SessionManagerContractor((getApplicationContext()));
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        //progressDialog= new ProgressDialog(AppliedJobs.this);

        sendRequest();
    }


    public void sendRequest() {
        HashMap<String, String> user = sessionManagerContractor.getUserDetails();

        // name
        String name = user.get(SessionManagerContractor.KEY_NAME);

        // email
        String email = user.get(SessionManagerContractor.KEY_EMAIL);
        Log.d(TAG, "Email "+email);
        loader.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GETAPPLIEDJOBS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                loader.dismiss();
                try {
                    //converting the string to json array object
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("Success").equalsIgnoreCase("true"))
                    {
                        JSONArray array=jsonObject.getJSONArray("Jobs");
                        {
                            Log.d(TAG, array.toString());
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject job = array.getJSONObject(i);
                                AppliedJobsModel appliedJobs = new AppliedJobsModel();
                                //adding the product to product list
                                appliedJobs.setJob_area(job.getString("job_area"));
                                appliedJobs.setJob_title(job.getString("job_title"));
                                appliedJobs.setJob_details(job.getString("job_details"));
                                appliedJobs.setJob_wages(job.getString("job_wages"));
                                appliedJobs.setJob_id(job.getString("job_id"));
                                appliedJobs.setApplied_by(job.getString("applied_by"));
                                appliedJobs.setCreated_by(job.getString("created_by"));
                                appliedJobs.setLabor_name(job.getString("labor_name"));
                                appliedJobs.setContractor_name(job.getString("contractor_name"));
                                appliedJobs.setApproved_status(job.getString("approved_status"));
                                appliedJobs.setApplied_date(job.getString("applied_date"));
                                joblist.add(appliedJobs);
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                jsonObject.getString("message"),

                                Toast.LENGTH_LONG).show();
                    }

                    Log.d(TAG, "jobgggggggggggggg" + joblist.size());
                    //creating adapter object and setting it to recyclerview
                    AppliedJobsAdapter adapter = new AppliedJobsAdapter(AppliedJobs.this, joblist);
                    recyclerViewjobs.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            System.out.println("Time Out and NoConnection...................." + error);
                            loader.dismiss();
                            // hideDialog();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(AppliedJobs.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(AppliedJobs.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server error......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(AppliedJobs.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(AppliedJobs.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(AppliedJobs.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }

                }){
        @Override
        protected Map<String, String> getParams() {
            // Creating Map String Params.
            Map<String, String> params = new HashMap<String, String>();
            params.put("created_by", user.get(SessionManagerContractor.KEY_EMAIL));
            return params;
        }

    };

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(int position) {

    }
}
