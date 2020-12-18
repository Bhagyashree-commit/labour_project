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
import com.example.labourmangement.Adapter.PaymentAdapter;
import com.example.labourmangement.Contractor.JobWages;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForArch;
import com.example.labourmangement.R;
import com.example.labourmangement.model.PaymentStatusModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentStatusAR extends AppCompatActivity {
    RecyclerView recyclerViewjobs;
    RecyclerView.LayoutManager layoutManager;
    private static final String TAG = JobWages.class.getSimpleName();
    List<PaymentStatusModel> jobwageslist;
 SessionForArch sessionForArch;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status_a_r);

        recyclerViewjobs = (RecyclerView) findViewById(R.id.RV_paystatus);
        recyclerViewjobs.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerViewjobs.setLayoutManager(layoutManager);

        jobwageslist = new ArrayList<PaymentStatusModel>();

        sessionForArch=new SessionForArch((getApplicationContext()));

        progressDialog =new ProgressDialog(PaymentStatusAR.this);

        getRequest();
    }

    public void getRequest() {
        HashMap<String, String> user = sessionForArch.getUserDetails();

        // name
        String name = user.get(SessionForArch.KEY_NAME);

        // email
        String email = user.get(SessionForArch.KEY_EMAIL);
        Log.d(TAG, "Email "+email);

        progressDialog.setMessage("Loading...Please Wait..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GETPAYMENTREQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("Success").equalsIgnoreCase("true")) {
                        JSONArray array = jsonObject.getJSONArray("Jobs");
                        {
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


                                jobwageslist.add(paymentStatusModel);
                            }
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                jsonObject.getString("message")+response,
                                Toast.LENGTH_LONG).show();

                    }

                    Log.d(TAG, "jobgggggggggggggg" + jobwageslist.size());
                    //creating adapter object and setting it to recyclerview
                    PaymentAdapter adapter = new PaymentAdapter(PaymentStatusAR.this, jobwageslist);
                    recyclerViewjobs.setAdapter(adapter);
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

                            Toast.makeText(PaymentStatusAR.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            progressDialog.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PaymentStatusAR.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PaymentStatusAR.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PaymentStatusAR.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            progressDialog.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PaymentStatusAR.this, "Error While Data Parsing", duration).show();

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