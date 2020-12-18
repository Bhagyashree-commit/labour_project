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
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.Labour.WagesApprovalRequest;
import com.example.labourmangement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WagesApprovalRequestENG extends AppCompatActivity {
    private static final String TAG = WagesApprovalRequestENG.class.getSimpleName();

    private Button btndone,btnhold;
   SessionForEngineer sessionForEngineer;
    String jobtitle,jobwages,jobid,appliedby,laborname,contractorname,contractorID;
    TextView name,wages,id,apppliedby,fetchname,cname,lname,contractor_ID;
 CustomLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wages_approval_request_e_n_g);

    getIncomingIntentMoney();
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        btndone=(Button)findViewById(R.id.btndoneENG);
        btnhold=(Button)findViewById(R.id.btnholdENG);
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnhold.setEnabled(false);


                sendmoneyrequest();

            }
        });
        btnhold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btndone.setEnabled(false);
                AlertDialog alertDialog = new AlertDialog.Builder(WagesApprovalRequestENG.this).create();
                alertDialog.setTitle("Your Payment Status");
                alertDialog.setMessage("Thank You For Response");
                alertDialog.setIcon(R.drawable.done);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        });


        sessionForEngineer = new SessionForEngineer(getApplicationContext());
        HashMap<String, String> user = sessionForEngineer.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);


    }

    private void getIncomingIntentMoney() {

        if (getIntent().hasExtra("job_title") && getIntent().hasExtra("job_wages")) {
            jobtitle = getIntent().getStringExtra("job_title");
            jobwages = getIntent().getStringExtra("job_wages");
            jobid = getIntent().getStringExtra("job_id");
            appliedby = getIntent().getStringExtra("labor_id");
            contractorname = getIntent().getStringExtra("contractor_name");
            laborname = getIntent().getStringExtra("labor_name");
            contractorID =getIntent().getStringExtra("approved_by");

            setImage(jobtitle, jobwages, jobid, appliedby,contractorname,laborname,contractorID);
        }
    }

    private void setImage(String job_title, String job_wages, String jobid, String appliedby, String contractor_name,String labor_name,String contractorID) {
        {
            name = findViewById(R.id.fetchjobtitleappliedmoneyENG);
            name.setText(job_title);

            wages = findViewById(R.id.fetchjobwagesappliedmoneyENG);
            wages.setText(job_wages);

            id=findViewById(R.id.idmoneyENG);
            id.setText(jobid);

            apppliedby =findViewById(R.id.appliednameENG);
            apppliedby.setText(appliedby);

            cname=findViewById(R.id.cnameENG);
            cname.setText(contractor_name);

            lname=findViewById(R.id.lnameENG);
            lname.setText(labor_name);

            contractor_ID=findViewById(R.id.contractorIDENG);
            contractor_ID.setText(contractorID);

        }
    }

    private void sendmoneyrequest() {
        jobtitle = name.getText().toString();
        jobwages = wages.getText().toString();
        jobid = id.getText().toString();
        appliedby=apppliedby.getText().toString();
        contractorname=cname.getText().toString();
        laborname=lname.getText().toString();
        contractorID=contractor_ID.getText().toString();
        String status = "done";

        HashMap<String, String> user = sessionForEngineer.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);
       loader.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_MONEYAPPROVEDREQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
               loader.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "[" + response + "]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    AlertDialog alertDialog = new AlertDialog.Builder(WagesApprovalRequestENG.this).create();
                    alertDialog.setTitle("Your Payment Status ");
                    alertDialog.setMessage(" Payment Successful! Thank You For Response");
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
                            Toast.makeText(WagesApprovalRequestENG.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                             loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(WagesApprovalRequestENG.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                             loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(WagesApprovalRequestENG.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                             loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(WagesApprovalRequestENG.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                             loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(WagesApprovalRequestENG.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                Log.d(TAG,"jobid"+jobid);
                Log.d(TAG,"jobtitle"+jobtitle);
                Log.d(TAG,"jobwages"+jobwages);
                Log.d(TAG,"labor_id"+email);
                Log.d(TAG,"labor_name"+name);
                Log.d(TAG,"status"+status);
                Log.d(TAG,"contractor_name"+contractorname);
                Log.d(TAG,"contractor_id"+contractorID);


                params.put("job_id", jobid);
                params.put("job_title", jobtitle);
                params.put("job_wages", jobwages);
                params.put("labor_id", email);
                params.put("labor_name", name);
                params.put("status", status);
                params.put("contractor_name", contractorname);
                params.put("contractor_id", contractorID);


                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(WagesApprovalRequestENG.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);


    }
}