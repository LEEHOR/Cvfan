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

public class BrandChoseAdapter extends BaseAdapter {
	//Activity activity;
    Context context;
    ArrayList<GsonResponse.BrandType> brands;
    boolean chosed = false;
    private int  selectItem=-1;

    public BrandChoseAdapter(Context context, ArrayList<GsonResponse.BrandType> brands){
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
                    .inflate(R.layout.brand_item, null);

            holder.tv_brand = (TextView)convertView.findViewById(R.id.tv_brand);
            holder.iv_chosed = (ImageView)convertView.findViewById(R.id.iv_chosed);
            holder.iv_chosed.setVisibility(View.INVISIBLE);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        
        /*final GsonResponse.BrandType choseBrand = brand;
        
        convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				UserInfoPersist.choseBrandData = choseBrand;
				chosed = true;
				view.findViewById(R.id.iv_chosed).setVisibility(View.VISIBLE);
				//activity.finish();
			}
		});
        if(chosed){
        	holder.iv_chosed.setVisibility(View.VISIBLE);
        	chosed = false;
        }*/
        
        holder.tv_brand.setText(brand.label);
        
        if(position==selectItem)
        {
        	holder.iv_chosed.setVisibility(View.VISIBLE);
        }
        else
        {
        	holder.iv_chosed.setVisibility(View.INVISIBLE);
        }
        
        return convertView;
    }
    
    class Holder{
        TextView tv_brand;
        ImageView iv_chosed;
    }
    
    public  void setSelectItem(int selectItem) {  
        this.selectItem = selectItem;  
    }  
}
