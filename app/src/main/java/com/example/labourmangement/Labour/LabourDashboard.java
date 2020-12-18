package com.example.labourmangement.Labour;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.labourmangement.Contractor.ContractorDashboard;
import com.example.labourmangement.Contractor.ContractorProfile;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LabourDashboard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = LabourDashboard.class.getSimpleName();
EditText labor_uname,labor_mobnum,labor_age,labor_address,labor_wages,labor_workinghours,labor_interestedarea,labor_refrename,labor_refercode,labor_interestarea1;
String labornameholder,labormobnumholder,laborageholder,laborgenderholder,laborwageholder,laborinterestedon,laboraddressholder,laborworkinghourholder,laborspinnerholder,laborinterestedareaholder,labortransportholder,laborinterestedareaholder1;
RadioGroup rg_gender,rg_interestedon;
RadioButton rb_daily,rbmothly,rb_weekly,rb_male,rb_female;
    private SessionManager session;
    Button btnsubmit;
    Spinner categoryspinner;
    MaterialBetterSpinner modeoftransport,wageratespinner,workinghourspinner;
    int flag;
   CustomLoader loader;
   CustomAdapter customAdapter;

    int images[] ={R.drawable.selectcat,R.drawable.genral_labor,R.drawable.new_labor, R.drawable.new_labor, R.drawable.brickwork_labor,R.drawable.new_labor,R.drawable.new_labor,R.drawable.new_labor, R.drawable.plaster, R.drawable.tile_fixer, R.drawable.plumber, R.drawable.fabricator, R.drawable.electrician, R.drawable.pop_worker, R.drawable.painter, R.drawable.rcc_carpenter };
    String[] SPINNER_DATA = {"SELECT CATEGORY","GENERAL LABOR",
            "RCC CARPENTER",
            "RCC FITTER",
            "MASAN" ,
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

    String[] SPINNER_DATA_HINDI={"श्रेणी का चयन करे","आम मजदूर",
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

    String[] SPINNERWAGERATE = {"300-400","400-500","500-600","600-700","700-800","800-900"};

    String[] SPINNERWORKINGHOUR = {"9AM-5PM","9:30AM-10:30PM","10AM-6PM","10:30AM-6:30PM"};

    String[] SPINNER_DATA_FORMODEOFTRANSPORT = {"WALK","TWO WHEELER","BUS","AUTO","CAB"};

    String[] SPINNER_DATA_FORMODEOFTRANSPORT_HINDI = {"पैदल","दो पहिया","बस","ऑटो","टैक्सी"};

    String[] SPINNER_DATA_FORMODEOFTRANSPORT_MARATHI = {"चालणे","दुचाकी","बस","ऑटो","टँक्सी"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_dashboard);

      //  labor_uname = (EditText) findViewById(R.id.etlabour_username);
        labor_mobnum = (EditText) findViewById(R.id.etlabour_mobilenum);
        labor_age = (EditText) findViewById(R.id.etlabour_age);

        labor_address = (EditText) findViewById(R.id.etlabour_postaladdress);
        wageratespinner = (MaterialBetterSpinner) findViewById(R.id.wageratespinner);

        workinghourspinner = (MaterialBetterSpinner) findViewById(R.id.workinghourspinner);
        labor_interestedarea = (EditText) findViewById(R.id.etlabour_workingarea1);
        labor_interestarea1 = (EditText) findViewById(R.id.etlabour_workingarea2);
        labor_refrename = (EditText) findViewById(R.id.edit_refrename);
        labor_refercode = (EditText) findViewById(R.id.edit_refrcode);
categoryspinner=findViewById(R.id.category_spinner);
        modeoftransport = (MaterialBetterSpinner) findViewById(R.id.category_modeoftransport);
        btnsubmit = (Button) findViewById(R.id.button_submitlabordata);
        rg_gender = (RadioGroup) findViewById(R.id.radiogroupgender);
        rg_interestedon = (RadioGroup) findViewById(R.id.radiogroupinterestworkon);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        getAllData();

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if (labor_age.getText().toString().length() == 0) {
                    labor_age.setError(" Enter Age");
                    labor_age.requestFocus();
                    flag = 1;
                }
                else if (labor_address.getText().toString().length() == 0) {
                    labor_address.setError(" Enter Address");
                    labor_address.requestFocus();
                    flag = 1;
                }
                else if (labor_interestedarea.getText().toString().length() == 0) {
                    labor_interestedarea.setError(" Enter Interested Area of work");
                    labor_interestedarea.requestFocus();
                    flag = 1;
                }
                else if(!(labor_address.getText().toString().length() ==6)){
                    labor_address.setError("Pincode should be Valid");
                    labor_address.requestFocus();
                }
              else if (flag == 0) {
                    getandset();
                }
            }
        });

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        String ref_name=user.get(SessionManager.KEY_REFNAME);

        String ref_code=user.get(SessionManager.KEY_REFCODE);

   ArrayAdapter<String> adaptermodeoftransport = new ArrayAdapter<String>(LabourDashboard.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA_FORMODEOFTRANSPORT);

        modeoftransport.setAdapter(adaptermodeoftransport);


        ArrayAdapter<String> adapterwagerate = new ArrayAdapter<String>(LabourDashboard.this, android.R.layout.simple_dropdown_item_1line, SPINNERWAGERATE);
        wageratespinner.setAdapter(adapterwagerate);

