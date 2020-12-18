package com.example.labourmangement.Contractor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.example.labourmangement.Admin.UpdateMain;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseConfiguration.SharedPrefManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.R;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContractorProfile extends AppCompatActivity {
    ImageView btn_profile,btn_tracking,btn_confirmation,btn_joboffer,btn_logout,btn_wages,btnpaystatus,btnalljobs;
    SessionManagerContractor sessionManagerContractor;
  //  loader loader;
    CustomLoader loader;
    TextView contractorname,contractorid;
    public static int backPressed = 0;
    private static final String TAG = ContractorProfile.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_profile);

        /*getSupportActionBar().setTitle("CONTRACTOR DASHBOARD");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.blue_900));
*/
        btn_profile=(ImageView)findViewById(R.id.btnprofile);
        btn_confirmation=(ImageView)findViewById(R.id.btnconfirmation);
        btn_joboffer=(ImageView)findViewById(R.id.btnjobpost);
        btn_logout=(ImageView)findViewById(R.id.btnlogoutcontractor);
        btn_tracking=(ImageView)findViewById(R.id.btnlivetracklabor);
        btn_wages=(ImageView)findViewById(R.id.btnwages);
        btnalljobs=(ImageView)findViewById(R.id.alljobs);
        btnpaystatus=(ImageView)findViewById(R.id.paystatus1);
        contractorname=(TextView)findViewById(R.id.textfetchusernamecontractor);


        sessionManagerContractor=new SessionManagerContractor((getApplicationContext()));

        HashMap<String, String> user = sessionManagerContractor.getUserDetails();

        // name
        String name = user.get(SessionManagerContractor.KEY_NAME);

        // email
        String email = user.get(SessionManagerContractor.KEY_EMAIL);

        Log.d(TAG,"Email"+email);
        Log.d(TAG,"Name"+name);

 contractorname.setText(name);


       FirebaseMessaging.getInstance().setAutoInitEnabled(true);


      //  String token = FirebaseInstanceId.getInstance().getToken();
      //  sendTokenToServer();
       // SharedPrefManager.getInstance(ContractorProfile.this).saveDeviceToken(token);
  //     Log.e(TAG, "Firebase token saved: " + token);

       // loader = new loader(ContractorProfile.this);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {
                Intent intent1 = new Intent(ContractorProfile.this, ContractorDashboard.class);
                startActivity(intent1);
            }
        });
        btn_joboffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ContractorProfile.this, PostJobs.class);
                startActivity(intent1);
            }
        });
        btn_wages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ContractorProfile.this, JobWages.class);
                startActivity(intent1);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ContractorProfile.this, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alertyesno);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);


                //findId
                TextView tvYes = (TextView) dialog.findViewById(R.id.tvOk);
                TextView tvCancel = (TextView) dialog.findViewById(R.id.tvcancel);
                TextView tvReason = (TextView) dialog.findViewById(R.id.textView22);
                TextView tvAlertMsg = (TextView) dialog.findViewById(R.id.tvAlertMsg);

                //set value
                //tvAlertMsg.setText("CONFIRMATION ALERT..!!!");
               // tvReason.setText("ARE YOU SURE WANT TO LOGOUT?");
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                //set listener
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        sessionManagerContractor.logoutUser();
                        startActivity(new Intent(ContractorProfile.this, UpdateMain.class));
                        finishAffinity();
                        dialog.dismiss();
                    }
                });


                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }

        });
        btn_confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ContractorProfile.this, AppliedJobs.class);
                startActivity(intent1);
            }
        });
        btn_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ContractorProfile.this, TrackLabor.class);
                startActivity(intent1);
            }
        });

        btnpaystatus.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {
                Intent intent1 = new Intent(ContractorProfile.this, PaymentStatusContractor.class);
                startActivity(intent1);
            }
        });
        btnalljobs.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {
                Intent intent1 = new Intent(ContractorProfile.this, AllJobs.class);
                startActivity(intent1);
            }
        });

    }



    private void sendTokenToServer() {



        HashMap<String, String> user = sessionManagerContractor.getUserDetails();

        // name
        String name = user.get(SessionManagerContractor.KEY_NAME);

        // email
        String email = user.get(SessionManagerContractor.KEY_EMAIL);

        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        //  email = editTextEmail.getText().toString();

        // String token= textViewToken.getText().toString();

        if (token == null) {
            loader.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("tokkkeeennnnnnn123", "["+response+"]");
                        loader.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(ContractorProfile.this, obj.getString("message"), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(ContractorProfile.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ContractorProfile.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ContractorProfile.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ContractorProfile.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ContractorProfile.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobilenum", email);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        backPressed = backPressed + 1;
        if (backPressed == 1) {
            Toast.makeText(ContractorProfile.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new CountDownTimer(5000, 1000) { // adjust the milli seconds here
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() { backPressed = 0;
                }
            }.start();
        }
        if (backPressed == 2) {
            backPressed = 0;
            finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


}
