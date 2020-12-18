package com.example.labourmangement.Engineer;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labourmangement.Admin.UpdateMain;
import com.example.labourmangement.Architect.JobStatusApprovalEng;
import com.example.labourmangement.Architect.PaymentStatusAR;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseHelper.SessionForEngineer;
import com.example.labourmangement.R;

import java.util.HashMap;

public class EngineerDashboard extends AppCompatActivity {

    private static final String TAG = EngineerDashboard.class.getSimpleName();

ImageButton btnengprofile,btnengjoboffer,btnengjobstatus;
ImageView btnenggetwages,btnengremark,btnlogouteng;
CustomLoader loader;
TextView engname,engid;
    public static int backPressed = 0;

SessionForEngineer sessionForEngineer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineer_dashboard);


        btnengprofile=(ImageButton) findViewById(R.id.btnengprofile);
        btnengjoboffer=(ImageButton) findViewById(R.id.btnengjoboffer);
        btnengjobstatus=(ImageButton) findViewById(R.id.btnengjobstatus);
        btnlogouteng=(ImageView)findViewById(R.id.btnlogouteng);
        btnengremark=(ImageView)findViewById(R.id.btnengremark);
        btnenggetwages=(ImageView)findViewById(R.id.btnenggetwages);
        engname=(TextView)findViewById(R.id.textfetchusernameengineer);
        engid=(TextView)findViewById(R.id.fetchuserIDengineer);

        sessionForEngineer=new SessionForEngineer((getApplicationContext()));

        HashMap<String, String> user = sessionForEngineer.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);

        Log.d(TAG,"Email"+email);
        Log.d(TAG,"Name"+name);

        engname.setText(name);
        engid.setText(email);


        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        btnengprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(EngineerDashboard.this, EngProfile.class);
                startActivity(intent1);
            }
        });

        btnengjoboffer.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {
                Intent intent1 = new Intent(EngineerDashboard.this, EngJobOffer.class);
                startActivity(intent1);
            }
        });
        btnengjobstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(EngineerDashboard.this, JobStatusApprovalEng.class);
                startActivity(intent1);
            }
        });
        btnenggetwages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(EngineerDashboard.this, GetWagesEngineer.class);
                startActivity(intent1);
            }
        });

        btnlogouteng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EngineerDashboard.this, android.R.style.Theme_Translucent_NoTitleBar);
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
                        sessionForEngineer.logoutUser();
                        startActivity(new Intent(EngineerDashboard.this, UpdateMain.class));
                        finishAffinity();
                        dialog.dismiss();
                    }
                });


                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                       // finishAffinity();
                    }
                });

            }

        });
        btnengremark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(EngineerDashboard.this, PaymentStatusAR.class);
                startActivity(intent1);
            }
        });


    }
    @Override
    public void onBackPressed() {
        backPressed = backPressed + 1;
        if (backPressed == 1) {
            Toast.makeText(EngineerDashboard.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
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