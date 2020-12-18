package com.example.labourmangement.Architect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.labourmangement.CustomLoader;
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
import java.util.Locale;
import java.util.Map;

public class JobApplyDetailsArch extends AppCompatActivity {
    private static final String TAG = JobApplyDetails.class.getSimpleName();

    private Button btn_approve,btn_reject;
   SessionForArch sessionForArch;
    ArrayList<JobModel> mjoblist;
    // loader loader;
    CustomLoader loader;
    String jobtitle,jobdetails,jobwages,jobarea,jobid,appliedby,applieddate,laborname,contractorname;
    TextView name,destcription,area,wages,id,textmgstitle,textmessagebody,apppliedby,applied_date,labor_name,contractor_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_apply_details_arch);


        getIncomingIntent();
        sessionForArch = new SessionForArch(getApplicationContext());
        sessionForArch = new SessionForArch(getApplicationContext());

        //   loader = new loader(JobApplyDetails.this);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        btn_approve=(Button) findViewById(R.id.btnapprove);
        btn_reject=(Button) findViewById(R.id.btnreject);

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendrejection();
                Toast.makeText(JobApplyDetailsArch.this, "Application Rejected", Toast.LENGTH_LONG).show();
                btn_approve.setEnabled(false);
                btn_approve.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));

            }
        });

        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendapproval();
                Toast.makeText(JobApplyDetailsArch.this, "Application Approved", Toast.LENGTH_LONG).show();
                btn_reject.setEnabled(false);
                btn_reject.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));

            }
        });

    }


    private void sendapproval() {



        HashMap<String, String> user1 = sessionForArch.getUserDetails();

        // name
        String namecon = user1.get(SessionForArch.KEY_NAME);

        // email
        String emailcon = user1.get(SessionForArch.KEY_EMAIL);

        String role=user1.get((SessionForArch.KEY_ROLE));

        Log.d(TAG, "Email: " + emailcon);
        Log.d(TAG, "NAmew: " + namecon);


        loader.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTAPPROVALREQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                loader.dismiss();
//                //hideDialog();
//                Log.i("tagconvertstr", "[" + response + "]");
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    AlertDialog alertDialog = new AlertDialog.Builder(JobApplyDetails.this).create();
//                    alertDialog.setTitle("Applied Job Details ");
//                    alertDialog.setMessage(" Your Job Is Approved");
//                    alertDialog.setIcon(R.drawable.done);
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    alertDialog.show();
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


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
                            Toast.makeText(JobApplyDetailsArch.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsArch.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsArch.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsArch.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsArch.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                params.put("created_by", emailcon);

                Log.d(TAG, "PP: " + params);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(JobApplyDetailsArch.this);

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

        HashMap<String, String> user = sessionForArch.getUserDetails();

        // name
        String name = user.get(SessionForArch.KEY_NAME);

        // email
        String email = user.get(SessionForArch.KEY_EMAIL);


        HashMap<String, String> user1 = sessionForArch.getUserDetails();

        // name
        String name1 = user1.get(SessionForArch.KEY_NAME);

        // email
        String email1 = user1.get(SessionForArch.KEY_EMAIL);

        loader.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTREJECTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                loader.dismiss();
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
                            loader.dismiss();
                            // hideDialog();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsArch.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsArch.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsArch.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsArch.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobApplyDetailsArch.this, "Error While Data Parsing", duration).show();

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
                params.put("applied_by", email);
                params.put("rejected_by", email1);
                params.put("status", status);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(JobApplyDetailsArch.this);

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

            name = findViewById(R.id.fetchjobtitleapplied);
            name.setText(job_title);

            destcription = findViewById(R.id.fetchjobdetailsapplied);
            destcription.setText(job_deatils);

            wages = findViewById(R.id.fetchjobwagesapplied);
            wages.setText(job_wages);

            area = findViewById(R.id.fetchjobareaapplied);
            area.setText(job_area);

            id=findViewById(R.id.id);
            id.setText(jobid);

            apppliedby =findViewById(R.id.fetchappliedbyname);
            apppliedby.setText(appliedby);

            applied_date=findViewById(R.id.fetchapplieddate);
            applied_date.setText(applieddate);

            labor_name=findViewById(R.id.fetchlaborname);
            labor_name.setText(laborname);

            contractor_name=findViewById(R.id.fetchcontractorname);
            contractor_name.setText(contractorname);



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}