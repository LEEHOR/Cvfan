package com.coahr.cvfan.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.listener.AnimateFirstDisplayListener;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.net.GsonResponse.BrandType;
import com.coahr.cvfan.net.GsonResponse.TruckType;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.util.UtilTools;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class TruckInfoActivity extends BaseActivity {

	private Button btn_no_maintain_station;
	private ProgressDialog myProgressDialog;

	private TextView tv_license_plate_number_detail;
	private TextView tv_owner_detail;
	private TextView tv_address_detail;
	private TextView tv_truck_brand_detail;
	private TextView tv_truck_model_detail;
	private TextView tv_engine_number_detail;
	private TextView tv_carframe_number_detail;
//	private TextView tv_truck_grade;
	private ImageView iv_driving_license_pic_more;
	private Intent intent;
	private String autoId;
	private GsonResponse.TruckInfoData truckInfo;

	private DisplayImageOptions options;
 	private ImageLoadingListener animateFirstListener;
 	private String plateNo;
 	private String brand;
 	private String model;
 	private String engineNo;
 	private String carframeNo;
 	private String brandId;
 	private String licenseimageUrl;
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.GET_TRUCK_INFO_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.GetTruckInfoResponse getTruckInfoResponse = new Gson().fromJson(msg.obj.toString().trim(),GsonResponse.GetTruckInfoResponse.class);
					truckInfo = getTruckInfoResponse.data;
					plateNo=getTruckInfoResponse.data.PLATE_NO;
					brand=getTruckInfoResponse.data.BRAND_NAME;
					model=getTruckInfoResponse.data.MODEL_NAME;
					engineNo=getTruckInfoResponse.data.ENGINE_NO;
					carframeNo=getTruckInfoResponse.data.FRAME_NO;
					brandId=getTruckInfoResponse.data.BRAND;
					licenseimageUrl = Config.REQUEST_URL+UtilTools.returnImageurlSmall(getTruckInfoResponse.data.LICENSE_FILE);
					tv_license_plate_number_detail.setText(getTruckInfoResponse.data.PLATE_NO);
					tv_owner_detail.setText(getTruckInfoResponse.data.NAME);
					tv_address_detail.setText(getTruckInfoResponse.data.ADDRESS);
					tv_truck_brand_detail.setText(getTruckInfoResponse.data.BRAND_NAME);
					tv_truck_model_detail.setText(getTruckInfoResponse.data.MODEL_NAME);
					tv_engine_number_detail.setText(getTruckInfoResponse.data.ENGINE_NO);
					tv_carframe_number_detail.setText(getTruckInfoResponse.data.FRAME_NO);
//					tv_truck_grade.setText(getTruckInfoResponse.data.AUTO_LEVEL);
				    imageLoader.displayImage(licenseimageUrl, iv_driving_license_pic_more, options, animateFirstListener);
					
					if(!getTruckInfoResponse.data.GUARANTEE.isEmpty()&&Integer.parseInt(getTruckInfoResponse.data.GUARANTEE)==2)
					{
						btn_no_maintain_station.setVisibility(View.GONE);
					}
					else if(!getTruckInfoResponse.data.GUARANTEE.isEmpty()&&Integer.parseInt(getTruckInfoResponse.data.GUARANTEE)==1)
					{
						btn_no_maintain_station.setVisibility(View.GONE);
					}
					// tv_driving_license_number_detail =
					// (TextView)midView.findViewById(R.id.tv_driving_license_number_detail);
				}
				break;
				
			case Config.RESPONSE_TYPE_ERROR:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(TruckInfoActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;
				
			case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(TruckInfoActivity.this, getResources().getString(R.string.netconnect_exception),
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

		setBelowContentView(R.layout.activity_truck_info_layout);

		setBackButtonVisibility();
		setTitileName(R.string.vehicle_manage);
		setRightButtonText(R.string.edit);
		 animateFirstListener = new AnimateFirstDisplayListener();
		 options = new DisplayImageOptions.Builder()
					.showStubImage(0)
					.showImageForEmptyUri(0)
					.showImageOnFail(0)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new SimpleBitmapDisplayer())
					.build();
		initUI();
	}

	private void initUI() {
		autoId = getIntent().getStringExtra("AUTO_ID");
		
		tv_license_plate_number_detail = (TextView) midView
				.findViewById(R.id.tv_license_plate_number_detail);
		tv_owner_detail = (TextView) midView.findViewById(R.id.tv_owner_detail);
		tv_address_detail = (TextView) midView
				.findViewById(R.id.tv_address_detail);
		tv_truck_brand_detail = (TextView) midView
				.findViewById(R.id.tv_truck_brand_detail);
		tv_truck_model_detail = (TextView) midView
				.findViewById(R.id.tv_truck_model_detail);
		tv_engine_number_detail = (TextView) midView
				.findViewById(R.id.tv_engine_number_detail);
		tv_carframe_number_detail = (TextView) midView
				.findViewById(R.id.tv_carframe_number_detail);
		iv_driving_license_pic_more=(ImageView)midView.findViewById(R.id.iv_driving_license_pic_more);
//		tv_truck_grade = (TextView) midView.findViewById(R.id.tv_truck_grade);
		btn_no_maintain_station = (Button) midView.findViewById(R.id.btn_no_maintain_station);
		btn_no_maintain_station.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		myProgressDialog = WaittingDialog.showHintDialog(
				TruckInfoActivity.this, R.string.query);
		myProgressDialog.show();
		ClientRequest.getTruckInfo(handler, autoId, UserInfoPersist.ownerRole);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_no_maintain_station:
			intent = new Intent();
			intent.setClass(TruckInfoActivity.this,
					TruckGuaranteActivity.class);
			intent.putExtra("AUTO_ID", autoId);
			intent.putExtra("PLATE_NO", truckInfo.PLATE_NO);
			intent.putExtra("FRAME_NO", truckInfo.FRAME_NO);
			startActivity(intent);
			break;
		case R.id.btn_right:
			UserInfoPersist.choseBrandData = new BrandType();
			UserInfoPersist.choseTruckTypeData = new TruckType();
			intent = new Intent();
			intent.setClass(TruckInfoActivity.this,ModifyTruckInfoActivity.class);
			intent.putExtra("AUTO_ID", autoId);
			intent.putExtra("PLATE_NO", plateNo);
			intent.putExtra("BRAND", brand);
			intent.putExtra("MODEL", model);
			intent.putExtra("ENGINE_NO", engineNo);
			intent.putExtra("BRAND_ID", brandId);
			intent.putExtra("LICENSE_URL", licenseimageUrl);
			startActivity(intent);
			break;
			
		default:
			break;
		}
	}
	
	@Override
    public void onPause() {
    	imageLoader.cancelDisplayTask(iv_driving_license_pic_more);
    	imageLoader.stop();
    	super.onPause();
    }
}
