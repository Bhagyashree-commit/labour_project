package com.example.labourmangement.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.Labour.JobDetails;
import com.example.labourmangement.Labour.LaborProfile;
import com.example.labourmangement.R;
import com.example.labourmangement.model.JobModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.example.labourmangement.Adapter.AppliedJobsAdapter.date_time;
import static com.example.labourmangement.Adapter.AppliedJobsAdapter.getTimeAgo;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    private Context context;
    private List<JobModel> jobModels;
    SessionManager session;
    CustomLoader loader;
    String jobTitle,jobDetails,jobID,jobWAges,jobArea,jobCreatedBy,jobCreatedByName;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }
    public JobAdapter(Context context, List JobModel) {
        this.context = context;
        this.jobModels = JobModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_job_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        session=new SessionManager(context);
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        holder.itemView.setTag(jobModels.get(position));

        JobModel pu = jobModels.get(position);

        Log.d("job id","40 "+pu.getJob_id());
       // holder.job_id.setText(pu.getJob_id());
        holder.jobtitle.setText(pu.getJob_title());
       // holder.job_details.setText(pu.getJob_details());
        holder.job_wages.setText(pu.getJob_wages());
        holder.job_area.setText(pu.getJob_area());
        holder.job_id.setText(pu.getJob_id());
        holder.created_by.setText(pu.getCreated_by());
        holder.contractor_name.setText(pu.getContractor_name());
        //holder.role.setText(pu.getRole());
      //  holder.date.setText(getTimeAgo(Long.parseLong(pu.getDate())));


        if(pu.getStatus().equals("Applied"))
        {
            holder.applyforjob.setVisibility(View.GONE);

            holder.applied.setVisibility(View.VISIBLE);
            holder.itemView.setEnabled(false);
        }
        else
        {
            holder.applyforjob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendData();

                }
            });
            holder.applyforjob.setVisibility(View.VISIBLE);
            holder.applied.setVisibility(View.GONE);
            holder.itemView.setEnabled(true);
        }

jobTitle=pu.getJob_title();
jobDetails=pu.getJob_details();
jobWAges=pu.getJob_wages();
jobID=pu.getJob_id();
jobArea=pu.getJob_area();
jobCreatedBy=pu.getCreated_by();
jobCreatedByName=pu.getContractor_name();

    }

    @Override
    public int getItemCount() {
        return jobModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView jobtitle;
        public TextView job_details;
        public TextView job_wages;
        public  TextView job_area;
        public  TextView job_id;
        public  TextView created_by;
        public TextView role;
        public  TextView contractor_name;
        public  TextView date;
        public  TextView applied;
        public Button applyforjob;

        public ViewHolder(View itemView) {
            super(itemView);

            jobtitle = itemView.findViewById(R.id.job_title);
            job_details = itemView.findViewById(R.id.job_details);
            job_wages = itemView.findViewById(R.id.job_wages);
            job_area = itemView.findViewById(R.id.job_area);
            job_id = itemView.findViewById(R.id.job_id);
            created_by = itemView.findViewById(R.id.createdby);
            contractor_name = itemView.findViewById(R.id.createdbyname);
            role = itemView.findViewById(R.id.role);
            date = itemView.findViewById(R.id.date);
            applyforjob = itemView.findViewById(R.id.applyforjob);
            applied = itemView.findViewById(R.id.applystatus);



        }

    }
    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;


        }
        // long now = getCurrentTime(ctx);
        long now = System.currentTimeMillis();

        if (time > now || time <= 0) {
            return null;
        }
        // TODO: localize
        final long diff = now - time;

        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS)
        {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            //mins = diff / MINUTE_MILLIS ;
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            if((diff/HOUR_MILLIS)==1)
            {
                return  "an hour ago";
            }
            else {
                return diff / HOUR_MILLIS + " hours ago";
            }
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return date_time;
        }    }



    private void sendData() {
//        jobtitle = name.getText().toString();
//        jobdetails = destcription.getText().toString();
//        jobwages = wages.getText().toString();
//        jobarea = area.getText().toString();
//        jobid = id.getText().toString();
//        jobcreatedby = jobcreated_by.getText().toString();

        String status = "Applied";

        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);
        // Showing progress dialog at user registration time.

        loader.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTAPPLIEDJOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                loader.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "[" + response + "]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Job Application ");
                    Log.e("TAG","NEW TAG"+response);
                    alertDialog.setMessage(jsonObject.getString("message"));
                    alertDialog.setIcon(R.drawable.done);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context,
                                        LaborProfile.class);
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

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
                            Toast.makeText(context, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            System.out.println("AuthFailureError.........................." + error);
                            // hideDialog();
                            loader.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, "Your Are Not Authrized..", duration).show();
                        } else if (error instanceof ServerError) {
                            System.out.println("server erroer......................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, "Server Error", duration).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            System.out.println("NetworkError........................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, "Please Check Your Internet Connection", duration).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            System.out.println("parseError............................." + error);
                            //hideDialog();
                            loader.dismiss();

                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, "Error While Data Parsing", duration).show();

                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("job_id", jobID);
                params.put("job_title", jobTitle);
                params.put("job_wages", jobWAges);
                params.put("job_area", jobArea);
                params.put("applied_by", email);
                params.put("contractor_name", jobCreatedByName);
                params.put("labor_name", name);
                params.put("status", status);
                params.put("created_by", jobCreatedBy);
Log.e("Adapter",""+params);
                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
}
