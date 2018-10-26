package com.coahr.cvfan.adapter;

import java.util.ArrayList;

import com.coahr.cvfan.R;
import com.coahr.cvfan.net.GsonResponse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BrandListAdapter extends BaseAdapter {
	//Activity activity;
    Context context;
    ArrayList<GsonResponse.BrandType> brands;

    public BrandListAdapter(Context context, ArrayList<GsonResponse.BrandType> brands){
    	//this.activity = activity;
        this.context = context;
        this.brands = brands;
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return brands.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return brands.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        
        GsonResponse.BrandType brand = brands.get(position);
        
        if(convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.brand_pop_item, null);

            holder.tv_brand = (TextView)convertView.findViewById(R.id.tv_brand);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
              
        holder.tv_brand.setText(brand.label);
                
        return convertView;
    }
    
    class Holder{
        TextView tv_brand;
    }

}
