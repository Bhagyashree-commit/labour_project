package com.example.labourmangement.Owner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.labourmangement.Contractor.PostJobs;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForOwner;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PostJobOwner extends AppCompatActivity {
    private static final String TAG = PostJobs.class.getSimpleName();
Button postjobowner;
EditText etjobtitlebyowner,etjobdetailsowner,etjobwagesowner,etjobareaowner;
String jobtitlebyowner,jobdetailsbyowner,jobwagesbyowner,jobareabyowner,NameHolder,roleholder,Emailholder;
SessionForOwner sessionForOwner;
CustomLoader loader;
int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job_owner);



        etjobtitlebyowner=(EditText)findViewById(R.id.editjobttitleowner);
        etjobdetailsowner=(EditText)findViewById(R.id.editjobdetailsowner);
        etjobwagesowner=(EditText)findViewById(R.id.editjobwagesowner);
        etjobareaowner=(EditText)findViewById(R.id.editjobareaowner);
        postjobowner=(Button)findViewById(R.id.submijobdataowner);


        sessionForOwner=new SessionForOwner((getApplicationContext()));

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        postjobowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionForOwner.checkLogin();
                HashMap<String, String> user = sessionForOwner.getUserDetails();

                // name
                String name = user.get(SessionForOwner.KEY_NAME);

                // email
                String email = user.get(SessionForOwner.KEY_EMAIL);

                String role=user.get(SessionForOwner.KEY_ROLE);

                Log.d(TAG, "ROLE "+role);



                jobtitlebyowner= etjobtitlebyowner.getText().toString().trim();
                jobdetailsbyowner = etjobdetailsowner.getText().toString().trim();
                jobwagesbyowner = etjobwagesowner.getText().toString().trim();
                jobareabyowner = etjobareaowner.getText().toString().trim();
                Emailholder=email;
                NameHolder=name;
                roleholder=role;


                flag=0;
                if(etjobtitlebyowner.getText().toString().length()==0){
                    etjobtitlebyowner.setError(" Enter Job Title");
                    etjobtitlebyowner.requestFocus();
                    flag=1;

                }
                flag=0;
                if(etjobareaowner.getText().toString().length()==0){
                    etjobareaowner.setError(" Enter Job Area");
                    etjobareaowner.requestFocus();
                    flag=1;

                }
                flag=0;
                if(etjobdetailsowner.getText().toString().length()==0){
                    etjobdetailsowner.setError(" Enter Job Details");
                    etjobdetailsowner.requestFocus();
                    flag=1;

                }
                flag=0;
                if(etjobwagesowner.getText().toString().length()==0){
                    etjobwagesowner.setError(" Enter Job Wages");
                    etjobwagesowner.requestFocus();
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
       loader.show();
        Log.d(TAG, "Inserting Response:hiiiiiiiiiiiiiii ");
        Log.d(TAG, "ROLE "+roleholder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTJOB,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response:hiiiiiiiiiiiiiii " + response.toString());
                 loader.dismiss();
                //hideDialog();
                Log.i("bhagyaaaaaa", "["+response+"]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                   /* Toast.makeText(getApplicationContext(),
                            jsonObject.getString("message")+response,
                            Toast.LENGTH_LONG).show();*/
                    AlertDialog alertDialog = new AlertDialog.Builder(PostJobOwner.this).create();
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
                             loader.dismiss();
                            // hideDialog();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJobOwner.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                             loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJobOwner.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                             loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJobOwner.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                             loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJobOwner.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                             loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJobOwner.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("job_title", jobtitlebyowner);
                params.put("job_details", jobdetailsbyowner);
                params.put("job_wages", jobwagesbyowner);
                params.put("job_area", jobareabyowner);
                params.put("created_by", Emailholder);
                params.put("contractor_name", NameHolder);
                params.put("role",roleholder);



                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(PostJobOwner.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);


        etjobareaowner.setText("");
        etjobwagesowner.setText("");
        etjobtitlebyowner.setText("");
        etjobdetailsowner                .setText("");
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