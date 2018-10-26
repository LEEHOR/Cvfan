package com.coahr.cvfan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coahr.cvfan.R;

public class DistanceListAdapter extends BaseAdapter {
	//Activity activity;
    Context context;
    String[] distances;

    public DistanceListAdapter(Context context, String[] distances){
    	//this.activity = activity;
        this.context = context;
        this.distances = distances;
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return distances.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return distances[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        
        String distance = distances[position];
        
        if(convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.brand_pop_item, null);

            holder.tv_brand = (TextView)convertView.findViewById(R.id.tv_brand);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
              
        holder.tv_brand.setText(distance);
                
        return convertView;
    }
    
    class Holder{
        TextView tv_brand;
    }

}
