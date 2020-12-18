package com.example.labourmangement.Labour;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.labourmangement.Adapter.AppliedJobsAdapter;
import com.example.labourmangement.Adapter.JobAdapter;
import com.example.labourmangement.Contractor.AppliedJobs;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseConfiguration.SharedPrefManager;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.R;
import com.example.labourmangement.model.AppliedJobsModel;
import com.example.labourmangement.model.JobModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobOffer extends AppCompatActivity implements JobAdapter.OnItemClickListener {
    private static final String TAG = JobOffer.class.getSimpleName();

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
Button buttonDisplayToken;
TextView textViewToken;
    List<JobModel> jobmodellist;
CustomLoader loader;
    SessionManager session;
    TextView fetchname;

    RequestQueue rq;
   // public  static final String EXTRA_URL = " image_path";
    public  static  final  String EXTRA_TITLE = "job_title";
    public  static  final  String EXTRA_DETAILS = "job_details";
    public  static  final  String EXTRA_WAGES = "job_wages";
    public  static  final  String EXTRA_AREA = "job_area";
    public  static  final  String EXTRA_ID = "job_id";

    //  public  static  final  String EXTRA_DESCRIPTION = "product_description";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_offer);

        //getting views from xml
        textViewToken = (TextView) findViewById(R.id.textViewToken);
        buttonDisplayToken = (Button) findViewById(R.id.buttonDisplayToken);
        fetchname=(TextView)findViewById(R.id.fetchname);

        buttonDisplayToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == buttonDisplayToken) {
                    //getting token from shared preferences
                    String token = SharedPrefManager.getInstance(JobOffer.this).getDeviceToken();

                    //if token is not null
                    if (token != null) {
                        //displaying the token
                        textViewToken.setText(token);
                    } else {
                        //if token is null that means something wrong
                        textViewToken.setText("Token not generated");
                    }
                }
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        jobmodellist = new ArrayList<>();

        sendOffer();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

  String role= user.get(SessionManager.KEY_ROLE);

        fetchname=(TextView)findViewById(R.id.fetchname);
        fetchname.setText(name);

    }


    public void sendOffer(){

    final String role ="contractor";

        //loader.setMessage("Loading...Please Wait..");
        loader.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GETJOBOFFER,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("testttttttt", response);


                        loader.dismiss();

                        try {
                            //converting the string to json array object
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("Success").equalsIgnoreCase("true")) {
                                JSONArray array = jsonObject.getJSONArray("Jobs");
                                {
                                    //traversing through all the object
                                    for (int i = 0; i < array.length(); i++) {

                                        //getting product object from json array
                                        JSONObject job = array.getJSONObject(i);
                                        JobModel jobModel = new JobModel();
                                        //adding the product to product list
                                        jobModel.setJob_area(job.getString("job_area"));
                                        jobModel.setJob_title(job.getString("job_title"));
                                        jobModel.setJob_details(job.getString("job_details"));
                                        jobModel.setJob_wages(job.getString("job_wages"));
                                        jobModel.setJob_id(job.getString("job_id"));
                                        jobModel.setCreated_by(job.getString("created_by"));
                                        jobModel.setRole(job.getString("role"));
                                        jobModel.setContractor_name(job.getString("contractor_name"));
                                        jobModel.setDate(job.getString("post_date"));


                                        jobmodellist.add(jobModel);
                                    }
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_LONG).show();
                            }


                            Log.d(TAG, "jobgggggggggggggg" + jobmodellist.size());
                            //creating adapter object and setting it to recyclerview
                            JobAdapter adapter = new JobAdapter(JobOffer.this, jobmodellist);
                            recyclerView.setAdapter(adapter);

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
                            Toast.makeText(JobOffer.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobOffer.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobOffer.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobOffer.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(JobOffer.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("role", role );
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

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this,JobDetails.class);
        JobModel clickedItem=jobmodellist.get(position);
    //   detailIntent.putExtra(EXTRA_URL,clickedItem.getImage_path());
        detailIntent.putExtra(EXTRA_TITLE,clickedItem.getJob_title());
        detailIntent.putExtra(EXTRA_DETAILS,clickedItem.getJob_details());
        detailIntent.putExtra(EXTRA_WAGES,clickedItem.getJob_wages());
        detailIntent.putExtra(EXTRA_AREA,clickedItem.getJob_area());
        detailIntent.putExtra(EXTRA_ID,clickedItem.getJob_id());


        startActivity(detailIntent);

    }
}
