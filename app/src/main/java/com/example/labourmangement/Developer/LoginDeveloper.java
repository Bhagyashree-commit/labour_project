package com.example.labourmangement.Developer;

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
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SQLiteHandler;
import com.example.labourmangement.DatabaseHelper.SessionForDeveloper;
import com.example.labourmangement.DatabaseHelper.SessionForOwner;
import com.example.labourmangement.Engineer.LoginEngineer;
import com.example.labourmangement.Labour.ForgetPassword;
import com.example.labourmangement.Owner.LoginOwner;
import com.example.labourmangement.Owner.OwnerDashboard;
import com.example.labourmangement.Owner.Register_Owner;
import com.example.labourmangement.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginDeveloper extends AppCompatActivity {
    TextView alrreadyuser,forgetpass;

    Button loginowner;
    private static final String TAG = LoginDeveloper.class.getSimpleName();
    private ProgressDialog pDialog;
    EditText edit_name, edit_password;
    SessionForDeveloper sessionForDeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_developer);


        alrreadyuser=findViewById(R.id.text_newDEVusernewlodin);
        forgetpass=findViewById(R.id.forgetpass);
        loginowner=findViewById(R.id.btnloginDEV);
        edit_name = (EditText) findViewById(R.id.et_loginDEVmobilenumber);
        edit_password = (EditText) findViewById(R.id.et_loginDEVpassword);

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionForDeveloper.set("Page","DevLogin");
                sessionForDeveloper.commit();
                Intent intent = new Intent(LoginDeveloper.this, ForgetPassword.class);
                startActivity(intent);

                //getforgetpass(email);
            }
        });
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        sessionForDeveloper = new SessionForDeveloper(getApplicationContext());

        HashMap<String, String> user = sessionForDeveloper.getUserDetails();

        // name
        String name = user.get(SessionForDeveloper.KEY_NAME);

        // email
        String email = user.get(SessionForDeveloper.KEY_EMAIL);

        //sessioncon.createUserLoginSession(email,name);

        pDialog=new ProgressDialog(LoginDeveloper.this);

        Log.d(TAG, "Email contractor555"+email);
        Log.d(TAG, "name contractor555 "+name);

        // Check if user is already logged in or not
        if (sessionForDeveloper.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginDeveloper.this, DeveloperDashboard.class);
            startActivity(intent);
            finish();
        }


        // initCreateAccountTextView();
        // initViews();
        // Login button Click Event
        loginowner.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = edit_name.getText().toString().trim();
                String password = edit_password.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // sessioncon.createUserLoginSession(email,name);
                    // textname.setText(name);
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });



        alrreadyuser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Register_developer.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        String role="Developer";
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGINCONTRACTOR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

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
                        sessionForDeveloper.createUserLoginSession(email,name,role,refcode,refname);

                        sessionForDeveloper.setLogin(true,email);


                        // Launch main activity
                        Intent intent = new Intent(LoginDeveloper.this,
                                DeveloperDashboard.class);
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
                    pDialog.dismiss();
                    // hideDialog();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(LoginDeveloper.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    System.out.println("AuthFailureError.........................." + error);
                    // hideDialog();
                    pDialog.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(LoginDeveloper.this, "Your Are Not Authrized..", duration).show();
                } else if (error instanceof ServerError) {
                    System.out.println("server erroer......................." + error);
                    //hideDialog();
                    pDialog.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(LoginDeveloper.this, "Server Error", duration).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    System.out.println("NetworkError........................." + error);
                    //hideDialog();
                    pDialog.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(LoginDeveloper.this, "Please Check Your Internet Connection", duration).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    System.out.println("parseError............................." + error);
                    //hideDialog();
                    pDialog.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(LoginDeveloper.this, "Error While Data Parsing", duration).show();

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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();

    }
}