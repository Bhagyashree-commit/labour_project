package com.example.labourmangement.Owner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.example.labourmangement.Adapter.AppliedJobsOwnerAdapter;
import com.example.labourmangement.Adapter.PaymentStatusAdapter;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForOwner;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.Labour.WriteReview;
import com.example.labourmangement.R;
import com.example.labourmangement.model.AppliedJobsModel;
import com.example.labourmangement.model.AppliedJobsModelOwner;
import com.example.labourmangement.model.PaymentStatusModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OwnerReview extends AppCompatActivity {
    private static final String TAG = WriteReview.class.getSimpleName();
    Button btn_submit;
    TextView textreview;
    SessionManager session;
    TextView fetchname;
    RatingBar ratingBar;
   /* RecyclerView rv_paymentstatus;
    RecyclerView.LayoutManager layoutManager;
    private static final String TAG = PaymentStatus.class.getSimpleName();
    List<PaymentStatusModel> joblist;
    CustomLoader loader;
    SessionForOwner sessionForOwner;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        fetchname=(TextView)findViewById(R.id.fetchname);
        ratingBar=(RatingBar)findViewById(R.id.rating);
        btn_submit=(Button)findViewById(R.id.submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float rating = ratingBar.getRating();
                Log.e("TAG", "" + rating);
                //loader.show();

                if (rating == 0) {

                    Toast.makeText(getApplicationContext(),
                            "Please give Review",
                            Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Thank You For Your Review",
                            Toast.LENGTH_LONG).show();
                }
                // loader.dismiss();
            }

        });
//textreview.setText("");


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);


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




      /*  rv_paymentstatus = (RecyclerView) findViewById(R.id.RV_paystatus);
        rv_paymentstatus.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        rv_paymentstatus.setLayoutManager(layoutManager);

        joblist = new ArrayList<PaymentStatusModel>();
        sessionForOwner=new SessionForOwner((getApplicationContext()));
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);*/


        //getRequest();
    }
   /* public void getRequest() {

    HashMap<String, String> user = sessionForOwner.getUserDetails();

    // name
    String name = user.get(sessionForOwner.KEY_NAME);

    // email
    String email = user.get(sessionForOwner.KEY_EMAIL);

    Log.d(TAG, "Email "+email);
    //loader.setMessage("Loading....Please Wait..");
    loader.show();


    StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_PAYMENTSTATUS, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.d(TAG, response.toString());
            loader.dismiss();
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
                PaymentStatusAdapter adapter = new PaymentStatusAdapter(PaymentStatus.this, joblist);
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
                        Toast.makeText(PaymentStatus.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                    } else if (error instanceof AuthFailureError) {
                        //TODO
                        System.out.println("AuthFailureError.........................." + error);
                        // hideDialog();
                        loader.dismiss();
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(PaymentStatus.this, "Your Are Not Authrized..", duration).show();
                    } else if (error instanceof ServerError) {
                        System.out.println("server error......................." + error);
                        //hideDialog();
                        loader.dismiss();

                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(PaymentStatus.this, "Server Error", duration).show();
                        //TODO
                    } else if (error instanceof NetworkError) {
                        System.out.println("NetworkError........................." + error);
                        //hideDialog();
                        loader.dismiss();

                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(PaymentStatus.this, "Please Check Your Internet Connection", duration).show();
                        //TODO
                    } else if (error instanceof ParseError) {
                        System.out.println("parseError............................." + error);
                        //hideDialog();
                        loader.dismiss();

                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(PaymentStatus.this, "Error While Data Parsing", duration).show();

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
}*/


