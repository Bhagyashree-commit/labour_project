package com.example.labourmangement.Contractor;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.labourmangement.DatabaseHelper.SQLiteHandler;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register_contractor extends AppCompatActivity {
    private static final String TAG = Register_contractor.class.getSimpleName();

    Button btnregister;
    TextView alreadyauser;
    EditText et_username,et_emailid,et_password ,et_mobilenumber,et_conpassword,et_referralcode,et_referralname;
    CustomLoader loader;
    private SessionManagerContractor sessioncon;
    private SQLiteHandler db;
    String  namePattern = "[a-zA-Z]+";
    String MobilePattern = "[0-9]{10}";
    String emailPattern ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; //"[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    int flag =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_contractor);
//        getSupportActionBar().hide();
        btnregister=(Button)findViewById(R.id.button_registerapp);
        alreadyauser=(TextView)findViewById(R.id.textView_existing_user);
        et_emailid=(EditText)findViewById(R.id.editTextregisteremail);
        et_password=(EditText)findViewById(R.id.editTextregisterpassword);
        et_username=(EditText)findViewById(R.id.editTextregistername);
        et_conpassword=(EditText)findViewById(R.id.editTextregisterconfirmpassword);
        et_referralcode=(EditText)findViewById(R.id.editrefercodeC);
        et_referralname=(EditText)findViewById(R.id.editrefrednameC);


        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        loader.setCancelable(false);

        // Session manager
        sessioncon = new SessionManagerContractor(getApplicationContext());
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
//        if (sessioncon.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(Register_contractor.this,
//                    ContractorProfile.class);
//            startActivity(intent);
//            finish();
//        }


        // Register Button Click event
        btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = et_username.getText().toString().trim();
                String email = et_emailid.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String cpassword = et_conpassword.getText().toString().trim();
                String refName = et_referralname.getText().toString().trim();
                String refCode = et_referralcode.getText().toString().trim();

                //flag=0;
                if(et_username.getText().toString().length()==0 ){
                    et_username.setError(" User Name should be valid");
                    et_username.requestFocus();
                  //  flag=1;

                }

                else if(et_password.getText().toString().length()< 4 || et_password.length()>10){
                    et_password.setError(" Password should be between 4 to 10 character");
                    et_password.requestFocus();
                    //flag=1;

                }

                else if(et_emailid.getText().toString().trim().length()<10){
                    Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
                    et_emailid.requestFocus();
                   // flag=1;

                }

               else if(cpassword.isEmpty() || !cpassword.equals(password))
                {

                    Toast.makeText(Register_contractor.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    //flag=1;
                }
                else{
                    registerUser(name,email,password,refName,refCode);
                }
            }
        });

        // Link to Login Screen
        alreadyauser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivityContractorLogin.class);
                startActivity(i);
                finish();
            }
        });
    }






    private void registerUser(final String name, final String email,
                              final String password,final String refname,final String refcode) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
String role="contractor";

        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTERCONTRACTOR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                Log.i("tagconvertstr", "["+response+"]");
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                       // String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String role = user.getString("role");


                        String refName = user.getString("ref_name");
                        String refCode = user.getString("ref_code");

                        sessioncon.createUserLoginSession(email,name,role,refName,refCode);
                        Log.d(TAG, "Email contractor"+email);
                        Log.d(TAG, "name contractor "+name);
                        Log.d(TAG, "role contractor "+role);
                        // Inserting row in users table
                     //   db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(
                                Register_contractor.this,
                                MainActivityContractorLogin.class);

                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
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
                    // hideDialog();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_contractor.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    System.out.println("AuthFailureError.........................." + error);
                    // hideDialog();
                    loader.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_contractor.this, "Your Are Not Authrized..", duration).show();
                } else if (error instanceof ServerError) {
                    System.out.println("server erroer......................." + error);
                    //hideDialog();
                    loader.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_contractor.this, "Server Error", duration).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    System.out.println("NetworkError........................." + error);
                    //hideDialog();
                    loader.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_contractor.this, "Please Check Your Internet Connection", duration).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    System.out.println("parseError............................." + error);
                    //hideDialog();
                    loader.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_contractor.this, "Error While Data Parsing", duration).show();

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

        private void showDialog() {
            if (!loader.isShowing())
                loader.show();
        }

        private void hideDialog() {
            if (loader.isShowing())
                loader.dismiss();
        }


}
