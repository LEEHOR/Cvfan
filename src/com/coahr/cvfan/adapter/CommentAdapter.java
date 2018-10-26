package com.coahr.cvfan.adapter;

import java.util.ArrayList;

import com.coahr.cvfan.R;
import com.coahr.cvfan.net.GsonResponse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
    Context context;
    ArrayList<GsonResponse.StationCommentItem> commentList;

    public CommentAdapter(Context context, ArrayList<GsonResponse.StationCommentItem> commentList){
        this.context = context;
        this.commentList = commentList;
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        
        GsonResponse.StationCommentItem commentItem = commentList.get(position);
        
        if(convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.exp_exchange_title, null);

            holder.tv_nickname = (TextView)convertView.findViewById(R.id.tv_nickname);
            holder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
            holder.tv_comment = (TextView)convertView.findViewById(R.id.tv_comment);
            holder.iv_head_icon = (ImageView)convertView.findViewById(R.id.iv_head_icon);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        
        holder.tv_nickname.setText(commentItem.DRIVER_NAME);
        holder.tv_date.setText(commentItem.UPDATED_DATE);
        holder.tv_comment.setText(commentItem.COMMENT);
                
        return convertView;
    }
    
    class Holder{
        TextView tv_nickname, tv_date, tv_comment;
        ImageView iv_head_icon;
    }

}
