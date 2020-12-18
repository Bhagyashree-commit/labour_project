package com.example.labourmangement.Labour;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
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
import com.example.labourmangement.Contractor.ContractorProfile;
import com.example.labourmangement.Contractor.JobWages;
import com.example.labourmangement.Contractor.Register_contractor;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseConfiguration.SharedPrefManager;
import com.example.labourmangement.DatabaseConfiguration.VolleySingleton;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.R;
import com.example.labourmangement.model.JobModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobDetails extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = JobDetails.class.getSimpleName();

    private Button btnclicktoapply;
    private List<String> devices;
    SessionManager session;
    Spinner spinner;
    SessionManagerContractor sessionManagerContractor;
    ArrayList<JobModel> mjoblist;
   CustomLoader loader;
    TextView fetchname;
    String jobtitle, jobdetails, jobwages, jobarea, jobid,jobcreatedby,jobcreatedbyname;
    TextView name, destcription, area, wages, id, jobcreated_by,textmgstitle, textmessagebody,jobcreatedby_name;
Button buttonSendPush;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private TextView textViewToken;
    private long mLastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);


        getIncomingIntent1();
        session = new SessionManager(getApplicationContext());
        sessionManagerContractor = new SessionManagerContractor(getApplicationContext());

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        btnclicktoapply = (Button) findViewById(R.id.btnapply);
        textViewToken = (TextView) findViewById(R.id.textViewToken);
        fetchname=(TextView)findViewById(R.id.fetchname);

      //  buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonSendPush = (Button) findViewById(R.id.buttonSendNotification);

        //adding listener to view
        buttonSendPush.setOnClickListener(this);


        btnclicktoapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();

            }
        });
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        fetchname=(TextView)findViewById(R.id.fetchname);
        fetchname.setText(name);
    }

    private void sendData() {
        jobtitle = name.getText().toString();
        jobdetails = destcription.getText().toString();
        jobwages = wages.getText().toString();
        jobarea = area.getText().toString();
        jobid = id.getText().toString();
        jobcreatedby = jobcreated_by.getText().toString();

        String status = "Applied";

        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);
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

                    AlertDialog alertDialog = new AlertDialog.Builder(JobDetails.this).create();
                    alertDialog.setTitle("Job Application ");
                    Log.e("TAG","NEW TAG"+response);
                    alertDialog.setMessage(jsonObject.getString("message"));
                    alertDialog.setIcon(R.drawable.done);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) { Intent intent = new Intent(JobDetails.this,
                            LaborProfile.class);
                            startActivity(intent);
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
                            Toast.makeText(JobDetails.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobDetails.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobDetails.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobDetails.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobDetails.this, "Error While Data Parsing", duration).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(JobDetails.this);

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

                name = findViewById(R.id.fetchjobtitle);
                name.setText(job_title);

                destcription = findViewById(R.id.fetchjobdetails);
                destcription.setText(job_deatils);

                wages = findViewById(R.id.fetchjobwages);
                wages.setText(job_wages);

                area = findViewById(R.id.fetchjobarea);
                area.setText(job_area);

                id = findViewById(R.id.id);
                id.setText(jobid);

                jobcreated_by=findViewById(R.id.fetchjobcreatedby);
                jobcreated_by.setText(jobcreatedby);


                jobcreatedby_name=findViewById(R.id.fetchjobcreatedbyname);
                jobcreatedby_name.setText(jobcreatedbyname);

            }
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onClick(View view) {

    }
}
