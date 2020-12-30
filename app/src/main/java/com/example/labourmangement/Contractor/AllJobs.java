package com.example.labourmangement.Contractor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.labourmangement.Adapter.AllJobsAdapter;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Owner.OwnerReview;
import com.example.labourmangement.R;
import com.example.labourmangement.model.AllJobsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllJobs extends AppCompatActivity {
    RecyclerView rv_paymentstatus;
    RecyclerView.LayoutManager layoutManager;
    private static final String TAG = OwnerReview.class.getSimpleName();
    List<AllJobsModel> joblist;
   // loader loader;
   CustomLoader loader;
    SessionManagerContractor sessionManagerContractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_jobs);

        rv_paymentstatus = (RecyclerView) findViewById(R.id.rv_alljobs);
        rv_paymentstatus.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        rv_paymentstatus.setLayoutManager(layoutManager);

        joblist = new ArrayList<AllJobsModel>();
        sessionManagerContractor=new SessionManagerContractor((getApplicationContext()));
       // loader= new loader(AllJobs.this);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        alljobs();

    }
    public void alljobs() {

        HashMap<String, String> user = sessionManagerContractor.getUserDetails();

        // name
        String name = user.get(sessionManagerContractor.KEY_NAME);

        // email
        String email = user.get(sessionManagerContractor.KEY_EMAIL);

        Log.d(TAG, "Email "+email);
      
        loader.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ALLJOBS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                loader.dismiss();
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);
                    if(array == null){
                        Toast.makeText(getApplicationContext(),
                                "Sorry No Record Found"+response,
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        Log.d(TAG, array.toString());
                        //traversing through all the object
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject job = array.getJSONObject(i);
                            AllJobsModel allJobsModel = new AllJobsModel();
                            allJobsModel.setJob_id(job.getString("job_id"));
                            allJobsModel.setJob_title(job.getString("job_title"));
                            allJobsModel.setJob_wages(job.getString("job_wages"));
                            allJobsModel.setJob_area(job.getString("job_area"));
                            allJobsModel.setJob_details(job.getString("job_details"));
                            allJobsModel.setPost_date(job.getString("post_date"));
                            allJobsModel.setApproved_status(job.getString("approved_status"));
                            allJobsModel.setContractor_name(job.getString("contractor_name"));
                            joblist.add(allJobsModel);
                        }
                    }
                    Log.d(TAG, "jobgggggggggggggg" + joblist.size());
                    //creating adapter object and setting it to recyclerview
                    AllJobsAdapter adapter = new AllJobsAdapter(AllJobs.this, joblist);
                    rv_paymentstatus.setAdapter(adapter);
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
                            Toast.makeText(AllJobs.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(AllJobs.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server error......................." + error);
                            //hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(AllJobs.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(AllJobs.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(AllJobs.this, "Error While Data Parsing", duration).show();
                        }
                    }

                }){
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("created_by",email);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}