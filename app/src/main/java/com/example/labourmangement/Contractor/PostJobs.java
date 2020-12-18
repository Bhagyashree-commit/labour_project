package com.example.labourmangement.Contractor;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Labour.CustomAdapter;
import com.example.labourmangement.Labour.ForgetPassword;
import com.example.labourmangement.Labour.JobDetails;
import com.example.labourmangement.Labour.LabourDashboard;
import com.example.labourmangement.Labour.Register_labour;
import com.example.labourmangement.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PostJobs extends AppCompatActivity {
    private static final String TAG = PostJobs.class.getSimpleName();

    EditText etjobtitle,etjobdetails,etjobwages,etjobarea;
    Button btnpost;
    String jobtittleholder,jobdetailsholder,jobwagesholder,jobareaholder,Emailholder,ContractorNameHolder,roleholder,wagwes,labortype;
    SessionManagerContractor sessionManagerContractor;
    int flag;
    CustomAdapter customAdapter;
    //loader loader;
    CustomLoader loader;
    Spinner categoryspinner;
    MaterialBetterSpinner wageratespinner;
    int images[] = {R.drawable.selectcat,R.drawable.genral_labor,R.drawable.new_labor, R.drawable.new_labor, R.drawable.brickwork_labor,R.drawable.new_labor,R.drawable.new_labor,R.drawable.new_labor, R.drawable.plaster, R.drawable.tile_fixer, R.drawable.plumber, R.drawable.fabricator, R.drawable.electrician, R.drawable.pop_worker, R.drawable.painter, R.drawable.rcc_carpenter };
    String[] SPINNER_DATA = {"SELECT OF CATEGORY ","GENERAL LABOR",
            "RCC CARPENTER",
            "RCC FITTER",
            "MASAN",
            "BRICKWORK",
            "UCR MASONRY",
            "WATERPROOFING",
            "PLASTER",
            "TILE FIXER",
            "PLUMBER",
            "FABRICATOR",
            "ELECTRICIAN",
            "POP WORKER",
            "PAINTER",
            "FURNITURE CARPENTER"};

    String[] SPINNER_DATA_HINDI={"श्रेणी का चयन करें","आम मजदूर",
            "आरसीसी कारपेंटर",
            "आरसीसी फिटर",
            "MASAN -विषय वर्ग- BRICKWORK, UCR MASONRY, वॉटरप्रूफिंग",
            "प्लास्टर",
            "टाइल  फिक्सर",
            "प्लम्बर",
            "फ़ेब्रिकेटर",
            "बिजली मिस्त्री",
            "पीओपी कार्यकर्ता",
            "चित्रकार",
            "फर्नीचर वाहक"};
    String[] SPINNER_DATA_MARATHI={"श्रेणी निवडा","सामान्य कामगार",
            "आरसीसी सुतार",
            "आरसीसी फिटर",
            "गवंडी  -सम श्रेणी - वीटकाम, दगडी काम , वॉटरप्रूफिंग",
            "प्लास्टर",
            "टाइल फिक्सर",
            "प्लंबर",
            "फॅब्रिकेटर",
            "इलेक्ट्रीकियन",
            "पीओपी कामगार",
            "चित्रकार",
            "फर्निचर सुतार"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_jobs);

        etjobtitle=(EditText)findViewById(R.id.editjobttitle);
        etjobdetails=(EditText)findViewById(R.id.editjobdetails);
        etjobwages=(EditText)findViewById(R.id.edit_wagerate);
        etjobarea=(EditText)findViewById(R.id.editjobarea);
        btnpost=(Button)findViewById(R.id.submijobdata);
        categoryspinner =findViewById(R.id.spinn);
        sessionManagerContractor=new SessionManagerContractor((getApplicationContext()));


            if(sessionManagerContractor.get("Lang").equalsIgnoreCase("en")){
                customAdapter=new CustomAdapter(getApplicationContext(),images,SPINNER_DATA);
                categoryspinner.setAdapter(customAdapter);
               // Log.e("swati2",sessionManagerContractor.get("Lang"));
            }
            else if(sessionManagerContractor.get("Lang").equalsIgnoreCase("hi")){
                customAdapter=new CustomAdapter(getApplicationContext(),images,SPINNER_DATA_HINDI);
                categoryspinner.setAdapter(customAdapter);
                Log.e("swati3",sessionManagerContractor.get("Lang"));
            }
            else if(sessionManagerContractor.get("Lang").equalsIgnoreCase("mar")){
                customAdapter=new CustomAdapter(getApplicationContext(),images,SPINNER_DATA_MARATHI);
                categoryspinner.setAdapter(customAdapter);
                Log.e("swati4",sessionManagerContractor.get("Lang"));

            }
            else{
                customAdapter=new CustomAdapter(getApplicationContext(),images,SPINNER_DATA);
                categoryspinner.setAdapter(customAdapter);
        }


etjobtitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    @Override
    public void onFocusChange(View view, boolean b) {
     Log.d(TAG,"BHANU"+view.getId());
    }
});

         loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionManagerContractor.checkLogin();
                HashMap<String, String> user = sessionManagerContractor.getUserDetails();

                // name
                String name = user.get(SessionManager.KEY_NAME);

                // email
                String email = user.get(SessionManager.KEY_EMAIL);
                String role=user.get(SessionManager.KEY_ROLE);
                Log.d(TAG, "Email "+email);
                jobtittleholder= etjobtitle.getText().toString().trim();
                jobareaholder = etjobarea.getText().toString().trim();
                jobdetailsholder = SPINNER_DATA[categoryspinner.getSelectedItemPosition()];
                jobwagesholder = etjobwages.getText().toString().trim();
                Emailholder=email;
                ContractorNameHolder=name;
                roleholder=role;



                if(etjobtitle.getText().toString().length()==0){
                    etjobtitle.setError(" Enter Job Title");
                    etjobtitle.requestFocus();


                }
             else if(etjobwages.getText().toString().length()==0){
                    etjobwages.setError(" Enter Job Wages");
                    etjobwages.requestFocus();

                }
               else if(etjobarea.getText().toString().length()==0){
                    etjobarea.setError(" Enter Job Area");
                    etjobarea.requestFocus();
                }
                else if(!(etjobarea.getText().toString().length() ==6)){
                    etjobarea.setError("Pincode should be Valid");
                    etjobarea.requestFocus();
                }

                else {
                    postnewjob();
                }
            }
        });

    }


    private void postnewjob(){
        loader.show();
        Log.d(TAG, "Inserting Response:hiiiiiiiiiiiiiii ");
        Log.d(TAG, "ROLE "+roleholder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTJOB,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response:hiiiiiiiiiiiiiii " + response.toString());
                loader.dismiss();
                //hideDialog();
                Log.i("bhagyaaaaaa", "["+response+"]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    AlertDialog alertDialog = new AlertDialog.Builder(PostJobs.this).create();
                    alertDialog.setTitle("Job Post ");
                    alertDialog.setMessage(jsonObject.getString("message"));
                    alertDialog.setIcon(R.drawable.done);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(PostJobs.this, ContractorProfile.class);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();



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
                            Toast.makeText(PostJobs.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJobs.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJobs.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJobs.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PostJobs.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("job_title", jobtittleholder);
                params.put("job_details",jobdetailsholder);
                params.put("job_wages", jobwagesholder);
                params.put("job_area", jobareaholder);
                params.put("created_by", Emailholder);
                params.put("contractor_name", ContractorNameHolder);
                params.put("role",roleholder);

                Log.e("tag","params"+params);

                return params;


            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(PostJobs.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);


        etjobarea.setText("");
        etjobdetails.setText("");
        etjobtitle.setText("");
        etjobwages.setText("");
    }



}
