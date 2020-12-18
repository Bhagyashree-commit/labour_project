package com.example.labourmangement.Architect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.labourmangement.Adapter.PaymentStatusAdapter;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForArch;
import com.example.labourmangement.Owner.OwnerReview;
import com.example.labourmangement.R;
import com.example.labourmangement.model.PaymentStatusModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckPaymentStatus extends AppCompatActivity {
    RecyclerView rv_paymentstatus;
    RecyclerView.LayoutManager layoutManager;
    private static final String TAG = OwnerReview.class.getSimpleName();
    List<PaymentStatusModel> joblist;
    ProgressDialog progressDialog;
    SessionForArch sessionForOwner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payment_status);


        rv_paymentstatus = (RecyclerView) findViewById(R.id.RV_paystatusA);
        rv_paymentstatus.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        rv_paymentstatus.setLayoutManager(layoutManager);

        joblist = new ArrayList<PaymentStatusModel>();
        sessionForOwner=new SessionForArch((getApplicationContext()));
        progressDialog= new ProgressDialog(CheckPaymentStatus.this);

        getRequest();
    }
    public void getRequest() {

        HashMap<String, String> user = sessionForOwner.getUserDetails();

        // name
        String name = user.get(SessionForArch.KEY_NAME);

        // email
        String email = user.get(SessionForArch.KEY_EMAIL);


        Log.d(TAG, "Email "+email);
        progressDialog.setMessage("Loading....Please Wait..");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_PAYMENTSTATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                progressDialog.dismiss();
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);
                    Log.d(TAG, array.toString());
                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject job = array.getJSONObject(i);
                        PaymentStatusModel paymentStatusModel = new PaymentStatusModel();
                        //adding the product to product list
                        paymentStatusModel.setJob_id(job.getString("job_id"));
                        paymentStatusModel.setJob_title(job.getString("job_title"));
                        paymentStatusModel.setJob_wages(job.getString("job_wages"));
                        paymentStatusModel.setLabor_id(job.getString("labor_id"));
                        paymentStatusModel.setContractor_id(job.getString("contractor_id"));
                        paymentStatusModel.setStatus(job.getString("status"));
                        paymentStatusModel.setLabor_name(job.getString("labor_name"));
                        paymentStatusModel.setContractor_name(job.getString("contractor_name"));


                        joblist.add(paymentStatusModel);
                    }

                    Log.d(TAG, "jobgggggggggggggg" + joblist.size());
                    //creating adapter object and setting it to recyclerview
                    PaymentStatusAdapter adapter = new PaymentStatusAdapter(CheckPaymentStatus.this, joblist);
                    rv_paymentstatus.setAdapter(adapter);
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
                            progressDialog.dismiss();
                            // hideDialog();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(CheckPaymentStatus.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            progressDialog.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(CheckPaymentStatus.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server error......................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(CheckPaymentStatus.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(CheckPaymentStatus.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(CheckPaymentStatus.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }

                }){
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("contractor_id",email);
                return params;
            }

        };

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);

    }
}