package com.example.labourmangement.Engineer;

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
import com.example.labourmangement.Adapter.EnginnerWagesAdapter;
import com.example.labourmangement.Adapter.GetWagesLaborAdapter;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForEngineer;
import com.example.labourmangement.R;
import com.example.labourmangement.model.GetWagesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetWagesEngineer extends AppCompatActivity {

    SessionForEngineer sessionManager;
    RecyclerView recyclerViewWages;
    RecyclerView.LayoutManager layoutManager;
    private static final String TAG = GetWagesEngineer.class.getSimpleName();
    List<GetWagesModel> wageslist;
   CustomLoader loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_wages_engineer);

       // getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        recyclerViewWages = (RecyclerView) findViewById(R.id.recyclerview_getwagesENG);
        recyclerViewWages.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);


        recyclerViewWages.setLayoutManager(layoutManager);

        wageslist = new ArrayList<GetWagesModel>();

        sessionManager = new SessionForEngineer((getApplicationContext()));
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        getLaborWages();
       
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);

        // getIncomingIntentMoney();
    }

    public void getLaborWages() {
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        String name = user.get(SessionForEngineer.KEY_NAME);

        // email
        String email = user.get(SessionForEngineer.KEY_EMAIL);



    /*    progressDialog.setMessage("Loading...Please Wait..");
        progressDialog.show();*/
        Log.d(TAG, "Inserting Response");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GETMONEYREQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                 loader.dismiss();
                try {
                    //converting the string to json array object
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("Success").equalsIgnoreCase("true")) {
                        JSONArray array = jsonObject.getJSONArray("Jobs");
                        {
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject job = array.getJSONObject(i);
                                GetWagesModel getWagesModel = new GetWagesModel();
                                //adding the product to product list
                                getWagesModel.setJob_id(job.getString("job_id"));
                                getWagesModel.setJob_title(job.getString("job_title"));
                                getWagesModel.setJob_wages(job.getString("job_wages"));
                                getWagesModel.setLabor_id(job.getString("applied_by"));
                                getWagesModel.setContractor_id(job.getString("created_by"));
                                getWagesModel.setContractor_name((job.getString("contractor_name")));
                                getWagesModel.setLabor_name(job.getString("labor_name"));
                                getWagesModel.setDate(job.getString("applied_date"));
                                getWagesModel.setWages_approval_status(job.getString("wages_approval_status"));

                                wageslist.add(getWagesModel);
                            }
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                jsonObject.getString("message")+response,
                                Toast.LENGTH_LONG).show();


                    }

                    Log.d(TAG, "Hello" + wageslist.size());
                    //creating adapter object and setting it to recyclerview
                    EnginnerWagesAdapter adapter = new EnginnerWagesAdapter(GetWagesEngineer.this, wageslist);
                    recyclerViewWages.setAdapter(adapter);
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
                            Toast.makeText(GetWagesEngineer.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                             loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(GetWagesEngineer.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                             loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(GetWagesEngineer.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                             loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(GetWagesEngineer.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                             loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(GetWagesEngineer.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("applied_by", email);
                return params;
            }

        };

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}