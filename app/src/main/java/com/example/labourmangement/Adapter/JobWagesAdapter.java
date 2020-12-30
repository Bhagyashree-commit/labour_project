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
import com.example.labourmangement.Contractor.ContractorProfile;
import com.example.labourmangement.Contractor.JobApplyDetails;
import com.example.labourmangement.Contractor.JobWages;
import com.example.labourmangement.Contractor.WagesRequest;
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionManagerContractor;
import com.example.labourmangement.Labour.JobDetails;
import com.example.labourmangement.R;
import com.example.labourmangement.model.AppliedJobsModel;
import com.example.labourmangement.model.JobWagesModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class JobWagesAdapter extends RecyclerView.Adapter<JobWagesAdapter.ViewHolder>  {
    private Context context;
    String jobtitile,jobWAges,jobID;
    SessionManagerContractor sessionManagerContractor;
    CustomLoader loader;
    private List<JobWagesModel> jobwagesmodel;
    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }
    public JobWagesAdapter(Context context, List jobwagesmodel) {
        this.context = context;
        this.jobwagesmodel = jobwagesmodel;
    }
    @Override
    public JobWagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_wages_list, parent, false);
        JobWagesAdapter.ViewHolder viewHolder = new JobWagesAdapter.ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(JobWagesAdapter.ViewHolder holder, int position) {
        sessionManagerContractor=new SessionManagerContractor(context);
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        holder.itemView.setTag(jobwagesmodel.get(position));

        JobWagesModel pu = jobwagesmodel.get(position);

        Log.d("job id","40 "+pu.getJob_id());
        // holder.job_id.setText(pu.getJob_id());
        holder.jobtitle.setText(pu.getJob_title());
        holder.job_wages.setText(pu.getJob_wages());
        holder.job_id.setText(pu.getJob_id());
        holder.labor_name.setText(pu.getLabor_name());
        holder.contractor_name.setText(pu.getContractor_name());
        holder.created_by.setText(pu.getCreated_by());
        holder.applied_by.setText(pu.getApplied_by());
jobtitile=pu.getJob_title();
jobWAges=pu.getJob_wages();
jobID=pu.getJob_id();
        if(pu.getWages_status().equalsIgnoreCase("true"))
        {
            holder.wages_status1.setVisibility(View.GONE);
            Log.e("new","new"+pu.getWages_status());

            holder.wages_status.setVisibility(View.VISIBLE);
            holder.itemView.setEnabled(false);
        }
        else
        {
            holder.wages_status1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendMoney();
                }
            });
            holder.wages_status1.setVisibility(View.VISIBLE);
            holder.wages_status.setVisibility(View.GONE);
            holder.itemView.setEnabled(true);
        }

//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: Product Name: " + jobwagesmodel.get(position));
//                Intent intent = new Intent(context, WagesRequest.class);
//                intent.putExtra("job_title", pu.getJob_title());
//                intent.putExtra("job_wages",pu.getJob_wages());
//                intent.putExtra("job_id",pu.getJob_id());
//                intent.putExtra("labor_name",pu.getLabor_name());
//                intent.putExtra("contractor_name",pu.getContractor_name());
//                intent.putExtra("applied_by",pu.getApplied_by());
//                intent.putExtra("created_by",pu.getCreated_by());
//
//                context.startActivity(intent);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return jobwagesmodel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView jobtitle;
        public TextView job_wages;
        public  TextView job_id;
        public  TextView applied_by;
        public  TextView created_by;
        public  TextView contractor_name;
        public  TextView labor_name;
        public  TextView wages_status;
        public Button wages_status1;

        public ViewHolder(View itemView) {
            super(itemView);

            jobtitle = itemView.findViewById(R.id.jobtitlewages);
            job_wages = itemView.findViewById(R.id.jobwageswages);
            job_id = itemView.findViewById(R.id.textjobidwages);
            applied_by = itemView.findViewById(R.id.applied_by);

            created_by = itemView.findViewById(R.id.createdbyid);
            contractor_name = itemView.findViewById(R.id.contractorname);
            labor_name= itemView.findViewById(R.id.laborname);
            wages_status= itemView.findViewById(R.id.wages_status);
            wages_status1= itemView.findViewById(R.id.wages_status1);

        }
    }

    private void sendMoney() {
     

        HashMap<String, String> user1 = sessionManagerContractor.getUserDetails();

        // name
        String namecon = user1.get(SessionManagerContractor.KEY_NAME);

        // email
        String emailcon = user1.get(SessionManagerContractor.KEY_EMAIL);

        Log.d(TAG, "Email: " + emailcon);

        loader.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_WAGESREQUESTINSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                loader.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "[" + response + "]");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Send Money ");
                    alertDialog.setMessage(" Payment Successful");
                    alertDialog.setIcon(R.drawable.done);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, ContractorProfile.class);
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
                params.put("created_by", emailcon);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
}
