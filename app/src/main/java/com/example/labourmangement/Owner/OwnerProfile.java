package com.example.labourmangement.Owner;

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
import com.example.labourmangement.Contractor.ContractorDashboard;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForOwner;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Labour.LabourDashboard;
import com.example.labourmangement.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OwnerProfile extends AppCompatActivity {
    private static final String TAG = OwnerProfile.class.getSimpleName();

    EditText editText_uname,editText_mobnum,editText_address,editText_areaofoperation,editText_workinghour;
    Button btnsubmitcontractor,logout;
    RadioGroup rg_interestedon;
    String etunameholder,etmobnumholder,etaddressholder,etareaofoperationholder,etworkinghourholder,etradiogroholder;
    int flag;
    MaterialBetterSpinner category_workinghours;
CustomLoader loader;
SessionForOwner sessionForOwner;
    String[] SPINNERWORKINGHOUR = {"ARCHITECT","ENGINEER","CONTRACTOR","LABOURER"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);


//
        editText_uname = (EditText) findViewById(R.id.etowner_username);
        editText_mobnum = (EditText) findViewById(R.id.etowner_mobilenum);
        editText_address=(EditText)findViewById(R.id.etowner_postaladdress);
        editText_areaofoperation=(EditText)findViewById(R.id.etowner_areaofoperation);
        category_workinghours=(MaterialBetterSpinner)findViewById(R.id.category_workinghoursofowner);
        rg_interestedon=(RadioGroup)findViewById(R.id.rg_interestedonworking);
        btnsubmitcontractor=(Button)findViewById(R.id.button_submitowner);


        ArrayAdapter<String> adapterworkinghours = new ArrayAdapter<String>(OwnerProfile.this, android.R.layout.simple_dropdown_item_1line, SPINNERWORKINGHOUR);

        category_workinghours.setAdapter(adapterworkinghours);


        category_workinghours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                etworkinghourholder = (String) adapterView.getItemAtPosition(i);
            }
        });

        //logout=(Button)findViewById(R.id.btn_logoutcontractor);
        sessionForOwner = new SessionForOwner(getApplicationContext());
        sessionForOwner.checkLogin();
        HashMap<String, String> user = sessionForOwner.getUserDetails();

        // name
        String name = user.get(SessionForOwner.KEY_NAME);

        // email
        String email = user.get(SessionForOwner.KEY_EMAIL);

        editText_uname.setText(name);
        editText_mobnum.setText(email);
        Log.d(TAG, "name contractor555 "+name);
        Log.d(TAG, "emaillllll contractor555 "+email);

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
        HashMap<String, String> user = sessionForOwner.getUserDetails();

        // name
        String name = user.get(SessionForOwner.KEY_NAME);

        // email
        String email = user.get(SessionForOwner.KEY_EMAIL);

        String role=user.get(SessionForOwner.KEY_ROLE);
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

                    AlertDialog alertDialog = new AlertDialog.Builder(OwnerProfile.this).create();
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
                            Toast.makeText(OwnerProfile.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(OwnerProfile.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(OwnerProfile.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(OwnerProfile.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(OwnerProfile.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("contractor_name",name );
                params.put("contractor_mobnum", email);
                params.put("contractor_areaofoperation", etareaofoperationholder);
                params.put("contractor_address", etaddressholder);
                params.put("working_hours", etworkinghourholder);
                params.put("interested_on", etradiogroholder);
                params.put("role",role);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(OwnerProfile.this);
        requestQueue.add(stringRequest);

    }

    // Creating method to get value from EditText.
    public void GetValueFromEditText(){
       // etunameholder = editText_uname.getText().toString().trim();
       // etmobnumholder = editText_mobnum.getText().toString().trim();
        etaddressholder = editText_address.getText().toString().trim();
        etareaofoperationholder = editText_areaofoperation.getText().toString().trim();
        etradiogroholder = ((RadioButton) findViewById(rg_interestedon.getCheckedRadioButtonId())).getText().toString();

    }
    private void getdata(){

        sessionForOwner=new SessionForOwner(this);

        HashMap<String, String> user = sessionForOwner.getUserDetails();

        // name
        String name = user.get(SessionForOwner.KEY_NAME);

        // email
        String email = user.get(SessionForOwner.KEY_EMAIL);
        String role =user.get(SessionForOwner.KEY_ROLE);


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
                            Toast.makeText(OwnerProfile.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(OwnerProfile.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(OwnerProfile.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(OwnerProfile.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(OwnerProfile.this, "Error While Data Parsing", duration).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(OwnerProfile.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
}