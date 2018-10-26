package com.coahr.cvfan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.adapter.TruckTypeListAdapter;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.net.GsonResponse.TruckType;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class TruckTypeListActivity extends BaseActivity {
	
	private ListView lv_brand_list;
	private TruckTypeListAdapter truckTypeListAdapter;
	private int lastClickPosition = -1;
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.GET_TRUCK_TYPE_LIST_RESPONSE_TYPE:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) {
                    GsonResponse.TruckTypeListResponse truckTypeListResponse = new Gson()
                            .fromJson(msg.obj.toString(),
                                    GsonResponse.TruckTypeListResponse.class);
                    if (truckTypeListResponse.status_code.equals("0")) {
                        if (truckTypeListResponse.data != null) {
                            TruckType truckTypes = new TruckType();
                            truckTypes.LABEL = getResources().getString(R.string.nolimit);
                            UserInfoPersist.truckTypes.add(truckTypes);
                            UserInfoPersist.truckTypes.clear();
                            for (int i = 0; i < truckTypeListResponse.data.length; i++) {
                            	UserInfoPersist.truckTypes.add(truckTypeListResponse.data[i]);
                            }
                        }
                    }
                    truckTypeListAdapter.notifyDataSetChanged();
                }
                break;
            case Config.RESPONSE_TYPE_ERROR:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(TruckTypeListActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
                break;
                
            case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(TruckTypeListActivity.this, getResources().getString(R.string.netconnect_exception),
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
                
        setBelowContentView(R.layout.activity_brand_list_layout);
        
        setBackButtonVisibility();
        setTitileName(R.string.type_chose);
        setRightButtonText(R.string.ok);
        btn_right.setOnClickListener(this);
        initUI();
        

		myProgressDialog = WaittingDialog.showHintDialog(TruckTypeListActivity.this,
				R.string.query);
		myProgressDialog.show();
		ClientRequest.getTruckTypeList(handler, UserInfoPersist.choseBrandData.id);
    }
    
    private void initUI(){
    	lv_brand_list = (ListView)midView.findViewById(R.id.lv_brand_list);
    	truckTypeListAdapter = new TruckTypeListAdapter(TruckTypeListActivity.this, UserInfoPersist.truckTypes);
    	lv_brand_list.setAdapter(truckTypeListAdapter);
    	
    	lv_brand_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if (lastClickPosition == -1) {
					view.findViewById(R.id.iv_chosed).setVisibility(
							View.VISIBLE);
				}else{
					parent.getChildAt(lastClickPosition).findViewById(R.id.iv_chosed).setVisibility(View.INVISIBLE);
					view.findViewById(R.id.iv_chosed).setVisibility(
							View.VISIBLE);
				}
				lastClickPosition = position;
				UserInfoPersist.choseTruckTypeData = UserInfoPersist.truckTypes
						.get(position);
			}
		});
    }
    
    @Override
    public void onClick(View v) {
    	super.onClick(v);
    	switch (v.getId()) {
		case R.id.btn_right:
			TruckTypeListActivity.this.finish();
			break;

		default:
			break;
		}
    }
}
