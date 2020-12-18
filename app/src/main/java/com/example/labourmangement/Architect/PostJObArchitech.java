package com.example.labourmangement.Architect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.labourmangement.Contractor.AllJobs;
import com.example.labourmangement.Contractor.PostJobs;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForArch;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostJObArchitech extends AppCompatActivity {
    private static final String TAG = PostJObArchitech.class.getSimpleName();

    EditText etjobtitle,etjobdetails,etjobwages,etjobarea;
    Button btnpost;
    String jobtittleholder,jobdetailsholder,jobwagesholder,jobareaholder,Emailholder,ContractorNameHolder,roleholder;
  SessionForArch sessionForArch;
    int flag;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_j_ob_architech);

        etjobtitle=(EditText)findViewById(R.id.editjobttitleA);
        etjobdetails=(EditText)findViewById(R.id.editjobdetailsA);
        etjobwages=(EditText)findViewById(R.id.editjobwagesA);
        etjobarea=(EditText)findViewById(R.id.editjobareaA);
        btnpost=(Button)findViewById(R.id.submijobdataA);


        sessionForArch=new SessionForArch((getApplicationContext()));

        progressDialog = new ProgressDialog(PostJObArchitech.this);


        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionForArch.checkLogin();
                HashMap<String, String> user = sessionForArch.getUserDetails();

                // name
                String name = user.get(SessionForArch.KEY_NAME);

                // email
                String email = user.get(SessionForArch.KEY_EMAIL);
                String role=user.get(SessionForArch.KEY_ROLE);
                Log.d(TAG, "Email "+email);



                jobtittleholder= etjobtitle.getText().toString().trim();
                jobareaholder = etjobarea.getText().toString().trim();
                jobdetailsholder = etjobdetails.getText().toString().trim();
                jobwagesholder = etjobwages.getText().toString().trim();
                Emailholder=email;
                ContractorNameHolder=name;
                roleholder=role;


                flag=0;
                if(etjobtitle.getText().toString().length()==0){
                    etjobtitle.setError(" Enter Job Title");
                    etjobtitle.requestFocus();
                    flag=1;

                }
                flag=0;
                if(etjobarea.getText().toString().length()==0){
                    etjobarea.setError(" Enter Job Area");
                    etjobarea.requestFocus();
                    flag=1;

                }
                flag=0;
                if(etjobdetails.getText().toString().length()==0){
                    etjobdetails.setError(" Enter Job Details");
                    etjobdetails.requestFocus();
                    flag=1;

                }
                flag=0;
                if(etjobwages.getText().toString().length()==0){
                    etjobwages.setError(" Enter Job Wages");
                    etjobwages.requestFocus();
                    flag=1;

                }

                if(flag==0){
                    postnewjob();
                }
            }
        });

    }


    private void postnewjob(){
        // GetValueFromEditText();
        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Loading... Please Wait.. ");
        progressDialog.show();
        Log.d(TAG, "Inserting Response:hiiiiiiiiiiiiiii ");
        Log.d(TAG, "ROLE "+roleholder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTJOB,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response:hiiiiiiiiiiiiiii " + response.toString());
                progressDialog.dismiss();
                //hideDialog();
                Log.i("bhagyaaaaaa", "["+response+"]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                   /* Toast.makeText(getApplicationContext(),
                            jsonObject.getString("message")+response,
                            Toast.LENGTH_LONG).show();*/
                    AlertDialog alertDialog = new AlertDialog.Builder(PostJObArchitech.this).create();
                    alertDialog.setTitle("Job Post ");
                    alertDialog.setMessage(" Your Job Post Is successful");
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
                            Toast.makeText(PostJObArchitech.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            progressDialog.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJObArchitech.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJObArchitech.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJObArchitech.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJObArchitech.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("job_title", jobtittleholder);
                params.put("job_details", jobdetailsholder);
                params.put("job_wages", jobwagesholder);
                params.put("job_area", jobareaholder);
                params.put("created_by", Emailholder);
                params.put("contractor_name", ContractorNameHolder);
                params.put("role",roleholder);



                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(PostJObArchitech.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);


        etjobarea.setText("");
        etjobdetails.setText("");
        etjobtitle.setText("");
        etjobwages.setText("");

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