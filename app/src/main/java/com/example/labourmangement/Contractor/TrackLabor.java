package com.example.labourmangement.Contractor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.labourmangement.Adapter.AppliedJobsAdapter;
import com.example.labourmangement.Adapter.TrackLaborAdapter;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.R;
import com.example.labourmangement.model.AppliedJobsModel;
import com.example.labourmangement.model.TrackLaborModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class TrackLabor extends AppCompatActivity {
    RecyclerView recyclerViewjobs;
    RecyclerView.LayoutManager layoutManager;
    private static final String TAG = AppliedJobs.class.getSimpleName();
    List<TrackLaborModel> labortracklist;
    SessionManagerContractor sessionManagerContractor;
CustomLoader loader;
Button track;
public static double lat;
public static Double lng;

    public static List<String> locationList  = new ArrayList<>();
    public static List<String> idList  = new ArrayList<>();
    public static List<String> labornameist  = new ArrayList<>();
    public static  ArrayList<HashMap<String,String>> mArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_labor);
        recyclerViewjobs = (RecyclerView) findViewById(R.id.recyclerviewlabortrack);
        recyclerViewjobs.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerViewjobs.setLayoutManager(layoutManager);

        labortracklist = new ArrayList<TrackLaborModel>();

        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        track=findViewById(R.id.track);


        sessionManagerContractor=new SessionManagerContractor((getApplicationContext()));
        getLabor();

track.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(locationList.size()==0){
            Toast.makeText(getApplicationContext(),
                    "Please Click Any One Check Box",
                    Toast.LENGTH_LONG).show();
        }
        else{

            mArrayList.clear();
            HashMap<String,String> map;

            for(int i=0;i<locationList.size();i++)
            {
                Geocoder geocoder = new Geocoder(TrackLabor.this);
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(locationList.get(i), 1);
                    if(addresses.size() > 0) {
                        double latitude= addresses.get(0).getLatitude();
                        double longitude= addresses.get(0).getLongitude();
                        map=new HashMap<>();
                        map.put("lat",""+latitude);
                        map.put("lng",""+longitude);
                        map.put("id",""+idList);
                        map.put("name",""+labornameist.get(i));
                        mArrayList.add(map);


                        Log.e("latitude",""+latitude);
                        Log.e("id",""+idList);
                        Log.e("Laborname",""+labornameist.get(i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("Testtttttt", "Array "+mArrayList.toString());

            }
            Intent intent1 = new Intent(TrackLabor.this, MapsActivityTrackLabor.class);
            startActivity(intent1);
        }

        Log.d(TAG, "address "+locationList.toString());
    }
});

    }
    public void getLabor() {
        HashMap<String, String> user = sessionManagerContractor.getUserDetails();

        // name
        String name = user.get(SessionManagerContractor.KEY_NAME);

        // email
        String email = user.get(SessionManagerContractor.KEY_EMAIL);
        Log.d(TAG, "Email "+email);

        loader.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GETTRACKINGLABOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.dismiss();
                Log.d(TAG, response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("Success").equalsIgnoreCase("true"))
                    {
                        JSONArray array=jsonObject.getJSONArray("Jobs");
                        Log.d(TAG, array.toString());
                        //traversing through all the object
                        for (int i = 0; i < array.length(); i++) {

                            //getting product object from json array
                            JSONObject job = array.getJSONObject(i);
                            TrackLaborModel trackLaborModel = new TrackLaborModel();
                            //adding the product to product list
                            trackLaborModel.setJob_id(job.getString("job_id"));
                            trackLaborModel.setJob_title(job.getString("job_title"));
                            // trackLaborModel.setLatitude(job.getString("latitude"));
                            // trackLaborModel.setLongitude(job.getString("longitude"));
                            trackLaborModel.setLastaddress(job.getString("lastaddress"));
                            trackLaborModel.setTime(job.getString("time"));
                            trackLaborModel.setLabor_id(job.getString("labor_id"));
                            trackLaborModel.setContractor_id(job.getString("contractor_id"));
                            trackLaborModel.setJob_area(job.getString("job_area"));
                            trackLaborModel.setJob_wages(job.getString("job_wages"));
                            trackLaborModel.setLaborname(job.getString("labor_name"));
                            labortracklist.add(trackLaborModel);
                        }
                    }
                    else {
                        track.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),
                                jsonObject.getString("message"),
                                Toast.LENGTH_LONG).show();
                    }
                    TrackLaborAdapter adapter = new TrackLaborAdapter(TrackLabor.this, labortracklist);
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
                            loader.dismiss();
                            // hideDialog();
                             int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(TrackLabor.this, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(TrackLabor.this, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(TrackLabor.this, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(TrackLabor.this, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(TrackLabor.this, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("contractor_id", user.get(SessionManagerContractor.KEY_EMAIL));
                return params;
            }

        };

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
    public void convertAddress(String LASTADD) {
        if (LASTADD != null && !LASTADD.isEmpty()) {
            try {
                Geocoder coder = new Geocoder(this);
                List<Address> addressList = coder.getFromLocationName(LASTADD, 1);
                if (addressList != null && addressList.size() > 0) {
                    lat = addressList.get(0).getLatitude();
                    lng = addressList.get(0).getLongitude();
                    //Log.d(TAG, "lat " + lat);
                    //Log.d(TAG, "long " + lng);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } // end catch
        } // end if
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
