package com.example.labourmangement.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labourmangement.Contractor.JobApplyDetails;
import com.example.labourmangement.Contractor.JobWages;
import com.example.labourmangement.Contractor.MapsActivity;
import com.example.labourmangement.Contractor.MapsActivityTrackLabor;
import com.example.labourmangement.Contractor.TrackLabor;
import com.example.labourmangement.Labour.JobDetails;
import com.example.labourmangement.R;
import com.example.labourmangement.model.AppliedJobsModel;
import com.example.labourmangement.model.TrackLaborModel;

import java.util.HashMap;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class TrackLaborAdapter extends RecyclerView.Adapter<TrackLaborAdapter.ViewHolder>  {
    private Context context;
    private List<TrackLaborModel> trackLabor;
    double lat;
    double lng;

    public  interface OnItemClickListener{
        void onClick(View view);

        void onItemClick(int position);
    }
    public TrackLaborAdapter(Context context, List trackLabor) {
        this.context = context;
        this.trackLabor = trackLabor;
    }
    @Override
    public TrackLaborAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracking_labor, parent, false);
        TrackLaborAdapter.ViewHolder viewHolder = new TrackLaborAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrackLaborAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(trackLabor.get(position));

        TrackLaborModel pu = trackLabor.get(position);

        Log.d("job id","40 "+pu.getJob_id());
        // holder.job_id.setText(pu.getJob_id());
        holder.jobtitle.setText(pu.getJob_title());
        //holder.lastadd.setText(pu.getLastaddress());
        holder.labor_id.setText(pu.getLabor_id());
        holder.time.setText(pu.getTime());
        holder.job_id.setText(pu.getJob_id());
        holder.contractor_id.setText(pu.getContractor_id());
        holder.job_area.setText(pu.getJob_area());
        holder.job_wages.setText(pu.getJob_wages());
        holder.latitude.setText(pu.getLatitude());
        holder.longitude.setText(pu.getLongitude());
        holder.laborname.setText(pu.getLaborname());
        holder.lastadd.setText(pu.getLastaddress());
       // String LASTADD=pu.getLastaddress();
       // Log.d(TAG, "addressddddddddddd "+LASTADD);

// end convertAddress

        holder.checkBoxTrack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    TrackLabor.locationList.add(pu.getLastaddress());
                    TrackLabor.idList.add(pu.getLabor_id());
                    TrackLabor.labornameist.add(pu.getLaborname());

                }
                else{
                    TrackLabor.locationList.remove(pu.getLastaddress());
                    TrackLabor.idList.remove(pu.getLabor_id());
                    TrackLabor.labornameist.remove(pu.getLaborname());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return trackLabor.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView jobtitle;
        public TextView lastadd;
        public TextView time;
        public  TextView labor_id;
        public  TextView job_id;
        public  TextView contractor_id;
        public  TextView job_area;
        public TextView job_wages;
        public TextView latitude;
        public CheckBox checkBoxTrack;
        public TextView longitude;
        public  TextView laborname;

        public ViewHolder(View itemView) {
            super(itemView);

            jobtitle = itemView.findViewById(R.id.textjobtitile);
         //   lastadd = (TextView) itemView.findViewById(R.id.textlastaddress);
            time = itemView.findViewById(R.id.texttime);
            labor_id = itemView.findViewById(R.id.textlabor_id);
            job_id = itemView.findViewById(R.id.textjobid);
            contractor_id = itemView.findViewById(R.id.textcontractor_id);
            job_area = itemView.findViewById(R.id.textjob_area);
            job_wages = itemView.findViewById(R.id.textjob_wages);
            latitude = itemView.findViewById(R.id.textlastaddress1);
            longitude = itemView.findViewById(R.id.textlastaddress2);
            lastadd = itemView.findViewById(R.id.textlastaddressss);
            checkBoxTrack= itemView.findViewById(R.id.checkboxtrack);
            laborname= itemView.findViewById(R.id.textlabor_idname);
          // convertAddress();

        }
    }
    public void convertAddress(String LASTADD) {
        if (LASTADD != null && !LASTADD.isEmpty()) {
            try {
                Geocoder coder = new Geocoder(context);
                List<Address> addressList = coder.getFromLocationName(LASTADD, 1);
                if (addressList != null && addressList.size() > 0) {
                    lat = addressList.get(0).getLatitude();
                    lng = addressList.get(0).getLongitude();
                    //Log.d(TAG, "lat " + lat);
                    //Log.d(TAG, "long " + lng);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } // end catch
        } // end if
    }
}
