package com.example.labourmangement.Engineer;

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
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForEngineer;
import com.example.labourmangement.R;
import com.example.labourmangement.model.JobModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EngJobDetails extends AppCompatActivity {
    private static final String TAG = EngJobDetails.class.getSimpleName();

    private Button btnclicktoapply;
    SessionForEngineer sessionForEngineer;
    ArrayList<JobModel> mjoblist;
   CustomLoader loader;
    TextView fetchname;
    String jobtitle, jobdetails, jobwages, jobarea, jobid,jobcreatedby,jobcreatedbyname;
    TextView name, destcription, area, wages, id, jobcreated_by,textmgstitle, textmessagebody,jobcreatedby_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eng_job_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getIncomingIntent1();

        sessionForEngineer = new SessionForEngineer(getApplicationContext());

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        btnclicktoapply = (Button) findViewById(R.id.btnapplyENG);



        btnclicktoapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sendData();
            }
        });

    }

    private void sendData() {
        jobtitle = name.getText().toString();
        jobdetails = destcription.getText().toString();
        jobwages = wages.getText().toString();
        jobarea = area.getText().toString();
        jobid = id.getText().toString();
        jobcreatedby = jobcreated_by.getText().toString();

        String status = "Applied";

        HashMap<String, String> user = sessionForEngineer.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);
        // Showing progress dialog at user registration time.
      loader.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTAPPLIEDJOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                 loader.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "[" + response + "]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                   /* Toast.makeText(getApplicationContext(),
                            jsonObject.getString("message") + response,
                            Toast.LENGTH_LONG).show();
*/


                    AlertDialog alertDialog = new AlertDialog.Builder(EngJobDetails.this).create();
                    alertDialog.setTitle("Job Application ");
                    alertDialog.setMessage(" Successfuly Applied For This Job Plz Wait For Approval"
                    );
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
                             loader.dismiss();
                            // hideDialog();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngJobDetails.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                             loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngJobDetails.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                             loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngJobDetails.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                             loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngJobDetails.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                             loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngJobDetails.this, "Error While Data Parsing", duration).show();

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
                params.put("created_by", jobcreatedby);
                params.put("contractor_name", jobcreatedbyname);
                params.put("labor_name", name);
                params.put("status", status);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(EngJobDetails.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
    public void getIncomingIntent1() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");


        if (getIntent().hasExtra("job_title") && getIntent().hasExtra("job_details")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");

            jobtitle = getIntent().getStringExtra("job_title");
            jobdetails = getIntent().getStringExtra("job_details");
            jobwages = getIntent().getStringExtra("job_wages");
            jobarea = getIntent().getStringExtra("job_area");
            jobid = getIntent().getStringExtra("job_id");
            jobcreatedby = getIntent().getStringExtra("created_by");
            jobcreatedbyname = getIntent().getStringExtra("contractor_name");

            setImage1(jobtitle, jobdetails, jobwages, jobarea, jobid,jobcreatedby,jobcreatedbyname);
            // setImage( image_path,product_name);
        }


    }


    public void setImage1(String job_title, String job_deatils, String job_wages, String job_area, String jobid, String jobcreatedby, String jobcreatedbyname) {
        {
            Log.d(TAG, "setImage: setting te image and name to widgets.");
            //Intent intent=getIntent();
            // String imagepath=intent.getStringExtra("image_path");

            name = findViewById(R.id.fetchjobtitleENG);
            name.setText(job_title);

            destcription = findViewById(R.id.fetchjobdetailsENG);
            destcription.setText(job_deatils);

            wages = findViewById(R.id.fetchjobwagesENG);
            wages.setText(job_wages);

            area = findViewById(R.id.fetchjobareaENG);
            area.setText(job_area);

            id = findViewById(R.id.idENG);
            id.setText(jobid);

            jobcreated_by=findViewById(R.id.fetchjobcreatedbyENG);
            jobcreated_by.setText(jobcreatedby);


            jobcreatedby_name=findViewById(R.id.fetchjobcreatedbynameENG);
            jobcreatedby_name.setText(jobcreatedbyname);

        }
    }

}