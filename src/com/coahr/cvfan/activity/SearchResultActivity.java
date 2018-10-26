package com.coahr.cvfan.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.zipper.framwork.core.ZLog;

import com.coahr.cvfan.R;
import com.coahr.cvfan.adapter.ServiceStationAdapter;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.google.gson.Gson;

public class SearchResultActivity extends BaseActivity {

	private ListView lv_search_list;

	private ArrayList<GsonResponse.StationDetail> stationList;
	private ServiceStationAdapter ssa;
	private String stationName;
	private TextView tv_click_search_msg;
	private ImageView iv_no_data;
	private ImageView iv_click_search;


	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case Config.GET_STATION_lIST_RESPONSE_TYPE:
				progressDialog.dismiss();
				if (msg != null) {
					GsonResponse.GetStationListResponse loginResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.GetStationListResponse.class);
					if (loginResponse.status_code.equals("0")) {
						ZLog.e("query station list succeed!");
						if (loginResponse.data != null) {
							for (int i = 0; i < loginResponse.data.length; i++) {
								stationList.add(loginResponse.data[i]);
							}
							
							if(stationList.size()<=0)
							{
								iv_no_data.setVisibility(View.VISIBLE);
								lv_search_list.setVisibility(View.GONE);
								iv_click_search.setVisibility(View.GONE);
								tv_click_search_msg.setVisibility(View.VISIBLE);
								tv_click_search_msg.setText("暂无数据");
							}
							else
							{
								iv_no_data.setVisibility(View.GONE);
								iv_click_search.setVisibility(View.GONE);
								tv_click_search_msg.setVisibility(View.GONE);
								lv_search_list.setVisibility(View.VISIBLE);
							}
							ssa.notifyDataSetChanged();
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
                        Toast.makeText(SearchResultActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;
			 case Config.NET_CONNECT_EXCEPTION:
				progressDialog.dismiss();
				lv_search_list.setVisibility(View.GONE);
				iv_no_data.setVisibility(View.GONE);
				tv_click_search_msg.setVisibility(View.VISIBLE);
				tv_click_search_msg.setText("点击刷新");
				iv_click_search.setVisibility(View.VISIBLE);
				Toast.makeText(SearchResultActivity.this, getResources().getString(R.string.netconnect_exception),
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

		setBelowContentView(R.layout.activity_search_result_layout);
		initView();
	}

	private void initView() {
		iv_no_data=(ImageView)midView.findViewById(R.id.iv_no_data);
		iv_click_search=(ImageView)midView.findViewById(R.id.iv_click_search);
		iv_click_search.setOnClickListener(this);
		tv_click_search_msg=(TextView)midView.findViewById(R.id.tv_click_search_msg);
		
		stationName = getIntent().getStringExtra("stationname");
		setBackButtonVisibility();
		setTitileName(R.string.search_station);

		ssa = new ServiceStationAdapter(SearchResultActivity.this, stationList);
		stationList = new ArrayList<GsonResponse.StationDetail>();

		setRightButtonGone();
		lv_search_list = (ListView) midView.findViewById(R.id.lv_search_list);
		ssa = new ServiceStationAdapter(this, stationList);
		lv_search_list.setAdapter(ssa);

		progressDialog=CustomProgressDialog.createDialog(this);
		progressDialog.show();
		
		if(UserInfoPersist.choseProvince.LABEL!=null&&UserInfoPersist.choseCity.LABEL!=null&&UserInfoPersist.choseBrandData.label!=null)
		{
			ClientRequest.getStationList(handler, "0", "100",UserInfoPersist.choseBrandData.label+"","",
					Config.latitude + "", Config.longitude + "",
					UserInfoPersist.choseProvince.LABEL,
					UserInfoPersist.choseCity.LABEL, stationName + "");
		}
		else if(UserInfoPersist.choseProvince.LABEL!=null&&UserInfoPersist.choseCity.LABEL!=null)
		{
			ClientRequest.getStationList(handler, "0", "100", "", "",
					Config.latitude + "", Config.longitude + "",
					UserInfoPersist.choseProvince.LABEL,
					UserInfoPersist.choseCity.LABEL, stationName + "");
		}
		else if(UserInfoPersist.choseProvince.LABEL!=null&&UserInfoPersist.choseBrandData.label!=null)
		{
			ClientRequest.getStationList(handler, "0", "100", UserInfoPersist.choseBrandData.label+"","",
					Config.latitude + "", Config.longitude + "",
					UserInfoPersist.choseProvince.LABEL,
					"", stationName + "");
		}
		else if(UserInfoPersist.choseProvince.LABEL!=null)
		{
			ClientRequest.getStationList(handler, "0", "100", "", "",
					Config.latitude + "", Config.longitude + "",
					UserInfoPersist.choseProvince.LABEL,
					"", stationName + "");
		}
		else if(UserInfoPersist.choseBrandData.label!=null)
		{
			ClientRequest.getStationList(handler, "0", "100", UserInfoPersist.choseBrandData.label+"", "",
					Config.latitude + "", Config.longitude + "",
					UserInfoPersist.choseProvince.LABEL,
					"", stationName + "");
		}
		else
		{
			ClientRequest.getStationList(handler, "0", "100", "", "",
					Config.latitude + "", Config.longitude + "",
					"",
					"", stationName + "");
		}

		lv_search_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(SearchResultActivity.this,
						ServiceStationDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Config.STATION_DETAIL_INFO,
						stationList.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	@Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.iv_click_search:
			progressDialog.show();
			ClientRequest.getTruckBrandList(handler);
			ClientRequest.getStationList(handler, "0", "50", "", "",
					Config.latitude + "", Config.longitude + "", "",
					"", "");
			break;  
        default:
            break;
        }
    }
}