//             if(session.get("Lang").equalsIgnoreCase("en")){
//            customAdapter=new CustomAdapter(getApplicationContext(),images,SPINNER_DATA);
//            categoryspinner.setAdapter(customAdapter);
//            // Log.e("swati2",sessionManagerContractor.get("Lang"));
//        }
//        else if(session.get("Lang").equalsIgnoreCase("hi")){
//            customAdapter=new CustomAdapter(getApplicationContext(),images,SPINNER_DATA_HINDI);
//            categoryspinner.setAdapter(customAdapter);
//            Log.e("swati3",session.get("Lang"));
//        }
//        else if(session.get("Lang").equalsIgnoreCase("mar")){
//            customAdapter=new CustomAdapter(getApplicationContext(),images,SPINNER_DATA_MARATHI);
//            categoryspinner.setAdapter(customAdapter);
//            Log.e("swati4",session.get("Lang"));
//
//        }
//        else{
//            customAdapter=new CustomAdapter(getApplicationContext(),images,SPINNER_DATA);
//            categoryspinner.setAdapter(customAdapter);
//        }

        Log.e("testingal",""+session.get("Lang"));
if(session.get("Lang").equalsIgnoreCase("en")){
     customAdapter=new CustomAdapter(getApplicationContext(),images,SPINNER_DATA);
    categoryspinner.setAdapter(customAdapter);
}
else if(session.get("Lang").equalsIgnoreCase("hi")){
     customAdapter=new CustomAdapter(getApplicationContext(),images,SPINNER_DATA_HINDI);
    categoryspinner.setAdapter(customAdapter);

        }
