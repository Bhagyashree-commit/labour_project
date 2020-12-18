package com.example.labourmangement.Labour;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.labourmangement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPassword extends AppCompatActivity {
    private static final String TAG = ForgetPassword.class.getSimpleName();
    Button opt_forgetpassbtn,sendotp,resendotp;
    EditText et_otpforgetpassmobnum,otp_textbox_one,otp_textbox_two,otp_textbox_three,otp_textbox_five,otp_textbox_four,otp_textbox_six;
  SessionManager sessionManager;
    ProgressDialog ploader;
    LinearLayout lin;
    int flag;
    CustomLoader loader;
    String details;
    String mobilenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        opt_forgetpassbtn=(Button)findViewById(R.id.opt_forgetpassbtn);
        et_otpforgetpassmobnum=(EditText) findViewById(R.id.et_otpforgetpassmobnum);

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        otp_textbox_one = findViewById(R.id.editTextone1);
        otp_textbox_two = findViewById(R.id.editTexttwo2);
        otp_textbox_three = findViewById(R.id.editTextthree3);
        otp_textbox_four = findViewById(R.id.editTextfour4);
        otp_textbox_five= findViewById(R.id.editTextfive5);
        otp_textbox_six = findViewById(R.id.editTextsix6);
        sendotp = findViewById(R.id.sendotp);
        lin = findViewById(R.id.layout_otp);
        resendotp = findViewById(R.id.resendotp);

        EditText[] edit = {otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four,otp_textbox_five,otp_textbox_six};

        otp_textbox_one.addTextChangedListener(new GenericTextWatcher(otp_textbox_one, edit));
        otp_textbox_two.addTextChangedListener(new GenericTextWatcher(otp_textbox_two, edit));
        otp_textbox_three.addTextChangedListener(new GenericTextWatcher(otp_textbox_three, edit));
        otp_textbox_four.addTextChangedListener(new GenericTextWatcher(otp_textbox_four, edit));
        otp_textbox_five.addTextChangedListener(new GenericTextWatcher(otp_textbox_five, edit));
        otp_textbox_six.addTextChangedListener(new GenericTextWatcher(otp_textbox_six, edit));


        ploader = new ProgressDialog(ForgetPassword.this);
        //sessionManager=new SessionManager(ForgetPassword.this);
        lin.setVisibility(View.INVISIBLE);
        opt_forgetpassbtn.setVisibility(View.INVISIBLE);
      //  et_otpforgetpassmobnum.setVisibility(View.INVISIBLE);

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=0;
                if(et_otpforgetpassmobnum.getText().toString().length() < 10){
                    et_otpforgetpassmobnum.setError(" Mobile number should be valid");
                    et_otpforgetpassmobnum.requestFocus();
                    flag=1;
                }
                else if(!et_otpforgetpassmobnum.getText().toString().isEmpty()) {

                    checkmobilenum();
//                    getOTP();
//
                    sendotp.setVisibility(View.INVISIBLE);

                }
            }
        });

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                flag=0;
                if(et_otpforgetpassmobnum.getText().toString().length() < 10){
                    et_otpforgetpassmobnum.setError(" Mobile number should be valid");
                    et_otpforgetpassmobnum.requestFocus();
                    flag=1;
                }
                if(flag==0) {
                    getOTP();
                    otp_textbox_one.setText("");
                   otp_textbox_two.setText("");
                   otp_textbox_three.setText("");
                   otp_textbox_four.setText("");
                   otp_textbox_five.setText("");
                   otp_textbox_six.setText("");


                }
            }
        });

        opt_forgetpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  String mobilenum = et_otpforgetpassmobnum.getText().toString().trim();
                String otp1= otp_textbox_one.getText().toString().trim();
                String otp2= otp_textbox_two.getText().toString().trim();
                String otp3= otp_textbox_three.getText().toString().trim();
                String otp4= otp_textbox_four.getText().toString().trim();
                String otp5= otp_textbox_five.getText().toString().trim();
                String otp6= otp_textbox_six.getText().toString().trim();

                String otp=(otp1+otp2+otp3+otp4+otp5+otp6);

               hitsecondAPI( otp, details);
            }

        });
    }

    private void checkmobilenum(){
        mobilenumber=et_otpforgetpassmobnum.getText().toString().trim();
        loader.show();

      //  String Url=AppConfig.URL_FORGETPASS+et_otpforgetpassmobnum.getText().toString()+"/AUTOGEN";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_FORGET,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                loader.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "["+response+"]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("success").equalsIgnoreCase("1")) {
                        //details= jsonObject.getString("Details");
                        getOTP();


                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Please Enter a Valid Number",
                                Toast.LENGTH_LONG).show();
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
                            Toast.makeText(ForgetPassword.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ForgetPassword.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ForgetPassword.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ForgetPassword.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ForgetPassword.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }


                }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",mobilenumber );

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(ForgetPassword.this);
        requestQueue.add(stringRequest);

    }

    private void getOTP(){

        loader.show();

        String Url=AppConfig.URL_FORGETPASS+et_otpforgetpassmobnum.getText().toString()+"/AUTOGEN";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                loader.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "["+response+"]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        details= jsonObject.getString("Details");

                        Toast.makeText(getApplicationContext(),
                                "OTP Is Send Successfully",
                                Toast.LENGTH_LONG).show();
                        lin.setVisibility(View.VISIBLE);
                        opt_forgetpassbtn.setVisibility(View.VISIBLE);

                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Please Enter a Valid Number",
                                Toast.LENGTH_LONG).show();
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
                            Toast.makeText(ForgetPassword.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ForgetPassword.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ForgetPassword.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ForgetPassword.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ForgetPassword.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }


        });
        RequestQueue requestQueue = Volley.newRequestQueue(ForgetPassword.this);
        requestQueue.add(stringRequest);

    }


    private void hitsecondAPI(final String otp,final String details){
        mobilenumber=et_otpforgetpassmobnum.getText().toString().trim();
        loader.show();

        String NextUrl=AppConfig.URL+details+"/"+otp;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NextUrl,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                loader.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "["+response+"]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                      String mgs= jsonObject.getString("Details");

                        Toast.makeText(getApplicationContext(),
                                "OTP Is Matched Successfully",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ForgetPassword.this, ResetPassword.class);
                        intent.putExtra("mobilenumber",mobilenumber);
                        startActivity(intent);

                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Sorry! OTP Is not Matched ",
                                Toast.LENGTH_LONG).show();
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
                            Toast.makeText(ForgetPassword.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ForgetPassword.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ForgetPassword.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ForgetPassword.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(ForgetPassword.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }


                });
        RequestQueue requestQueue = Volley.newRequestQueue(ForgetPassword.this);
        requestQueue.add(stringRequest);

    }
}