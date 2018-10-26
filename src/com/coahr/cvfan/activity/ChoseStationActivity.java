package com.coahr.cvfan.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.zipper.framwork.core.ZLog;

import com.coahr.cvfan.R;
import com.coahr.cvfan.adapter.ServiceStationAdapter;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.net.GsonResponse.AreaData;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class ChoseStationActivity extends BaseActivity {
	
	private RelativeLayout rl_chose_area;
	private TextView tv_city_chose_detail;
	private EditText et_search_station_name;
	private ListView lv_service_station;
	private ImageView iv_delete_text,iv_search_station,iv_area_direction;
	private ServiceStationAdapter ssAdapter;
	private ArrayList<GsonResponse.StationDetail> stationList;
//	private GsonResponse.StationDetail choseStation = new GsonResponse.StationDetail();
	
	Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case Config.GET_STATION_lIST_RESPONSE_TYPE:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg != null) {
                    GsonResponse.GetStationListResponse loginResponse = new Gson()
                            .fromJson(msg.obj.toString(),
                                    GsonResponse.GetStationListResponse.class);
                    if (loginResponse.status_code.equals("0")) {
                        ZLog.e("query station list succeed!");
                        if (loginResponse.data != null) {
                            stationList.clear();
                            for (int i = 0; i < loginResponse.data.length; i++) {
                                stationList.add(loginResponse.data[i]);
                            }
                            ssAdapter.notifyDataSetChanged();
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
                        Toast.makeText(ChoseStationActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
                break;
        	case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(ChoseStationActivity.this, getResources().getString(R.string.netconnect_exception),
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
                
        setBelowContentView(R.layout.activity_chose_station_layout);
        
        setBackButtonVisibility();
        setTitileName(R.string.chose_station);
        setRightButtonText(R.string.ok);
        initUI();
    }
    
    private void initUI(){
    	UserInfoPersist.choseProvince=new AreaData();
    	UserInfoPersist.choseCity=new AreaData();
    	rl_chose_area = (RelativeLayout) midView.findViewById(R.id.rl_chose_area);
    	rl_chose_area.setOnClickListener(this);
    	tv_city_chose_detail = (TextView) midView.findViewById(R.id.tv_city_chose_detail);
    	iv_delete_text=(ImageView)midView.findViewById(R.id.iv_delete_text);
    	lv_service_station = (ListView) midView.findViewById(R.id.lv_service_station);
    	iv_search_station=(ImageView)midView.findViewById(R.id.iv_search_station);
    	et_search_station_name=(EditText)midView.findViewById(R.id.et_search_station_name);
    	iv_area_direction=(ImageView)midView.findViewById(R.id.iv_area_direction);
    	stationList = new ArrayList<GsonResponse.StationDetail>();
    	ssAdapter = new ServiceStationAdapter(ChoseStationActivity.this, stationList);
    	lv_service_station.setAdapter(ssAdapter);
    	btn_right.setOnClickListener(this);
    	iv_delete_text.setOnClickListener(this);
    	iv_search_station.setOnClickListener(this);
    	lv_service_station.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				UserInfoPersist.choseStation = stationList.get(position);
				ssAdapter.setSelectItem(position);
				ssAdapter.notifyDataSetInvalidated();
			}
		});
    	
    	myProgressDialog = WaittingDialog.showHintDialog(ChoseStationActivity.this, R.string.query);
		myProgressDialog.show();
    	ClientRequest.getStationList(handler, "0", "100", "", "", Config.latitude + "", Config.longitude + "",
				"", "", "");
    }
    
    @Override
    public void onClick(View v) {
    	super.onClick(v);
    	String  searchName=et_search_station_name.getText().toString().trim();
    	switch (v.getId()) {
		case R.id.rl_chose_area:
			Intent intent = new Intent();
			intent.setClass(ChoseStationActivity.this, AreaChoseActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_right:
			ChoseStationActivity.this.finish();
			break;
		case R.id.iv_delete_text:
			tv_city_chose_detail.setText(null);
			tv_city_chose_detail.setHint(getResources().getString(R.string.chose_city));
			iv_delete_text.setVisibility(View.GONE);
			iv_area_direction.setVisibility(View.VISIBLE);
        	UserInfoPersist.choseProvince=new AreaData();
        	UserInfoPersist.choseCity=new AreaData();
//        	if(!searchName.isEmpty())
//        	{
//        		myProgressDialog = WaittingDialog.showHintDialog(ChoseStationActivity.this, R.string.query);
//        		myProgressDialog.show();
//        		ClientRequest.getStationList(handler, "0", "100", "", "", Config.latitude + "", Config.longitude + "",
//						"", "", searchName);
//        	}
//        	else
//        	{
//        		myProgressDialog = WaittingDialog.showHintDialog(ChoseStationActivity.this, R.string.query);
//        		myProgressDialog.show();
//        		ClientRequest.getStationList(handler, "0", "100", "", "", Config.latitude + "", Config.longitude + "",
//						"", "", "");
//        	}
			break;
		case R.id.iv_search_station:
			
			if(!et_search_station_name.getText().toString().trim().isEmpty())
			{
				myProgressDialog = WaittingDialog.showHintDialog(ChoseStationActivity.this, R.string.query);
				myProgressDialog.show();
				ClientRequest.getStationList(handler, "0", "100", "", "", Config.latitude + "", Config.longitude + "",
						UserInfoPersist.choseProvince.LABEL, UserInfoPersist.choseCity.LABEL, searchName);
			}
			else if (UserInfoPersist.choseProvince.LABEL != null && UserInfoPersist.choseCity.LABEL != null) {
				myProgressDialog = WaittingDialog.showHintDialog(ChoseStationActivity.this, R.string.query);
				myProgressDialog.show();
				ClientRequest.getStationList(handler, "0", "100", "", "", Config.latitude + "", Config.longitude + "",
						UserInfoPersist.choseProvince.LABEL, UserInfoPersist.choseCity.LABEL, "");
			}
	    	else if(UserInfoPersist.choseProvince.LABEL !=null)
	    	{
	    		myProgressDialog = WaittingDialog.showHintDialog(ChoseStationActivity.this, R.string.query);
				myProgressDialog.show();
	    		ClientRequest.getStationList(handler, "0", "100", "", "", Config.latitude + "", Config.longitude + "",
						UserInfoPersist.choseProvince.LABEL, "", "");
	    	}
	    	else
	    	{
	    		myProgressDialog = WaittingDialog.showHintDialog(ChoseStationActivity.this, R.string.query);
				myProgressDialog.show();
	    		ClientRequest.getStationList(handler, "0", "100", "", "", Config.latitude + "", Config.longitude + "",
						"", "", "");
	    	}
			break;
		default:
			break;
		}
    }    
    
    @Override
    public void onResume() {
    	super.onResume();
    	if (UserInfoPersist.choseProvince.LABEL != null && UserInfoPersist.choseCity.LABEL != null) {
    		tv_city_chose_detail.setText(UserInfoPersist.choseProvince.LABEL + "" + " "
					+ UserInfoPersist.choseCity.LABEL + "");
    		iv_area_direction.setVisibility(View.GONE);
    		iv_delete_text.setVisibility(View.VISIBLE);
		}
    	else if(UserInfoPersist.choseProvince.LABEL !=null)
    	{
    		tv_city_chose_detail.setText(UserInfoPersist.choseProvince.LABEL + "");
    		iv_area_direction.setVisibility(View.GONE);
    		iv_delete_text.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		iv_area_direction.setVisibility(View.VISIBLE);
    	}
    	
		if (UserInfoPersist.choseBrandData.label != null) {
			tv_city_chose_detail.setText(UserInfoPersist.choseBrandData.label + "");
		}
//		myProgressDialog = WaittingDialog.showHintDialog(ChoseStationActivity.this, R.string.query);
//		myProgressDialog.show();
//		ClientRequest.getStationList(handler, "0", "100", "", "", Config.latitude + "", Config.longitude + "",
//					UserInfoPersist.choseProvince.LABEL, UserInfoPersist.choseCity.LABEL, "");
    }
}
