package com.coahr.cvfan.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.activity.RegisterTruckActivity;
import com.coahr.cvfan.adapter.TruckInfoAdapter;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class VehicleManageFragment extends BaseFragment {
    
    private ListView lv_vehicle; 
    
    private TextView  tv_prompt_msg;
    private TruckInfoAdapter tiAdapter;
    private TextView tv_click_search_msg;
	private ImageView iv_no_data;
	private ImageView iv_click_search;
	private RelativeLayout rl_unregister;
    
    private ArrayList<GsonResponse.TruckItem> truckList;
    
    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case Config.GET_DRIVER_TRUCK_INFO_RESPONSE_TYPE:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if(msg.obj != null){
                    GsonResponse.MyTrucksInfoResponse myTrucksInfoResponse = new Gson().fromJson(msg.obj.toString().trim(),
                            GsonResponse.MyTrucksInfoResponse.class);
                    truckList.clear();
                    for (int i = 0; i < myTrucksInfoResponse.data.length; i++) {
                        truckList.add(myTrucksInfoResponse.data[i]);
                    }
                	tv_prompt_msg.setText(getResources().getString(R.string.having_auto_msg));
                	
    				rl_unregister.setVisibility(View.VISIBLE);
                	if(truckList.size()<=0)
					{
                		rl_unregister.setVisibility(View.GONE);
						iv_no_data.setVisibility(View.VISIBLE);
						lv_vehicle.setVisibility(View.GONE);
						iv_click_search.setVisibility(View.GONE);
						tv_click_search_msg.setVisibility(View.VISIBLE);
						tv_click_search_msg.setText("暂无数据");
					}
					else
					{
						iv_no_data.setVisibility(View.GONE);
						iv_click_search.setVisibility(View.GONE);
						tv_click_search_msg.setVisibility(View.GONE);
						lv_vehicle.setVisibility(View.VISIBLE);
					}
                	
                }
                else
                {
                	tv_prompt_msg.setText(getResources().getString(R.string.not_auto_msg));
                }
                tiAdapter.notifyDataSetChanged();
                break;
                
            case Config.RESPONSE_TYPE_ERROR:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(getActivity(), headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
                break;
            case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				rl_unregister.setVisibility(View.GONE);
				lv_vehicle.setVisibility(View.GONE);
				iv_no_data.setVisibility(View.GONE);
				tv_click_search_msg.setVisibility(View.VISIBLE);
				tv_click_search_msg.setText("点击刷新");
				iv_click_search.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), getResources().getString(R.string.netconnect_exception),
	                     Toast.LENGTH_LONG).show();
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
        initView();
        return baseView;
    }
    
    private void initView(){
        setBelowContentView(R.layout.vehicle_manage_frag);
        setTitileName(R.string.vehicle_manage);
        setRightButtonText(R.string.register_truck);
        
        btn_right.setOnClickListener(this);
        iv_no_data=(ImageView)midView.findViewById(R.id.iv_no_data);
		iv_click_search=(ImageView)midView.findViewById(R.id.iv_click_search);
		iv_click_search.setOnClickListener(this);
		tv_click_search_msg=(TextView)midView.findViewById(R.id.tv_click_search_msg);
        truckList = new ArrayList<GsonResponse.TruckItem>();
        
        rl_unregister=(RelativeLayout)midView.findViewById(R.id.rl_unregister);
        lv_vehicle = (ListView)midView.findViewById(R.id.lv_vehicle);
        tv_prompt_msg=(TextView)midView.findViewById(R.id.tv_prompt_msg);
        tiAdapter = new TruckInfoAdapter(getActivity(), truckList);
        lv_vehicle.setAdapter(tiAdapter);
        /*lv_vehicle.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TruckInfoEditActivity.class);
                startActivity(intent);
            }
        });
        
        myProgressDialog = WaittingDialog.showHintDialog(getActivity(), R.string.query);
        myProgressDialog.show();
        ClientRequest.getMyTruckInfo(handler, UserInfoPersist.driverId);
        */
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	myProgressDialog = WaittingDialog.showHintDialog(getActivity(), R.string.query);
        myProgressDialog.show();
        ClientRequest.getMyTruckInfo(handler, UserInfoPersist.driverId);
    }
    
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.btn_right:
        	UserInfoPersist.choseBrandData = new GsonResponse.BrandType();
        	UserInfoPersist.choseTruckTypeData=new GsonResponse.TruckType();
            Intent intent = new Intent();
            intent.setClass(getActivity(), RegisterTruckActivity.class);
            startActivity(intent);
            break;
        case R.id.iv_click_search:
        	myProgressDialog = WaittingDialog.showHintDialog(getActivity(), R.string.query);
            myProgressDialog.show();
            ClientRequest.getMyTruckInfo(handler, UserInfoPersist.driverId);
            break;   
        default:
            break;
        }
    }

}
