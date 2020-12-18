package com.example.labourmangement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.labourmangement.Developer.DevJobDetails;
import com.example.labourmangement.Engineer.EngJobDetails;
import com.example.labourmangement.R;
import com.example.labourmangement.model.JobModel;

import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class JobAdapterDev extends RecyclerView.Adapter<JobAdapterDev.ViewHolder> {

    private Context context;
    private List<JobModel> jobModels;

    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }
    public JobAdapterDev(Context context, List JobModel) {
        this.context = context;
        this.jobModels = JobModel;
    }

    @Override
    public JobAdapterDev.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_job_list, parent, false);
        JobAdapterDev.ViewHolder viewHolder = new JobAdapterDev.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(JobAdapterDev.ViewHolder holder, int position) {
        holder.itemView.setTag(jobModels.get(position));

        JobModel pu = jobModels.get(position);

        Log.d("job id","40 "+pu.getJob_id());
        // holder.job_id.setText(pu.getJob_id());
        holder.jobtitle.setText(pu.getJob_title());
        holder.job_details.setText(pu.getJob_details());
        holder.job_wages.setText(pu.getJob_wages());
        holder.job_area.setText(pu.getJob_area());
        holder.job_id.setText(pu.getJob_id());
        holder.created_by.setText(pu.getCreated_by());
        holder.contractor_name.setText(pu.getContractor_name());
        holder.role.setText(pu.getRole());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Product Name: " + jobModels.get(position));
                Intent intent = new Intent(context, DevJobDetails.class);
                intent.putExtra("job_title", pu.getJob_title());
                intent.putExtra("job_details", pu.getJob_details());
                intent.putExtra("job_wages",pu.getJob_wages());
                intent.putExtra("job_area",pu.getJob_area());
                intent.putExtra("job_id",pu.getJob_id());
                intent.putExtra("created_by",pu.getCreated_by());
                intent.putExtra("contractor_name",pu.getContractor_name());

                context.startActivity(intent);
            }
        });


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

        }
    }

}
