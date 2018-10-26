package com.coahr.cvfan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.zipper.framwork.core.ZLog;

import com.coahr.cvfan.R;
import com.coahr.cvfan.fragment.VehicleManageFragment;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.net.GsonResponse.StationDetail;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class TruckGuaranteActivity extends BaseActivity {

	private RelativeLayout rl_driving_license_pic;
	private Button btn_no_maintain_station;
	private TextView tv_license_plate_number_detail;
	private TextView tv_carframe_number_detail;
	private TextView tv_assurance_station_detail;
	private String platNo;
	private String frameNo;
	private String autoId;
	private Intent intent;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.REQUEST_STATION_ASSURANCE_RESPONSE_TPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg != null) {
					GsonResponse.StationAssuranceResponse stationAssuranceResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.StationAssuranceResponse.class);
					if (stationAssuranceResponse.status_code.equals("0")) {
						ZLog.e("request station assurance succeed!");
						Toast.makeText(
								TruckGuaranteActivity.this,
								getResources().getString(
										R.string.commit_assurance_succeed)
										.toString(), Toast.LENGTH_LONG).show();
						finish();
					}
				}
				break;
			case Config.RESPONSE_TYPE_ERROR:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(TruckGuaranteActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;
			case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(TruckGuaranteActivity.this, getResources().getString(R.string.netconnect_exception),
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

		setBelowContentView(R.layout.activity_truck_guarante_layout);

		setBackButtonVisibility();
		setTitileName(R.string.truck_guarante);
		setRightButtonGone();
		platNo = getIntent().getStringExtra("PLATE_NO");
		frameNo = getIntent().getStringExtra("FRAME_NO");
		autoId = getIntent().getStringExtra("AUTO_ID");
		initUI();

	}

	private void initUI() {
		UserInfoPersist.choseStation = new StationDetail();
		btn_no_maintain_station = (Button) midView
				.findViewById(R.id.btn_no_maintain_station);
		btn_no_maintain_station.setOnClickListener(this);

		tv_license_plate_number_detail = (TextView) midView
				.findViewById(R.id.tv_license_plate_number_detail);
		tv_carframe_number_detail = (TextView) midView
				.findViewById(R.id.tv_carframe_number_detail);
		rl_driving_license_pic = (RelativeLayout) midView
				.findViewById(R.id.rl_driving_license_pic);
		rl_driving_license_pic.setOnClickListener(this);
		tv_assurance_station_detail = (TextView) midView
				.findViewById(R.id.tv_assurance_station_detail);

		tv_license_plate_number_detail.setText(platNo);
		tv_carframe_number_detail.setText(frameNo);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_no_maintain_station:
			if(!tv_assurance_station_detail.getText().toString().isEmpty())
			{
				
				myProgressDialog = WaittingDialog.showHintDialog(
						TruckGuaranteActivity.this, R.string.comitting);
				myProgressDialog.show();
				ClientRequest.requestStationAssurance(handler, autoId,
						UserInfoPersist.choseStation.STATION_ID);
			}
			else
			{
				Toast.makeText(TruckGuaranteActivity.this,getResources().getString(R.string.applystation_unchoose),
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.rl_driving_license_pic:
			intent = new Intent();
			intent.setClass(TruckGuaranteActivity.this,
					ChoseStationActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onRestart();
		if (UserInfoPersist.choseStation.NAME != null) {
			tv_assurance_station_detail.setText(UserInfoPersist.choseStation.NAME);
		}
	}
}
