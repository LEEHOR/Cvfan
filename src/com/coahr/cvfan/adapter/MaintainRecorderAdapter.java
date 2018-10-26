package com.coahr.cvfan.adapter;

import java.util.ArrayList;

import com.coahr.cvfan.R;
import com.coahr.cvfan.activity.MaintainPaperActivity;
import com.coahr.cvfan.net.GsonResponse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MaintainRecorderAdapter extends BaseAdapter {
    Context context;
    ArrayList<GsonResponse.MaintainPaperItem> maintainPapers;

    public MaintainRecorderAdapter(Context context,
            ArrayList<GsonResponse.MaintainPaperItem> maintainPapers) {
        this.context = context;
        this.maintainPapers = maintainPapers;
    }

    @Override
    public int getCount() {
        return maintainPapers.size();
    }

    @Override
    public Object getItem(int position) {
        return maintainPapers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        GsonResponse.MaintainPaperItem maintainPaperItem = maintainPapers
                .get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.maintain_recorder_item, null);

            holder.tv_maintain_date = (TextView) convertView
                    .findViewById(R.id.tv_maintain_date);
            holder.tv_maintain_station = (TextView) convertView
                    .findViewById(R.id.tv_maintain_station);
            holder.tv_pay_state = (TextView) convertView
                    .findViewById(R.id.tv_pay_state);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        
        if(maintainPaperItem.ENTER_DATE!=null&&!"".endsWith(maintainPaperItem.ENTER_DATE))
        {
        	holder.tv_maintain_date.setText("维修日期:"
        			+ maintainPaperItem.ENTER_DATE);
        }
        else
        {
        	holder.tv_maintain_date.setText("维修日期:");
        }
        
        if(maintainPaperItem.NAME!=null&&!"".equals(maintainPaperItem.NAME))
        {
        	holder.tv_maintain_station.setText("服务站:" + maintainPaperItem.NAME);
        }
        else
        {
        	holder.tv_maintain_station.setText("服务站:");
        }
        
        holder.tv_pay_state.setText(maintainPaperItem.PAYMENT_STATUS);
        
        
        final String serviceID = maintainPaperItem.SERVICE_ID;
        convertView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(context, MaintainPaperActivity.class);
                intent.putExtra("serviceid", serviceID);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class Holder {
        TextView tv_maintain_date, tv_maintain_station, tv_pay_state;
    }
}
