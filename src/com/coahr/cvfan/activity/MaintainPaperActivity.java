package com.coahr.cvfan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class MaintainPaperActivity extends BaseActivity {

	private Button btn_appraise;
	private String serviceid;
	private TextView tv_construct_number_detail;
	private TextView tv_maintain_plate_number_detail;
	private TextView tv_in_date_detail;
	private TextView tv_out_date_detail;
	private TextView tv_client_detail;
	private TextView tv_contract_detail;
	private TextView tv_contract_phone_number_detail;
	private TextView tv_service_detail;
	private TextView tv_assurance_station_detail;
	private TextView tv_price_detail;
	private TextView tv_maintain_vip_grade_detail;
	private TextView tv_pay_price_detail;
	private TextView tv_describe_detail;
	
	private ImageView iv_already_pay;
	private String station_id;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.GET_MAINTAIN_PAPER_DETAIL_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.GetMaintainPaperDetailResponse getMaintainPaperDetailResponse = new Gson()
							.fromJson(
									msg.obj.toString().trim(),
									GsonResponse.GetMaintainPaperDetailResponse.class);
					if (getMaintainPaperDetailResponse.data != null) {
						tv_construct_number_detail.setText(getMaintainPaperDetailResponse.data.SERVICE_CODE);
						tv_maintain_plate_number_detail.setText(getMaintainPaperDetailResponse.data.AUTO_PLATE_NO);
						tv_in_date_detail.setText(getMaintainPaperDetailResponse.data.ENTER_DATE);
						tv_out_date_detail.setText(getMaintainPaperDetailResponse.data.LEAVE_DATE);
						tv_client_detail.setText(getMaintainPaperDetailResponse.data.OWNER_NAME);
						tv_contract_detail.setText(getMaintainPaperDetailResponse.data.CONTACTER);
						tv_contract_phone_number_detail.setText(getMaintainPaperDetailResponse.data.CONTACT_TEL);
						tv_service_detail.setText(getMaintainPaperDetailResponse.data.SERVICE_STATION_NAME);
						tv_assurance_station_detail.setText(getMaintainPaperDetailResponse.data.GURANTEE_STATION_NAME);
						tv_price_detail.setText(getMaintainPaperDetailResponse.data.SERVICE_AMOUNT);
						tv_maintain_vip_grade_detail.setText(getMaintainPaperDetailResponse.data.AUTO_LEVEl);
						tv_pay_price_detail.setText(getMaintainPaperDetailResponse.data.PAYMENT_AMOUNT);
						tv_describe_detail.setText(getMaintainPaperDetailResponse.data.SERVICE_ITEMS);
						station_id=getMaintainPaperDetailResponse.data.STATION_ID;
						if (getMaintainPaperDetailResponse.data.PAYMENT_STATUS.equals("1")) {
							iv_already_pay.setVisibility(View.VISIBLE);
						}
						else
						{
							iv_already_pay.setImageDrawable(getResources().getDrawable(R.drawable.weixiudan_img2));
							iv_already_pay.setVisibility(View.VISIBLE);
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
                        Toast.makeText(MaintainPaperActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;
				
			 case Config.NET_CONNECT_EXCEPTION:
					WaittingDialog.cancelHintDialog(myProgressDialog);
					Toast.makeText(MaintainPaperActivity.this, getResources().getString(R.string.netconnect_exception),
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

		setBelowContentView(R.layout.activity_maintain_paper_layout);

		initUI();
	}

	private void initUI() {
		setBackButtonVisibility();
		setTitileName(R.string.maintail_paper);
		setRightButtonGone();

		serviceid = getIntent().getStringExtra("serviceid");
		
		tv_construct_number_detail = (TextView)midView.findViewById(R.id.tv_construct_number_detail);
		tv_maintain_plate_number_detail = (TextView)midView.findViewById(R.id.tv_maintain_plate_number_detail);
		tv_in_date_detail = (TextView)midView.findViewById(R.id.tv_in_date_detail);
		tv_out_date_detail = (TextView)midView.findViewById(R.id.tv_out_date_detail);
		tv_client_detail = (TextView)midView.findViewById(R.id.tv_client_detail);
		tv_contract_detail = (TextView)midView.findViewById(R.id.tv_contract_detail);
		tv_contract_phone_number_detail = (TextView)midView.findViewById(R.id.tv_contract_phone_number_detail);
		tv_service_detail = (TextView)midView.findViewById(R.id.tv_service_detail);
		tv_assurance_station_detail = (TextView)midView.findViewById(R.id.tv_assurance_station_detail);
		tv_price_detail = (TextView)midView.findViewById(R.id.tv_price_detail);
		tv_maintain_vip_grade_detail = (TextView)midView.findViewById(R.id.tv_maintain_vip_grade_detail);
		tv_pay_price_detail = (TextView)midView.findViewById(R.id.tv_pay_price_detail);
		tv_describe_detail = (TextView)midView.findViewById(R.id.tv_describe_detail);
		
		iv_already_pay = (ImageView)midView.findViewById(R.id.iv_already_pay);

		btn_appraise = (Button) midView.findViewById(R.id.btn_appraise);
		btn_appraise.setOnClickListener(this);

		myProgressDialog = WaittingDialog.showHintDialog(
				MaintainPaperActivity.this, R.string.query);
		myProgressDialog.show();
		ClientRequest.getMaintainPaperDetails(handler, serviceid);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_appraise:
			Intent intent = new Intent();
			intent.putExtra("station_id", station_id);
			intent.setClass(MaintainPaperActivity.this,
					AppraiseStationActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
