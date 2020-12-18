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
import com.example.labourmangement.model.AllJobsModel;
import com.example.labourmangement.model.AppliedJobsModel;

import java.util.List;

import static com.android.volley.VolleyLog.TAG;
import static com.example.labourmangement.Adapter.AppliedJobsAdapter.date_time;
import static com.example.labourmangement.Adapter.AppliedJobsAdapter.getTimeAgo;

public class AllJobsAdapter  extends RecyclerView.Adapter<AllJobsAdapter.ViewHolder> {

    private Context context;
    private List<AllJobsModel> appliedjob;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static  String date_time;

    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }
    public AllJobsAdapter(Context context, List appliedjob) {
        this.context = context;
        this.appliedjob = appliedjob;
    }

    @Override
    public AllJobsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_job_list, parent, false);
        AllJobsAdapter.ViewHolder viewHolder = new AllJobsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AllJobsAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(appliedjob.get(position));

        AllJobsModel pu = appliedjob.get(position);

        Log.d("job id","40 "+pu.getJob_id());
        // holder.job_id.setText(pu.getJob_id());
        holder.jobtitle.setText(pu.getJob_title());
        holder.job_details.setText(pu.getJob_details());
        holder.job_wages.setText(pu.getJob_wages());
        holder.job_area.setText(pu.getJob_area());
        holder.job_id.setText(pu.getJob_id());
//holder.postdate.setText(pu.getPost_date());
        holder.contractor_name.setText(pu.getContractor_name());
        holder.postdate.setText(getTimeAgo(Long.parseLong(pu.getPost_date())));



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
        public  TextView postdate;

        public TextView contractor_name;

        public ViewHolder(View itemView) {
            super(itemView);

            jobtitle = itemView.findViewById(R.id.job_titleapply);
            job_details = itemView.findViewById(R.id.job_detailsapply);
            job_wages = itemView.findViewById(R.id.job_wagesapply);
            job_area = itemView.findViewById(R.id.job_areaapply);
            job_id = itemView.findViewById(R.id.job_idapply);
            appliedby = itemView.findViewById(R.id.applied_byapply);
            postdate = itemView.findViewById(R.id.applieddateapply);
            created_by = itemView.findViewById(R.id.createdbyapply);
            contractor_name= itemView.findViewById(R.id.createdbyapplyname);


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
}
