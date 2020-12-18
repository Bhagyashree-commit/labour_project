package com.example.labourmangement.Admin;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.labourmangement.Architect.CheckPaymentStatus;
import com.example.labourmangement.Architect.Register_Architect;
import com.example.labourmangement.Contractor.ContractorProfile;
import com.example.labourmangement.Contractor.PostJobs;
import com.example.labourmangement.Contractor.Register_contractor;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Developer.Register_developer;
import com.example.labourmangement.Engineer.Register_Engineer;
import com.example.labourmangement.Labour.Register_labour;
import com.example.labourmangement.Owner.Register_Owner;
import com.example.labourmangement.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UpdateMain extends AppCompatActivity {
    ConstraintLayout animContractor;
    ConstraintLayout animOwner;
    ConstraintLayout animArchitect;
    ImageView shareApp;
    SessionManager sessionManager;

    private static final String TAG = UpdateMain.class.getSimpleName();
    RelativeLayout relContractor;
    RelativeLayout relLabourer;
    RelativeLayout relArchitect;
    RelativeLayout relowner;
    RelativeLayout reldeveloper;
    RelativeLayout relengineer;
    TextView openLink;
    //Spinner langauge;
    ArrayAdapter<String> spinnerArrayAdapter;
Spinner langspinner;
SessionManagerContractor sessionManagerContractor;

    Locale myLocale;
    String currentLanguage ;
    public static int backPressed = 0;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.update_main_activity);
sessionManager=new SessionManager(this);
sessionManagerContractor=new SessionManagerContractor(this);

        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.select));
        list.add("English");
        list.add("हिंदी");
        list.add("मराठी");

        //currentLanguage = getIntent().getStringExtra(currentLanguage);
        animContractor = findViewById(R.id.contract);
        animOwner = findViewById(R.id.owner);
        animArchitect = findViewById(R.id.archi);
        openLink = findViewById(R.id.open_site);
        //langauge=findViewById(R.id.spin_language);

        relContractor = findViewById(R.id.relContractor);
        relLabourer = findViewById(R.id.relLabourer);
        relArchitect = findViewById(R.id.relArchitect);
        relowner = findViewById(R.id.relowner);
        reldeveloper = findViewById(R.id.reldeveloper);
        relengineer = findViewById(R.id.relengineer);

         langspinner = findViewById(R.id.spin_language);
        shareApp = findViewById(R.id.shareApp);

        ObjectAnimator anim = ObjectAnimator.ofInt(openLink, "backgroundColor", Color.WHITE, Color.RED,
                Color.YELLOW);
        anim.setDuration(1500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langspinner.setAdapter(adapter);

        langspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:

                        break;
                    case 1:
                        sessionManager.set("Lang","en");
                        sessionManager.commit();
                        sessionManagerContractor.set("Lang","en");
                        sessionManagerContractor.commit();

                        setLocale("en");

                        break;
                    case 2:
                        sessionManager.set("Lang","hi");
                        sessionManager.commit();
                        sessionManagerContractor.set("Lang","hi");
                        sessionManagerContractor.commit();
                        setLocale("hi");

                        break;
                    case 3:
                        sessionManager.set("Lang","mar");
                        sessionManager.commit();
                        sessionManagerContractor.set("Lang","mar");
                        sessionManagerContractor.commit();
                        setLocale("mar");

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Insert Subject here");
                String app_url = " https://drive.google.com/file/d/1qnIAtbiBw4St_HKagdUE5-2-VFlfLlOc/view?usp=sharing";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
        animContractor.startAnimation(inFromLeftAnimation());
        animOwner.startAnimation(inFromLeftAnimation());
        animArchitect.startAnimation(inFromRightAnimation());

        relContractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionManagerContractor.get("Lang").isEmpty()){
                    Toast.makeText(UpdateMain.this, "plz select lang", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.e("ttttttt",sessionManager.get("Lang"));
                    Intent i = new Intent(UpdateMain.this,
                        Register_contractor.class);
                startActivity(i);
                }
            }
        });
        openLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        OpenWebsite.class);
                startActivity(i);
            }
        });

        relLabourer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionManager.get("Lang").isEmpty()){
                    Toast.makeText(UpdateMain.this, "plz select lang", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("testingalgfg11",""+sessionManager.get("Lang"));
                    Intent i = new Intent(getApplicationContext(),
                            Register_labour.class);
                    startActivity(i);
                }


            }
        });



        relowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),
//                        Register_Owner.class);
//                startActivity(i);
                AlertDialog alertDialog = new AlertDialog.Builder(UpdateMain.this).create();

                alertDialog.setMessage("Comming Soon");
                alertDialog.setIcon(R.drawable.done);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        relArchitect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),
//                        Register_Architect.class);
//                startActivity(i);
                AlertDialog alertDialog = new AlertDialog.Builder(UpdateMain.this).create();

                alertDialog.setMessage("Comming Soon");
                alertDialog.setIcon(R.drawable.done);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        });

        relengineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),
//                        Register_Engineer.class);
//                startActivity(i);

                AlertDialog alertDialog = new AlertDialog.Builder(UpdateMain.this).create();

                alertDialog.setMessage("Comming Soon");
                alertDialog.setIcon(R.drawable.done);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        });

        reldeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),
//                        Register_developer.class);
//                startActivity(i);
                AlertDialog alertDialog = new AlertDialog.Builder(UpdateMain.this).create();

                alertDialog.setMessage("Comming Soon");
                alertDialog.setIcon(R.drawable.done);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(700);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(700);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, UpdateMain.class);
            refresh.putExtra(currentLanguage, localeName);
            startActivity(refresh);
        } else {
            Toast.makeText(UpdateMain.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        backPressed = backPressed + 1;
        if (backPressed == 1) {
            Toast.makeText(UpdateMain.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
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