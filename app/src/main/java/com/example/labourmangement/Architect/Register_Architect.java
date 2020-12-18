package com.example.labourmangement.Architect;

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
import com.example.labourmangement.DatabaseHelper.SessionForArch;
import com.example.labourmangement.DatabaseHelper.SessionForOwner;
import com.example.labourmangement.Owner.LoginOwner;
import com.example.labourmangement.Owner.OwnerDashboard;
import com.example.labourmangement.Owner.Register_Owner;
import com.example.labourmangement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register_Architect extends AppCompatActivity {
TextView alredyarchitecht;
    private static final String TAG = Register_Architect.class.getSimpleName();
    TextView alreadyauser;
    Button registerarch;
    EditText etarchname,etarchmobnum,etarchpassword,etarchconpass,et_refname,et_refcode;
    private ProgressDialog pDialog;
    private SessionForArch sessionForArch;
    private SQLiteHandler db;
    String  namePattern = "[a-zA-Z]+";
    String emailPattern ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; //"[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    int flag =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__architect);

        alredyarchitecht=findViewById(R.id.textnewarchitecht);

        etarchname=findViewById(R.id.etarchnameregister);
        etarchmobnum=findViewById(R.id.etarchmobnumrergister);
        etarchpassword=findViewById(R.id.etarchpasswordregister);
        etarchconpass=findViewById(R.id.etconpassarchregister);
        registerarch=findViewById(R.id.btn_archregister);
        et_refname=findViewById(R.id.editrefrednameA);
        et_refcode=findViewById(R.id.editrefercodeA);


        alredyarchitecht.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginArchitech.class);
                startActivity(i);
                finish();
            }
        });



        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        sessionForArch = new SessionForArch(getApplicationContext());
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (sessionForArch.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent= new Intent(Register_Architect.this,
                    ArchitechDashboard.class);
            startActivity(intent);
            finish();
        }





        registerarch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = etarchname.getText().toString().trim();
                String email = etarchmobnum.getText().toString().trim();
                String password = etarchpassword.getText().toString().trim();
                String cpassword = etarchconpass.getText().toString().trim();
                String refName = et_refname.getText().toString().trim();
                String refCode = et_refcode.getText().toString().trim();

                String role="Owner";


                if(etarchname.getText().toString().length()==0 || etarchname.getText().toString().trim().matches(namePattern)){
                    etarchname.setError(" User Name should be valid");
                    etarchname.requestFocus();


                }

                else if(etarchpassword.getText().toString().length()< 4 || etarchpassword.length()>10){
                    etarchpassword.setError(" Password should be between 4 to 10 character");
                    etarchpassword.requestFocus();
                }

               else if(etarchmobnum.getText().toString().length() < 10){
                    etarchmobnum.setError(" Mobile number should be valid");
                    etarchmobnum.requestFocus();
                }

              else if(cpassword.isEmpty() || !cpassword.equals(password))
                {
                    Toast.makeText(Register_Architect.this, "Password does not match", Toast.LENGTH_SHORT).show();

                }
             else{
                    registerUser(name,email,password,refName,refCode);
                }
            }
        });


    }

    private void registerUser(final String name, final String email,
                              final String password,final String refname,final String refcode) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        String role="Architect";
        pDialog.setMessage("Registering ...");
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
                        //String role=user.getString("role");

                        sessionForArch.createUserLoginSession(email,name,role,refName,refCode);
                        Log.d(TAG, "Email contractor"+email);
                        Log.d(TAG, "name contractor "+name);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(
                                Register_Architect.this,
                                LoginArchitech.class);

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
                    pDialog.dismiss();
                    // hideDialog();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Architect.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    System.out.println("AuthFailureError.........................." + error);
                    // hideDialog();
                    pDialog.dismiss();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Architect.this, "Your Are Not Authrized..", duration).show();
                } else if (error instanceof ServerError) {
                    System.out.println("server erroer......................." + error);
                    //hideDialog();
                    pDialog.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Architect.this, "Server Error", duration).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    System.out.println("NetworkError........................." + error);
                    //hideDialog();
                    pDialog.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Architect.this, "Please Check Your Internet Connection", duration).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    System.out.println("parseError............................." + error);
                    //hideDialog();
                    pDialog.dismiss();

                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(Register_Architect.this, "Error While Data Parsing", duration).show();

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