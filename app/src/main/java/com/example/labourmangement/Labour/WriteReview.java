package com.example.labourmangement.Labour;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labourmangement.Contractor.ContractorProfile;
import com.example.labourmangement.Contractor.PaymentStatusContractor;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.R;

import java.util.HashMap;
import java.util.Locale;

public class WriteReview extends AppCompatActivity {

    private static final String TAG = WriteReview.class.getSimpleName();
    Button btn_submit;
    TextView textreview;
    SessionManager session;
    TextView fetchname;
    RatingBar ratingBar;
    CustomLoader loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        //textreview=(TextView)findViewById(R.id.tv);
        fetchname=(TextView)findViewById(R.id.fetchname);
        ratingBar=(RatingBar)findViewById(R.id.rating);
        btn_submit=(Button)findViewById(R.id.submit);
        session=new SessionManager(this);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        if(session.get("rating").equals("")){
            Toast.makeText(getApplicationContext(),
                    "Please give Review",
                    Toast.LENGTH_LONG).show();
        }
        else{
            ratingBar.setRating(Float.parseFloat(session.get("rating")));
        }

btn_submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Float rating = ratingBar.getRating();
        Log.e("TAG", "" + rating);
        session.set("rating", String.valueOf(rating));
        session.commit();
        Log.e("BHAGYA",session.get("rating"));
        loader.show();

        if (rating == 0) {

            Toast.makeText(getApplicationContext(),
                    "Please give Review",
                    Toast.LENGTH_LONG).show();

        }
        else if(session.get("rating").length()==0){
            ratingBar.setRating(Float.parseFloat(session.get("rating")));
        }

        else {
            Toast.makeText(getApplicationContext(),
                    "Thank You For Your Review",
                    Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(WriteReview.this, LaborProfile.class);
            startActivity(intent1);
        }
        loader.dismiss();

    }
});
//textreview.setText("");


    }


}
