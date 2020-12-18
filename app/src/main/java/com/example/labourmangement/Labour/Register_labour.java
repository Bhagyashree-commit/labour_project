package com.example.labourmangement.Labour;

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
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SQLiteHandler;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Register_labour extends AppCompatActivity {
    private static final String TAG = Register_labour.class.getSimpleName();

    TextView alreadyauser;
    EditText et_username,et_emailid,et_password ,et_mobilenumber,et_conpassword,et_referralcode,et_referralname;
    // SqliteHelper sqliteHelper;
   CustomLoader loader;
    private SessionManager session;
    private SQLiteHandler db;
    Button btnregister;
    ProgressDialog progressDialog;
    String  namePattern = "[a-zA-Z]+";
    String MobilePattern = "[0-9]{10}";
    String emailPattern ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; //"[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    int flag =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_labour);
        //getSupportActionBar().hide();

        btnregister=(Button)findViewById(R.id.button_registerapp12);
        alreadyauser=(TextView)findViewById(R.id.textView_existing_user);
        et_emailid=(EditText)findViewById(R.id.editTextregisteremail);
        et_password=(EditText)findViewById(R.id.editTextregisterpassword);
        et_username=(EditText)findViewById(R.id.editTextregistername);
        et_conpassword=(EditText)findViewById(R.id.editTextregisterconfirmpassword11);
        et_referralcode=(EditText)findViewById(R.id.editrefercode);
        et_referralname=(EditText)findViewById(R.id.editrefredname);


        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        // Session manager
        session = new SessionManager(getApplicationContext());
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());


        //initTextViewLogin();
        //  initViews();
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register_labour.this,
                    LaborProfile.class);
            startActivity(intent);
            finish();
        }


        // Register Button Click event
        btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = et_username.getText().toString().trim();
                String email = et_emailid.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String refCode = et_referralcode.getText().toString().trim();
                String refName = et_referralname.getText().toString().trim();
                String confirmpass = et_conpassword.getText().toString().trim();
               // flag=0;
                if(et_username.getText().toString().length()==0 || et_username.getText().toString().trim().matches(namePattern)){
                    et_username.setError(" User Name should be valid");
                    et_username.requestFocus();
                   // flag=1;

                }

                else if(et_password.getText().toString().length()< 4 || et_password.length()>10){
                    et_password.setError(" Password should be between 4 to 10 character");
                    et_password.requestFocus();
                   // flag=1;

                }


               else if(et_emailid.getText().toString().length() < 10){
                    et_emailid.setError(" Mobile number should be valid");
                    et_emailid.requestFocus();
                   // flag=1;

                }
//
                else if(confirmpass.isEmpty() || !confirmpass.equals(password))
                {
                    Toast.makeText(Register_labour.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    //flag=1;
                }

              else  if(et_referralname.getText().toString().length()==0 || et_referralname.getText().toString().trim().matches(namePattern)){
                    et_referralname.setError(" Referral Name should be valid");
                    et_referralname.requestFocus();
                   // flag=1;

                }

               else if(et_referralcode.getText().toString().length()==0){
                    et_referralcode.setError(" Referral Mobile number should be valid");
                    et_referralcode.requestFocus();
                    //flag=1;

                }
                else {
                    registerUser(name,email,password,refName,refCode);
                }
            }
        });

        // Link to Login Screen
        alreadyauser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivityLaourLogin.class);
                startActivity(i);
                finish();
            }
        });
    }






    private void registerUser(final String name, final String email,
                              final String password,final String refname, final String refcode) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        String role="labor";

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

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String role = user.getString("role");
                        String refName = user.getString("ref_name");
                        String refCode = user.getString("ref_code");

                        session.createUserLoginSession(email,name,role,refName,refCode);
                        Log.d(TAG, "Email "+email);
                        Log.d(TAG, "name "+name);
                        // Inserting row in users table
                     //   db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
                        Bundle extras = new Bundle();
                        extras.putString("email", email);
                        extras.putString("name", name);
                       // Log.d(TAG, "NAME454 " + uname);

                        // Launch login activity
                        Intent intent = new Intent(
                                Register_labour.this,
                                MainActivityLaourLogin.class);
                        intent.putExtras(extras);

                        intent.putExtra("name", name);

                        startActivity(intent);
                        finish();
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
                    progressDialog.dismiss();
                    // hideDialog();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_labour.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    System.out.println("AuthFailureError.........................." + error);
                    // hideDialog();
                    progressDialog.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_labour.this, "Your Are Not Authrized..", duration).show();
                } else if (error instanceof ServerError) {
                    System.out.println("server erroer......................." + error);
                    //hideDialog();
                    progressDialog.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_labour.this, "Server Error", duration).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    System.out.println("NetworkError........................." + error);
                    //hideDialog();
                    progressDialog.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_labour.this, "Please Check Your Internet Connection", duration).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    System.out.println("parseError............................." + error);
                    //hideDialog();
                    progressDialog.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_labour.this, "Error While Data Parsing", duration).show();

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
        RequestQueue requestQueue= Volley.newRequestQueue(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
