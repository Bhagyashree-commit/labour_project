package com.example.labourmangement.Adapter;

import android.content.Context;
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
import com.example.labourmangement.Contractor.ContractorProfile;
import com.example.labourmangement.Contractor.JobApplyDetails;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Labour.JobDetails;
import com.example.labourmangement.R;
import com.example.labourmangement.model.AppliedJobsModel;
import com.example.labourmangement.model.JobModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class AppliedJobsAdapter extends RecyclerView.Adapter<AppliedJobsAdapter.ViewHolder> {

    private Context context;
    private List<AppliedJobsModel> appliedjob;
    SessionManagerContractor sessionManagerContractor;
    CustomLoader loader;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static  String date_time;


    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }
    public AppliedJobsAdapter(Context context, List appliedjob) {
        this.context = context;
        this.appliedjob = appliedjob;
    }

    @Override
    public AppliedJobsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.applied_job_list, parent, false);
        AppliedJobsAdapter.ViewHolder viewHolder = new AppliedJobsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AppliedJobsAdapter.ViewHolder holder, int position) {
        sessionManagerContractor=new SessionManagerContractor(context);
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        holder.itemView.setTag(appliedjob.get(position));

        AppliedJobsModel pu = appliedjob.get(position);

        Log.d("job id","40 "+pu.getJob_id());
        // holder.job_id.setText(pu.getJob_id());
        holder.jobtitle.setText(pu.getJob_title());
        holder.job_details.setText(pu.getJob_details());
        holder.job_wages.setText(pu.getJob_wages());
        holder.job_area.setText(pu.getJob_area());
        holder.job_id.setText(pu.getJob_id());
        holder.appliedby.setText(pu.getApplied_by());
        holder.created_by.setText(pu.getCreated_by());
       holder.applieddate.setText(pu.getApplied_date());
        holder.labor_name.setText(pu.getLabor_name());
        holder.contractor_name.setText(pu.getContractor_name());



        if(pu.getApproved_status().equalsIgnoreCase("true"))
        {
holder.approved_status1.setVisibility(View.GONE);
Log.e("new","new"+pu.getApproved_status());

            holder.approved_status.setVisibility(View.VISIBLE);
            holder.itemView.setEnabled(false);
        }
        else
        {
            holder.approved_status1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendApproval();
                }
            });
            holder.approved_status1.setVisibility(View.VISIBLE);
            holder.approved_status.setVisibility(View.GONE);
            holder.itemView.setEnabled(true);
        }


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: Product Name: " + appliedjob.get(position));
//                Intent intent = new Intent(context, JobApplyDetails.class);
//                intent.putExtra("job_title", pu.getJob_title());
//                intent.putExtra("job_details", pu.getJob_details());
//                intent.putExtra("job_wages",pu.getJob_wages());
//                intent.putExtra("job_area",pu.getJob_area());
//                intent.putExtra("job_id",pu.getJob_id());
//                intent.putExtra("applied_by",pu.getApplied_by());
//                intent.putExtra("created_by",pu.getCreated_by());
//                intent.putExtra("applied_date",pu.getApplied_date());
//                intent.putExtra("contractor_name",pu.getContractor_name());
//                intent.putExtra("labor_name",pu.getLabor_name());
//
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return appliedjob.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView jobtitle;
        public TextView job_details;
        public TextView job_wages;
        public  TextView job_area;
        public  TextView job_id;
        public  TextView appliedby;
        public  TextView applieddate;
        public  TextView created_by;
        public  TextView labor_name;
        public TextView contractor_name;
        public Button approved_status1;
        public TextView approved_status;

        public ViewHolder(View itemView) {
            super(itemView);

            jobtitle = itemView.findViewById(R.id.job_titleapply);
            job_details = itemView.findViewById(R.id.job_detailsapply);
            job_wages = itemView.findViewById(R.id.job_wagesapply);
            job_area = itemView.findViewById(R.id.job_areaapply);
            job_id = itemView.findViewById(R.id.job_idapply);
            appliedby = itemView.findViewById(R.id.applied_byapply);
            applieddate = itemView.findViewById(R.id.applieddateapply);
            created_by = itemView.findViewById(R.id.createdbyapply);
            labor_name= itemView.findViewById(R.id.applied_byapplyname);
            contractor_name= itemView.findViewById(R.id.createdbyapplyname);
            approved_status1= itemView.findViewById(R.id.approved_status1);
            //approved_status2= itemView.findViewById(R.id.approved_status2);
            approved_status= itemView.findViewById(R.id.approved_status);

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

    private void sendApproval() {
        HashMap<String, String> user1 = sessionManagerContractor.getUserDetails();

        // name
        String namecon = user1.get(SessionManagerContractor.KEY_NAME);

        // email
        String emailcon = user1.get(SessionManagerContractor.KEY_EMAIL);

        String role=user1.get((SessionManagerContractor.KEY_ROLE));

        Log.d(TAG, "Email: " + emailcon);
        Log.d(TAG, "NAmew: " + namecon);


        loader.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTAPPROVALREQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                loader.dismiss();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if(jsonObject.getString("success").equalsIgnoreCase("1")) {
                        Toast.makeText(context, "Application Approved", Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(context, ContractorProfile.class);
                        context.startActivity(intent1);
                    }
                    else {

                    }
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

                params.put("created_by", emailcon);

                Log.d(TAG, "PP: " + params);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
        
        
}
