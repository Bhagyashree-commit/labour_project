package com.example.labourmangement.Engineer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.labourmangement.DatabaseHelper.SessionForEngineer;
import com.example.labourmangement.DatabaseHelper.SessionForEngineer;
import com.example.labourmangement.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EngProfile extends AppCompatActivity {

    private static final String TAG = EngProfile.class.getSimpleName();
    EditText editText_uname,edit_ref_name,edit_ref_code,editText_mobnum,editText_address,editText_areaofoperation,editText_workinghour;
    Button btnsubmitcontractor,logout;
    RadioGroup rg_interestedon;
    String etunameholder,etmobnumholder,etaddressholder,etareaofoperationholder,etworkinghourholder,etradiogroholder;
    int flag;
    MaterialBetterSpinner category_workinghours;
    // loader loader;
    CustomLoader loader;

    String[] SPINNERWORKINGHOUR = {"RESIDENTIAL/COMMERCIAL","INDUSTRIAL","INTRA PROJECTS","PROJECT MANAGEMENT"};


    private SessionForEngineer sessioncon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eng_profile);


     /*  editText_uname = (EditText) findViewById(R.id.etcontractor_username);
        editText_mobnum = (EditText) findViewById(R.id.etcontractor_mobilenum);*/
        editText_address=(EditText)findViewById(R.id.eteng_postaladdress);
        editText_areaofoperation=(EditText)findViewById(R.id.eteng_areaofoperation);
       /* edit_ref_name=(EditText)findViewById(R.id.et_ref_name);
        edit_ref_code=(EditText)findViewById(R.id.et_ref_code);*/
        category_workinghours=(MaterialBetterSpinner)findViewById(R.id.spinn_eng_spinn);
        rg_interestedon=(RadioGroup)findViewById(R.id.rg_interestedonworkingENG);
        btnsubmitcontractor=(Button)findViewById(R.id.btn_submiteng);


        ArrayAdapter<String> adapterworkinghours = new ArrayAdapter<String>(EngProfile.this, android.R.layout.simple_dropdown_item_1line, SPINNERWORKINGHOUR);

        category_workinghours.setAdapter(adapterworkinghours);


        category_workinghours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                etworkinghourholder = (String) adapterView.getItemAtPosition(i);
            }
        });

        //logout=(Button)findViewById(R.id.btn_logoutcontractor);
        sessioncon = new SessionForEngineer(getApplicationContext());
        sessioncon.checkLogin();
        HashMap<String, String> user = sessioncon.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);
        String role =user.get(SessionForEngineer.KEY_ROLE);
        String ref_code =user.get(SessionForEngineer.KEY_REFCODE);
        String ref_name =user.get(SessionForEngineer.KEY_REFNAME);

        Log.d(TAG, "name contractor555 "+name);
        Log.d(TAG, "emaillllll contractor555 "+email);
        Log.d(TAG, "Job ROLE "+role);

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);



        getdata();
        btnsubmitcontractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=0;

                if(editText_address.getText().toString().length()==0){
                    editText_address.setError(" Enter Address");
                    editText_address.requestFocus();
                    flag=1;
                }
                if(editText_areaofoperation.getText().toString().length()==0){
                    editText_areaofoperation.setError(" Enter Area Of Operation");
                    editText_areaofoperation.requestFocus();
                    flag=1;
                }
                if(flag==0){
                    savedata();

                }

            }
        });
        
    }

    private void savedata(){

        sessioncon=new SessionForEngineer(this);

        HashMap<String, String> user = sessioncon.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);
        String role =user.get(SessionForEngineer.KEY_ROLE);

        GetValueFromEditText();
        // Showing progress dialog at user registration time.
        //loader.setMessage("Loading...Please Wait..");
        loader.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTCONTRACTORDATA,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                loader.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "["+response+"]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    AlertDialog alertDialog = new AlertDialog.Builder(EngProfile.this).create();
                    alertDialog.setTitle("Success ");
                    alertDialog.setMessage("Data Stored Succesfully");
                    alertDialog.setIcon(R.drawable.done);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
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
                            Toast.makeText(EngProfile.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngProfile.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngProfile.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngProfile.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngProfile.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {



                Log.d(TAG, "carea "+etareaofoperationholder);
                Log.d(TAG, "caddress "+etaddressholder);
                Log.d(TAG, "workinghour "+etworkinghourholder);
                Log.d(TAG, "radiobutton "+etradiogroholder);


                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("contractor_name", name);
                params.put("contractor_mobnum", email);
                params.put("contractor_areaofoperation", etareaofoperationholder);
                params.put("contractor_address", etaddressholder);
                params.put("working_hours", etworkinghourholder);
                params.put("interested_on", etradiogroholder);
                params.put("role", user.get(SessionForEngineer.KEY_ROLE));


                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(EngProfile.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    // Creating method to get value from EditText.
    public void GetValueFromEditText(){

        etaddressholder = editText_address.getText().toString().trim();
        etareaofoperationholder = editText_areaofoperation.getText().toString().trim();
        etradiogroholder = ((RadioButton) findViewById(rg_interestedon.getCheckedRadioButtonId())).getText().toString();


    }

    private void getdata(){

        sessioncon=new SessionForEngineer(this);

        HashMap<String, String> user = sessioncon.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);
        String role =user.get(SessionForEngineer.KEY_ROLE);


        // loader.setMessage("Loading...Please Wait..");
        loader.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GETCONTRACTORDATA,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("geetttttttt", response);


                loader.dismiss();
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);
                    Log.d(TAG, array.toString());
                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject job = array.getJSONObject(i);
                        // editText_uname.setText(job.getString("contractor_name").toString());
                        // editText_mobnum.setText(job.getString("contractor_mobnum").toString());
                        editText_address.setText(job.getString("contractor_address").toString());
                        editText_areaofoperation.setText(job.getString("contractor_areaofoperation").toString());
                        category_workinghours.setText(job.getString("working_hours").toString());
                        rg_interestedon.getCheckedRadioButtonId();
                        //rg_interestedon.(job.getString("interested_on").toString());



                    }


                } catch (JSONException e) {

                    Log.e("testerroor",e.toString());
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
                            Toast.makeText(EngProfile.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngProfile.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngProfile.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngProfile.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngProfile.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {



                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                params.put("contractor_mobnum", email);


                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(EngProfile.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }


}