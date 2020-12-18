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

import com.example.labourmangement.Engineer.WagesApprovalRequestENG;
import com.example.labourmangement.R;
import com.example.labourmangement.model.GetWagesModel;

import java.util.List;

public class EnginnerWagesAdapter extends RecyclerView.Adapter<EnginnerWagesAdapter.ViewHolder>  {
    private Context context;
    private List<GetWagesModel> getWagesModels;

    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }

    public EnginnerWagesAdapter(Context context, List getWagesModels) {
        this.context = context;
        this.getWagesModels = getWagesModels;
    }
    @NonNull
    @Override
    public EnginnerWagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_wages, parent, false);
        EnginnerWagesAdapter.ViewHolder viewHolder = new EnginnerWagesAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EnginnerWagesAdapter.ViewHolder holder, int position) {
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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, WagesApprovalRequestENG.class);
                intent.putExtra("job_title", pu.getJob_title());
                intent.putExtra("job_wages",pu.getJob_wages());
                intent.putExtra("job_id",pu.getJob_id());
                intent.putExtra("labor_id",pu.getLabor_id());
                intent.putExtra("labor_name",pu.getLabor_name());
                intent.putExtra("contractor_name",pu.getContractor_name());
                intent.putExtra("approved_by",pu.getContractor_id());
                context.startActivity(intent);
            }
        });
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

        public ViewHolder(View itemView) {
            super(itemView);
            job_title = itemView.findViewById(R.id.textjobtitilewages);
            labor_id = itemView.findViewById(R.id.textlabor_idwages);
            job_id = itemView.findViewById(R.id.textjobidwages);
            job_wages = itemView.findViewById(R.id.textjob_wageswages);
            labor_name=itemView.findViewById(R.id.labornamename145);
            contractor_name=itemView.findViewById(R.id.contractorname145);
            contractor_id=itemView.findViewById(R.id.textcontractor_idwages);
        }
    }
}
