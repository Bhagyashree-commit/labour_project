package com.example.labourmangement.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labourmangement.Labour.LaborProfile;
import com.example.labourmangement.R;
import com.example.labourmangement.model.ProfileModel;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private List<ProfileModel>myLists;
    private Context context;

    public ProfileAdapter(List<ProfileModel> myLists, Context context) {
        this.myLists = myLists;
        this.context = context;
    }

    public ProfileAdapter(List<ProfileModel> myLists, Context context, LaborProfile laborProfile) {
        this.myLists = myLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileModel myList=myLists.get(position);
        holder.textView.setText(myList.getDesc());
        holder.img.setImageDrawable(context.getResources().getDrawable(myList.getImage()));
    }

    @Override
    public int getItemCount() {
        return myLists.size();
    }

    public void setOnItemClickListner(LaborProfile laborProfile) {
    }

    public interface OnItemClickListener {
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.image);
            textView= itemView.findViewById(R.id.desc);
        }
    }
}





