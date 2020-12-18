package com.example.labourmangement.Contractor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
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
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
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

public class PaymentStatusContractor extends AppCompatActivity {
    RecyclerView rv_paymentstatus;
    RecyclerView.LayoutManager layoutManager;
    private static final String TAG = OwnerReview.class.getSimpleName();
    List<PaymentStatusModel> joblist;
 //   loader loader;
    CustomLoader loader;
    Button btnsubmitreview;
    RatingBar review;
 SessionManagerContractor sessionManagerContractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status_contractor);
        rv_paymentstatus = (RecyclerView) findViewById(R.id.RV_CpaymentStatus);
        btnsubmitreview = (Button) findViewById(R.id.btnsubmitreview);
        review = (RatingBar) findViewById(R.id.rating);

        rv_paymentstatus.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        rv_paymentstatus.setLayoutManager(layoutManager);

        joblist = new ArrayList<PaymentStatusModel>();
        sessionManagerContractor=new SessionManagerContractor((getApplicationContext()));
      //  loader= new loader(PaymentStatusContractor.this);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        String rate=sessionManagerContractor.get("rating");

        Log.e("","new ratingggg"+rate);
   // review.setRating(Float.parseFloat((rate)));



        if(sessionManagerContractor.get("rating").equals("")){
            Toast.makeText(getApplicationContext(),
                    "Please give Review",
                    Toast.LENGTH_LONG).show();
        }
        else{
            review.setRating(Float.parseFloat(sessionManagerContractor.get("rating")));
        }

        btnsubmitreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float rating = review.getRating();
                Log.e("TAG", "" + rating);
                sessionManagerContractor.set("rating", String.valueOf(rating));
                sessionManagerContractor.commit();
                Log.e("BHAGYA",sessionManagerContractor.get("rating"));
                loader.show();

                if (rating == 0) {

                    Toast.makeText(getApplicationContext(),
                            "Please give Review",
                            Toast.LENGTH_LONG).show();

                }
                else if(sessionManagerContractor.get("rating").length()==0){
                    review.setRating(Float.parseFloat(sessionManagerContractor.get("rating")));
                }

                else {
                    Toast.makeText(getApplicationContext(),
                            "Thank You For Your Review",
                            Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(PaymentStatusContractor.this, ContractorProfile.class);
                    startActivity(intent1);
                }
                loader.dismiss();

            }
        });
       // getRequest1();
    }

    public void getRequest1() {

        HashMap<String, String> user = sessionManagerContractor.getUserDetails();

        // name
        String name = user.get(sessionManagerContractor.KEY_NAME);

        // email
        String email = user.get(sessionManagerContractor.KEY_EMAIL);

        Log.d(TAG, "Email "+email);
      
        loader.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_PAYMENTSTATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                loader.dismiss();
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


                                joblist.add(paymentStatusModel);
                            }
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                jsonObject.getString("message")+response,
                                Toast.LENGTH_LONG).show();

                    }
                    //converting the string to json array object
                   // JSONArray array = new JSONArray(response);
                   // Log.d(TAG, array.toString());
                    //traversing through all the object


                    Log.d(TAG, "jobgggggggggggggg" + joblist.size());
                    //creating adapter object and setting it to recyclerview
                    PaymentStatusAdapter adapter = new PaymentStatusAdapter(PaymentStatusContractor.this, joblist);
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
                            loader.dismiss();
                            // hideDialog();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PaymentStatusContractor.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PaymentStatusContractor.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server error......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PaymentStatusContractor.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PaymentStatusContractor.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(PaymentStatusContractor.this, "Error While Data Parsing", duration).show();

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