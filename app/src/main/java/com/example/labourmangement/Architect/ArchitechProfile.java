package com.example.labourmangement.Architect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.example.labourmangement.Contractor.AllJobs;
import com.example.labourmangement.Contractor.ContractorDashboard;
import com.example.labourmangement.Contractor.ContractorProfile;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForArch;
import com.example.labourmangement.DatabaseHelper.SessionForOwner;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Owner.OwnerProfile;
import com.example.labourmangement.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ArchitechProfile extends AppCompatActivity {
    private static final String TAG = ArchitechProfile.class.getSimpleName();

    EditText editText_uname,edit_ref_name,edit_ref_code,editText_mobnum,editText_address,editText_areaofoperation,editText_workinghour;
    Button btnsubmitcontractor,logout;
    RadioGroup rg_interestedon;
    String etunameholder,etmobnumholder,etaddressholder,etareaofoperationholder,etworkinghourholder,etradiogroholder;
    int flag;
    MaterialBetterSpinner category_workinghours;
    ProgressDialog progressDialog;
    SessionForArch sessionForArch;
    CustomLoader loader;
    String[] SPINNERWORKINGHOUR = {"RESIDENTIAL/COMMERCIAL","INDUSTRIAL","LANDSCAPING","INTERIOR DESIGN"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architech_profile);


getdata();

        editText_uname = (EditText) findViewById(R.id.etarch_username);
        editText_mobnum = (EditText) findViewById(R.id.etarch_mobilenum);
        editText_address=(EditText)findViewById(R.id.etarch_postaladdress);
        editText_areaofoperation=(EditText)findViewById(R.id.etarch_areaofoperation);
        category_workinghours=(MaterialBetterSpinner)findViewById(R.id.category_workinghoursofarch);
        rg_interestedon=(RadioGroup)findViewById(R.id.rg_interestedonworking);
        btnsubmitcontractor=(Button)findViewById(R.id.button_submitarchitecht);
        edit_ref_name=(EditText)findViewById(R.id.et_ref_nameA);
        edit_ref_code=(EditText)findViewById(R.id.et_ref_codeA);

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        ArrayAdapter<String> adapterworkinghours = new ArrayAdapter<String>(ArchitechProfile.this, android.R.layout.simple_dropdown_item_1line, SPINNERWORKINGHOUR);

        category_workinghours.setAdapter(adapterworkinghours);


        category_workinghours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                etworkinghourholder = (String) adapterView.getItemAtPosition(i);
            }
        });

        //logout=(Button)findViewById(R.id.btn_logoutcontractor);
        sessionForArch = new SessionForArch(getApplicationContext());
        sessionForArch.checkLogin();
        HashMap<String, String> user = sessionForArch.getUserDetails();

        // name
        String name = user.get(SessionForOwner.KEY_NAME);

        // email
        String email = user.get(SessionForOwner.KEY_EMAIL);

        String refname = user.get(SessionForOwner.KEY_REFNAME);

        String refcode = user.get(SessionForOwner.KEY_REFCODE);

        editText_uname.setText(name);
        editText_mobnum.setText(email);
        edit_ref_code.setText(refcode);
        edit_ref_name.setText(refname);
        Log.d(TAG, "name contractor555 "+name);
        Log.d(TAG, "emaillllll contractor555 "+email);

        progressDialog = new ProgressDialog(ArchitechProfile.this);
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

        sessionForArch=new SessionForArch(this);

        HashMap<String, String> user = sessionForArch.getUserDetails();

        // name
        String name = user.get(SessionForArch.KEY_NAME);

        // email
        String email = user.get(SessionForArch.KEY_EMAIL);
        String role =user.get(SessionForArch.KEY_ROLE);
        GetValueFromEditText();

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

                    AlertDialog alertDialog = new AlertDialog.Builder(ArchitechProfile.this).create();
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
                            Toast.makeText(ArchitechProfile.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ArchitechProfile.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ArchitechProfile.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ArchitechProfile.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ArchitechProfile.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d(TAG, "cname "+name);
                Log.d(TAG, "cmob "+email);
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
                params.put("role", user.get(SessionManagerContractor.KEY_ROLE));


                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(ArchitechProfile.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    public void GetValueFromEditText(){

        etaddressholder = editText_address.getText().toString().trim();
        etareaofoperationholder = editText_areaofoperation.getText().toString().trim();
        etradiogroholder = ((RadioButton) findViewById(rg_interestedon.getCheckedRadioButtonId())).getText().toString();


    }

    private void getdata(){

        sessionForArch=new SessionForArch(this);

        HashMap<String, String> user = sessionForArch.getUserDetails();

        // name
        String name = user.get(SessionForArch.KEY_NAME);

        // email
        String email = user.get(SessionForArch.KEY_EMAIL);
        String role =user.get(SessionForArch.KEY_ROLE);


        // loader.setMessage("Loading...Please Wait..");
//        loader.show();

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
                        editText_uname.setText(job.getString("contractor_name").toString());
                        editText_mobnum.setText(job.getString("contractor_mobnum").toString());
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
                            Toast.makeText(ArchitechProfile.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ArchitechProfile.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ArchitechProfile.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ArchitechProfile.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ArchitechProfile.this, "Error While Data Parsing", duration).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(ArchitechProfile.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}