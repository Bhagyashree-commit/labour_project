package com.example.labourmangement.Engineer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.example.labourmangement.Adapter.JobAdapter;
import com.example.labourmangement.Adapter.JobAdapterEng;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForEngineer;
import com.example.labourmangement.Labour.JobOffer;
import com.example.labourmangement.R;
import com.example.labourmangement.model.JobModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EngJobOffer extends AppCompatActivity {
    private static final String TAG = EngJobOffer.class.getSimpleName();

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<JobModel> jobmodellist;
    CustomLoader loader;
    SessionForEngineer sessionForEngineer;
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
        setContentView(R.layout.activity_eng_job_offer);


            fetchname=(TextView)findViewById(R.id.fetchname);

            recyclerView = (RecyclerView) findViewById(R.id.recycleViewENGOFFERr);
            recyclerView.setHasFixedSize(true);
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

            layoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(layoutManager);

            jobmodellist = new ArrayList<>();

            sendOffer();

            sessionForEngineer = new SessionForEngineer(getApplicationContext());
            HashMap<String, String> user = sessionForEngineer.getUserDetails();

            // name
            String name = user.get(SessionForEngineer.KEY_NAME);

            // email
            String email = user.get(SessionForEngineer.KEY_EMAIL);



        }


    public void sendOffer(){

        final String role ="architect";

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
                                jsonObject.getString("message")+response,
                                Toast.LENGTH_LONG).show();
                    }


                    Log.d(TAG, "jobgggggggggggggg" + jobmodellist.size());
                    //creating adapter object and setting it to recyclerview
                    JobAdapter adapter = new JobAdapter(EngJobOffer.this, jobmodellist);
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
                            Toast.makeText(EngJobOffer.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngJobOffer.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngJobOffer.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngJobOffer.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(EngJobOffer.this, "Error While Data Parsing", duration).show();

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



}