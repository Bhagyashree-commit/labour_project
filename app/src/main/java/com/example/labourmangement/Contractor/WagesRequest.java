
package com.example.labourmangement.Contractor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Labour.GetWages;
import com.example.labourmangement.Labour.WagesApprovalRequest;
import com.example.labourmangement.R;
import com.example.labourmangement.model.JobModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WagesRequest extends AppCompatActivity {

    private static final String TAG = WagesRequest.class.getSimpleName();
    SessionManagerContractor sessionManagerContractor;
  CustomLoader loader;
    Button btnsendmoney;
    String jobtitleW,jobdetailsW,jobwagesW,jobareaW,jobidW,appliedbyW,applieddateW,laborname,contractornameW,createdbyW;
    TextView name,created_by,contractor_name,wages,id,textmgstitle,textmessagebody,apppliedby,applied_date,labor_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wages_request);

        sessionManagerContractor = new SessionManagerContractor(getApplicationContext());
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        btnsendmoney=(Button)findViewById(R.id.btnsendmoney);

        btnsendmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMoney();
            }
        });
        getIncomingIntentfromwagesrequest();
       // sendMoney();
    }


    private void getIncomingIntentfromwagesrequest() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");


        if (getIntent().hasExtra("job_title") && getIntent().hasExtra("job_wages")) {
            Log.d(TAG, "job_title"+jobtitleW);

            jobtitleW = getIntent().getStringExtra("job_title");
            jobwagesW = getIntent().getStringExtra("job_wages");
            laborname = getIntent().getStringExtra("labor_name");
            jobidW = getIntent().getStringExtra("job_id");
           appliedbyW = getIntent().getStringExtra("applied_by");
             contractornameW = getIntent().getStringExtra("contractor_name");
             createdbyW=getIntent().getStringExtra("created_by");

            setImage(jobtitleW,  jobwagesW,jobidW, appliedbyW,laborname,createdbyW,contractornameW);

            // setImage( image_path,product_name);
        }


    }

    private void setImage(String job_title, String job_wages, String jobid, String appliedby, String laborname, String createdby, String contractorname) {
        {
            Log.d(TAG, "setImage: setting te image and name to widgets.");
            //Intent intent=getIntent();
            // String imagepath=intent.getStringExtra("image_path");

            name = findViewById(R.id.fetchjobtitlewages);
            name.setText(job_title);



            wages = findViewById(R.id.fetchjobwageswages);
            wages.setText(job_wages);

            labor_name = findViewById(R.id.fetchlaborIDwages);
            labor_name.setText(laborname);

            id=findViewById(R.id.idwages);
            id.setText(jobid);

           apppliedby =findViewById(R.id.appliedbyid);
            apppliedby.setText(appliedby);

            created_by=findViewById(R.id.createdbyid);
            created_by.setText(createdby);

contractor_name=findViewById(R.id.contractornamewages);
contractor_name.setText(contractorname);

        }
    }



    private void sendMoney() {
        jobtitleW = name.getText().toString();
        jobwagesW = wages.getText().toString();
        jobidW = id.getText().toString();


        HashMap<String, String> user1 = sessionManagerContractor.getUserDetails();

        // name
        String namecon = user1.get(SessionManagerContractor.KEY_NAME);

        // email
        String emailcon = user1.get(SessionManagerContractor.KEY_EMAIL);

        Log.d(TAG, "Email: " + emailcon);
        
        loader.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_WAGESREQUESTINSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                loader.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "[" + response + "]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    AlertDialog alertDialog = new AlertDialog.Builder(WagesRequest.this).create();
                    alertDialog.setTitle("Send Money ");
                    alertDialog.setMessage(" Payment Successful");
                    alertDialog.setIcon(R.drawable.done);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(WagesRequest.this, ContractorProfile.class);
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
                            Toast.makeText(WagesRequest.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();

                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(WagesRequest.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(WagesRequest.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(WagesRequest.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(WagesRequest.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("created_by", emailcon);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(WagesRequest.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

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
}
