package com.coahr.cvfan.adapter;

import com.coahr.cvfan.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GalleryAdapter extends BaseAdapter {
    Context context;

    public GalleryAdapter(Context context){
        this.context = context;
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 9;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        if(convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.gallery_item, null);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        return convertView;
    }
    
    class Holder{
        
    }

}
