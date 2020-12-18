package com.example.labourmangement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.labourmangement.Admin.UpdateMain;
import com.example.labourmangement.Contractor.ContractorProfile;
import com.example.labourmangement.Contractor.MainActivityContractorLogin;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Labour.LaborProfile;
import com.example.labourmangement.Labour.MainActivityLaourLogin;

public class SplashScreen extends AppCompatActivity {

    private boolean isFirstAnimation = false;
    TextView shantal;
    SessionManagerContractor sessionManagerContractor;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        Animation hold = AnimationUtils.loadAnimation(this, R.anim.hold);
        /* Translates ImageView from current position to its original position, scales it from
        larger scale to its original scale.*/
        final Animation translateScale = AnimationUtils.loadAnimation(this, R.anim.translate_scale);

        final ImageView imageView = findViewById(R.id.ivLogo);
shantal=findViewById(R.id.shantal);

sessionManagerContractor=new SessionManagerContractor(this);
sessionManager=new SessionManager(this);

        translateScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isFirstAnimation) {
                    imageView.clearAnimation();
                    if (sessionManagerContractor.get("role")=="contractor") {
                        // User is already logged in. Take him to main activity
                        Intent intent = new Intent(SplashScreen.this, MainActivityContractorLogin.class);
                        startActivity(intent);
                       // finish();
                        Log.e("","+contractor profile");
                    }
                    else if(sessionManager.get("role")=="labor"){
                        Intent intent = new Intent(SplashScreen.this, MainActivityLaourLogin.class);
                        startActivity(intent);
                        Log.e("","+contractor login");
                    }
//                    else if(sessionManager.isLoggedIn()){
//                        Intent intent = new Intent(SplashScreen.this, LaborProfile.class);
//                        startActivity(intent);
//                        Log.e("","+labor profile");
//                    }
//                    else if(!(sessionManager.isNotLoggedIn())){
//                        Intent intent = new Intent(SplashScreen.this, MainActivityLaourLogin.class);
//                        startActivity(intent);
//                        Log.e("","+labor login");
//                    }
                    else{
                        Intent intent = new Intent(SplashScreen.this, UpdateMain.class);
                        startActivity(intent);
                        Log.e("","+update");
                    }

                }

                isFirstAnimation = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        hold.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.clearAnimation();
                imageView.startAnimation(translateScale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(hold);

        //getSupportActionBar().hide();
        //StartAnimations();
        new Handler().postDelayed(new Runnable() {


            @Override public void run() {
                // This method will be executed once the timer is over
//                Intent i = new Intent(SplashScreen.this, MainActivity.class);
//                startActivity(i);
//                finish();
            }
        }, 2000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


}
