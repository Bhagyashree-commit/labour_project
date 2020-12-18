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
import com.example.labourmangement.R;
import com.example.labourmangement.model.AppliedJobsModel;
import com.example.labourmangement.model.PaymentStatusModel;

import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class PaymentStatusAdapter extends RecyclerView.Adapter<PaymentStatusAdapter.ViewHolder> {
    private Context context;
    private List<PaymentStatusModel> appliedjob;

    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }
    public PaymentStatusAdapter(Context context, List appliedjob) {
        this.context = context;
        this.appliedjob = appliedjob;
    }

    @Override
    public PaymentStatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_list, parent, false);
        PaymentStatusAdapter.ViewHolder viewHolder = new PaymentStatusAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PaymentStatusAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(appliedjob.get(position));

        PaymentStatusModel pu = appliedjob.get(position);

        Log.d("job id","40 "+pu.getJob_id());
        // holder.job_id.setText(pu.getJob_id());
        holder.jobtitle.setText(pu.getJob_title());
        holder.job_wages.setText(pu.getJob_wages());
        holder.job_id.setText(pu.getJob_id());
        holder.labor_name.setText(pu.getLabor_name());
        holder.contractor_name.setText(pu.getContractor_name());
        holder.contractor_id.setText(pu.getContractor_id());
        holder.labor_id.setText(pu.getLabor_id());
        holder.status.setText(pu.getStatus());

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Product Name: " + appliedjob.get(position));
                Intent intent = new Intent(context, JobApplyDetails.class);
                intent.putExtra("job_title", pu.getJob_title());
                intent.putExtra("job_wages",pu.getJob_wages());;
                intent.putExtra("job_id",pu.getJob_id());
                intent.putExtra("contractor_name",pu.getContractor_name());
                intent.putExtra("labor_name",pu.getLabor_name());
                intent.putExtra("contractor_id",pu.getContractor_id());
                intent.putExtra("status",pu.getStatus());
                intent.putExtra("labor_id",pu.getLabor_id());

                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return appliedjob.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView jobtitle;
        public TextView job_wages;
        public  TextView job_id;
        public  TextView labor_name;
        public TextView contractor_name;
        public  TextView labor_id;
        public TextView contractor_id;
        public TextView status;

        public ViewHolder(View itemView) {
            super(itemView);

            jobtitle = itemView.findViewById(R.id.jtitle);
            job_wages = itemView.findViewById(R.id.jwages);
            job_id = itemView.findViewById(R.id.jid);
            labor_name= itemView.findViewById(R.id.wlname);
            contractor_name= itemView.findViewById(R.id.wcname);
            status= itemView.findViewById(R.id.wstatus);
            labor_id= itemView.findViewById(R.id.lid);
            contractor_id= itemView.findViewById(R.id.cid);

        }
    }
}
