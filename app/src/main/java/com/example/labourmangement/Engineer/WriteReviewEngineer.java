package com.example.labourmangement.Engineer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.labourmangement.DatabaseHelper.SessionForEngineer;
import com.example.labourmangement.R;

import java.util.HashMap;

public class WriteReviewEngineer extends AppCompatActivity {
    private static final String TAG = WriteReviewEngineer.class.getSimpleName();
    Button btn_submit;
    SessionForEngineer sessionForEngineer;
    TextView fetchname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review_engineer);

        fetchname=(TextView)findViewById(R.id.fetchname);

        btn_submit=(Button)findViewById(R.id.submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(WriteReviewEngineer.this).create();
                alertDialog.setTitle("Review");
                alertDialog.setMessage("Review Submitted");
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
        sessionForEngineer = new SessionForEngineer(getApplicationContext());
        HashMap<String, String> user = sessionForEngineer.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);

        fetchname=(TextView)findViewById(R.id.fetchname);
        fetchname.setText(name);

    }
}