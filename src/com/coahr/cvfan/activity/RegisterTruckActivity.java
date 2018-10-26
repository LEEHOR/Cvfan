package com.coahr.cvfan.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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

public class RegisterTruckActivity extends BaseActivity {
	private final String IMAGE_TYPE = "image/*";

	
	private final int IMAGE_GALLERY = 0;
	private final int IMAGE_CAMERA = 1;

	private RelativeLayout rl_truck_brand, rl_truck_model,
			rl_driving_license_pic;
	private Intent intent;
	private ImageView tv_image_url;
	private TextView tv_truck_brand_detail, tv_truck_model_detail;
	private Button rl_truck_register;
	private String licenseFileUri = "";
	private EditText et_license_plate_number_detail;
	//private EditText et_owner_detail;
//	private EditText et_address_detail;
	private EditText et_engine_number_detail;
	private EditText et_carframe_number_detail;
	private LayoutInflater inflater;
	private PopupWindow pw_photo;
		
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.REGISTER_MY_TRUCK_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.HeadResponse headResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.HeadResponse.class);
					if (headResponse.status_code.equals("0")) {
						Toast.makeText(
								RegisterTruckActivity.this,
								getResources().getString(
										R.string.register_success),
								Toast.LENGTH_SHORT).show();
						RegisterTruckActivity.this.finish();
					}
				}
				break;

			case Config.RESPONSE_TYPE_ERROR:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(RegisterTruckActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;
				
			 case Config.NET_CONNECT_EXCEPTION:
					WaittingDialog.cancelHintDialog(myProgressDialog);
					Toast.makeText(RegisterTruckActivity.this, getResources().getString(R.string.netconnect_exception),
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

		setBelowContentView(R.layout.activity_register_truck_layout);

		setBackButtonVisibility();
		setTitileName(R.string.truck_register);
		setRightButtonGone();

		initUI();
	}

	private void initUI() {
		rl_truck_brand = (RelativeLayout) midView
				.findViewById(R.id.rl_truck_brand);
		rl_truck_model = (RelativeLayout) midView
				.findViewById(R.id.rl_truck_model);
		tv_truck_brand_detail = (TextView) midView
				.findViewById(R.id.tv_truck_brand_detail);
		tv_truck_model_detail = (TextView) midView
				.findViewById(R.id.tv_truck_model_detail);

		rl_truck_register = (Button) midView
				.findViewById(R.id.rl_truck_register);
		rl_driving_license_pic = (RelativeLayout) midView
				.findViewById(R.id.rl_driving_license_pic);
		tv_image_url = (ImageView) midView.findViewById(R.id.tv_image_url);

		et_license_plate_number_detail = (EditText) midView
				.findViewById(R.id.et_license_plate_number_detail);
		//et_owner_detail = (EditText) midView.findViewById(R.id.et_owner_detail);
//		et_address_detail = (EditText) midView.findViewById(R.id.et_address_detail);
		et_engine_number_detail = (EditText) midView
				.findViewById(R.id.et_engine_number_detail);
		et_carframe_number_detail = (EditText) midView
				.findViewById(R.id.et_carframe_number_detail);

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
		rl_truck_brand.setOnClickListener(this);
		rl_truck_model.setOnClickListener(this);
		rl_truck_register.setOnClickListener(this);
		rl_driving_license_pic.setOnClickListener(this);
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
		intent = new Intent();
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_truck_brand:
			intent.setClass(RegisterTruckActivity.this, BrandListActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_truck_model:
			if (tv_truck_brand_detail.getText()!=null&&!tv_truck_brand_detail.getText().equals("")) {
				intent.setClass(RegisterTruckActivity.this,
						TruckTypeListActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(RegisterTruckActivity.this,
						getResources().getString(R.string.brand_first),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.rl_truck_register:
			String plateNo = et_license_plate_number_detail.getText()
					.toString() + "";
//			String address = et_address_detail.getText().toString() + "";
			String engineNo = et_engine_number_detail.getText().toString() + "";
			String frameNo = et_carframe_number_detail.getText().toString()+ "";
			String brand_detail=tv_truck_brand_detail.getText().toString()+"";
			
			if(plateNo.isEmpty())
			{
				Toast.makeText(RegisterTruckActivity.this, getResources().getString(R.string.plateNo_not_msg),
						Toast.LENGTH_LONG).show();
			}
			else if(brand_detail.isEmpty())
			{
				Toast.makeText(RegisterTruckActivity.this, "车辆品牌不能为空",
						Toast.LENGTH_LONG).show();
			}
			else
			{
				myProgressDialog = WaittingDialog.showHintDialog(
						RegisterTruckActivity.this, R.string.comitting);
				myProgressDialog.show();
				ClientRequest.registerTruck(handler, licenseFileUri, plateNo,
						 engineNo, frameNo);
			}
			break;
		case R.id.rl_driving_license_pic:
			initPhotoChoice();
			pw_photo.showAtLocation(findViewById(R.id.truck_register_layout), Gravity.BOTTOM, 0, 0);
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
				tv_image_url.setImageBitmap(bm);
			} catch (IOException e) {
				Log.e("TAG-->Error", e.toString());
			}
			break;
		case IMAGE_CAMERA:
			String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.v("TestFile",
                        "SD card is not avaiable/writeable right now.");
                Toast.makeText(RegisterTruckActivity.this, "SD card 未挂载",
						Toast.LENGTH_LONG).show();
                return;
            }
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            FileOutputStream b = null;
            File file = new File(Config.LOCAL_IMAGE_URL);
            file.mkdirs();// 创建文件夹
            licenseFileUri = Config.LOCAL_IMAGE_URL + "/" +System.currentTimeMillis() + ".jpg";
            
            tv_image_url.setImageBitmap(bitmap);

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