else if(session.get("Lang").equalsIgnoreCase("mar")) {
     customAdapter=new CustomAdapter(getApplicationContext(),images,SPINNER_DATA_MARATHI);
    categoryspinner.setAdapter(customAdapter);

}
else
{
    customAdapter=new CustomAdapter(getApplicationContext(),images,SPINNER_DATA);
    categoryspinner.setAdapter(customAdapter);
}

   ArrayAdapter<String> adapterwokinghour = new ArrayAdapter<String>(LabourDashboard.this, android.R.layout.simple_dropdown_item_1line, SPINNERWORKINGHOUR);

        workinghourspinner.setAdapter(adapterwokinghour);

        workinghourspinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                laborworkinghourholder = (String) adapterView.getItemAtPosition(i);
            }
        });
        wageratespinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                laborwageholder = (String) adapterView.getItemAtPosition(i);
            }
        });

        modeoftransport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                labortransportholder = (String) adapterView.getItemAtPosition(i);
            }
        });

    }
    private void getandset(){
        GetValueFromEditText();

        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        String ref_name=user.get(SessionManager.KEY_REFNAME);

        String ref_code=user.get(SessionManager.KEY_REFCODE);

       loader.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTLABORDATA,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "Inserting Response: " + response.toString());
                        loader.dismiss();
                        //hideDialog();
                        Log.i("tagconvertstr", "["+response+"]");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            AlertDialog alertDialog = new AlertDialog.Builder(LabourDashboard.this).create();
                            alertDialog.setTitle("Success ");
                            alertDialog.setMessage(" Data Stored Successfull");
                            alertDialog.setIcon(R.drawable.done);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();

                            Intent intent = new Intent(LabourDashboard.this, LaborProfile.class);
                            startActivity(intent);

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
                            Toast.makeText(LabourDashboard.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(LabourDashboard.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(LabourDashboard.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(LabourDashboard.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(LabourDashboard.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("labor_name", name);
                params.put("labor_mobnum", email);
                params.put("labor_gender", laborgenderholder);
                params.put("labor_age", laborageholder);
                params.put("labor_address", laboraddressholder);
                params.put("labor_category", laborspinnerholder);
                params.put("labor_wagerate", laborwageholder);
                params.put("transport_mode", labortransportholder);
                params.put("interest_work", laborinterestedon);
                params.put("labor_workinghour",laborworkinghourholder);
                params.put("particular_area", laborinterestedareaholder);
                params.put("particular_area1", laborinterestedareaholder1);
                Log.e("NIS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LabourDashboard.this);
        requestQueue.add(stringRequest);
    }

// Creating method to get value from EditText.
public void GetValueFromEditText(){
        laborspinnerholder=SPINNER_DATA[categoryspinner.getSelectedItemPosition()];
    laborageholder = labor_age.getText().toString().trim();
    laborinterestedareaholder = labor_interestedarea.getText().toString().trim();
    laborinterestedareaholder1 = labor_interestarea1.getText().toString().trim();
    laboraddressholder =labor_address.getText().toString().trim();
    laborinterestedon = ((RadioButton) findViewById(rg_interestedon.getCheckedRadioButtonId())).getText().toString();
    laborgenderholder = ((RadioButton) findViewById(rg_gender.getCheckedRadioButtonId())).getText().toString();

}


    private void getAllData(){
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);


        loader.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GETALLDATAOFLABOR,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                loader.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "["+response+"]");
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    if(array.length()!=0) {
                        btnsubmit.setVisibility(View.INVISIBLE);
                        for (int i = 0; i < array.length(); i++) {

                            //getting product object from json array
                            JSONObject job = array.getJSONObject(i);
                            labor_age.setText(job.getString("labor_age").toString());
                            labor_address.setText(job.getString("labor_address").toString());
                           // categoryspinner.getSelectedItem((job.getString("labor_category").toString()));



                            for(int j=0;j<SPINNER_DATA.length;j++)
                            {
                                if(job.getString("labor_category").equalsIgnoreCase(SPINNER_DATA[j]))

                                {
                                    categoryspinner.setSelection(j);
                                }
                            }
                            wageratespinner.setText(job.getString("labor_wagerate").toString());
                            modeoftransport.setText(job.getString("transport_mode").toString());
                            workinghourspinner.setText(job.getString("labor_workinghour").toString());
                            labor_interestedarea.setText(job.getString("particular_area").toString());
                            labor_interestarea1.setText(job.getString("particular_area1").toString());

                        }
                    }
                    else {

                    }

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
                            Toast.makeText(LabourDashboard.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(LabourDashboard.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(LabourDashboard.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(LabourDashboard.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(LabourDashboard.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                params.put("labor_mobnum", email);
                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(LabourDashboard.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
