package com.example.labourmangement.Labour;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.labourmangement.R;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int images[];
    String[] fruit;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, int[] flags, String[] fruit) {
        this.context = applicationContext;
        this.images = flags;
        this.fruit = fruit;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageViewc);
        TextView names = (TextView) view.findViewById(R.id.textViewc);
        icon.setImageResource(images[i]);
        names.setText(fruit[i]);

        Log.e("RASS",""+fruit[i]);
        return view;
    }
}
