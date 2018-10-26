package com.coahr.cvfan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.baidu.nplatform.comapi.basestruct.GeoPoint;
import com.coahr.cvfan.R;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.CircleBitmapDisplayer;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.util.UtilTools;
import com.coahr.cvfan.view.HintDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ServiceStationDetailActivity extends BaseActivity {

	/* private ImageView iv_address, iv_phone_number; */
	private TextView tv_address;
	private TextView tv_phone_number;
	private TextView tv_station_name;
	// private TextView tv_comment_count;
	private RatingBar r__quality_score;
	private RatingBar r__time_score;
	private RatingBar r__price_score;
	private HintDialog hintDialog;
	private ImageView iv_station_head_icon;
	private Intent intent;

	private TextView tv_quality_score_detail;
	private TextView tv_time_score_detail;
	private TextView tv__price_score_detail;
	private TextView tv_own_brand_detail;
	private TextView tv_service_memo_detail;
	private TextView tv_contacter_number;

	private RelativeLayout rl_appraise;
	private RelativeLayout rl_location;
	// private RelativeLayout rl_phone;
	private RelativeLayout r_promotion_list_port;

	// 使用开源的webimageloader
	private DisplayImageOptions options;
	protected ImageLoader imageLoader;
	private ImageLoadingListener animateFirstListener;
	private String imageUrl;

	private GsonResponse.StationDetail stationDetail;

	private GeoPoint startGPoint;
	private GeoPoint endGPoint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setBelowContentView(R.layout.station_info);

		intent = getIntent();
		stationDetail = (GsonResponse.StationDetail) intent
				.getSerializableExtra(Config.STATION_DETAIL_INFO);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_station_logo)
				.showImageForEmptyUri(R.drawable.default_station_logo)
				.showImageOnFail(R.drawable.default_station_logo)
				// .displayer(new CircleBitmapDisplayer()) // 圆形图片
				// .displayer(new SimpleBitmapDisplayer())
				.displayer(new RoundedBitmapDisplayer(10)) // 圆角图片
				.build();

		initUI();
	}

	private void initUI() {

		setBackButtonVisibility();
		setTitileName(R.string.station_detail);

		setRightButtonGone();

		tv_station_name = (TextView) midView.findViewById(R.id.tv_station_name);
		tv_station_name.setText(stationDetail.NAME);
		tv_own_brand_detail = (TextView) midView
				.findViewById(R.id.tv_own_brand_detail);
		tv_own_brand_detail.setText(stationDetail.BRAND);
		tv_service_memo_detail = (TextView) midView
				.findViewById(R.id.tv_service_memo_detail);
		tv_service_memo_detail.setText(stationDetail.SERVICE_MEMO);
		tv_contacter_number = (TextView) midView
				.findViewById(R.id.tv_contacter_number);

		if (!stationDetail.CONTACTER.isEmpty()) {
			tv_contacter_number.setText(stationDetail.CONTACTER);
		}
		iv_station_head_icon = (ImageView) midView
				.findViewById(R.id.iv_station_head_icon);
		imageUrl = Config.REQUEST_URL + UtilTools.rsfLogo(stationDetail.LOGO_FILE);
		imageLoader.displayImage(imageUrl, iv_station_head_icon, options,
				animateFirstListener);

		Log.e("ImageURL", imageUrl);

		/*
		 * iv_address = (ImageView) midView.findViewById(R.id.iv_address);
		 * iv_address.setOnClickListener(this);
		 * 
		 * iv_phone_number = (ImageView) midView
		 * .findViewById(R.id.iv_phone_number);
		 * iv_phone_number.setOnClickListener(this);
		 */
		// tv_comment_count = (TextView)
		// midView.findViewById(R.id.tv_comment_count);
		tv_address = (TextView) midView.findViewById(R.id.tv_address);
		tv_address.setText(stationDetail.ADDRESS);
		// tv_address.setOnClickListener(this);
		r__quality_score = (RatingBar) midView
				.findViewById(R.id.r__quality_score);
		r__time_score = (RatingBar) midView.findViewById(R.id.r__time_score);
		r__price_score = (RatingBar) midView.findViewById(R.id.r__price_score);
		tv_quality_score_detail = (TextView) midView
				.findViewById(R.id.tv_quality_score_detail);
		tv_time_score_detail = (TextView) midView
				.findViewById(R.id.tv_time_score_detail);
		tv__price_score_detail = (TextView) midView
				.findViewById(R.id.tv__price_score_detail);
		r_promotion_list_port = (RelativeLayout) midView
				.findViewById(R.id.r_promotion_list_port);

		if (stationDetail.PROMOTION_FLAG != "1") {
			r_promotion_list_port.setVisibility(View.GONE);
		}
		if (stationDetail.QUALITY_SCORE.isEmpty()) {
			r__quality_score.setRating(0.0f);
		} else {
			tv_quality_score_detail.setText(stationDetail.QUALITY_SCORE + "分");
			r__quality_score.setRating(Float
					.valueOf(stationDetail.QUALITY_SCORE));
		}

		if (stationDetail.TIME_SCORE.isEmpty()) {
			r__time_score.setRating(0.0f);
		} else {
			tv_time_score_detail.setText(stationDetail.TIME_SCORE + "分");
			r__time_score.setRating(Float.valueOf(stationDetail.TIME_SCORE));
		}

		if (stationDetail.PRICE_SCORE.isEmpty()) {
			r__price_score.setRating(0.0f);
		} else {
			tv__price_score_detail.setText(stationDetail.PRICE_SCORE + "分");
			r__price_score.setRating(Float.valueOf(stationDetail.PRICE_SCORE));
		}

		// if (stationDetail.COMMENT_COUNT.isEmpty()) {
		// String count = getResources().getString(R.string.comment_count);
		// count = String.format(count, 0);
		// tv_comment_count.setText(count);
		// } else {
		// String count = getResources().getString(R.string.comment_count);
		// count = String.format(count,
		// Integer.parseInt(stationDetail.COMMENT_COUNT));
		// tv_comment_count.setText(count);
		// }

		tv_phone_number = (TextView) midView.findViewById(R.id.tv_phone_number);
		tv_phone_number.setText(stationDetail.CONTACT_TEL);
		tv_phone_number.setOnClickListener(this);

		// iv_appraise = (ImageView) midView.findViewById(R.id.iv_appraise);
		// iv_appraise.setOnClickListener(this);

		// rl_appraise = (RelativeLayout)
		// midView.findViewById(R.id.rl_appraise);
		// rl_appraise.setOnClickListener(this);

		rl_location = (RelativeLayout) midView.findViewById(R.id.rl_location);
		rl_location.setOnClickListener(this);
		// rl_phone = (RelativeLayout) midView.findViewById(R.id.rl_phone);
		// rl_phone.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		/*
		 * case R.id.iv_address:
		 * 
		 * break;
		 */
		/*
		 * case R.id.iv_phone_number: hintDialog = new
		 * HintDialog(ServiceStationDetailActivity.this,
		 * tv_phone_number.getText().toString().trim());
		 * 
		 * hintDialog.show(); break; case R.id.tv_address:
		 * 
		 * break; case R.id.tv_phone_number: hintDialog = new
		 * HintDialog(ServiceStationDetailActivity.this,
		 * tv_phone_number.getText().toString().trim());
		 * 
		 * hintDialog.show(); break;
		 */
		// case R.id.rl_phone:
		// hintDialog = new HintDialog(ServiceStationDetailActivity.this,
		// tv_phone_number.getText().toString().trim());
		//
		// hintDialog.show();
		// break;
		// case R.id.iv_appraise:
		// intent = new Intent(ServiceStationDetailActivity.this,
		// AppraiseStationActivity.class);
		// intent.putExtra("station_id", stationDetail.STATION_ID);
		// startActivity(intent);
		// break;

		// case R.id.rl_appraise:
		// intent = new Intent(ServiceStationDetailActivity.this,
		// CommentActivity.class);
		// intent.putExtra("station_id", stationDetail.STATION_ID);
		// startActivity(intent);
		// break;

		case R.id.rl_location:
			 intent = new Intent(ServiceStationDetailActivity.this,
			 StationMapLocationActivity.class); Bundle bundle = new Bundle();
			 bundle.putSerializable(Config.STATION_DETAIL_INFO,
			 stationDetail); intent.putExtras(bundle); startActivity(intent);
			 

//			startGPoint = new GeoPoint(
//					(int) (Double.parseDouble(Config.latitude) * 1e6),
//					(int) (Double.parseDouble(Config.longitude) * 1e6));
//			endGPoint = new GeoPoint(
//					(int) (Double.parseDouble(stationDetail.POS_LAT) * 1e6),
//					(int) (Double.parseDouble(stationDetail.POS_LONG) * 1e6));
//
//			Log.e("starPoint latitude", Config.latitude);
//			Log.e("starPoint longitude", Config.longitude);
//			Log.e("endPoint latitude", stationDetail.POS_LAT);
//			Log.e("endPoint longitude", stationDetail.POS_LONG);
//
//			baiDuNavigation(startGPoint, endGPoint);
			break;

		default:
			break;
		}
	}

	private void baiDuNavigation(GeoPoint startPos, GeoPoint endPos) {

		BaiduNaviManager.getInstance().launchNavigator(this,
				Double.parseDouble(Config.latitude),
				Double.parseDouble(Config.longitude), "start",
				Double.parseDouble(stationDetail.POS_LAT),
				Double.parseDouble(stationDetail.POS_LONG), "end",
				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, true,
				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY,
				new OnStartNavigationListener() {

					@Override
					public void onJumpToDownloader() {

						Toast.makeText(ServiceStationDetailActivity.this,
								"请安装最新版本的百度地图", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onJumpToNavigator(Bundle configParams) {
						Intent intent = new Intent(
								ServiceStationDetailActivity.this,
								BNavigatorActivity.class);
						intent.putExtras(configParams);
						startActivity(intent);
						// StationMapLocationActivity.this.finish();
					}

				}); // 跳转监听
	}
}
