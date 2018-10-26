package com.coahr.cvfan.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.mapcontrol.BNMapController;
import com.baidu.navisdk.comapi.routeplan.BNRoutePlaner;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.baidu.navisdk.ui.routeguide.BNavigator;
import com.baidu.nplatform.comapi.basestruct.GeoPoint;
import com.coahr.cvfan.MainApplication;
import com.coahr.cvfan.R;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;

public class StationMapLocationActivity extends BaseActivity {

	private MapView mapview_station;
	private BaiduMap mBaiduMap;
	private BitmapDescriptor mCurrentMarker;
	private GsonResponse.StationDetail stationDetail;

	private BNaviPoint startPoint;
	private BNaviPoint endPoint;
	
	private GeoPoint startGPoint;
	private GeoPoint endGPoint;

	private BMapManager mBMapMan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setBelowContentView(R.layout.activity_station_map_location_layout);

		setBackButtonVisibility();
		setTitileName(R.string.line_gps);
		setRightButtonGone();
		initUI();

		stationDetail = (GsonResponse.StationDetail) getIntent()
				.getSerializableExtra(Config.STATION_DETAIL_INFO);

		// 开启定位图层
		mBaiduMap = mapview_station.getMap();
		mBaiduMap.setMyLocationEnabled(true);

		// 构造定位数据
		MyLocationData locData = new MyLocationData.Builder().accuracy(20)
				// 定位精度20米
				.direction(100)
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.latitude(Double.parseDouble(Config.latitude))
				.longitude(Double.parseDouble(Config.longitude)).build();

		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding); // 设置定位数据
		mBaiduMap.setMyLocationData(locData); // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
		MyLocationConfiguration config = new MyLocationConfiguration(
				MyLocationConfiguration.LocationMode.COMPASS, true, null);
		mBaiduMap.setMyLocationConfigeration(config);

		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		// 构建MarkerOption，用于在地图上添加Marker

		OverlayOptions option = new MarkerOptions().position(
				new LatLng(Double.parseDouble(stationDetail.POS_LAT), Double
						.parseDouble(stationDetail.POS_LONG))).icon(bitmap);

		// 在地图上添加Marker，并显示 //
		mBaiduMap.addOverlay(option);
		// 当不需要定位图层时关闭定位图层 //
//		mBaiduMap.setMyLocationEnabled(false);
//
//		// 起始点
//		startPoint = new BNaviPoint(Double.parseDouble(Config.latitude),
//				Double.parseDouble(Config.longitude), "");
//
//		// 目标点
//		endPoint = new BNaviPoint(Double.parseDouble(stationDetail.POS_LAT),
//				Double.parseDouble(stationDetail.POS_LONG), "");
//		
//		startGPoint = new GeoPoint((int)(Double.parseDouble(Config.latitude) * 1e6), (int)(Double.parseDouble(Config.longitude) * 1e6));
//		endGPoint = new GeoPoint((int)(Double.parseDouble(stationDetail.POS_LAT) * 1e6), (int)(Double.parseDouble(stationDetail.POS_LONG) * 1e6));
//		
//		Log.e("starPoint latitude", Config.latitude);
//		Log.e("starPoint longitude", Config.longitude);
//		Log.e("endPoint latitude", stationDetail.POS_LAT);
//		Log.e("endPoint longitude", stationDetail.POS_LONG);
//
//		baiDuNavigation(startGPoint, endGPoint);
	}

	private void baiDuNavigation(GeoPoint startPos, GeoPoint endPos) {

		BaiduNaviManager.getInstance().launchNavigator
		(this, Double.parseDouble(Config.latitude), Double.parseDouble(Config.longitude), "start", 
				Double.parseDouble(stationDetail.POS_LAT), Double.parseDouble(stationDetail.POS_LONG), "end",
				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, true, BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, 
				new OnStartNavigationListener() {

			@Override
			public void onJumpToDownloader() {
				 
				Toast.makeText(StationMapLocationActivity.this,
						"请安装最新版本的百度地图", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onJumpToNavigator(Bundle configParams) {
				Intent intent = new Intent(
						StationMapLocationActivity.this,
						BNavigatorActivity.class);
				intent.putExtras(configParams);
				startActivity(intent);
				//StationMapLocationActivity.this.finish();
			}
			
		}); // 跳转监听
	}

	private void initUI() {
		mapview_station = (MapView) midView.findViewById(R.id.mapview_station);
	}

	@Override
	public void onResume() {
		BNavigator.getInstance().resume();
		super.onResume();
		BNMapController.getInstance().onResume();
	};

	@Override
	public void onPause() {
		BNavigator.getInstance().pause();
		super.onPause();
		BNMapController.getInstance().onPause();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		BNavigator.getInstance().onConfigurationChanged(newConfig);
		super.onConfigurationChanged(newConfig);
	}

	public void onBackPressed() {
		BNavigator.getInstance().onBackPressed();
	}

	@Override
	public void onDestroy() {
		BNavigator.destory();
		BNRoutePlaner.getInstance().setObserver(null);
		super.onDestroy();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
    		finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
