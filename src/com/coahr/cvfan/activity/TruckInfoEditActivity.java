package com.coahr.cvfan.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class TruckInfoEditActivity extends BaseActivity {
	
	private final String IMAGE_TYPE = "image/*";

	private final int IMAGE_GALLERY = 0;
	private final int IMAGE_CAMERA = 1;

	
	private String licenseFileUri = "";
	private String autoId;

	private Button btn_no_maintain_station;
	private ProgressDialog myProgressDialog;

	private TextView tv_license_plate_number_detail;
	private TextView tv_owner_detail;
	private TextView tv_address_detail;
	private TextView tv_truck_brand_detail;
	private TextView tv_truck_model_detail;
	private TextView tv_engine_number_detail;
	private TextView tv_carframe_number_detail;
	private TextView tv_truck_grade;
	private TextView tv_image_url;
	// private TextView tv_driving_license_number_detail;

	private EditText et_license_plate_number_detail;
	private EditText et_address_detail;
	private EditText et_engine_number_detail;
	private EditText et_carframe_number_detail;

	private ImageView iv_driving_license_pic_more,iv_truck_model_more,iv_truck_brand;
	
	private RelativeLayout rl_owner;
	private RelativeLayout rl_truck_grade;
	private RelativeLayout rl_driving_license_pic;
	private RelativeLayout rl_truck_brand;
	private RelativeLayout rl_truck_model;
	private RelativeLayout rl_address;
	private boolean editStatus = false;
	private Intent intent;
	
	private LayoutInflater inflater;
	private PopupWindow pw_photo;

	private GsonResponse.TruckInfoData truckInfo;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.GET_TRUCK_INFO_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.GetTruckInfoResponse getTruckInfoResponse = new Gson()
							.fromJson(msg.obj.toString().trim(),
									GsonResponse.GetTruckInfoResponse.class);
					truckInfo = getTruckInfoResponse.data;
					tv_license_plate_number_detail
							.setText(getTruckInfoResponse.data.PLATE_NO);
					tv_owner_detail.setText(getTruckInfoResponse.data.NAME);
					tv_address_detail
							.setText(getTruckInfoResponse.data.ADDRESS);
					tv_truck_brand_detail
							.setText(getTruckInfoResponse.data.BRAND_NAME);
					tv_truck_model_detail
							.setText(getTruckInfoResponse.data.MODEL_NAME);
					tv_engine_number_detail
							.setText(getTruckInfoResponse.data.ENGINE_NO);
					tv_carframe_number_detail
							.setText(getTruckInfoResponse.data.FRAME_NO);
					tv_truck_grade
							.setText(getTruckInfoResponse.data.AUTO_LEVEL);
					if(!getTruckInfoResponse.data.GUARANTEE.isEmpty()&&Integer.parseInt(getTruckInfoResponse.data.GUARANTEE)==2)
					{
						btn_no_maintain_station.setVisibility(View.GONE);
					}
					else if(!getTruckInfoResponse.data.GUARANTEE.isEmpty()&&Integer.parseInt(getTruckInfoResponse.data.GUARANTEE)==1)
					{
						btn_no_maintain_station.setVisibility(View.GONE);
					}
					// tv_driving_license_number_detail =
					// (TextView)midView.findViewById(R.id.tv_driving_license_number_detail);
				}
				break;
				
			case Config.MODIFY_MY_TRUCK_INFO_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.ModifyTruckInfoResponse modifyTruckInfoResponse = new Gson()
					.fromJson(msg.obj.toString().trim(),
							GsonResponse.ModifyTruckInfoResponse.class);
					
					if(modifyTruckInfoResponse.status_code.equals("0")){
						Toast.makeText(TruckInfoEditActivity.this, getResources().getString(R.string.modify_truck_info_success),
								Toast.LENGTH_LONG).show();

						hideEdit();
					}
				}
				break;

			case Config.RESPONSE_TYPE_ERROR:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(TruckInfoEditActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;
				
			case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(TruckInfoEditActivity.this, getResources().getString(R.string.netconnect_exception),
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

		setBelowContentView(R.layout.activity_edit_truck_info_layout);

		setBackButtonVisibility();
		setTitileName(R.string.vehicle_manage);
		setRightButtonText(R.string.edit);

		initUI();
	}

	private void initUI() {
		intent = new Intent();
		autoId = getIntent().getStringExtra("AUTO_ID");

		
		tv_license_plate_number_detail = (TextView) midView
				.findViewById(R.id.tv_license_plate_number_detail);
		tv_owner_detail = (TextView) midView.findViewById(R.id.tv_owner_detail);
		tv_address_detail = (TextView) midView
				.findViewById(R.id.tv_address_detail);
		tv_truck_brand_detail = (TextView) midView
				.findViewById(R.id.tv_truck_brand_detail);
		tv_truck_model_detail = (TextView) midView
				.findViewById(R.id.tv_truck_model_detail);
		tv_engine_number_detail = (TextView) midView
				.findViewById(R.id.tv_engine_number_detail);
		tv_carframe_number_detail = (TextView) midView
				.findViewById(R.id.tv_carframe_number_detail);
		tv_truck_grade = (TextView) midView.findViewById(R.id.tv_truck_grade);
		tv_image_url = (TextView) midView.findViewById(R.id.tv_image_url);
		// tv_driving_license_number_detail =
		// (TextView)midView.findViewById(R.id.tv_driving_license_number_detail);

		et_license_plate_number_detail = (EditText) midView
				.findViewById(R.id.et_license_plate_number_detail);
		et_address_detail = (EditText) midView
				.findViewById(R.id.et_address_detail);
		et_engine_number_detail = (EditText) midView
				.findViewById(R.id.et_engine_number_detail);
		et_carframe_number_detail = (EditText) midView
				.findViewById(R.id.et_carframe_number_detail);

		iv_driving_license_pic_more=(ImageView)midView.findViewById(R.id.iv_driving_license_pic_more);
		iv_truck_model_more=(ImageView)midView.findViewById(R.id.iv_truck_model_more);
		iv_truck_brand=(ImageView)midView.findViewById(R.id.iv_truck_brand);
		
		rl_owner = (RelativeLayout) midView.findViewById(R.id.rl_owner);
		rl_truck_grade = (RelativeLayout) midView
				.findViewById(R.id.rl_truck_grade);
		rl_driving_license_pic = (RelativeLayout) midView.findViewById(R.id.rl_driving_license_pic);
		
		
		rl_truck_brand = (RelativeLayout) midView.findViewById(R.id.rl_truck_brand);
		rl_truck_model = (RelativeLayout) midView.findViewById(R.id.rl_truck_model);
		rl_address = (RelativeLayout) midView.findViewById(R.id.rl_address);
		
		btn_no_maintain_station = (Button) midView
				.findViewById(R.id.btn_no_maintain_station);
		btn_no_maintain_station.setOnClickListener(this);

		myProgressDialog = WaittingDialog.showHintDialog(
				TruckInfoEditActivity.this, R.string.query);
		myProgressDialog.show();
		ClientRequest.getTruckInfo(handler, autoId, UserInfoPersist.ownerRole);

		btn_right.setOnClickListener(this);
	}

	private void showEdit() {
		rl_driving_license_pic.setOnClickListener(this);
		rl_truck_brand.setOnClickListener(this);
		rl_truck_model.setOnClickListener(this);
		btn_no_maintain_station.setVisibility(View.GONE);
		rl_owner.setVisibility(View.GONE);
		rl_truck_grade.setVisibility(View.GONE);
		tv_license_plate_number_detail.setVisibility(View.GONE);
		tv_address_detail.setVisibility(View.GONE);
		tv_engine_number_detail.setVisibility(View.GONE);
		tv_carframe_number_detail.setVisibility(View.GONE);
		rl_address.setVisibility(View.GONE);
		
		
		iv_driving_license_pic_more.setImageResource(R.drawable.xq_img7);
		iv_truck_model_more.setImageResource(R.drawable.xq_img7);
		iv_truck_brand.setImageResource(R.drawable.xq_img7);
		iv_driving_license_pic_more.setVisibility(View.VISIBLE);
		iv_truck_model_more.setVisibility(View.VISIBLE);
		iv_truck_brand.setVisibility(View.VISIBLE);
		
		et_license_plate_number_detail.setVisibility(View.VISIBLE);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		et_address_detail.setVisibility(View.VISIBLE);
		et_engine_number_detail.setVisibility(View.VISIBLE);
		et_carframe_number_detail.setVisibility(View.VISIBLE);

		et_license_plate_number_detail.setText(truckInfo.PLATE_NO);
		et_address_detail.setText(truckInfo.ADDRESS);
		et_engine_number_detail.setText(truckInfo.ENGINE_NO);
		et_carframe_number_detail.setText(truckInfo.FRAME_NO);
	}

	private void hideEdit() {
		myProgressDialog = WaittingDialog.showHintDialog(
				TruckInfoEditActivity.this, R.string.query);
		myProgressDialog.show();
		ClientRequest.getTruckInfo(handler, autoId, UserInfoPersist.ownerRole);
		
		rl_owner.setVisibility(View.VISIBLE);
		rl_truck_grade.setVisibility(View.VISIBLE);
		tv_license_plate_number_detail.setVisibility(View.VISIBLE);
		tv_address_detail.setVisibility(View.VISIBLE);
		tv_engine_number_detail.setVisibility(View.VISIBLE);
		tv_carframe_number_detail.setVisibility(View.VISIBLE);
		rl_address.setVisibility(View.VISIBLE);

		et_license_plate_number_detail.setVisibility(View.GONE);
		et_address_detail.setVisibility(View.GONE);
		et_engine_number_detail.setVisibility(View.GONE);
		et_carframe_number_detail.setVisibility(View.GONE);

		tv_license_plate_number_detail.setText(truckInfo.PLATE_NO);
		tv_owner_detail.setText(truckInfo.NAME);
		tv_address_detail.setText(truckInfo.ADDRESS);
		tv_truck_brand_detail.setText(truckInfo.BRAND_NAME);
		tv_truck_model_detail.setText(truckInfo.MODEL_NAME);
		tv_engine_number_detail.setText(truckInfo.ENGINE_NO);
		tv_carframe_number_detail.setText(truckInfo.FRAME_NO);
		tv_truck_grade.setText(truckInfo.AUTO_LEVEL);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (UserInfoPersist.choseBrandData != null) {
			tv_truck_brand_detail.setText(UserInfoPersist.choseBrandData.label);
		} else {
			tv_truck_brand_detail.setText(R.string.nolimit);
		}

		if (UserInfoPersist.choseTruckTypeData != null) {
			tv_truck_model_detail
					.setText(UserInfoPersist.choseTruckTypeData.LABEL);
		} else {
			tv_truck_model_detail.setText(R.string.nolimit);
		}
		
		
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_no_maintain_station:
			intent = new Intent();
			intent.setClass(TruckInfoEditActivity.this,
					TruckGuaranteActivity.class);
			intent.putExtra("AUTO_ID", autoId);
			intent.putExtra("PLATE_NO", truckInfo.PLATE_NO);
			intent.putExtra("FRAME_NO", truckInfo.FRAME_NO);
			startActivity(intent);
			break;
		case R.id.btn_right:
			if (!editStatus) {
				showEdit();
				setRightButtonText(R.string.ok);
				editStatus = true;
			} else {
				setRightButtonText(R.string.edit);
				editStatus = false;
				String plateNo = et_license_plate_number_detail.getText().toString();
				//String address = et_address_detail.getText().toString();
				String engineNo = et_engine_number_detail.getText().toString();
				String frameNo = et_carframe_number_detail.getText().toString();
				if (UserInfoPersist.choseBrandData != null) {
					if (UserInfoPersist.choseTruckTypeData != null) {
						myProgressDialog = WaittingDialog.showHintDialog(
								TruckInfoEditActivity.this, R.string.comitting);
						myProgressDialog.show();
						ClientRequest.modifyTruckInfo(handler, licenseFileUri,
								plateNo, engineNo, frameNo, autoId);
					}else{
						Toast.makeText(TruckInfoEditActivity.this,
								getResources().getString(R.string.type_unchoose),
								Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(TruckInfoEditActivity.this,
							getResources().getString(R.string.brand_unchoose),
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
			
		case R.id.rl_driving_license_pic:
			initPhotoChoice();
			pw_photo.showAtLocation(findViewById(R.id.truck_edit_layout), Gravity.BOTTOM, 0, 0);
			break;
		case R.id.btn_from_camera:
			if(pw_photo.isShowing()){
				pw_photo.dismiss();
			}
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, IMAGE_CAMERA);			
			break;
		case R.id.btn_from_pictures:
			if(pw_photo.isShowing()){
				pw_photo.dismiss();
			}			
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
			getAlbum.setType(IMAGE_TYPE);
			startActivityForResult(getAlbum, IMAGE_GALLERY);
			break;
		case R.id.btn_cancel:
			if(pw_photo.isShowing()){
				pw_photo.dismiss();
			}
			break;
		case R.id.rl_truck_brand:
			intent.setClass(TruckInfoEditActivity.this, BrandListActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_truck_model:
			if (UserInfoPersist.choseBrandData != null) {
				intent.setClass(TruckInfoEditActivity.this,
						TruckTypeListActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(TruckInfoEditActivity.this,
						getResources().getString(R.string.brand_first),
						Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}
	
	private void initPhotoChoice() {
		inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.activity_setting_photo_menu,
				null);
		pw_photo = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		pw_photo.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.del_dot_big));
		view.findViewById(R.id.btn_from_camera).setOnClickListener(this);
		view.findViewById(R.id.btn_from_pictures).setOnClickListener(this);
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(resultCode, resultCode, data);
		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
			Log.e("TAG->onresult", "ActivityResult resultCode error");
			return;
		}

		Bitmap bm = null;

		// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口

		ContentResolver resolver = getContentResolver();

		// 此处的用于判断接收的Activity是不是你想要的那个
		switch (requestCode) {
		case IMAGE_GALLERY:
			try {
				Uri originalUri = data.getData(); // 获得图片的uri
				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);

				// 这里开始的第二部分，获取图片的路径：
				String[] proj = { MediaStore.Images.Media.DATA };
				// 好像是android多媒体数据库的封装接口，具体的看Android文档
				Cursor cursor = managedQuery(originalUri, proj, null, null,
						null);
				// 按我个人理解 这个是获得用户选择的图片的索引值
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// 将光标移至开头 ，这个很重要，不小心很容易引起越界
				cursor.moveToFirst();
				// 最后根据索引值获取图片路径
				licenseFileUri = cursor.getString(column_index);
				tv_image_url.setText(licenseFileUri);
			} catch (IOException e) {
				Log.e("TAG-->Error", e.toString());
			}
			break;
		case IMAGE_CAMERA:
			String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.v("TestFile",
                        "SD card is not avaiable/writeable right now.");
                Toast.makeText(TruckInfoEditActivity.this, "SD card 未挂载",
						Toast.LENGTH_LONG).show();
                return;
            }
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            FileOutputStream b = null;
            File file = new File(Config.LOCAL_IMAGE_URL);
            file.mkdirs();// 创建文件夹
            licenseFileUri = Config.LOCAL_IMAGE_URL + "/" +System.currentTimeMillis() + ".jpg";
            
            tv_image_url.setText(licenseFileUri);

            try {
                b = new FileOutputStream(licenseFileUri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }			
			break;
		default:
			break;
		}
	}
}
