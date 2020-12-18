package com.example.labourmangement.Developer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labourmangement.Admin.UpdateMain;
import com.example.labourmangement.Architect.ArchitechDashboard;
import com.example.labourmangement.DatabaseHelper.SessionForDeveloper;
import com.example.labourmangement.Labour.WriteReview;
import com.example.labourmangement.Owner.AppliedJobOwner;
import com.example.labourmangement.Owner.WagesByOwner;
import com.example.labourmangement.R;

import java.util.HashMap;

public class DeveloperDashboard extends AppCompatActivity {
    ImageView btn_profile,btn_paystatus,btn_confirmation,btn_joboffer,btn_logout,btn_wages;
    SessionForDeveloper sessionForDeveloper;
    ProgressDialog progressDialog;
    TextView contractorname,contractorid;
    public static int backPressed = 0;
    private static final String TAG = DeveloperDashboard.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_dashboard);


        btn_profile=(ImageView)findViewById(R.id.btndevprofile);
        btn_confirmation=(ImageView)findViewById(R.id.btndevjobstatus);
        btn_joboffer=(ImageView)findViewById(R.id.btndevjoboffer);
        btn_logout=(ImageView)findViewById(R.id.btnlogoutdev);
        btn_wages=(ImageView)findViewById(R.id.btndevgetwages);
        contractorname=(TextView)findViewById(R.id.textfetchusernamedeveloper);
        contractorid=(TextView)findViewById(R.id.fetchuserIDdeveloper);
        btn_paystatus=(ImageView) findViewById(R.id.btndevremark);

        sessionForDeveloper=new SessionForDeveloper((getApplicationContext()));

        HashMap<String, String> user = sessionForDeveloper.getUserDetails();

        // name
        String name = user.get(SessionForDeveloper.KEY_NAME);

        // email
        String email = user.get(SessionForDeveloper.KEY_EMAIL);

        Log.d(TAG,"Email"+email);
        Log.d(TAG,"Name"+name);

        contractorname.setText(name);
        contractorid.setText(email);


        progressDialog = new ProgressDialog(DeveloperDashboard.this);

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {
                Intent intent1 = new Intent(DeveloperDashboard.this, DeveloperProfile.class);
                startActivity(intent1);
            }
        });
        btn_joboffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DeveloperDashboard.this, DevJObOffer.class);
                startActivity(intent1);
            }
        });
        btn_paystatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DeveloperDashboard.this, WriteReview.class);
                startActivity(intent1);
            }
        });

        btn_wages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DeveloperDashboard.this, WagesByOwner.class);
                startActivity(intent1);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(DeveloperDashboard.this, android.R.style.Theme_Translucent_NoTitleBar);
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
               // tvReason.setText("ARE YOU SURE YOU WANT TO LOGOUT?");
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                //set listener
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        sessionForDeveloper.logoutUser();
                        startActivity(new Intent(DeveloperDashboard.this, UpdateMain.class));
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
                Intent intent1 = new Intent(DeveloperDashboard.this, AppliedJobOwner.class);
                startActivity(intent1);
            }
        });


    }
    @Override
    public void onBackPressed() {
        backPressed = backPressed + 1;
        if (backPressed == 1) {
            Toast.makeText(DeveloperDashboard.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
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