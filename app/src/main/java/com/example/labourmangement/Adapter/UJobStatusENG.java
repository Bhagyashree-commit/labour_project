package com.example.labourmangement.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
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
import com.example.labourmangement.Contractor.PostJobs;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionForEngineer;

import com.example.labourmangement.R;
import com.example.labourmangement.model.GetWagesModel;
import com.example.labourmangement.model.JobsStatusModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;



public class UJobStatusENG extends RecyclerView.Adapter<UJobStatusENG.ViewHolder> {
    private Context context;
    private List<JobsStatusModel> statusModels;
    SessionForEngineer sessionForEngineer;
    ProgressDialog progressDialog;


    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }

    public UJobStatusENG(Context context, List statusModels) {
        this.context = context;
        this.statusModels = statusModels;
    }


    @NonNull
    @Override
    public UJobStatusENG.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_list, parent, false);
        UJobStatusENG.ViewHolder viewHolder = new UJobStatusENG.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UJobStatusENG.ViewHolder holder, int position) {
        holder.itemView.setTag(statusModels.get(position));

        JobsStatusModel jobs = statusModels.get(position);

        Log.d("job id","40 "+jobs.getJob_id());
        // holder.job_id.setText(pu.getJob_id());
       holder.jobtitle.setText(jobs.getJob_title());
        holder.job_details.setText(jobs.getJob_details());
        holder.job_wages.setText(jobs.getJob_wages());
        holder.job_area.setText(jobs.getJob_area());
        holder.job_id.setText(jobs.getJob_id());
        holder.appliedby.setText(jobs.getApplied_by());
        holder.applieddate.setText(jobs.getApplied_date());
        holder.approved_by.setText(jobs.getCreated_by());
        holder.approved_byname.setText(jobs.getApproved_byname());



       holder.btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String JobID=statusModels.get(position).getJob_id();
                String JobTITLE=statusModels.get(position).getJob_title();
       String JobArea=statusModels.get(position).getJob_area();
       String JobWages=statusModels.get(position).getJob_wages();
       String Role ="Engineer";
       String STatus="done";
       String APProval_givenby=statusModels.get(position).getCreated_by();
       String APprovalGivenbyname=statusModels.get(position).getApproved_byname();

                Log.e("name111",statusModels.get(position).getJob_title());



                    Log.d(TAG, "Inserting Response:hiiiiiiiiiiiiiii ");

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERTENGRESPONSE,new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d(TAG, "Inserting Response:hiiiiiiiiiiiiiii " + response.toString());
                            //   progressDialog.dismiss();
                            //hideDialog();
                            Log.i("bhagyaaaaaa", "["+response+"]");

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                alertDialog.setTitle("Job Post ");
                                alertDialog.setMessage(" Your Job Post Is successful");
                                alertDialog.setIcon(R.drawable.done);
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
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
                                        // progressDialog.dismiss();
                                        // hideDialog();
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast.makeText(context, "Connection Time Out.. Please Check Your Internet Connection", duration).show();
                                    } else if (error instanceof AuthFailureError) {
                                        //TODO
                                        System.out.println("AuthFailureError.........................." + error);
                                        // hideDialog();
                                        // progressDialog.dismiss();
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast.makeText(context, "Your Are Not Authrized..", duration).show();
                                    } else if (error instanceof ServerError) {
                                        System.out.println("server erroer......................." + error);
                                        //hideDialog();
                                        // progressDialog.dismiss();

                                        int duration = Toast.LENGTH_SHORT;
                                        Toast.makeText(context, "Server Error", duration).show();
                                        //TODO
                                    } else if (error instanceof NetworkError) {
                                        System.out.println("NetworkError........................." + error);
                                        //hideDialog();
                                        // progressDialog.dismiss();

                                        int duration = Toast.LENGTH_SHORT;
                                        Toast.makeText(context, "Please Check Your Internet Connection", duration).show();
                                        //TODO
                                    } else if (error instanceof ParseError) {
                                        System.out.println("parseError............................." + error);
                                        //hideDialog();
                                        // progressDialog.dismiss();

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
                            params.put("job_id",JobID);
                            params.put("job_title",JobTITLE);
                            params.put("job_wages", JobWages);
                            params.put("job_area",JobArea);
                            params.put("aprroval_given", APProval_givenby);
                            params.put(" approval_given_by", APprovalGivenbyname);
                            params.put("role",Role);
                            params.put("status",STatus);

                            return params;
                        }

                    };

                    // Creating RequestQueue.
                    RequestQueue requestQueue = Volley.newRequestQueue(context);

                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add(stringRequest);

                }

        });


    }

    @Override
    public int getItemCount() {
        return statusModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView jobtitle;
        public TextView job_details;
        public TextView job_wages;
        public  TextView job_area;
        public  TextView job_id;
        public  TextView appliedby;
        public  TextView applieddate;
        public  TextView approved_by;
        public  TextView approved_byname;
        private Button btndone;



        public ViewHolder(View itemView) {
            super(itemView);

            jobtitle = itemView.findViewById(R.id.job_title1);
            job_details = itemView.findViewById(R.id.job_details1);
            job_wages = itemView.findViewById(R.id.job_wages1);
            job_area = itemView.findViewById(R.id.job_area1);
            job_id = itemView.findViewById(R.id.job_id1);
            appliedby = itemView.findViewById(R.id.applied_by1);
            applieddate = itemView.findViewById(R.id.applieddate1);
            approved_by = itemView.findViewById(R.id.approved_by1);
            approved_byname = itemView.findViewById(R.id.approved_byname);
            btndone=itemView.findViewById(R.id.btn);


        }
    }



}
