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
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class DriveLicenseCertificationActivity extends BaseActivity {

	private final String IMAGE_TYPE = "image/*";

	private final int IMAGE_GALLERY = 0;
	private final int IMAGE_CAMERA = 1;

	private EditText et_license_auth_name, et_license_auth_num;
	private String name, licenseNo, licenseFile;

	private Button btn_certification;

	private ImageView iv_license_auth_photo,tv_license_auth_photo;
	private RelativeLayout rl_chose_image;
	
	private LayoutInflater inflater;
	private PopupWindow pw_photo;
	private String path;
	private File webimage_file;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.IDENTIFY_DRIVER_LICENSE_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);

				if (msg.obj != null) {
					GsonResponse.CertificateDriverLicenseResponse certificateDriverLicenseResponse = new Gson()
							.fromJson(
									msg.obj.toString().trim(),
									GsonResponse.CertificateDriverLicenseResponse.class);

					if (certificateDriverLicenseResponse.status_code
							.equals("0")) {
						Toast.makeText(DriveLicenseCertificationActivity.this,
								"提交认证成功", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DriveLicenseCertificationActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;
			case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(DriveLicenseCertificationActivity.this, getResources().getString(R.string.netconnect_exception),
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

		setBelowContentView(R.layout.drivinglicense_authentication);

		setBackButtonVisibility();
		setTitileName(R.string.identity_certification);
		setRightButtonGone();

		initUI();

	}

	private void initUI() {
		et_license_auth_name = (EditText) midView
				.findViewById(R.id.et_license_auth_name);
		et_license_auth_num = (EditText) midView
				.findViewById(R.id.et_license_auth_num);
		btn_certification = (Button) midView
				.findViewById(R.id.btn_certification);

		tv_license_auth_photo = (ImageView) midView
				.findViewById(R.id.tv_license_auth_photo);
		iv_license_auth_photo = (ImageView) midView
				.findViewById(R.id.iv_license_auth_photo);
		rl_chose_image = (RelativeLayout) midView
				.findViewById(R.id.rl_chose_image);

		rl_chose_image.setOnClickListener(this);

		btn_certification.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_certification:
			name = et_license_auth_name.getText().toString().trim();
			licenseNo = et_license_auth_num.getText().toString().trim();
			if (licenseFile == null||licenseFile == "") {
				Toast.makeText(DriveLicenseCertificationActivity.this, "驾照证照片不能为空",
						Toast.LENGTH_LONG).show();
				break;
			}
			if (!name.isEmpty()) {
				if (!licenseNo.isEmpty()) {
					myProgressDialog = WaittingDialog.showHintDialog(
							DriveLicenseCertificationActivity.this,
							R.string.certification);
					myProgressDialog.show();
					ClientRequest.driverCertification(handler,
							UserInfoPersist.userID, name, licenseNo,
							licenseFile);
				}else{
					Toast.makeText(DriveLicenseCertificationActivity.this, "驾照号码为必填项",
							Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(DriveLicenseCertificationActivity.this, "姓名为必填项",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.rl_chose_image:
			initPhotoChoice();
			pw_photo.showAtLocation(findViewById(R.id.ll_authentication), Gravity.BOTTOM, 0, 0);
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
				licenseFile = cursor.getString(column_index);
				tv_license_auth_photo.setImageBitmap(bm);
			} catch (IOException e) {
				Log.e("TAG-->Error", e.toString());
			}
			break;
		case IMAGE_CAMERA:
			String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.v("TestFile",
                        "SD card is not avaiable/writeable right now.");
                Toast.makeText(DriveLicenseCertificationActivity.this, "SD card 未挂载",
						Toast.LENGTH_LONG).show();
                return;
            }
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            FileOutputStream b = null;
            File file = new File(Config.LOCAL_IMAGE_URL);
            file.mkdirs();// 创建文件夹
            licenseFile = Config.LOCAL_IMAGE_URL + "/" +System.currentTimeMillis() + ".jpg";
            
			tv_license_auth_photo.setImageBitmap(bitmap);

            try {
                b = new FileOutputStream(licenseFile);
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
