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
import com.example.labourmangement.CustomLoader;
import com.example.labourmangement.DatabaseConfiguration.AppConfig;
import com.example.labourmangement.DatabaseHelper.SessionManager;
import com.example.labourmangement.Labour.LaborProfile;
import com.example.labourmangement.Labour.WagesApprovalRequest;
import com.example.labourmangement.R;
import com.example.labourmangement.model.GetWagesModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.labourmangement.Adapter.AppliedJobsAdapter.date_time;

public class GetWagesLaborAdapter  extends RecyclerView.Adapter<GetWagesLaborAdapter.ViewHolder>{
    private static final String TAG = GetWagesLaborAdapter.class.getSimpleName();
    private Context context;
    private List<GetWagesModel> getWagesModels;
    SessionManager session;
    CustomLoader loader;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }

    public GetWagesLaborAdapter(Context context, List getWagesModels) {
        this.context = context;
        this.getWagesModels = getWagesModels;
    }
    @NonNull
    @Override
    public GetWagesLaborAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_wages, parent, false);
        GetWagesLaborAdapter.ViewHolder viewHolder = new GetWagesLaborAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GetWagesLaborAdapter.ViewHolder holder, int position) {
        session=new SessionManager(context);
        loader = new CustomLoader(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        holder.itemView.setTag(getWagesModels.get(position));


        GetWagesModel pu = getWagesModels.get(position);

        Log.d("job id","40 "+pu.getJob_id());
        // holder.job_id.setText(pu.getJob_id());
        holder.job_title.setText(pu.getJob_title());
        //holder.lastadd.setText(pu.getLastaddress());
        holder.labor_id.setText(pu.getLabor_id());
        holder.job_id.setText(pu.getJob_id());
        holder.job_wages.setText(pu.getJob_wages());
        holder.contractor_name.setText((pu.getContractor_name()));
        holder.labor_name.setText(pu.getLabor_name());
        holder.contractor_id.setText(pu.getContractor_id());
       // holder.date.setText(getTimeAgo(Long.parseLong(pu.getDate())));
        holder.date.setText(pu.getDate());


        if(pu.getWages_approval_status().equalsIgnoreCase("true"))
        {
            holder.wages_status1.setVisibility(View.GONE);
            holder.wages_approval_status.setVisibility(View.VISIBLE);
            holder.itemView.setEnabled(false);
        }
        else
        {
            holder.wages_status1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendmoneyrequest();
                }
            });
            holder.wages_status1.setVisibility(View.VISIBLE);
            holder.wages_approval_status.setVisibility(View.GONE);
            holder.itemView.setEnabled(true);
        }



//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(context, WagesApprovalRequest.class);
//                intent.putExtra("job_title", pu.getJob_title());
//                intent.putExtra("job_wages",pu.getJob_wages());
//                intent.putExtra("job_id",pu.getJob_id());
//                intent.putExtra("labor_id",pu.getLabor_id());
//                intent.putExtra("labor_name",pu.getLabor_name());
//                intent.putExtra("contractor_name",pu.getContractor_name());
//                intent.putExtra("approved_by",pu.getContractor_id());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return getWagesModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView job_title;
        public  TextView labor_id;
        public  TextView job_id;

        public TextView job_wages;
        public TextView labor_name;
        public  TextView contractor_name;
        public  TextView contractor_id;
        public TextView date;
        public TextView wages_approval_status;
        public Button wages_status1;

        public ViewHolder(View itemView) {
            super(itemView);
            job_title = itemView.findViewById(R.id.textjobtitilewages);
            labor_id = itemView.findViewById(R.id.textlabor_idwages);
            job_id = itemView.findViewById(R.id.textjobidwages);
            job_wages = itemView.findViewById(R.id.textjob_wageswages);
            labor_name=itemView.findViewById(R.id.labornamename145);
            contractor_name=itemView.findViewById(R.id.contractorname145);
            contractor_id=itemView.findViewById(R.id.textcontractor_idwages);
            date=itemView.findViewById(R.id.wage_date);
            wages_approval_status=itemView.findViewById(R.id.wages_approval_status);
            wages_status1=itemView.findViewById(R.id.wages_status1);
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



    private void sendmoneyrequest() {
//        jobtitle = name.getText().toString();
//        jobwages = wages.getText().toString();
//        jobid = id.getText().toString();
//        appliedby=apppliedby.getText().toString();
//        contractorname=cname.getText().toString();
//        laborname=lname.getText().toString();
//        contractorID=contractor_ID.getText().toString();
//        String status = "done";

        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        loader.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_MONEYAPPROVEDREQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Inserting Response: " + response.toString());
                loader.dismiss();
                //hideDialog();
                Log.i("tagconvertstr", "[" + response + "]");
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Your Payment Status ");
                    alertDialog.setMessage(" Payment Successful! Thank You For Response");
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


                params.put("applied_by", email);




                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
}
