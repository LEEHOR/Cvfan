package com.coahr.cvfan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class AppraiseStationActivity extends BaseActivity {

    private RatingBar rb_service_quality, rb_service_speed, rb_service_price,
            rb_final;
    private EditText et_suggest;

    private float quality_rating, price_rating, speed_rating, general_rating;

    private String station_id;

    
    private TextView tv_quality_hint;
    private TextView tv_speed_hint;
    private TextView tv_price_hint;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case Config.APPRAISE_STATION_RESPONSE_TYPE:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                
                if (msg != null) {
                	GsonResponse.AppraiseStationResponse appraiseStationResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.AppraiseStationResponse.class);
					if (appraiseStationResponse.status_code.equals("0")) {
						Toast.makeText(AppraiseStationActivity.this,
								"点评成功", Toast.LENGTH_SHORT).show();
		                AppraiseStationActivity.this.finish();
					}
				}
                break;
            case Config.RESPONSE_TYPE_ERROR:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(AppraiseStationActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;
            case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(AppraiseStationActivity.this, getResources().getString(R.string.netconnect_exception),
	                     Toast.LENGTH_LONG).show();
				break;
            default:
                break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBelowContentView(R.layout.activity_appraise_station_layout);
        initUI();
    }

    private void initUI() {
        station_id = getIntent().getStringExtra("station_id");

        setBackButtonVisibility();
        setTitileName(R.string.add_appraise);
        setRightButtonText(R.string.publish);

        btn_right.setOnClickListener(this);

        rb_final = (RatingBar) midView.findViewById(R.id.rb_final);
        rb_service_quality = (RatingBar) midView
                .findViewById(R.id.rb_service_quality);
        rb_service_speed = (RatingBar) midView
                .findViewById(R.id.rb_service_speed);
        rb_service_price = (RatingBar) midView
                .findViewById(R.id.rb_service_price);
        //初始化这个值
        general_rating=rb_final.getRating();
        quality_rating=rb_service_quality.getRating();
        speed_rating=rb_service_speed.getRating();
        price_rating=rb_service_price.getRating();
        
        et_suggest = (EditText) midView.findViewById(R.id.et_suggest);
        
        tv_quality_hint = (TextView) midView.findViewById(R.id.tv_quality_hint);
        tv_speed_hint = (TextView) midView.findViewById(R.id.tv_speed_hint);
        tv_price_hint = (TextView) midView.findViewById(R.id.tv_price_hint);
        
        rb_service_quality
                .setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                            float rating, boolean fromUser) {
                        quality_rating = rating;
                        rb_service_quality.setRating(rating);
                        general_rating = (rb_service_quality.getRating()
                                + rb_service_speed.getRating() + rb_service_price
                                .getRating()) / 3;
                        rb_final.setRating(general_rating);
                        
                        tv_quality_hint.setText(getQuality(rating));
                    }
                });

        rb_service_price
                .setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                            float rating, boolean fromUser) {
                        price_rating = rating;
                        rb_service_price.setRating(rating);
                        general_rating = (rb_service_quality.getRating()
                                + rb_service_speed.getRating() + rb_service_price
                                .getRating()) / 3;
                        rb_final.setRating(general_rating);
                        tv_price_hint.setText(getQuality(rating));
                    }
                });

        rb_service_speed
                .setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                            float rating, boolean fromUser) {
                        speed_rating = rating;
                        rb_service_speed.setRating(rating);
                        general_rating = (rb_service_quality.getRating()
                                + rb_service_speed.getRating() + rb_service_price
                                .getRating()) / 3;
                        rb_final.setRating(general_rating);
                        tv_speed_hint.setText(getQuality(rating));
                    }
                });
    }
    
    private String getQuality(float rating){
    	if(rating <= 1){
    		return "很差";
    	}else if(rating > 1 && rating <= 2){
    		return "差";
    	}else if(rating > 2 && rating <= 3){
    		return "一般";
    	}else if(rating > 3 && rating <= 4){
    		return "好";
    	}else{
    		return "非常好";
    	}
    	
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.btn_right:
        	if("".equals(et_suggest.getText().toString().trim()))
        	{
        		Toast.makeText(
						AppraiseStationActivity.this,"评价内容不能为空",
						Toast.LENGTH_SHORT).show();
        		break;
        	}
        		
    		myProgressDialog = WaittingDialog.showHintDialog(
    				 AppraiseStationActivity.this, R.string.comitting);
    		myProgressDialog.show();
    		ClientRequest.appraiseStation(handler, quality_rating,
    				 price_rating, speed_rating, general_rating, et_suggest.getText().toString().trim(), station_id);
            break;

        default:
            break;
        }
    }
}
