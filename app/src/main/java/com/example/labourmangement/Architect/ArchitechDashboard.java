package com.example.labourmangement.Architect;

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
import com.example.labourmangement.Contractor.ContractorProfile;
import com.example.labourmangement.DatabaseHelper.SessionForArch;
import com.example.labourmangement.R;

import java.util.HashMap;

public class ArchitechDashboard extends AppCompatActivity {
    ImageView btn_profile,btn_payment,btn_confirmation,btn_joboffer,btn_logout,btn_wages;
    SessionForArch sessionForArch;
    ProgressDialog progressDialog;
    public static int backPressed = 0;
    TextView contractorname,contractorid;
    private static final String TAG = ArchitechDashboard.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architech_dashboard);

        btn_profile=(ImageView)findViewById(R.id.btnarchprofile);
        btn_confirmation=(ImageView)findViewById(R.id.btnjobstatusarpprovearch);
        btn_joboffer=(ImageView)findViewById(R.id.btnjobpostarch);
        btn_logout=(ImageView)findViewById(R.id.btnlogoutarch);
          btn_payment=(ImageView)findViewById(R.id.btnremarkarch);
        btn_wages=(ImageView)findViewById(R.id.btnwagesarch);
        contractorname=(TextView)findViewById(R.id.textfetchusernamearch);
        contractorid=(TextView)findViewById(R.id.fetchuserIDarch);

        sessionForArch=new SessionForArch((getApplicationContext()));

        HashMap<String, String> user = sessionForArch.getUserDetails();

        // name
        String name = user.get(SessionForArch.KEY_NAME);

        // email
        String email = user.get(SessionForArch.KEY_EMAIL);

        Log.d(TAG,"Email"+email);
        Log.d(TAG,"Name"+name);

        contractorname.setText(name);
        contractorid.setText(email);


        progressDialog = new ProgressDialog(ArchitechDashboard.this);
        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ArchitechDashboard.this, PaymentStatusAR.class);
                startActivity(intent1);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {
                Intent intent1 = new Intent(ArchitechDashboard.this, ArchitechProfile.class);
                startActivity(intent1);
            }
        });
        btn_joboffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ArchitechDashboard.this, PostJObArchitech.class);
                startActivity(intent1);
            }
        });
        btn_wages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ArchitechDashboard.this, JobWagesArchitech.class);
                startActivity(intent1);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ArchitechDashboard.this, android.R.style.Theme_Translucent_NoTitleBar);
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
               // tvAlertMsg.setText("Confirmation Alert..!!!");
                //tvReason.setText("Are you sure want to logout?");
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                //set listener
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        sessionForArch.logoutUser();
                        startActivity(new Intent(ArchitechDashboard.this, UpdateMain.class));
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
                Intent intent1 = new Intent(ArchitechDashboard.this, AppliedJobsArchitecht.class);
                startActivity(intent1);
            }
        });


    }
    @Override
    public void onBackPressed() {
        backPressed = backPressed + 1;
        if (backPressed == 1) {
            Toast.makeText(ArchitechDashboard.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
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