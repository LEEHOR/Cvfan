package com.coahr.cvfan.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.zipper.framwork.core.ZLog;

import com.coahr.cvfan.R;
import com.coahr.cvfan.adapter.CityListAdapter;
import com.coahr.cvfan.adapter.ProvinceListAdapter;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.net.GsonResponse.AreaData;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class AreaChoseActivity extends BaseActivity {

	private ListView lv_province_list;
	private ListView lv_city_list;

	private CityListAdapter cityListAdapter;
	private ProvinceListAdapter provinceListAdapter;
	private ArrayList<GsonResponse.AreaData> provinceList;

	private ArrayList<GsonResponse.AreaData> cityList;

//	private int lastChoseCityPosition = -1;
//	private int lastChoseProvincePosition = -1;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.GET_PROVINCE_LIST_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg != null) {
					GsonResponse.AreaListResponse areaListResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.AreaListResponse.class);
					if (areaListResponse.status_code.equals("0")) {
						ZLog.e("query Province list succeed!");
						if (areaListResponse.data != null) {
							provinceList.clear();
							for (int i = 0; i < areaListResponse.data.length; i++) {
								provinceList.add(areaListResponse.data[i]);
							}
							provinceListAdapter.notifyDataSetChanged();
						}
					}
				}
				break;
			case Config.GET_CITY_LIST_RESPONSE_TYPE:
				if (msg != null) {
					GsonResponse.AreaListResponse areaListResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.AreaListResponse.class);
					if (areaListResponse.status_code.equals("0")) {
						ZLog.e("query City list succeed!");
						if (areaListResponse.data != null) {
							cityList.clear();
							// lastChoseCityPosition = -1;
							for (int i = 0; i < areaListResponse.data.length; i++) {
								cityList.add(areaListResponse.data[i]);
							}
							cityListAdapter.notifyDataSetChanged();
//
//							if ((lastChoseCityPosition != -1)
//									&& (cityList.size() > lastChoseCityPosition)) {
//								lv_city_list.getChildAt(lastChoseCityPosition)
//										.findViewById(R.id.iv_chosed)
//										.setVisibility(View.INVISIBLE);
//								lastChoseCityPosition = -1;
//							}
						}
					}
				}
				break;

			case Config.RESPONSE_TYPE_ERROR:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(AreaChoseActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;
				
			 case Config.NET_CONNECT_EXCEPTION:
					WaittingDialog.cancelHintDialog(myProgressDialog);
					Toast.makeText(AreaChoseActivity.this, getResources().getString(R.string.netconnect_exception),
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

		setBelowContentView(R.layout.activity_area_chose_layout);

		setBackButtonVisibility();
		setTitileName(R.string.chose_area);
		setRightButtonText(R.string.ok);

		initUI();
	}

	private void initUI() {		

		UserInfoPersist.choseProvince = new AreaData();
		UserInfoPersist.choseCity = new AreaData();

		btn_right.setOnClickListener(this);
		provinceList = new ArrayList<GsonResponse.AreaData>();
		provinceListAdapter = new ProvinceListAdapter(AreaChoseActivity.this,
				provinceList);
		lv_province_list = (ListView) midView
				.findViewById(R.id.lv_province_list);
		lv_province_list.setAdapter(provinceListAdapter);

		lv_province_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				UserInfoPersist.choseProvince = provinceList.get(position);
				ClientRequest.getCityList(handler,
							UserInfoPersist.choseProvince.ID);
				provinceListAdapter.setSelectItem(position);
				provinceListAdapter.notifyDataSetInvalidated();
				cityListAdapter.setSelectItem(-1);
				UserInfoPersist.choseCity=new AreaData();
//				if (lastChoseProvincePosition != -1) {
//					parent.getChildAt(lastChoseProvincePosition)
//							.setBackgroundColor(
//									getResources().getColor(R.color.normal_bg));
//				}
//				view.setBackgroundColor(getResources().getColor(R.color.chosed));
//				lastChoseProvincePosition = position;
			}
		});

		cityList = new ArrayList<GsonResponse.AreaData>();
		cityListAdapter = new CityListAdapter(AreaChoseActivity.this, cityList);
		lv_city_list = (ListView) midView.findViewById(R.id.lv_city_list);
		lv_city_list.setAdapter(cityListAdapter);
		lv_city_list.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				if ((lastChoseCityPosition != -1)
//						&& (cityList.size() > lastChoseCityPosition)) {
//					parent.getChildAt(lastChoseCityPosition)
//							.findViewById(R.id.iv_chosed)
//							.setVisibility(View.INVISIBLE);
//				}
//				view.findViewById(R.id.iv_chosed).setVisibility(View.VISIBLE);
//				lastChoseCityPosition = position;
				UserInfoPersist.choseCity = cityList.get(position);
				cityListAdapter.setSelectItem(position);
				cityListAdapter.notifyDataSetInvalidated();
			}
		});
	
    	myProgressDialog = WaittingDialog.showHintDialog(
    			AreaChoseActivity.this, R.string.query);
    	myProgressDialog.show();
    	ClientRequest.getProvinceList(handler);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_right:
			AreaChoseActivity.this.finish();
			break;
		case R.id.btn_back:// 返回按钮
			UserInfoPersist.choseProvince = new AreaData();
			UserInfoPersist.choseCity = new AreaData();
			this.finish();
			break;

		default:
			break;
		}
	}
}
