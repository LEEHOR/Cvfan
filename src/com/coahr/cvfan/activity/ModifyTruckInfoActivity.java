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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.listener.AnimateFirstDisplayListener;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.net.GsonResponse.BrandType;
import com.coahr.cvfan.net.GsonResponse.TruckType;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class ModifyTruckInfoActivity extends BaseActivity {

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_GALLERY = 0;
	private final int IMAGE_CAMERA = 1;

	private RelativeLayout rl_truck_brand, rl_truck_model;
	private Intent intent;
	private ImageView tv_image_url;
	private TextView tv_truck_brand_detail, tv_truck_model_detail;
	private RelativeLayout rl_driving_license_pic;
	private String licenseFileUri = "";
	private EditText et_license_plate_number_detail;
	private EditText et_engine_number_detail;
	private EditText et_carframe_number_detail;
	private LayoutInflater inflater;
	private PopupWindow pw_photo;
	private String plateNo;
	private String brand;
	private String model;
	private String engineNo;
	private String carframeNo;
	private String licenseimageUrl;
	private String autoId;
	private String brandId;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.MODIFY_MY_TRUCK_INFO_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.HeadResponse headResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.HeadResponse.class);
					if (headResponse.status_code.equals("0")) 
					{
						Toast.makeText(
								ModifyTruckInfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
						ModifyTruckInfoActivity.this.finish();
					}
					else
					{
						Toast.makeText(
								ModifyTruckInfoActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
						ModifyTruckInfoActivity.this.finish();
					}
					
				} 
				break;

			case Config.RESPONSE_TYPE_ERROR:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.HeadResponse headResponse = new Gson()
							.fromJson(msg.obj.toString().trim(),
									GsonResponse.HeadResponse.class);
					Toast.makeText(ModifyTruckInfoActivity.this,
							headResponse.status_text, Toast.LENGTH_LONG).show();
				}
				break;

			case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(
						ModifyTruckInfoActivity.this,
						getResources().getString(R.string.netconnect_exception),
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

		setBelowContentView(R.layout.activity_modify_truck_info_layout);

		setBackButtonVisibility();
		setTitileName(R.string.edit);
		setRightButtonText(R.string.ok);
		animateFirstListener = new AnimateFirstDisplayListener();
		options = new DisplayImageOptions.Builder()
				.showStubImage(0)
				.showImageForEmptyUri(0)
				.showImageOnFail(0)
				.cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new SimpleBitmapDisplayer()).build();
		initUI();
	}

	private void initUI() {
		intent = getIntent();
		autoId=intent.getStringExtra("AUTO_ID");
		plateNo = intent.getStringExtra("PLATE_NO");
		brand = intent.getStringExtra("BRAND");
		brandId = intent.getStringExtra("BRAND_ID");
		model = intent.getStringExtra("MODEL");
		engineNo = intent.getStringExtra("ENGINE_NO");
		carframeNo = intent.getStringExtra("CARFRAME_NO");
		licenseimageUrl = intent.getStringExtra("LICENSE_URL");

		tv_truck_brand_detail = (TextView) midView.findViewById(R.id.tv_truck_brand_detail);
		if(brand!=null)
		{
			tv_truck_brand_detail.setText(brand);
		}
		tv_truck_model_detail = (TextView) midView.findViewById(R.id.tv_truck_model_detail);
		
		if(model!=null)
		{
			tv_truck_model_detail.setText(model);
		}
		
		tv_image_url = (ImageView) midView.findViewById(R.id.tv_image_url);
		et_license_plate_number_detail = (EditText) midView.findViewById(R.id.et_license_plate_number_detail);
		et_license_plate_number_detail.setText(plateNo);
		rl_driving_license_pic=(RelativeLayout)midView.findViewById(R.id.rl_driving_license_pic);
		rl_driving_license_pic.setOnClickListener(this);
		et_engine_number_detail = (EditText) midView.findViewById(R.id.et_engine_number_detail);
		if(engineNo!=null)
		{
			et_engine_number_detail.setText(engineNo);
		}
		
		et_carframe_number_detail = (EditText) midView.findViewById(R.id.et_carframe_number_detail);
		if(carframeNo!=null)
		{
			et_carframe_number_detail.setText(carframeNo);
		}

		
	    imageLoader.displayImage(licenseimageUrl, tv_image_url, options, animateFirstListener);
	    
	    UserInfoPersist.choseBrandData=new BrandType();
	    UserInfoPersist.choseBrandData.id=brandId;
		rl_truck_brand = (RelativeLayout) midView.findViewById(R.id.rl_truck_brand);
		rl_truck_model = (RelativeLayout) midView.findViewById(R.id.rl_truck_model);
		rl_truck_brand.setOnClickListener(this);
		rl_truck_model.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (UserInfoPersist.choseBrandData != null) {
			if(UserInfoPersist.choseBrandData.label!=null&&!"".equals(UserInfoPersist.choseBrandData.label))
			{
				tv_truck_brand_detail.setText(UserInfoPersist.choseBrandData.label);
			}
			else
			{
				tv_truck_model_detail.setText("");
			}
		} 

		if (UserInfoPersist.choseTruckTypeData != null) {
			if(UserInfoPersist.choseTruckTypeData.LABEL!=null&&!"".equals(UserInfoPersist.choseTruckTypeData.LABEL))
			{
				tv_truck_model_detail.setText(UserInfoPersist.choseTruckTypeData.LABEL);
			}
		} 
		
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_right:
			String plateNo = et_license_plate_number_detail.getText()
					.toString();
			// String address = et_address_detail.getText().toString();
			String engineNo = et_engine_number_detail.getText().toString();
			String frameNo = et_carframe_number_detail.getText().toString();
			String brand_detail=tv_truck_brand_detail.getText().toString()+"";
			
			if(plateNo.isEmpty())
			{
				Toast.makeText(ModifyTruckInfoActivity.this, getResources().getString(R.string.plateNo_not_msg),
						Toast.LENGTH_LONG).show();
			}
			else if(brand_detail.isEmpty())
			{
				Toast.makeText(ModifyTruckInfoActivity.this, "车辆品牌不能为空",
						Toast.LENGTH_LONG).show();
			}
			else
			{
				myProgressDialog = WaittingDialog.showHintDialog(
						ModifyTruckInfoActivity.this, R.string.comitting);
				myProgressDialog.show();
				ClientRequest.modifyTruckInfo(handler, licenseFileUri,
						plateNo, engineNo, frameNo, autoId);
			}
			break;

		case R.id.rl_driving_license_pic:
			initPhotoChoice();
			pw_photo.showAtLocation(findViewById(R.id.truck_modify_layout),
					Gravity.BOTTOM, 0, 0);
			break;
		case R.id.btn_from_camera:
			if (pw_photo.isShowing()) {
				pw_photo.dismiss();
			}
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, IMAGE_CAMERA);
			break;
		case R.id.btn_from_pictures:
			if (pw_photo.isShowing()) {
				pw_photo.dismiss();
			}
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
			getAlbum.setType(IMAGE_TYPE);
			startActivityForResult(getAlbum, IMAGE_GALLERY);
			break;
		case R.id.btn_cancel:
			if (pw_photo.isShowing()) {
				pw_photo.dismiss();
			}
			break;
		case R.id.rl_truck_brand:
			intent.setClass(ModifyTruckInfoActivity.this,
					BrandListActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_truck_model:
			if (UserInfoPersist.choseBrandData != null) {
				intent.setClass(ModifyTruckInfoActivity.this,
						TruckTypeListActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(ModifyTruckInfoActivity.this,
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
		View view = inflater
				.inflate(R.layout.activity_setting_photo_menu, null);
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
				Toast.makeText(ModifyTruckInfoActivity.this, "SD card 未挂载",
						Toast.LENGTH_LONG).show();
				return;
			}
			Bitmap bitmap = (Bitmap) data.getExtras().get("data");
			FileOutputStream b = null;
			File file = new File(Config.LOCAL_IMAGE_URL);
			file.mkdirs();// 创建文件夹
			licenseFileUri = Config.LOCAL_IMAGE_URL + "/"
					+ System.currentTimeMillis() + ".jpg";

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

	@Override
	public void onPause() {
		imageLoader.cancelDisplayTask(tv_image_url);
		imageLoader.stop();
		super.onPause();
	}
}
