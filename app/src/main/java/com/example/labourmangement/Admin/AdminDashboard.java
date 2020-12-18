package com.example.labourmangement.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.Labour.LabourDashboard;
import com.example.labourmangement.R;

import java.util.Locale;

public class AdminDashboard extends AppCompatActivity {

    ImageView im_jobofer, im_payment,im_remark,im_confirmation,im_review,im_tracking;
    TextView textusername;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

       /* textusername=(TextView)findViewById(R.id.textusewrname);
        im_jobofer=(ImageView)findViewById(R.id.imjoboffer);
        im_payment=(ImageView)findViewById(R.id.impayment);
        im_remark=(ImageView)findViewById(R.id.imremark);
        im_confirmation=(ImageView)findViewById(R.id.imconfirmation);
        im_review=(ImageView)findViewById(R.id.imreview);
        im_tracking=(ImageView)findViewById(R.id.imtracking);

        Bundle extras = getIntent().getExtras();
        String username = null;
        if(extras != null) {
            username = extras.getString("username");
            textusername.setText("Welcome to Labour Management System \n" + username);
        }
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
