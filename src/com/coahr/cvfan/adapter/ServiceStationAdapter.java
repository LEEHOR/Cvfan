package com.coahr.cvfan.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.coahr.cvfan.R;
import com.coahr.cvfan.listener.AnimateFirstDisplayListener;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UtilTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ServiceStationAdapter extends BaseAdapter{
    Context context;
    private ArrayList<GsonResponse.StationDetail> stationList;
    private int  selectItem=-1;    

	// 使用开源的webimageloader
	private DisplayImageOptions options;
	protected ImageLoader imageLoader;
	private ImageLoadingListener animateFirstListener;
	private LatLng p1LL;
	private LatLng p2LL;
	private double distance;
	private String sDistance;
    public ServiceStationAdapter(Context context, ArrayList<GsonResponse.StationDetail> stationList){
        this.context = context;
        this.stationList = stationList;
        
        animateFirstListener = new AnimateFirstDisplayListener();
		imageLoader = ImageLoader.getInstance();

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_station_logo)
				.showImageForEmptyUri(R.drawable.default_station_logo)
				.showImageOnFail(R.drawable.default_station_logo)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.NONE)
				// .displayer(new SimpleBitmapDisplayer())
				//.displayer(new CircularBitmapDisplayer()) // 圆形图片
				.displayer(new RoundedBitmapDisplayer(10)) //圆角图片
				.build();

    }
    
    @Override
    public int getCount() {
        return stationList.size();
    }

    @Override
    public Object getItem(int position) {
        return stationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        GsonResponse.StationDetail station = stationList.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.service_station_item, null);
            holder.headIcon = (ImageView) convertView.findViewById(R.id.iv_station_icon);
            holder.stationName = (TextView) convertView.findViewById(R.id.tv_station_name);
            holder.stationAddress = (TextView) convertView.findViewById(R.id.iv_address);
            holder.iv_check_flag=(ImageView)convertView.findViewById(R.id.iv_check_flag);
            holder.distance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.salesPromotion = (ImageView) convertView.findViewById(R.id.iv_sales_promotion);
            holder.discount = (ImageView) convertView.findViewById(R.id.iv_discount);
            holder.iv_maintenance_union = (ImageView) convertView.findViewById(R.id.iv_maintenance_union);
            holder.r_geller_point=(RatingBar)convertView.findViewById(R.id.r_geller_point);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        
        String imageUrl = Config.REQUEST_URL + UtilTools.rsfLogo(station.LOGO_FILE);
        imageLoader.displayImage(imageUrl, holder.headIcon, options, animateFirstListener);
        Log.e("ImageURL", imageUrl);
        //imageLoader.loadImage(Config.REQUEST_URL + station.LOGO_FILE, options, animateFirstListener);

        holder.stationName.setText(station.NAME);
        holder.stationAddress.setText(station.ADDRESS);
        
        p1LL=new LatLng(Float.parseFloat(Config.latitude), Float.parseFloat(Config.longitude));
        p2LL = new LatLng(Float.parseFloat(station.POS_LAT), Float.parseFloat(station.POS_LONG));
        distance =DistanceUtil.getDistance(p1LL,p2LL);
        
        sDistance=String.valueOf(distance/1000);
        holder.distance.setText(sDistance.substring(0, sDistance.indexOf(".")+2)  +"km");
        
        if (station.IS_MEMBER.equals("1")) {
            holder.iv_maintenance_union.setVisibility(View.VISIBLE);
        }else{
            holder.iv_maintenance_union.setVisibility(View.GONE);
        }
        
        if(station.DISCOUNT_FLAG.equals("1"))
        {
        	holder.discount.setVisibility(View.VISIBLE);
        }
        else
        {
        	holder.discount.setVisibility(View.GONE);
        }
        
        if(station.PROMOTION_FLAG.equals("1"))
        {
        	holder.salesPromotion.setVisibility(View.VISIBLE);
        }
        else
        {
        	holder.salesPromotion.setVisibility(View.GONE);
        }
        /*if (station.IS_MEMBER.equals("1")) {
            holder.iv_maintenance_union.setVisibility(View.VISIBLE);
        }else{
            holder.iv_maintenance_union.setVisibility(View.GONE);
        }*/
        if(station.GENERAL_SCORE.isEmpty())
        {
        	holder.r_geller_point.setRating(0.0f);
        }
        else
        {
        	holder.r_geller_point.setRating(new  Float(station.GENERAL_SCORE));
        }
        
        if(position==selectItem)
        {
        	holder.iv_check_flag.setVisibility(View.VISIBLE);
        }
        else
        {
        	holder.iv_check_flag.setVisibility(View.GONE);
        }
        
        return convertView;
    }        
    class Holder{
    	ImageView iv_check_flag;
        ImageView headIcon;
        TextView  stationName;
        TextView  stationAddress;
        TextView  distance;
        ImageView salesPromotion;
        ImageView discount;
        ImageView iv_maintenance_union;
        RatingBar r_geller_point;

    }
    
    public  void setSelectItem(int selectItem) {  
        this.selectItem = selectItem;  
    }  
}