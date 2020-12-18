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
import com.example.labourmangement.Contractor.WagesRequest;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForArch;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WagesRequestAR extends AppCompatActivity {

    private static final String TAG = WagesRequest.class.getSimpleName();
    SessionForArch sessionForArch;
    ProgressDialog progressDialog;
    Button btnsendmoney;
    String jobtitleW,jobdetailsW,jobwagesW,jobareaW,jobidW,appliedbyW,applieddateW,laborname,contractornameW,createdbyW;
    TextView name,created_by,contractor_name,wages,id,textmgstitle,textmessagebody,apppliedby,applied_date,labor_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wages_request_a_r);


        sessionForArch = new SessionForArch(getApplicationContext());
        progressDialog = new ProgressDialog(WagesRequestAR.this);

        btnsendmoney=(Button)findViewById(R.id.btnsendmoneyAR);

        btnsendmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMoney();
            }
        });
        getIncomingIntentfromwagesrequest();

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

            name = findViewById(R.id.fetchjobtitlewagesAR);
            name.setText(job_title);



            wages = findViewById(R.id.fetchjobwageswagesAR);
            wages.setText(job_wages);

            labor_name = findViewById(R.id.fetchlaborIDwagesAR);
            labor_name.setText(laborname);

            id=findViewById(R.id.idwagesAR);
            id.setText(jobid);

            apppliedby =findViewById(R.id.appliedbyidAR);
            apppliedby.setText(appliedby);

            created_by=findViewById(R.id.createdbyidAR);
            created_by.setText(createdby);

            contractor_name=findViewById(R.id.contractornamewagesAR);
            contractor_name.setText(contractorname);

        }
    }



    private void sendMoney() {
        jobtitleW = name.getText().toString();
        jobwagesW = wages.getText().toString();
        jobidW = id.getText().toString();


        HashMap<String, String> user1 = sessionForArch.getUserDetails();

        // name
        String namecon = user1.get(SessionForArch.KEY_NAME);

        // email
        String emailcon = user1.get(SessionForArch.KEY_EMAIL);

        Log.d(TAG, "Email: " + emailcon);

        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Loading...Please Wait..");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_WAGESREQUESTINSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                progressDialog.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "[" + response + "]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                   /* Toast.makeText(getApplicationContext(),
                            jsonObject.getString("message") + response,
                            Toast.LENGTH_LONG).show();*/

                    AlertDialog alertDialog = new AlertDialog.Builder(WagesRequestAR.this).create();
                    alertDialog.setTitle("Send Money ");
                    alertDialog.setMessage(" Payment Successful");
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
                            Toast.makeText(WagesRequestAR.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();

                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            progressDialog.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(WagesRequestAR.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(WagesRequestAR.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(WagesRequestAR.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(WagesRequestAR.this, "Error While Data Parsing", duration).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(WagesRequestAR.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

}