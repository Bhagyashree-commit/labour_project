package com.example.labourmangement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.labourmangement.Contractor.JobApplyDetails;
import com.example.labourmangement.Contractor.JobWages;
import com.example.labourmangement.Contractor.WagesRequest;
import com.example.labourmangement.Labour.JobDetails;
import com.example.labourmangement.R;
import com.example.labourmangement.model.AppliedJobsModel;
import com.example.labourmangement.model.JobWagesModel;

import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class JobWagesAdapter extends RecyclerView.Adapter<JobWagesAdapter.ViewHolder>  {
    private Context context;
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


        if(pu.getWages_status().equalsIgnoreCase("true"))
        {
            holder.wages_status.setVisibility(View.VISIBLE);
            holder.itemView.setEnabled(false);
        }
        else
        {
            holder.wages_status.setVisibility(View.GONE);
            holder.itemView.setEnabled(true);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Product Name: " + jobwagesmodel.get(position));
                Intent intent = new Intent(context, WagesRequest.class);
                intent.putExtra("job_title", pu.getJob_title());
                intent.putExtra("job_wages",pu.getJob_wages());
                intent.putExtra("job_id",pu.getJob_id());
                intent.putExtra("labor_name",pu.getLabor_name());
                intent.putExtra("contractor_name",pu.getContractor_name());
                intent.putExtra("applied_by",pu.getApplied_by());
                intent.putExtra("created_by",pu.getCreated_by());

                context.startActivity(intent);
            }
        });


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

        }
    }
}
