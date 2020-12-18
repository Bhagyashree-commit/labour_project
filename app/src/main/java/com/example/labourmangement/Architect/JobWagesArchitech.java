package com.example.labourmangement.Architect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.example.labourmangement.Adapter.JobWagesAdapter;
import com.example.labourmangement.Adapter.WagesAdapterAR;
import com.example.labourmangement.Contractor.JobWages;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForArch;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.R;
import com.example.labourmangement.model.JobWagesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobWagesArchitech extends AppCompatActivity {
    RecyclerView recyclerViewjobs;
    RecyclerView.LayoutManager layoutManager;
    private static final String TAG = JobWagesArchitech.class.getSimpleName();
    List<JobWagesModel> jobwageslist;
  SessionForArch sessionForArch;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_wages_architech);


        recyclerViewjobs = (RecyclerView) findViewById(R.id.recycleViewjobwageAR);
        recyclerViewjobs.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerViewjobs.setLayoutManager(layoutManager);

        jobwageslist = new ArrayList<JobWagesModel>();

        sessionForArch=new SessionForArch((getApplicationContext()));

        progressDialog =new ProgressDialog(JobWagesArchitech.this);

        getRequest();

    }
    public void getRequest() {
        HashMap<String, String> user = sessionForArch.getUserDetails();

        // name
        String name = user.get(SessionForArch.KEY_NAME);

        // email
        String email = user.get(SessionForArch.KEY_EMAIL);

        Log.d(TAG, "Email "+email);

        progressDialog.setMessage("Loading...Please Wait..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GETWAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                progressDialog.dismiss();

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
                                JobWagesModel jobWagesModel = new JobWagesModel();
                                //adding the product to product list

                                jobWagesModel.setJob_title(job.getString("job_title"));
                                jobWagesModel.setJob_wages(job.getString("job_wages"));
                                jobWagesModel.setJob_id(job.getString("job_id"));
                                jobWagesModel.setApplied_by(job.getString("applied_by"));
                                jobWagesModel.setContractor_name(job.getString("contractor_name"));
                                jobWagesModel.setCreated_by(job.getString("created_by"));
                                jobWagesModel.setLabor_name(job.getString("labor_name"));
                                jobWagesModel.setWages_status(job.getString("wages_status"));

                                //  Toast.makeText(JobWages.this,"nothing to  show"+response, Toast.LENGTH_LONG).show();

                                jobwageslist.add(jobWagesModel);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                jsonObject.getString("message")+response,
                                Toast.LENGTH_LONG).show();
                    }
                    Log.d(TAG, "jobgggggggggggggg" + jobwageslist.size());
                    //creating adapter object and setting it to recyclerview
                    WagesAdapterAR adapter = new WagesAdapterAR(JobWagesArchitech.this, jobwageslist);
                    recyclerViewjobs.setAdapter(adapter);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            System.out.println("Time Out and NoConnection...................." + error);
                            progressDialog.dismiss();
                            // hideDialog();
                            int duration = Toast.LENGTH_SHORT;

                            Toast.makeText(JobWagesArchitech.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            progressDialog.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobWagesArchitech.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobWagesArchitech.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobWagesArchitech.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobWagesArchitech.this, "Error While Data Parsing", duration).show();

                            //TODO
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

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}