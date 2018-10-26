package com.coahr.cvfan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.adapter.BrandChoseAdapter;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.net.GsonResponse.BrandType;
import com.coahr.cvfan.net.GsonResponse.TruckType;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class BrandListActivity extends BaseActivity {

	private ListView lv_brand_list;
	private BrandChoseAdapter brandChoseAdapter;
//	private int lastClickPosition = -1;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.GET_TRUCK_BRAND_LIST_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.BrandListResponse brandListResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.BrandListResponse.class);
					if (brandListResponse.status_code.equals("0")) {
						if (brandListResponse.data != null) {
							GsonResponse.BrandType brandType = new GsonResponse.BrandType();
							brandType.label = getResources().getString(
									R.string.nolimit);
							UserInfoPersist.brands.add(brandType);
							for (int i = 0; i < brandListResponse.data.length; i++) {
								UserInfoPersist.brands
										.add(brandListResponse.data[i]);
							}
						}
					}
					brandChoseAdapter.notifyDataSetChanged();
				}
				break;
			case Config.RESPONSE_TYPE_ERROR:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(BrandListActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;
			case Config.NET_CONNECT_EXCEPTION:
					WaittingDialog.cancelHintDialog(myProgressDialog);
					Toast.makeText(BrandListActivity.this, getResources().getString(R.string.netconnect_exception),
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
		setTitileName(R.string.brand_chose);
		setRightButtonText(R.string.ok);
		btn_right.setOnClickListener(this);
		initUI();

		if (UserInfoPersist.brands == null
				|| UserInfoPersist.brands.size() == 0) {
			myProgressDialog = WaittingDialog.showHintDialog(
					BrandListActivity.this, R.string.query);
			myProgressDialog.show();
			ClientRequest.getTruckBrandList(handler);
		}
	}

	private void initUI() {
		// UserInfoPersist.brands = new ArrayList<GsonResponse.BrandType>();
		lv_brand_list = (ListView) midView.findViewById(R.id.lv_brand_list);
		brandChoseAdapter = new BrandChoseAdapter(BrandListActivity.this,
				UserInfoPersist.brands);
		lv_brand_list.setAdapter(brandChoseAdapter);
		lv_brand_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				if (lastClickPosition == -1) {
//					view.findViewById(R.id.iv_chosed).setVisibility(
//							View.VISIBLE);
//				} else {
//					parent.getChildAt(lastClickPosition)
//							.findViewById(R.id.iv_chosed)
//							.setVisibility(View.INVISIBLE);
//					view.findViewById(R.id.iv_chosed).setVisibility(
//							View.VISIBLE);
//				}
//				lastClickPosition = position;
				UserInfoPersist.choseBrandData = UserInfoPersist.brands
						.get(position);
				brandChoseAdapter.setSelectItem(position);
				UserInfoPersist.choseTruckTypeData=new TruckType();
				brandChoseAdapter.notifyDataSetInvalidated();
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_right:
			BrandListActivity.this.finish();
			break;
		case R.id.btn_back:// 返回按钮
			this.finish();
			break;

		default:
			break;
		}
	}
}
