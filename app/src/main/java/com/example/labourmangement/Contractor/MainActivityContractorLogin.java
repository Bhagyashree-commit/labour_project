package com.example.labourmangement.Contractor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.labourmangement.Admin.UpdateMain;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SQLiteHandler;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Labour.ForgetPassword;
import com.example.labourmangement.Labour.MainActivityLaourLogin;
import com.example.labourmangement.Labour.ResetPassword;
import com.example.labourmangement.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivityContractorLogin extends AppCompatActivity {

    private static final String TAG = MainActivityContractorLogin.class.getSimpleName();
    Button btnlogin;
    TextView textexistinguser;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;
    CustomLoader loader;
    private SessionManagerContractor sessioncon;
    // private SqliteHelper db;
    EditText edit_name, edit_password,textname;
    private SQLiteHandler db;
    TextView forgetpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contractor_login);
     //   getSupportActionBar().hide();

        btnlogin = (Button) findViewById(R.id.button_loginapp45);
        textexistinguser = (TextView) findViewById(R.id.textView_new_user2);
        edit_name = (EditText) findViewById(R.id.editTextLoginNameContractor);
        edit_password = (EditText) findViewById(R.id.editTextLoginpasswordContractor);
        forgetpass=findViewById(R.id.forgetpass);




        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        loader.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        sessioncon = new SessionManagerContractor(getApplicationContext());
        if (sessioncon.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(MainActivityContractorLogin.this,
                    ContractorProfile.class);
            startActivity(intent);
            finish();
        }


        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessioncon.set("Page","ContractorLogin");
                sessioncon.commit();
                Intent intent = new Intent(MainActivityContractorLogin.this, ForgetPassword.class);
                startActivity(intent);

                //getforgetpass(email);
            }
        });


        HashMap<String, String> user = sessioncon.getUserDetails();

        // name
        String name = user.get(SessionManagerContractor.KEY_NAME);

        // email
        String email = user.get(SessionManagerContractor.KEY_EMAIL);

        String role=user.get(SessionManagerContractor.KEY_ROLE);

        //sessioncon.createUserLoginSession(email,name);

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        Log.d(TAG, "Email contractor555"+email);
        Log.d(TAG, "name contractor555 "+name);
        Log.d(TAG, "ROLEEEEEE "+role);

        // Check if user is already logged in or not



        // initCreateAccountTextView();
        // initViews();
        // Login button Click Event
        btnlogin.setOnClickListener(new View.OnClickListener() {

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


        // Link to Register Screen
        textexistinguser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Register_contractor.class);
                startActivity(i);
                finish();
            }
        });

    }


    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        String role="contractor";
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
                        sessioncon.set("role",role);
                        sessioncon.commit();

                        // Inserting row in users table
                        //  sqliteHelper.addUser(name, email, uid, created_at);
                        sessioncon.createUserLoginSession(email,name,role,refcode,refname);

                        sessioncon.setLogin(true,email);


                        // Launch main activity
                        Intent intent = new Intent(MainActivityContractorLogin.this,
                                ContractorProfile.class);
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
                    // hideDialog();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MainActivityContractorLogin.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    System.out.println("AuthFailureError.........................." + error);
                    // hideDialog();
                    loader.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MainActivityContractorLogin.this, "Your Are Not Authrized..", duration).show();
                } else if (error instanceof ServerError) {
                    System.out.println("server erroer......................." + error);
                    //hideDialog();
                    loader.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MainActivityContractorLogin.this, "Server Error", duration).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    System.out.println("NetworkError........................." + error);
                    //hideDialog();
                    loader.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MainActivityContractorLogin.this, "Please Check Your Internet Connection", duration).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    System.out.println("parseError............................." + error);
                    //hideDialog();
                    loader.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MainActivityContractorLogin.this, "Error While Data Parsing", duration).show();

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
    @Override
    public void onBackPressed() {
       super. onBackPressed();
        Intent intent = new Intent(MainActivityContractorLogin.this, UpdateMain.class);
        startActivity(intent);
    }

}
