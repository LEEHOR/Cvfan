package com.coahr.cvfan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.listener.AnimateFirstDisplayListener;
import com.coahr.cvfan.util.CircleBitmapDisplayer;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.util.UtilTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class DriveLicenseCertificationInfoActivity extends BaseActivity {
	
	private TextView tv_license_auth_name_detail,tv_license_auth_num_detail;
	private ImageView iv_license_auth_photo;
	
 	private DisplayImageOptions options;
 	private ImageLoadingListener animateFirstListener;
 	private String licenseUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setBelowContentView(R.layout.drivinglicense_authentication_info);
		setBackButtonVisibility();
		setTitileName(R.string.identity_certification_info);
		setRightButtonGone();
		
		animateFirstListener = new AnimateFirstDisplayListener();
		options = new DisplayImageOptions.Builder()
				.showStubImage(0)
				.showImageForEmptyUri(0)
				.showImageOnFail(0)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new SimpleBitmapDisplayer())
				.build();
		initUI();

	}

	private void initUI() {
		tv_license_auth_name_detail=(TextView)midView.findViewById(R.id.tv_license_auth_name_detail);
		tv_license_auth_num_detail=(TextView)midView.findViewById(R.id.tv_license_auth_num_detail);
		iv_license_auth_photo=(ImageView)midView.findViewById(R.id.iv_license_auth_photo);
		if(UserInfoPersist.personalInfo.LICENSE_FILE!=null)
        {
			licenseUrl = Config.REQUEST_URL + UtilTools.returnImageurlSmall(UserInfoPersist.personalInfo.LICENSE_FILE);
			imageLoader.displayImage(licenseUrl, iv_license_auth_photo, options, animateFirstListener);
        }
		
		if(UserInfoPersist.personalInfo.NAME!=null)
		{
			tv_license_auth_name_detail.setText(UserInfoPersist.personalInfo.NAME);
		}
		
		if(UserInfoPersist.personalInfo.PLATE_NO!=null)
		{
			tv_license_auth_num_detail.setText(UserInfoPersist.personalInfo.PLATE_NO);
		}
	}
	
	 @Override
    public void onPause() {
    	imageLoader.cancelDisplayTask(iv_license_auth_photo);
    	imageLoader.stop();
    	super.onPause();
    }
}
