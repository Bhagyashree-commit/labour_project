package com.example.labourmangement.Architect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.labourmangement.Contractor.JobApplyDetails;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForArch;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.R;
import com.example.labourmangement.model.JobModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobAppliedDetailsEng extends AppCompatActivity {
    private static final String TAG = JobApplyDetails.class.getSimpleName();

    private Button btn_approve,btn_reject;
    ArrayList<JobModel> mjoblist;
    ProgressDialog progressDialog;
    String jobtitle,jobdetails,jobwages,jobarea,jobid,appliedby,applieddate,laborname,contractorname;
    TextView name,destcription,area,wages,id,textmgstitle,textmessagebody,apppliedby,applied_date,labor_name,contractor_name;

SessionForArch sessionForArch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_applied_details_eng);

        getIncomingIntent();

        sessionForArch = new SessionForArch(getApplicationContext());

        progressDialog = new ProgressDialog(JobAppliedDetailsEng.this);

        btn_approve=(Button) findViewById(R.id.btnapprove_AR);
        btn_reject=(Button) findViewById(R.id.btnreject_AR);

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendrejection();
                Toast.makeText(JobAppliedDetailsEng.this, "Application Rejected", Toast.LENGTH_LONG).show();
                btn_approve.setEnabled(false);
                btn_approve.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));

            }
        });

        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendapproval();
                Toast.makeText(JobAppliedDetailsEng.this, "Application Approved", Toast.LENGTH_LONG).show();
                btn_reject.setEnabled(false);
                btn_reject.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));

            }
        });

    }
    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");


        if (getIntent().hasExtra("job_title") && getIntent().hasExtra("job_details")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");

            jobtitle = getIntent().getStringExtra("job_title");
            jobdetails = getIntent().getStringExtra("job_details");
            jobwages = getIntent().getStringExtra("job_wages");
            jobarea = getIntent().getStringExtra("job_area");
            jobid = getIntent().getStringExtra("job_id");
            appliedby = getIntent().getStringExtra("applied_by");
            applieddate = getIntent().getStringExtra("applied_date");
            laborname = getIntent().getStringExtra("labor_name");
            contractorname = getIntent().getStringExtra("contractor_name");


            setImage(jobtitle, jobdetails, jobwages, jobarea ,jobid, appliedby,applieddate,laborname,contractorname);
            Log.d(TAG, "applied dateoooooo"+applied_date);
            // setImage( image_path,product_name);
        }


    }


    private void setImage(String job_title, String job_deatils, String job_wages, String job_area, String jobid, String appliedby, String applieddate, String laborname, String contractorname) {
        {
            Log.d(TAG, "setImage: setting te image and name to widgets.");
            //Intent intent=getIntent();
            // String imagepath=intent.getStringExtra("image_path");

            name = findViewById(R.id.fetchjobtitleappliedAR);
            name.setText(job_title);

            destcription = findViewById(R.id.fetchjobdetailsappliedAR);
            destcription.setText(job_deatils);

            wages = findViewById(R.id.fetchjobwagesappliedAR);
            wages.setText(job_wages);

            area = findViewById(R.id.fetchjobareaappliedAR);
            area.setText(job_area);

            id=findViewById(R.id.id);
            id.setText(jobid);

            apppliedby =findViewById(R.id.fetchappliedbynameAR);
            apppliedby.setText(appliedby);

            applied_date=findViewById(R.id.fetchapplieddateAR);
            applied_date.setText(applieddate);

            labor_name=findViewById(R.id.fetchlabornameAR);
            labor_name.setText(laborname);

            contractor_name=findViewById(R.id.fetchcontractornameAR);
            contractor_name.setText(contractorname);



        }
    }


    private void sendapproval() {
        jobtitle = name.getText().toString();
        jobdetails = destcription.getText().toString();
        jobwages = wages.getText().toString();
        jobarea = area.getText().toString();
        jobid = id.getText().toString();
        String status = "Approved";


        HashMap<String, String> user1 = sessionForArch.getUserDetails();

        // name
        String namecon = user1.get(SessionForArch.KEY_NAME);

        // email
        String emailcon = user1.get(SessionForArch.KEY_EMAIL);

        String role=user1.get((SessionForArch.KEY_ROLE));

        Log.d(TAG, "Email: " + emailcon);

        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Loading...Please Wait..");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTAPPROVALREQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                progressDialog.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "[" + response + "]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    /*Toast.makeText(getApplicationContext(),
                            jsonObject.getString("message") + response,
                            Toast.LENGTH_LONG).show();*/

                    AlertDialog alertDialog = new AlertDialog.Builder(JobAppliedDetailsEng.this).create();
                    alertDialog.setTitle("Applied Job Details ");
                    alertDialog.setMessage(" Your Job Is Approved");
                    alertDialog.setIcon(R.drawable.done);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();


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
                            progressDialog.dismiss();
                            // hideDialog();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobAppliedDetailsEng.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            progressDialog.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobAppliedDetailsEng.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobAppliedDetailsEng.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobAppliedDetailsEng.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobAppliedDetailsEng.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("job_id", jobid);
                params.put("job_title", jobtitle);
                params.put("job_details", jobdetails);
                params.put("job_wages", jobwages);
                params.put("job_area", jobarea);
                params.put("applied_by", appliedby);
                params.put("approved_by", emailcon);
                params.put("status", status);
                params.put("labor_name",laborname);
                params.put("contractor_name",namecon);
                params.put("role",role);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(JobAppliedDetailsEng.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }



    private void sendrejection() {
        jobtitle = name.getText().toString();
        jobdetails = destcription.getText().toString();
        jobwages = wages.getText().toString();
        jobarea = area.getText().toString();
        jobid = id.getText().toString();
        String status = "Reject";

        HashMap<String, String> user1 = sessionForArch.getUserDetails();

        // name
        String name1 = user1.get(SessionForArch.KEY_NAME);

        // email
        String email1 = user1.get(SessionForArch.KEY_EMAIL);



        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Loading... Please Wait..");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTREJECTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                progressDialog.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "[" + response + "]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getApplicationContext(),
                            jsonObject.getString("message") + response,
                            Toast.LENGTH_LONG).show();


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
                            progressDialog.dismiss();
                            // hideDialog();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobAppliedDetailsEng.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            progressDialog.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobAppliedDetailsEng.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobAppliedDetailsEng.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobAppliedDetailsEng.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobAppliedDetailsEng.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("job_id", jobid);
                params.put("job_title", jobtitle);
                params.put("job_details", jobdetails);
                params.put("job_wages", jobwages);
                params.put("job_area", jobarea);
                params.put("applied_by", appliedby);
                params.put("rejected_by", email1);
                params.put("status", status);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(JobAppliedDetailsEng.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

}