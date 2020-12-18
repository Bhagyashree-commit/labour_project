package com.example.labourmangement.Labour;

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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
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
import com.example.labourmangement.Adapter.ProfileAdapter;
import com.example.labourmangement.Admin.UpdateMain;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseConfiguration.SharedPrefManager;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LaborProfile extends AppCompatActivity implements ProfileAdapter.OnItemClickListener{
    private static final String TAG = LaborProfile.class.getSimpleName();
   
    CustomLoader loader;
    ImageButton im_laborProfile,im_joboffer,btnjobstatus;
ImageView im_labor_logout,im_getwages,btnremark;
    SessionManager sessionManager;
    TextView usernamedisplay,userID,refname;
    public static int backPressed = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labor_profile);

           im_laborProfile = (ImageButton) findViewById(R.id.btnlaborprofile);
        im_labor_logout = (ImageView) findViewById(R.id.btnlogoutlabor);
        im_joboffer = (ImageButton) findViewById(R.id.btnlaborjoboffer);
        im_getwages = (ImageView) findViewById(R.id.btngetwages);
        // btnlivetracking=(ImageButton)findViewById(R.id.btnlivetracking);
        btnjobstatus=(ImageButton)findViewById(R.id.btnjobstatus);
        btnremark=(ImageView)findViewById(R.id.btnremark);

        // btnremark=(ImageButton)findViewById(R.id.btnremark);
        usernamedisplay=(TextView)findViewById(R.id.textfetchusername) ;
        userID=(TextView)findViewById(R.id.fetchuserID) ;
        refname=(TextView)findViewById(R.id.textrefname);


        sessionManager = new SessionManager((getApplicationContext()));
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        String reffname=user.get(SessionManager.KEY_REFCODE);


        Log.e(TAG, "name777777: " + name);


        usernamedisplay.setText(name);
        userID.setText(reffname);
        refname.setText(email);



        im_laborProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LaborProfile.this, LabourDashboard.class);
                startActivity(intent1);
            }
        });

        im_getwages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LaborProfile.this, GetWages.class);
                startActivity(intent1);
            }
        });

        btnremark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LaborProfile.this, WriteReview.class);
                startActivity(intent1);
            }
        });


        im_joboffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LaborProfile.this, JobOffer.class);
                startActivity(intent1);
            }
        });
/*
        btnlivetracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent1 = new Intent(LaborProfile.this, LiveTracking.class);
                //.startActivity(intent1);
            }
        });*/

        im_labor_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    //  sessionManager.logoutUser();
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(LaborProfile.this);
                alertDialog2.setTitle("Confirm LogOut...");
                alertDialog2.setMessage("Are you sure you want to Logout ?");

                alertDialog2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        sessionManager.logoutUser();

                        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                        editor.clear();
                        editor.commit();
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                        sendBroadcast(broadcastIntent);

                        Intent intent1 = new Intent(LaborProfile.this, MainActivity.class);
                        intent1.putExtra("finish", true);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);

                    }
                });

                // Setting Negative "NO" Button
                alertDialog2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                alertDialog2.show();
*/
                    //Dialog
                    final Dialog dialog = new Dialog(LaborProfile.this, android.R.style.Theme_Translucent_NoTitleBar);
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
                   // tvAlertMsg.setText("CONFIRMATION ALERT..!!!");
                   // tvReason.setText("ARE YOU SURE WANTR TO LOGOUT?");
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialog.show();

                    //set listener
                    tvYes.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(View v) {
                            sessionManager.logoutUser();
                            startActivity(new Intent(LaborProfile.this, UpdateMain.class));
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

        btnjobstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LaborProfile.this, JobStatusApproval.class);
                startActivity(intent1);
            }
        });


    }


    private void sendTokenToServer() {

        //  loader.show();

        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

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
                      //  Log.i("tokkkeeennnnnnn123", "["+response+"]");
                        loader.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(LaborProfile.this, obj.getString("message"), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(LaborProfile.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(LaborProfile.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(LaborProfile.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(LaborProfile.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(LaborProfile.this, "Error While Data Parsing", duration).show();

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
    public void onBackPressed() {
        backPressed = backPressed + 1;
        if (backPressed == 1) {
            Toast.makeText(LaborProfile.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
