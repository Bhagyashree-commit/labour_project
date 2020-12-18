package com.example.labourmangement.Owner;

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
import com.example.labourmangement.DatabaseHelper.SessionForOwner;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.R;
import com.example.labourmangement.model.JobModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobApplyDetailsOwner extends AppCompatActivity {
    private static final String TAG = JobApplyDetailsOwner.class.getSimpleName();

    private Button btn_approveowner,btn_rejectowner;
    SessionForOwner sessionForOwner;
    ProgressDialog progressDialog;
    String jobtitle,jobdetails,jobwages,jobarea,jobid,appliedby,applieddate,laborname,contractorname;
    TextView name,destcription,area,wages,id,textmgstitle,textmessagebody,apppliedby,applied_date,labor_name,contractor_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_apply_details_owner);


        getIncomingIntent();

        sessionForOwner = new SessionForOwner(getApplicationContext());

        progressDialog = new ProgressDialog(JobApplyDetailsOwner.this);

        btn_approveowner=(Button) findViewById(R.id.btnapproveowner);
        btn_rejectowner=(Button) findViewById(R.id.btnrejectowner);

        btn_rejectowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendrejection();
                Toast.makeText(JobApplyDetailsOwner.this, "Application Rejected", Toast.LENGTH_LONG).show();
                btn_approveowner.setEnabled(false);
                btn_approveowner.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));

            }
        });

        btn_approveowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendapproval();
                Toast.makeText(JobApplyDetailsOwner.this, "Application Approved", Toast.LENGTH_LONG).show();
                btn_rejectowner.setEnabled(false);
                btn_rejectowner.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));

            }
        });

    }

    private void sendapproval() {
        jobtitle = name.getText().toString();
        jobdetails = destcription.getText().toString();
        jobwages = wages.getText().toString();
        jobarea = area.getText().toString();
        jobid = id.getText().toString();
        String status = "Approved";


        HashMap<String, String> user1 = sessionForOwner.getUserDetails();

        // name
        String namecon = user1.get(SessionForOwner.KEY_NAME);

        // email
        String emailcon = user1.get(SessionForOwner.KEY_EMAIL);

        String role=user1.get(SessionForOwner.KEY_ROLE);

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

                    AlertDialog alertDialog = new AlertDialog.Builder(JobApplyDetailsOwner.this).create();
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
                            Toast.makeText(JobApplyDetailsOwner.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            progressDialog.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsOwner.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsOwner.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsOwner.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsOwner.this, "Error While Data Parsing", duration).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(JobApplyDetailsOwner.this);

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


        HashMap<String, String> user1 = sessionForOwner.getUserDetails();

        // name
        String name1 = user1.get(SessionForOwner.KEY_NAME);

        // email
        String email1 = user1.get(SessionForOwner.KEY_EMAIL);
        String role =user1.get(SessionForOwner.KEY_ROLE);



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
                            Toast.makeText(JobApplyDetailsOwner.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            progressDialog.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsOwner.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsOwner.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsOwner.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsOwner.this, "Error While Data Parsing", duration).show();

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
                params.put("role",role);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(JobApplyDetailsOwner.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

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

            name = findViewById(R.id.fetchjobtitleappliedowner);
            name.setText(job_title);

            destcription = findViewById(R.id.fetchjobdetailsappliedowner);
            destcription.setText(job_deatils);

            wages = findViewById(R.id.fetchjobwagesappliedowner);
            wages.setText(job_wages);

            area = findViewById(R.id.fetchjobareaappliedowner);
            area.setText(job_area);

            id=findViewById(R.id.idowner);
            id.setText(jobid);

            apppliedby =findViewById(R.id.fetchappliedbynameowner);
            apppliedby.setText(appliedby);

            applied_date=findViewById(R.id.fetchapplieddateowner);
            applied_date.setText(applieddate);

            labor_name=findViewById(R.id.fetchlabornameowner);
            labor_name.setText(laborname);

            contractor_name=findViewById(R.id.fetchcontractornameowner);
            contractor_name.setText(contractorname);



        }
    }

}