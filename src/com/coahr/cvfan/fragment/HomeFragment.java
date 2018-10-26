package com.coahr.cvfan.fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.zipper.framwork.core.ZLog;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.coahr.cvfan.R;
import com.coahr.cvfan.activity.CustomProgressDialog;
import com.coahr.cvfan.activity.SearchStationActivity;
import com.coahr.cvfan.activity.ServiceStationDetailActivity;
import com.coahr.cvfan.adapter.BrandListAdapter;
import com.coahr.cvfan.adapter.DistanceListAdapter;
import com.coahr.cvfan.adapter.ServiceStationAdapter;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.AutoListView;
import com.coahr.cvfan.view.AutoListView.OnLoadListener;
import com.coahr.cvfan.view.AutoListView.OnRefreshListener;
import com.google.gson.Gson;

@SuppressLint("ValidFragment")
public class HomeFragment extends BaseFragment implements OnRefreshListener,
		OnLoadListener {

	private AutoListView lv_service_station;
	private ServiceStationAdapter ssa;

	private ArrayList<GsonResponse.StationDetail> stationList;
	private ArrayList<GsonResponse.StationDetail> stationList_new;

	private Button btn_brand, btn_distance;

	private LinearLayout rl_brand_distance;
	private LinearLayout ll_brand;
	private LinearLayout ll_distance;

	private PopupWindow brandPop, distancePop;

	private RelativeLayout rl_location_detail;
	private TextView tv_location_detail;
	private ListView brandList, distanceList;
	private BrandListAdapter brandListAdapter;

	private Intent intent;
	private TextView tv_click_search_msg;
	private ImageView iv_no_data;
	private ImageView iv_click_search;

	private String brand;
	private String distance = "200";

	private Drawable nav_up_brand;

	private Drawable nav_up_distance;

	private final Timer timer = new Timer();
	private TimerTask task;
	public String location_detail;

	private int flag = 0;

	// 百度地图获取经纬度
	private LocationClient locationClient = null;
	private static final int UPDATE_TIME = 300000;
	private static int LOCATION_COUTNS = 0;
	// 分页请求参数
	private  int start_index = 0;
	private  int request_length = 10;

	private int msg_what_status = AutoListView.LOAD;
	private int location_state;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.GET_STATION_lIST_RESPONSE_TYPE:
				// WaittingDialog.cancelHintDialog(myProgressDialog);
				progressDialog.dismiss();
				if (msg != null) {
					GsonResponse.GetStationListResponse loginResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.GetStationListResponse.class);
					if (loginResponse.status_code.equals("0")) {
						ZLog.e("query station list succeed!");
						if (loginResponse.data != null) {
							stationList_new.clear();
							for (int i = 0; i < loginResponse.data.length; i++) {
								stationList_new.add(loginResponse.data[i]);
							}
							start_index = start_index + request_length;
							if (Integer.parseInt(loginResponse.iTotalDisplayRecords) <= 0) {
								iv_no_data.setVisibility(View.VISIBLE);
								lv_service_station.setVisibility(View.GONE);
								iv_click_search.setVisibility(View.GONE);
								tv_click_search_msg.setVisibility(View.VISIBLE);
								tv_click_search_msg.setText("暂无数据");
							} else {
								iv_no_data.setVisibility(View.GONE);
								iv_click_search.setVisibility(View.GONE);
								tv_click_search_msg.setVisibility(View.GONE);
								lv_service_station.setVisibility(View.VISIBLE);
							}

							// ssa.notifyDataSetChanged();
							Message msg_load = this
									.obtainMessage(msg_what_status);
							this.sendMessage(msg_load);
						}
					}
				}
				break;
			case Config.GET_TRUCK_BRAND_LIST_RESPONSE_TYPE:
				// WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.BrandListResponse brandListResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.BrandListResponse.class);
					if (brandListResponse.status_code.equals("0")) {
						if (brandListResponse.data != null) {
							UserInfoPersist.brands = new ArrayList<GsonResponse.BrandType>();
							GsonResponse.BrandType brandType = new GsonResponse.BrandType();
							brandType.label = getActivity().getResources()
									.getString(R.string.nolimit);
							UserInfoPersist.brands.add(brandType);
							for (int i = 0; i < brandListResponse.data.length; i++) {
								UserInfoPersist.brands
										.add(brandListResponse.data[i]);
							}
						}
						if (location_detail != null
								&& !"".equals(location_detail)) {
							tv_location_detail.setText(location_detail);
						} else {
							tv_location_detail.setText("获取失败");
						}
					}
				}
				break;
			case Config.RESPONSE_TYPE_ERROR:
				progressDialog.dismiss();
				if (msg.obj != null) {
					GsonResponse.HeadResponse headResponse = new Gson()
							.fromJson(msg.obj.toString().trim(),
									GsonResponse.HeadResponse.class);
					Toast.makeText(getActivity(), headResponse.status_text,
							Toast.LENGTH_LONG).show();
				}
			case Config.NET_CONNECT_EXCEPTION:
				progressDialog.dismiss();
				lv_service_station.setVisibility(View.GONE);
				iv_no_data.setVisibility(View.GONE);
				tv_click_search_msg.setVisibility(View.VISIBLE);
				tv_click_search_msg.setText("点击刷新");
				iv_click_search.setVisibility(View.VISIBLE);
				Toast.makeText(
						getActivity(),
						getActivity().getResources().getString(
								R.string.netconnect_exception),
						Toast.LENGTH_LONG).show();
				tv_location_detail.setText("获取失败");
				break;
			case Config.TIMER_COUNT_RESPONSE_TYP:
				if (location_detail != null && !"".equals(location_detail)) {
					tv_location_detail.setText(location_detail);
				}
				break;

			case AutoListView.REFRESH:
				lv_service_station.onRefreshComplete();
				stationList.addAll(stationList_new);
				lv_service_station.setResultSize(stationList_new.size());
				ssa.notifyDataSetChanged();
				break;
			case AutoListView.LOAD:
				lv_service_station.onLoadComplete();
				stationList.addAll(stationList_new);
				lv_service_station.setResultSize(stationList_new.size());
				ssa.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// SDKInitializer.initialize(getActivity().getApplicationContext());
		initlocation();
		initView();
		return baseView;
	}

	private void initlocation() {
		locationClient = new LocationClient(getActivity()
				.getApplicationContext());
		// 设置定位条件
		LocationClientOption option = new LocationClientOption();
		// option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型。国测局经纬度坐标系:gcj02
										// 百度墨卡托坐标系:bd09 百度经纬度坐标系:bd09ll
		option.setProdName("cvfan"); // 设置产品线名称。
		option.setScanSpan(UPDATE_TIME); // 设置定时定位的时间间隔。单位毫秒
		option.setAddrType("all");
		locationClient.setLocOption(option);
	}

	private void locationAction() {
		// 注册位置监听器
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null) {
					return;
				}
				StringBuffer sb = new StringBuffer(256);
				sb.append("Time : ");
				sb.append(location.getTime());
				sb.append("\nError code : ");
				sb.append(location.getLocType());
				sb.append("\nLatitude : ");
				sb.append(location.getLatitude());
				sb.append("\nLontitude : ");
				sb.append(location.getLongitude());
				sb.append("\nRadius : ");
				sb.append(location.getRadius());
				if (location.getLocType() == BDLocation.TypeGpsLocation) {
					sb.append("\nSpeed : ");
					sb.append(location.getSpeed());
					sb.append("\nSatellite : ");
					sb.append(location.getSatelliteNumber());
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
					sb.append("\nAddress : ");
					sb.append(location.getAddrStr());
				}
				LOCATION_COUTNS++;
				sb.append("\n检查位置更新次数：");
				sb.append(String.valueOf(LOCATION_COUTNS));

				Config.latitude = location.getLatitude() + "";
				Config.longitude = location.getLongitude() + "";
				if (location.getAddrStr() != null
						&& !"".equals(location.getAddrStr())) {
					String a[] = location.getAddrStr().split("-");
					location_detail = a[0];
					tv_location_detail.setText(location_detail);
				}
				if (flag == 0) {
					ClientRequest.getTruckBrandList(handler);
					stationList.clear();
					location_state = location.getLocType();
					if (location_state != 62) {
						ClientRequest.getStationList(handler, start_index + "",
								request_length + "", "", distance,
								Config.latitude + "", Config.longitude + "",
								"", "", "");
					} else {
						ClientRequest.getStationList(handler, start_index + "",
								request_length + "", "", distance, "", "", "",
								"", "");
					}
					flag = 1;
				}
				ZLog.e(sb.toString() + "");
			}
		});

		if (locationClient == null) {
			return;
		}
		if (locationClient.isStarted()) {
			locationClient.stop();
		} else {
			locationClient.start();
			/*
			 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation(
			 * )后，每隔设定的时间，定位SDK就会进行一次定位。如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
			 * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
			 * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
			 */
			locationClient.requestLocation();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		// 判断GPS是否正常启动
		// locationManager =
		// (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		// if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
		// //返回开启GPS导航设置界面
		// gpsDialog = new OpenGPSDialog(getActivity());
		// gpsDialog.show();
		// }
		locationAction();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = 0;
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}
		// ImageLoader.getInstance().clearMemoryCache();
		// ImageLoader.getInstance().clearDiscCache();
	}

	private void initView() {
		setBelowContentView(R.layout.service_station);
		setTitileName(R.string.app_name);
		setRightButtonText(R.string.search);

		iv_no_data = (ImageView) midView.findViewById(R.id.iv_no_data);
		iv_click_search = (ImageView) midView
				.findViewById(R.id.iv_click_search);
		iv_click_search.setOnClickListener(this);
		ll_brand = (LinearLayout) midView.findViewById(R.id.ll_brand);
		ll_brand.setOnClickListener(this);
		ll_distance = (LinearLayout) midView.findViewById(R.id.ll_distance);
		ll_distance.setOnClickListener(this);
		lv_service_station = (AutoListView) midView
				.findViewById(R.id.lv_service_station);
		rl_brand_distance = (LinearLayout) midView
				.findViewById(R.id.rl_brand_distance);
		tv_location_detail = (TextView) midView
				.findViewById(R.id.tv_location_detail);
		tv_click_search_msg = (TextView) midView
				.findViewById(R.id.tv_click_search_msg);
		tv_location_detail.setText("正在获取中..");
		task = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = Config.TIMER_COUNT_RESPONSE_TYP;
				handler.sendMessage(message);
			}
		};
		timer.schedule(task, 1000);

		btn_brand = (Button) midView.findViewById(R.id.btn_brand);
		btn_brand.setOnClickListener(this);
		rl_location_detail = (RelativeLayout) midView
				.findViewById(R.id.rl_location_detail);
		rl_location_detail.setOnClickListener(this);
		btn_distance = (Button) midView.findViewById(R.id.btn_distance);
		btn_distance.setOnClickListener(this);
		stationList = new ArrayList<GsonResponse.StationDetail>();
		stationList_new = new ArrayList<GsonResponse.StationDetail>();
		ssa = new ServiceStationAdapter(getActivity(), stationList);
		lv_service_station.setAdapter(ssa);
		lv_service_station.setOnRefreshListener(this);
		lv_service_station.setOnLoadListener(this);
		lv_service_station.onRefreshComplete();

		lv_service_station.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position > 0) {
					intent = new Intent();
					intent.setClass(getActivity(),
							ServiceStationDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(Config.STATION_DETAIL_INFO,
							stationList.get(position - 1));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
		/*
		 * lv_service_station.setOnScrollListener(new OnScrollListener() {
		 * 
		 * @Override public void onScrollStateChanged(AbsListView view, int
		 * scrollState) {
		 * 
		 * }
		 * 
		 * @Override public void onScroll(AbsListView view, int
		 * firstVisibleItem, int visibleItemCount, int totalItemCount) {
		 * 
		 * } });
		 */
		progressDialog = CustomProgressDialog.createDialog(getActivity());
		progressDialog.show();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		nav_up_brand = getActivity().getResources().getDrawable(
				R.drawable.service_station_up);
		nav_up_brand.setBounds(0, 0, nav_up_brand.getIntrinsicWidth(),
				nav_up_brand.getIntrinsicHeight());

		nav_up_distance = getActivity().getResources().getDrawable(
				R.drawable.service_station_up);
		nav_up_distance.setBounds(0, 0, nav_up_distance.getIntrinsicWidth(),
				nav_up_distance.getIntrinsicHeight());

		switch (v.getId()) {
		case R.id.ll_brand:
			btn_brand.setCompoundDrawables(null, null, nav_up_brand, null);
			nav_up_brand = getActivity().getResources().getDrawable(
					R.drawable.service_station_button6);
			nav_up_brand.setBounds(0, 0, nav_up_brand.getIntrinsicWidth(),
					nav_up_brand.getIntrinsicHeight());
			showBrandListPopupWindow();
			break;
		case R.id.ll_distance:
			// 判断GPS是否正常启动
			// locationManager = (LocationManager)
			// getActivity().getSystemService(
			// Context.LOCATION_SERVICE);
			// if (!locationManager
			// .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// // // 返回开启GPS导航设置界面
			// // gpsDialog = new OpenGPSDialog(getActivity());
			// // gpsDialog.show();
			// } else {
			btn_distance
					.setCompoundDrawables(null, null, nav_up_distance, null);
			nav_up_distance = getActivity().getResources().getDrawable(
					R.drawable.service_station_button6);
			nav_up_distance.setBounds(0, 0,
					nav_up_distance.getIntrinsicWidth(),
					nav_up_distance.getIntrinsicHeight());
			showDistanceAreaChoicePopupWindow();
			// }
			break;
		case R.id.btn_brand:
			btn_brand.setCompoundDrawables(null, null, nav_up_brand, null);
			nav_up_brand = getActivity().getResources().getDrawable(
					R.drawable.service_station_button6);
			nav_up_brand.setBounds(0, 0, nav_up_brand.getIntrinsicWidth(),
					nav_up_brand.getIntrinsicHeight());
			showBrandListPopupWindow();
			break;
		case R.id.btn_distance:
			btn_distance
					.setCompoundDrawables(null, null, nav_up_distance, null);
			nav_up_distance = getActivity().getResources().getDrawable(
					R.drawable.service_station_button6);
			nav_up_distance.setBounds(0, 0,
					nav_up_distance.getIntrinsicWidth(),
					nav_up_distance.getIntrinsicHeight());
			showDistanceAreaChoicePopupWindow();
			// }
			break;
		case R.id.btn_right:
			UserInfoPersist.choseBrandData = new GsonResponse.BrandType();
			UserInfoPersist.choseProvince = new GsonResponse.AreaData();
			UserInfoPersist.choseCity = new GsonResponse.AreaData();

			intent = new Intent();
			intent.setClass(getActivity(), SearchStationActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_location_detail:
			tv_location_detail.setText("正在获取中..");
			task = new TimerTask() {
				@Override
				public void run() {
					Message message = new Message();
					message.what = Config.TIMER_COUNT_RESPONSE_TYP;
					handler.sendMessage(message);
				}
			};
			timer.schedule(task, 1000);
			break;
		case R.id.iv_click_search:
			progressDialog.show();
			ClientRequest.getTruckBrandList(handler);
			if (location_state != 62) {
				ClientRequest.getStationList(handler, start_index + "",
						request_length + "", "", distance, Config.latitude + "",
						Config.longitude + "", "", "", "");
			} else {
				ClientRequest.getStationList(handler, start_index + "",
						request_length + "", "", distance, "", "", "",
						"", "");
			}			
			break;
		default:
			break;
		}
	}

	// show brand list popupwindow
	private void showBrandListPopupWindow() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View popupView = inflater.inflate(R.layout.pop_list_layout, null);
		brandPop = new PopupWindow(popupView, LayoutParams.FILL_PARENT, 540);
		brandPop.setFocusable(true);
		brandPop.setBackgroundDrawable(new BitmapDrawable());

		brandPop.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				btn_brand.setCompoundDrawables(null, null, nav_up_brand, null);
			}
		});

		brandList = (ListView) popupView.findViewById(R.id.lv_list);

		brandListAdapter = new BrandListAdapter(getActivity(),
				UserInfoPersist.brands);
		brandList.setAdapter(brandListAdapter);
		brandList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				TextView tv_brand = (TextView) ((RelativeLayout) view)
						.findViewById(R.id.tv_brand);
				brand = tv_brand.getText().toString();

				if (brand.equals(getActivity().getResources().getString(
						R.string.nolimit))) {
					brand = "";
					btn_brand.setText(getActivity().getResources().getString(
							R.string.nolimit));
				} else {
					btn_brand.setText(brand);
				}

				ZLog.e(brand);

				btn_brand.setCompoundDrawables(null, null, nav_up_brand, null);
				dismissPop();
				progressDialog.show();
				start_index = 0;
				stationList.clear();
				if (location_state != 62) {
					if (distance != null && distance != "") {
						ClientRequest.getStationList(handler, start_index + "",
								request_length + "", brand + "", distance + "",
								Config.latitude + "", Config.longitude + "",
								"", "", "");
					} else {
						ClientRequest.getStationList(handler, start_index + "",
								request_length + "", brand + "", "",
								Config.latitude + "", Config.longitude + "",
								"", "", "");
					}
				} else {
					if (brand != null && brand != "") {
						ClientRequest.getStationList(handler, start_index + "",
								request_length + "", brand + "", distance, "", "", "",
								"", "");
					} else {
						ClientRequest.getStationList(handler, start_index + "",
								request_length + "", "", distance, "", "", "", "", "");
					}
				}
				ZLog.e(Config.latitude + "");
				ZLog.e(Config.longitude + "");
			}
		});

		brandPop.showAsDropDown(rl_brand_distance, 0, 0);
	}

	// show distance area choices popupwindow
	private void showDistanceAreaChoicePopupWindow() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View popupView = inflater.inflate(R.layout.pop_list_layout, null);
		distancePop = new PopupWindow(popupView, LayoutParams.FILL_PARENT, 540);
		distancePop.setFocusable(true);
		distancePop.setBackgroundDrawable(new BitmapDrawable());

		distancePop.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				btn_distance.setCompoundDrawables(null, null, nav_up_distance,
						null);
			}
		});

		String[] distance_data = new String[] { "不限", "20km", "50km", "100km",
				"200km" };

		distanceList = (ListView) popupView.findViewById(R.id.lv_list);
		distanceList.setAdapter(new DistanceListAdapter(getActivity(),
				distance_data));

		distancePop.showAsDropDown(rl_brand_distance, 0, 0);

		distanceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				TextView tv_brand = (TextView) ((RelativeLayout) view)
						.findViewById(R.id.tv_brand);
				distance = tv_brand.getText().toString();

				btn_distance.setText(distance);
				if (distance.equals("20km")) {
					distance = "20";
				} else if (distance.equals("50km")) {
					distance = "50";
				} else if (distance.equals("100km")) {
					distance = "100";
				} else if (distance.equals("200km") || distance.equals("不限")) {
					distance = "200";
				} else {
					distance = "";
				}

				ZLog.e(distance);
				btn_brand.setCompoundDrawables(null, null, nav_up_distance,
						null);
				dismissPop();

				progressDialog.show();
				start_index = 0;
				stationList.clear();
				if (location_state != 62) {
					if (brand != null && brand != "") {
						ClientRequest.getStationList(handler, start_index + "",
								request_length + "", brand + "", distance + "",
								Config.latitude + "", Config.longitude + "",
								"", "", "");
					} else {
						ClientRequest.getStationList(handler, start_index + "",
								request_length + "", "", distance + "",
								Config.latitude + "", Config.longitude + "",
								"", "", "");
					}
				} else {
					ClientRequest.getStationList(handler, start_index + "",
							request_length + "", "", distance + "",
							Config.latitude + "", Config.longitude + "", "",
							"", "");
				}

				ZLog.e(Config.latitude + "");
				ZLog.e(Config.longitude + "");
			}
		});
	}

	private void dismissPop() {
		if (distancePop != null && distancePop.isShowing()) {
			distancePop.dismiss();
		}
		if (brandPop != null && brandPop.isShowing()) {
			brandPop.dismiss();
		}
	}

	@Override
	public void onLoad() {
		loadData(AutoListView.LOAD);
	}

	@Override
	public void onRefresh() {
		loadData(AutoListView.REFRESH);
	}

	private void loadData(final int what) {
		msg_what_status = what;
		if (location_state != 62) {
			if (brand != null && brand != "") {
				ClientRequest
						.getStationList(handler, start_index + "",
								request_length + "", brand + "", distance + "",
								Config.latitude + "", Config.longitude + "",
								"", "", "");
			} else {
				ClientRequest.getStationList(handler, start_index + "",
						request_length + "", "", distance + "", Config.latitude
								+ "", Config.longitude + "", "", "", "");
			}
		} else {
			if (brand != null && brand != "") {
				ClientRequest.getStationList(handler, start_index + "",
						request_length + "", brand + "", distance, "", "", "",
						"", "");
			} else {
				ClientRequest.getStationList(handler, start_index + "",
						request_length + "", "", distance, "", "", "", "", "");
			}
		}
	}
}
