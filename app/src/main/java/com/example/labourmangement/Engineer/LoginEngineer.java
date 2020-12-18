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
import com.example.labourmangement.Contractor.MainActivityContractorLogin;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForEngineer;
import com.example.labourmangement.Labour.ForgetPassword;
import com.example.labourmangement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginEngineer extends AppCompatActivity {
    TextView alreadyeng,forgetpass;
    Button loginengineer;
    private static final String TAG = LoginEngineer.class.getSimpleName();
    CustomLoader loader;
    private SessionForEngineer sessionForEngineer;
    EditText edit_name, edit_password,textname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_engineer);



        alreadyeng=findViewById(R.id.textView_new_usereng);
        loginengineer=findViewById(R.id.btn_loginengineer);

        edit_name = (EditText) findViewById(R.id.loginengname);
        edit_password = (EditText) findViewById(R.id.loginengpassword);
        forgetpass =  findViewById(R.id.forgetpass);
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionForEngineer.set("Page","EngLogin");
                sessionForEngineer.commit();
                Intent intent = new Intent(LoginEngineer.this, ForgetPassword.class);
                startActivity(intent);

                //getforgetpass(email);
            }
        });


        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        // Session manager
        sessionForEngineer = new SessionForEngineer(getApplicationContext());

        HashMap<String, String> user = sessionForEngineer.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);

        //sessioncon.createUserLoginSession(email,name);

        Log.d(TAG, "Email contractor555"+email);
        Log.d(TAG, "name contractor555 "+name);

        // Check if user is already logged in or not
        if (sessionForEngineer.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginEngineer.this, EngineerDashboard.class);
            startActivity(intent);
            finish();
        }


        // initCreateAccountTextView();
        // initViews();
        // Login button Click Event
        loginengineer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = edit_name.getText().toString().trim();
                String password = edit_password.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {

                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });


        ;

        alreadyeng.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Register_Engineer.class);
                startActivity(i);
                finish();
            }
        });


    }

    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        String role="Engineer";
        loader.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGINCONTRACTOR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
               loader.dismiss();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login sessio
                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String role = user.getString("role");
                        String created_at = user
                                .getString("created_at");
                        String refname=user.getString("ref_name");
                        String refcode=user.getString("ref_code");

                        // Inserting row in users table
                        //  sqliteHelper.addUser(name, email, uid, created_at);
                        sessionForEngineer.createUserLoginSession(email,name,role,refname,refcode);

                        sessionForEngineer.setLogin(true,email);


                        // Launch main activity
                        Intent intent = new Intent(LoginEngineer.this,
                                EngineerDashboard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    System.out.println("Time Out and NoConnection...................." + error);
                    loader.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(LoginEngineer.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    System.out.println("AuthFailureError.........................." + error);
                   loader.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(LoginEngineer.this, "Your Are Not Authrized..", duration).show();
                } else if (error instanceof ServerError) {
                    System.out.println("server erroer......................." + error);
                   loader.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(LoginEngineer.this, "Server Error", duration).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    System.out.println("NetworkError........................." + error);
                   loader.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(LoginEngineer.this, "Please Check Your Internet Connection", duration).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    System.out.println("parseError............................." + error);
                  loader.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(LoginEngineer.this, "Error While Data Parsing", duration).show();

                    //TODO
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("role",role);

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