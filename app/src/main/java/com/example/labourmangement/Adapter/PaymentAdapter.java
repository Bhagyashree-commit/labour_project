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

import com.example.labourmangement.Architect.WagesRequestAR;
import com.example.labourmangement.R;
import com.example.labourmangement.model.JobWagesModel;

import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private Context context;
    private List<JobWagesModel> jobwagesmodel;
    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }
    public PaymentAdapter(Context context, List jobwagesmodel) {
        this.context = context;
        this.jobwagesmodel = jobwagesmodel;
    }
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_wages_list, parent, false);
        PaymentAdapter.ViewHolder viewHolder = new PaymentAdapter.ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(PaymentAdapter.ViewHolder holder, int position) {
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

        public ViewHolder(View itemView) {
            super(itemView);

            jobtitle = itemView.findViewById(R.id.jobtitlewages);
            job_wages = itemView.findViewById(R.id.jobwageswages);
            job_id = itemView.findViewById(R.id.textjobidwages);
            applied_by = itemView.findViewById(R.id.applied_by);

            created_by = itemView.findViewById(R.id.createdbyid);
            contractor_name = itemView.findViewById(R.id.contractorname);
            labor_name= itemView.findViewById(R.id.laborname);

        }
    }
}
