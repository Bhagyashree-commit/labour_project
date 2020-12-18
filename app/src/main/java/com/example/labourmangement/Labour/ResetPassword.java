package com.example.labourmangement.Labour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.labourmangement.Architect.LoginArchitech;
import com.example.labourmangement.Contractor.MainActivityContractorLogin;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForArch;
import com.example.labourmangement.DatabaseHelper.SessionForDeveloper;
import com.example.labourmangement.DatabaseHelper.SessionForEngineer;
import com.example.labourmangement.DatabaseHelper.SessionForOwner;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Developer.LoginDeveloper;
import com.example.labourmangement.Engineer.LoginEngineer;
import com.example.labourmangement.Owner.LoginOwner;
import com.example.labourmangement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {
    private static final String TAG = ResetPassword.class.getSimpleName();
EditText newpass,conpass;
Button resetpass;
CustomLoader loader;
SessionManagerContractor sessionManagerContractor;
SessionManager sessionManager;
SessionForArch sessionForArch;
SessionForEngineer sessionForEngineer;
SessionForDeveloper sessionForDeveloper;
SessionForOwner sessionForOwner;

String mobnum,newPassword,conPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        resetpass=findViewById(R.id.btn_resetpass);
        newpass=findViewById(R.id.edit_newpass);
        conpass=findViewById(R.id.edit_conpass);
        sessionManagerContractor=new SessionManagerContractor(this);
        sessionManager =new SessionManager(this);
        sessionForArch =new SessionForArch(this);
        sessionForEngineer =new SessionForEngineer(this);
        sessionForDeveloper =new SessionForDeveloper(this);
        sessionForOwner =new SessionForOwner(this);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobnum =getIntent().getStringExtra("mobilenumber");
                newPassword=newpass.getText().toString().trim();
                conPassword=conpass.getText().toString().trim();


                if (newpass.getText().toString().isEmpty() && conpass.getText().toString().isEmpty()) {
                    newpass.setFocusableInTouchMode(true);
                    newpass.setFocusable(true);
                    newpass.requestFocus();

                }

                else if(newpass.getText().toString().length()<4  || newpass.length()>10){
                    newpass.setError(" Password should be between 4 to 10 character");
                    newpass.requestFocus();
                }
                else if(newpass.getText().toString().isEmpty() || !conpass.getText().toString().equals(conPassword))
                {
                    Toast.makeText(ResetPassword.this, "Password does not match", Toast.LENGTH_SHORT).show();

                }
                else {

                    resetpass(mobnum,newPassword);
                }

            }
        });
    }

    private void resetpass(final String mobnum, final String newPassword) {
        loader.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_RESETPASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Reset Response: " + response.toString());
               loader.cancel();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("success").equalsIgnoreCase("1")) {
                        String mgs= jsonObject.getString("message");

                        Toast.makeText(getApplicationContext(),
                                "Password changed  Successfully!!! Go Back... and Try Login now", Toast.LENGTH_LONG).show();
Log.e("session contractor",""+sessionManagerContractor.get("Page"));
Log.e("session labor",""+sessionManager.get("Page"));
if(sessionManagerContractor.get("Page").equalsIgnoreCase("ContractorLogin")){
    Intent intent = new Intent(ResetPassword.this, MainActivityContractorLogin.class);
    startActivity(intent);
}
else if(sessionManager.get("Page").equalsIgnoreCase("LaborLogin")){
    Intent intent = new Intent(ResetPassword.this, MainActivityLaourLogin.class);
    startActivity(intent);
}
else if(sessionForArch.get("Page").equalsIgnoreCase("ArchLogin")){
    Intent intent = new Intent(ResetPassword.this, LoginArchitech.class);
    startActivity(intent);
}
else if(sessionForEngineer.get("Page").equalsIgnoreCase("EngLogin")){
    Intent intent = new Intent(ResetPassword.this, LoginEngineer.class);
    startActivity(intent);
}
else if(sessionForDeveloper.get("Page").equalsIgnoreCase("DevLogin")){
    Intent intent = new Intent(ResetPassword.this, LoginDeveloper.class);
    startActivity(intent);
}
else if(sessionForOwner.get("Page").equalsIgnoreCase("OwnLogin")){
    Intent intent = new Intent(ResetPassword.this, LoginOwner.class);
    startActivity(intent);
}
                        newpass.setText("");
                        conpass.setText("");

                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Some Error Occured.. Please Try later...",
                                Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
              loader.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",mobnum );
                params.put("encrypted_password", newPassword);
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
}