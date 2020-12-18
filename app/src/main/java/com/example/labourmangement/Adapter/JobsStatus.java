package com.example.labourmangement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labourmangement.Contractor.JobApplyDetails;
import com.example.labourmangement.Labour.jobStatusAndTrack;
import com.example.labourmangement.R;
import com.example.labourmangement.model.AppliedJobsModel;
import com.example.labourmangement.model.JobsStatusModel;

import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class JobsStatus extends RecyclerView.Adapter<JobsStatus.ViewHolder> {
    private Context context;
    private List<JobsStatusModel> statusModels;

    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }

    public JobsStatus(Context context, List statusModels) {
        this.context = context;
        this.statusModels = statusModels;
    }


    @NonNull
    @Override
    public JobsStatus.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_status_list, parent, false);
        JobsStatus.ViewHolder viewHolder = new JobsStatus.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull JobsStatus.ViewHolder holder, int position) {
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

        if(jobs.getTrack_status().equalsIgnoreCase("true"))
        {
            holder.track_status.setVisibility(View.VISIBLE);
            holder.itemView.setEnabled(false);
        }
        else
        {
            holder.track_status.setVisibility(View.GONE);
            holder.itemView.setEnabled(true);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Product Name: " + statusModels.get(position));
                Intent intent = new Intent(context, jobStatusAndTrack.class);
                intent.putExtra("job_title", jobs.getJob_title());
                intent.putExtra("job_details", jobs.getJob_details());
                intent.putExtra("job_wages",jobs.getJob_wages());
                intent.putExtra("job_area",jobs.getJob_area());
                intent.putExtra("job_id",jobs.getJob_id());
                intent.putExtra("applied_by",jobs.getApplied_by());
                intent.putExtra("applied_date",jobs.getApplied_date());
                intent.putExtra("created_by",jobs.getCreated_by());
                intent.putExtra("contractor_name",jobs.getApproved_byname());


                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return statusModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView jobtitle;
        public TextView job_details;
        public TextView job_wages;
        public  TextView job_area;
        public  TextView job_id;
        public  TextView appliedby;
        public  TextView applieddate;
        public  TextView approved_by;
        public  TextView approved_byname;
        public  TextView track_status;

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
            track_status = itemView.findViewById(R.id.track_status);

        }
    }
}
