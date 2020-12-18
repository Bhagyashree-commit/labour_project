package com.example.labourmangement.Engineer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register_Engineer extends AppCompatActivity {
EditText engname,engphone,engpass,engconpass,et_rname,et_rcode;
Button btn_registeteng;
TextView alredyexist;
CustomLoader loader;
    String MobilePattern = "[0-9]{10}";
SessionForEngineer sessionForEngineer;
    String  namePattern = "[a-zA-Z]+";
    private static final String TAG = Register_Engineer.class.getSimpleName();

int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__engineer);

        alredyexist=findViewById(R.id.engineerexit);

        engname=findViewById(R.id.regi_engname);
        engphone=findViewById(R.id.regi_engphone);
        engpass=findViewById(R.id.regi_engpass);
        engconpass=findViewById(R.id.regi_engconpass);
        btn_registeteng=findViewById(R.id.btn_registereng);
        et_rname=findViewById(R.id.editrefrednameE);
        et_rcode=findViewById(R.id.editrefercodeE);



        alredyexist.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginEngineer.class);
                startActivity(i);
                finish();
            }
        });

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        // Session manager
        sessionForEngineer = new SessionForEngineer(getApplicationContext());

        if (sessionForEngineer.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register_Engineer.this,
                    EngineerDashboard.class);
            startActivity(intent);
            finish();
        }

        btn_registeteng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = engname.getText().toString().trim();
                String email = engphone.getText().toString().trim();
                String password = engpass.getText().toString().trim();
                String cpassword = engconpass.getText().toString().trim();
                String role="Engineer";
                String refName = et_rcode.getText().toString().trim();
                String refCode = et_rname.getText().toString().trim();


                flag=0;
                if(engname.getText().toString().length()==0){
                    engname.setError(" User Name should be valid");
                    engname.requestFocus();
                    flag=1;

                }

                else if(engpass.getText().toString().length()< 4 || engpass.length()>10){
                    engpass.setError(" Password should be between 4 to 10 character");
                    engpass.requestFocus();
                    flag=1;

                }


               else if(engphone.getText().toString().length() < 10){
                    engphone.setError(" Mobile number should be valid");
                    engphone.requestFocus();
                    flag=1;

                }
//                else if(engphone.getText().toString().matches(MobilePattern)){
//
//
//                    Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
//                    engphone.requestFocus();
//
//                    flag=1;
//
//                }
              else if(cpassword.isEmpty() || !cpassword.equals(password))
                {

                    Toast.makeText(Register_Engineer.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    flag=1;
                }
                else{
                    registerUser(name,email,password,refName,refCode);
                }

               /* if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name,email,password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }*/
            }
        });

    }

    private void registerUser(final String name, final String email,
                              final String password,final String refname,final String refcode) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        String role="Engineer";
       loader.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTERCONTRACTOR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
              loader.dismiss();
                Log.i("tagconvertstr", "["+response+"]");
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String role = user.getString("role");
                        //String role=user.getString("role");
                        String created_at = user
                                .getString("created_at");
                        String refName = user.getString("ref_name");
                        String refCode = user.getString("ref_code");

                        sessionForEngineer.createUserLoginSession(email,name,role,refName,refCode);
                        Log.d(TAG, "Email contractor"+email);
                        Log.d(TAG, "name contractor "+name);

                        // Inserting row in users table
                       // db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(
                                Register_Engineer.this,
                                LoginEngineer.class);

                        startActivity(intent);
                        finish();
                        // Launch login activity
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    System.out.println("Time Out and NoConnection...................." + error);
                    loader.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Engineer.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    System.out.println("AuthFailureError.........................." + error);
                  loader.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Engineer.this, "Your Are Not Authrized..", duration).show();
                } else if (error instanceof ServerError) {
                    System.out.println("server erroer......................." + error);
                   loader.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Engineer.this, "Server Error", duration).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    System.out.println("NetworkError........................." + error);
                   loader.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Engineer.this, "Please Check Your Internet Connection", duration).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    System.out.println("parseError............................." + error);
                    loader.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Engineer.this, "Error While Data Parsing", duration).show();

                    //TODO
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("role",role);
                params.put("ref_name",refname);
                params.put("ref_code",refcode);

                return params;
            }

        };

        // Adding request to request queue
        // AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        // requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(strReq);
    }


}