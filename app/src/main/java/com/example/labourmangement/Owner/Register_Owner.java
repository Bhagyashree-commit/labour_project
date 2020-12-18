package com.example.labourmangement.Owner;

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
import com.example.labourmangement.Contractor.ContractorProfile;
import com.example.labourmangement.Contractor.MainActivityContractorLogin;
import com.example.labourmangement.Contractor.Register_contractor;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SQLiteHandler;
import com.example.labourmangement.DatabaseHelper.SessionForOwner;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Labour.MainActivityLaourLogin;
import com.example.labourmangement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register_Owner extends AppCompatActivity {
    private static final String TAG = Register_Owner.class.getSimpleName();
    TextView alreadyauser;
    Button registerowner;
    EditText etownername,etownermobnum,etownerpassword,etownerconpass,et_referralcode,et_referralname;
   CustomLoader loader;
    private SessionForOwner sessionown;
    private SQLiteHandler db;
    String  namePattern = "[a-zA-Z]+";
    String emailPattern ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; //"[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    int flag =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__owner);

            alreadyauser=findViewById(R.id.text_existingownerowner);
            etownername=findViewById(R.id.et_registerownername);
            etownermobnum=findViewById(R.id.et_registerowneremail);
            etownerpassword=findViewById(R.id.et_registerownerpassword);
            etownerconpass=findViewById(R.id.et_ownerregisterownerconfirmpass);
            registerowner=findViewById(R.id.btn_registerowner);
        et_referralcode=(EditText)findViewById(R.id.editrefercodeO);
        et_referralname=(EditText)findViewById(R.id.editrefrednameO);



        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        // Session manager
        sessionown = new SessionForOwner(getApplicationContext());
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (sessionown.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register_Owner.this,
                    OwnerDashboard.class);
            startActivity(intent);
            finish();
        }





        registerowner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = etownername.getText().toString().trim();
                String email = etownermobnum.getText().toString().trim();
                String password = etownerpassword.getText().toString().trim();
                String cpassword = etownerpassword.getText().toString().trim();
                String role="Owner";
                String refName = et_referralname.getText().toString().trim();
                String refCode = et_referralcode.getText().toString().trim();

                if(etownername.getText().toString().length()==0 || etownername.getText().toString().trim().matches(namePattern)){
                    etownername.setError(" User Name should be valid");
                    etownername.requestFocus();
                }

                else if(etownerpassword.getText().toString().length()< 4 || etownerpassword.length()>10){
                    etownerpassword.setError(" Password should be between 4 to 10 character");
                    etownerpassword.requestFocus();
                }

               else if(etownermobnum.getText().toString().length() < 10){
                    etownermobnum.setError(" Mobile number should be valid");
                    etownermobnum.requestFocus();
                }

              else if(cpassword.isEmpty() || !cpassword.equals(password))
                {
                    Toast.makeText(Register_Owner.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    flag=1;
                }
                else {
                    registerUser(name,email,password,refName,refCode);
                }

            }
        });

        alreadyauser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginOwner.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void registerUser(final String name, final String email,
                              final String password,final String refname,final String refcode) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        String role="Owner";
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

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String role = user.getString("role");
                        //String role=user.getString("role");

                        String refName=user.getString("ref_name");
                        String refCode=user.getString("ref_code");

                        sessionown.createUserLoginSession(email,name,role,refName,refCode);
                        Log.d(TAG, "Email contractor"+email);
                        Log.d(TAG, "name contractor "+name);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(
                                Register_Owner.this,
                                LoginOwner.class);

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
                    Toast.makeText(Register_Owner.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    System.out.println("AuthFailureError.........................." + error);
                    loader.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Owner.this, "Your Are Not Authrized..", duration).show();
                } else if (error instanceof ServerError) {
                    System.out.println("server erroer......................." + error);
                    loader.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Owner.this, "Server Error", duration).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    System.out.println("NetworkError........................." + error);
                    loader.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Owner.this, "Please Check Your Internet Connection", duration).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    System.out.println("parseError............................." + error);
                    loader.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Owner.this, "Error While Data Parsing", duration).show();

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