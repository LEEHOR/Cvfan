package com.coahr.cvfan.adapter;

import java.util.ArrayList;

import com.coahr.cvfan.R;
import com.coahr.cvfan.activity.TruckInfoActivity;
import com.coahr.cvfan.net.GsonResponse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TruckInfoAdapter extends BaseAdapter {
    Context context;
    ArrayList<GsonResponse.TruckItem> truckList;

    public TruckInfoAdapter(Context context, ArrayList<GsonResponse.TruckItem> truckList){
        this.context = context;
        this.truckList = truckList;
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return truckList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return truckList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        GsonResponse.TruckItem truckItem = truckList.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.vehicle_item, null);
            
            holder.tv_truck_plate_number = (TextView)convertView.findViewById(R.id.tv_truck_plate_number);
            holder.tv_autherise = (TextView)convertView.findViewById(R.id.tv_autherise);
            
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.tv_truck_plate_number.setText(truckItem.PLATE_NO);
        holder.tv_autherise.setText(truckItem.GUARANTEE);
        
        final String autoID = truckItem.AUTO_ID;
        
        convertView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(context, TruckInfoActivity.class);
                intent.putExtra("AUTO_ID", autoID);
                context.startActivity(intent);
            }
        });
                
        return convertView;
    }
    
    class Holder{
        TextView tv_truck_plate_number;
        TextView tv_autherise;
    }
}
