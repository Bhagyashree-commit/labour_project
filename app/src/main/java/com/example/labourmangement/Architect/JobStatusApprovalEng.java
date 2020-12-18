package com.example.labourmangement.Architect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.labourmangement.Adapter.JobsStatus;
import com.example.labourmangement.Adapter.UJobStatusENG;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForEngineer;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.Labour.JobStatusApproval;
import com.example.labourmangement.R;
import com.example.labourmangement.model.JobsStatusModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


public class JobStatusApprovalEng extends AppCompatActivity {
    private static final String TAG = JobStatusApprovalEng.class.getSimpleName();

    RecyclerView recyclerViewjobs;
    RecyclerView.LayoutManager layoutManager;
    List<JobsStatusModel> jobstatus;
CustomLoader loader;
   SessionForEngineer sessionForEngineer;
    TextView fetchname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_status_approval_eng);



        recyclerViewjobs = (RecyclerView) findViewById(R.id.recyclerviewjobsstatus_eng);
        recyclerViewjobs.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerViewjobs.setLayoutManager(layoutManager);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        sessionForEngineer=new SessionForEngineer((getApplicationContext()));

        jobstatus = new ArrayList<JobsStatusModel>();

        getapprovalRequest();
        fetchname=(TextView)findViewById(R.id.fetchname);

        sessionForEngineer = new SessionForEngineer(getApplicationContext());
        HashMap<String, String> user = sessionForEngineer.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);


    }

    public void getapprovalRequest() {

        sessionForEngineer.checkLogin();
        HashMap<String, String> user = sessionForEngineer.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);
        Log.d(TAG, "Email "+email);


        final String Emailholder = email;

        loader.show();
        Log.d(TAG, "Inserting Response");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GETJOBSTATUSBYID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                loader.dismiss();
                try {

                    //converting the string to json array object
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("Success").equalsIgnoreCase("true")) {
                        JSONArray array = jsonObject.getJSONArray("Jobs");
                        {
                            Log.d(TAG, array.toString());
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject job = array.getJSONObject(i);
                                JobsStatusModel jobstatus1 = new JobsStatusModel();
                                //adding the product to product list
                                jobstatus1.setJob_area(job.getString("job_area"));
                                jobstatus1.setJob_title(job.getString("job_title"));
                                jobstatus1.setJob_details(job.getString("job_details"));
                                jobstatus1.setJob_wages(job.getString("job_wages"));
                                jobstatus1.setJob_id(job.getString("job_id"));
                                jobstatus1.setApplied_by(job.getString("applied_by"));
                                jobstatus1.setCreated_by(job.getString("created_by"));
                                jobstatus1.setApplied_date(job.getString("applied_date"));
                                jobstatus1.setApproved_byname(job.getString("contractor_name"));
                                jobstatus1.setTrack_status(job.getString("track_status"));

                                jobstatus.add(jobstatus1);
                            }

                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                jsonObject.getString("message")+response,
                                Toast.LENGTH_LONG).show();
                    }
                    //converting the string to json array object

                    Log.d(TAG, "jobgggggggggggggg" + jobstatus.size());
                    //creating adapter object and setting it to recyclerview
                    JobsStatus adapter = new JobsStatus(JobStatusApprovalEng.this, jobstatus);
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
                            Toast.makeText(JobStatusApprovalEng.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobStatusApprovalEng.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobStatusApprovalEng.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobStatusApprovalEng.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobStatusApprovalEng.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }

                })  {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("applied_by", Emailholder);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(JobStatusApprovalEng.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }




}