package com.coahr.cvfan.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coahr.cvfan.R;
import com.coahr.cvfan.net.GsonResponse;

public class TruckTypeListAdapter extends BaseAdapter {
	//Activity activity;
	public static int TruckTypeChosePosition;
    Context context;
    ArrayList<GsonResponse.TruckType> truckTypes;

    public TruckTypeListAdapter(Context context, ArrayList<GsonResponse.TruckType> truckTypes){
    	//this.activity = activity;
        this.context = context;
        this.truckTypes = truckTypes;
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return truckTypes.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return truckTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        
        GsonResponse.TruckType truckType = truckTypes.get(position);
        
        if(convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.brand_item, null);

            holder.tv_brand = (TextView)convertView.findViewById(R.id.tv_brand);
            holder.iv_chosed = (ImageView)convertView.findViewById(R.id.iv_chosed);
            holder.iv_chosed.setVisibility(View.INVISIBLE);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        
        /*final  GsonResponse.TruckType choseType = truckType;
        final int clickPosition = position;
        
        convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				UserInfoPersist.choseTruckTypeData = choseType;
				TruckTypeChosePosition = clickPosition;
				view.findViewById(R.id.iv_chosed).setVisibility(View.VISIBLE);
			}
		});*/
        
        holder.tv_brand.setText(truckType.LABEL);
                
        return convertView;
    }
    
    class Holder{
        TextView tv_brand;
        ImageView iv_chosed;
    }

}
