package com.example.labourmangement.Labour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.labourmangement.Adapter.GetWagesLaborAdapter;
import com.example.labourmangement.Adapter.TrackLaborAdapter;
import com.example.labourmangement.Contractor.AppliedJobs;
import com.example.labourmangement.Contractor.TrackLabor;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.R;
import com.example.labourmangement.model.GetWagesModel;
import com.example.labourmangement.model.TrackLaborModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GetWages extends AppCompatActivity {
    SessionManager sessionManager;
    RecyclerView recyclerViewWages;
    RecyclerView.LayoutManager layoutManager;
    private static final String TAG = GetWages.class.getSimpleName();
    List<GetWagesModel> wageslist;
   CustomLoader loader;
    String jobtitleM, jobidM, jobwagesM, appliedbyM;
    TextView titleM, wagesM, idM, apppliedbyM,fetchname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_wages);


        recyclerViewWages = (RecyclerView) findViewById(R.id.recyclerview_getwages);
        recyclerViewWages.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        fetchname=(TextView)findViewById(R.id.fetchname);
        recyclerViewWages.setLayoutManager(layoutManager);

        wageslist = new ArrayList<GetWagesModel>();

        sessionManager = new SessionManager((getApplicationContext()));
         getLaborWages();
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        fetchname.setText(name);

        // getIncomingIntentMoney();

    }

    public void getLaborWages() {
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

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
                                jsonObject.getString("message"),
                                Toast.LENGTH_LONG).show();


                    }

                    Log.d(TAG, "Hello" + wageslist.size());
                    //creating adapter object and setting it to recyclerview
                    GetWagesLaborAdapter adapter = new GetWagesLaborAdapter(GetWages.this, wageslist);
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
                            Toast.makeText(GetWages.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(GetWages.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(GetWages.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(GetWages.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(GetWages.this, "Error While Data Parsing", duration).show();

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
