package com.example.labourmangement.Contractor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.example.labourmangement.Labour.jobStatusAndTrack;
import com.example.labourmangement.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PostJobs extends AppCompatActivity {
    private static final String TAG = PostJobs.class.getSimpleName();

    EditText etjobdetails,etjobwages;
    TextView etjobarea;
    AutoCompleteTextView etjobtitle;
    Button btnpost;
    String jobtittleholder,jobdetailsholder,jobwagesholder,jobareaholder,Emailholder,ContractorNameHolder,roleholder,wagwes,labortype;
    SessionManagerContractor sessionManagerContractor;
    int flag;
    CustomAdapter customAdapter;
    CustomAdapterTwo customAdapterTwo;
    String date,time,datetime;
    //loader loader;
    CustomLoader loader;
    Spinner categoryspinner;
    Spinner wageratespinner;
    GoogleMap mMap;
    Marker marker;
    int images[] = {R.drawable.selectcat,R.drawable.genral_labor,R.drawable.new_labor, R.drawable.new_labor, R.drawable.brickwork_labor,R.drawable.new_labor,R.drawable.new_labor,R.drawable.new_labor, R.drawable.plaster, R.drawable.tile_fixer, R.drawable.plumber, R.drawable.fabricator, R.drawable.electrician, R.drawable.pop_worker, R.drawable.painter, R.drawable.rcc_carpenter };
    String[] SPINNER_DATA = {"SELECT OF CATEGORY ",
            "GENERAL LABOR",
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

    String[] SPINNER_DATA_HINDI={"श्रेणी का चयन करें",
            "आम मजदूर",
            "आरसीसी कारपेंटर",
            "आरसीसी फिटर",
            "MASAN",
            "BRICKWORK",
            "UCR MASONRY",
            "वॉटरप्रूफिंग",
            "प्लास्टर",
            "टाइल फिक्सर",
            "प्लम्बर",
            "फ़ेब्रिकेटर",
            "बिजली मिस्त्री",
            "पीओपी कार्यकर्ता",
            "चित्रकार",
            "फर्नीचर वाहक"};

    String[] SPINNER_DATA_MARATHI={"श्रेणी निवडा",
            "सामान्य कामगार",
            "आरसीसी सुतार",
            "आरसीसी फिटर",
            "गवंडी",
            "वीटकाम",
            "दगडी काम" ,
            "वॉटरप्रूफिंग",
            "प्लास्टर",
            "टाइल फिक्सर",
            "प्लंबर",
            "फॅब्रिकेटर",
            "इलेक्ट्रीकियन",
            "पीओपी कामगार",
            "चित्रकार",
            "फर्निचर सुतार"};

    String[] SPINNERWAGERATE = {"SELECT WAGE RANGE","250","300","350","400","500","600","750","900","1100","1200"};
    String[] SPINNERWAGERATEHINDI = {"वेतन का चयन करें","250","300","350","400","500","600","750","900","1100","1200"};
    String[] SPINNERWAGERATEMARATHI = {"पृष्ठ रेंज निवडा","250","300","350","400","500","600","750","900","1100","1200"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_jobs);

        etjobtitle=findViewById(R.id.editjobttitle);
        wageratespinner=findViewById(R.id.wageratespinnereditjobwages);

        etjobdetails=(EditText)findViewById(R.id.editjobdetails);
        //etjobwages=(EditText)findViewById(R.id.edit_wagerate);
        etjobarea=findViewById(R.id.editjobarea);
        btnpost=(Button)findViewById(R.id.submijobdata);
        categoryspinner =findViewById(R.id.spinn);


//        ArrayAdapter<String> adapterwagerate = new ArrayAdapter<String>(PostJobs.this, android.R.layout.simple_dropdown_item_1line, SPINNERWAGERATE);
//        wageratespinner.setAdapter(adapterwagerate);
//        customAdapterTwo=new CustomAdapterTwo(getApplicationContext(),SPINNERWAGERATE);
//        wageratespinner.setAdapter(customAdapterTwo);

        etjobtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 Intent intent = new Intent(PostJobs.this, MapsActivityTwo.class);
                                    startActivity(intent);

            }
        });


        etjobarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(PostJobs.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        date = +selectedday + "/" + selectedmonth + "/" + selectedyear;
                        // eReminderDate.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                        Log.e("", "" + date);
                        // sessionManagerContractor.set("date","date");
                    }
                }, mYear, mMonth, mDay);
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                TimePickerDialog timePickerDialog = new TimePickerDialog(PostJobs.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if(hourOfDay>=0 && hourOfDay<12){
                                    time = hourOfDay + " : " + minute + " AM";
                                } else {
                                    if(hourOfDay == 12){
                                        time = hourOfDay + " : " + minute + "PM";
                                    } else{
                                        hourOfDay = hourOfDay -12;
                                        time = hourOfDay + " : " + minute + "PM";
                                    }
                                }
                                Log.e("", "" + time);
                                 datetime = date +"  "+ time;
                                etjobarea.setText(datetime);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();


            }
        });
        sessionManagerContractor=new SessionManagerContractor((getApplicationContext()));
        if(sessionManagerContractor.get("add").equalsIgnoreCase("")){
            Toast.makeText(PostJobs.this, "please select area",Toast.LENGTH_LONG).show();
        }
        else {
            etjobtitle.setText(sessionManagerContractor.get("add"));

        }


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




        if(sessionManagerContractor.get("Lang").equalsIgnoreCase("en")){
            customAdapterTwo=new CustomAdapterTwo(getApplicationContext(),SPINNERWAGERATE);
            wageratespinner.setAdapter(customAdapterTwo);

            // Log.e("swati2",sessionManagerContractor.get("Lang"));
        }
        else if(sessionManagerContractor.get("Lang").equalsIgnoreCase("hi")){
            customAdapterTwo=new CustomAdapterTwo(getApplicationContext(),SPINNERWAGERATEHINDI);
            wageratespinner.setAdapter(customAdapterTwo);

            Log.e("swati3",sessionManagerContractor.get("Lang"));
        }
        else if(sessionManagerContractor.get("Lang").equalsIgnoreCase("mar")){
            customAdapterTwo=new CustomAdapterTwo(getApplicationContext(),SPINNERWAGERATEMARATHI);
            wageratespinner.setAdapter(customAdapterTwo);

            Log.e("swati4",sessionManagerContractor.get("Lang"));

        }
        else{
            customAdapterTwo=new CustomAdapterTwo(getApplicationContext(),SPINNERWAGERATE);
            wageratespinner.setAdapter(customAdapterTwo);

        }


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
                jobtittleholder= sessionManagerContractor.get("add");
                jobareaholder = etjobarea.getText().toString().trim();
                jobdetailsholder = SPINNER_DATA[categoryspinner.getSelectedItemPosition()];
                jobwagesholder =SPINNERWAGERATE[wageratespinner.getSelectedItemPosition()];
              //  jobwagesholder = etjobwages.getText().toString().trim();
                Emailholder=email;
                ContractorNameHolder=name;
                roleholder=role;
//                if(etjobtitle.getText().toString().length()==0){
//                    etjobtitle.setError(" Enter Job Title");
//                    etjobtitle.requestFocus();
//                }
//             else if(etjobwages.getText().toString().length()==0){
//                    etjobwages.setError(" Enter Job Wages");
//                    etjobwages.requestFocus();
//                }
                if(etjobarea.getText().toString().length()==0){
                    etjobarea.setError(" Enter Date And Time");
                    etjobarea.requestFocus();
                }
//                else if(!(etjobarea.getText().toString().length() ==0)){
//                    etjobarea.setError("Enter Date And Time");
//                    etjobarea.requestFocus();
//                }
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

                Log.e("sita","params"+params);

                return params;


            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(PostJobs.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);


        //etjobarea.setText("");
        etjobdetails.setText("");
        etjobtitle.setText("");
       // etjobwages.setText("");
    }

    @Override
    public void onBackPressed() {
        super. onBackPressed();
        Intent intent = new Intent(PostJobs.this, ContractorProfile.class);
        startActivity(intent);
    }

}
