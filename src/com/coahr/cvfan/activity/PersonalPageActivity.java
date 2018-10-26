package com.coahr.cvfan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.adapter.GalleryAdapter;
import com.coahr.cvfan.listener.AnimateFirstDisplayListener;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.CircleBitmapDisplayer;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.util.UtilTools;
import com.coahr.cvfan.view.CVFanGridView;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class PersonalPageActivity extends BaseActivity {

	private RelativeLayout rl_personal_info;
	private RelativeLayout rl_truck_info;
	private RelativeLayout rl_drive_license_certificate;
	private RelativeLayout rl_bind_phone;
	private RelativeLayout rl_my_credit;
	private CVFanGridView gv_gallery;

	private ScrollView sv_personal_info;
	private ScrollView sv_truck_info;
	private ScrollView sv_gallery;

	private Drawable btn_up, btn_down;

	private GalleryAdapter gAdapter;

	private Intent intent;

	private Button btn_personal_info;
	// private Button btn_truck_info;
	// private Button btn_gallery;
	private Button btn_commit;

	private TextView tv_truck_brand_number;
	private TextView tv_nick_name_detail;
	private TextView tv_phone_number_detail;
	private TextView tv_area_detail;
	private TextView tv_age_detail;
	private TextView tv_drive_age_detail;
	private TextView tv_drive_license_certificate_detail;
	private TextView tv_bind_phone_detail;
	private TextView tv_my_credit;
	private TextView tv_vip_grade;
	private TextView tv_birthday_detail;
	private ImageView iv_person_head/* ,iv_person_head_icon */;
	private ImageView iv_drive_license_certificate_detail;

	// 使用开源的webimageloader
	private DisplayImageOptions options;
	private String imageUrl;
	private ImageLoadingListener animateFirstListener;

	private boolean editStatus = false;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.GET_DRIVER_TRUCK_INFO_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				break;

			case Config.GET_PERSONAL_INFO_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.GetPersonalInfoResponse getPersonalInfoResponse = new Gson()
							.fromJson(msg.obj.toString().trim(),
									GsonResponse.GetPersonalInfoResponse.class);
					UserInfoPersist.personalInfo = getPersonalInfoResponse.data;

					tv_truck_brand_number
							.setText(UserInfoPersist.personalInfo.PLATE_NO);
					tv_nick_name_detail
							.setText(UserInfoPersist.personalInfo.NICK_NAME);
					tv_phone_number_detail.setText(UserInfoPersist.phoneNum);
					tv_area_detail
							.setText(UserInfoPersist.personalInfo.ADDRESS);
					tv_age_detail.setText(UserInfoPersist.personalInfo.AGE);
					tv_drive_age_detail
							.setText(UserInfoPersist.personalInfo.LICENSE_AGE);
					tv_drive_license_certificate_detail
							.setText(UserInfoPersist.personalInfo.LICENSE_AUTHENTICATED);
					iv_drive_license_certificate_detail = (ImageView) midView
							.findViewById(R.id.iv_drive_license_certificate_detail);

					tv_bind_phone_detail
							.setText(UserInfoPersist.personalInfo.MOBILE_BINDED);
					tv_my_credit
							.setText(UserInfoPersist.personalInfo.MEMBER_SCORE);
					tv_vip_grade
							.setText(UserInfoPersist.personalInfo.MEMBER_LEVEL);
					tv_birthday_detail
							.setText(UserInfoPersist.personalInfo.BORN_DATE);
					// iv_person_head=(ImageView)midView.findViewById(R.id.iv_person_head);
					// iv_person_head_icon=(ImageView)midView.findViewById(R.id.iv_person_head_icon);

					imageUrl = Config.REQUEST_URL+ UtilTools.returnImageurlSmall(UserInfoPersist.personalInfo.LOGO_FILE);
					imageLoader.displayImage(imageUrl, iv_person_head, options);
					// imageLoader.displayImage(imageUrl, iv_person_head_icon,
					// options);
					Log.e("ImageURL", imageUrl);
				}
				break;

			case Config.RESPONSE_TYPE_ERROR:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.HeadResponse headResponse = new Gson()
							.fromJson(msg.obj.toString().trim(),
									GsonResponse.HeadResponse.class);
					Toast.makeText(PersonalPageActivity.this,
							headResponse.status_text, Toast.LENGTH_LONG).show();
				}
				break;

			case Config.GET_GALLERY_LIST_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				break;

			case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(
						PersonalPageActivity.this,
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

		setBelowContentView(R.layout.personal_page_activity);
		setTitileName(R.string.personal_page);
		setRightButtonVisibility();
		setRightButtonText(R.string.edit_personinfo);

		imageLoader = ImageLoader.getInstance();
		animateFirstListener = new AnimateFirstDisplayListener();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_driver_logo)
				.showImageForEmptyUri(R.drawable.default_driver_logo)
				.showImageOnFail(R.drawable.default_driver_logo)
				.cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.NONE)
				// .displayer(new SimpleBitmapDisplayer())
//				.displayer(new RoundedBitmapDisplayer(10)) // 圆角图片
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new CircleBitmapDisplayer()) // 圆形图片
				.build();

		initView();
	}

	private void initView() {
		tv_truck_brand_number = (TextView) midView
				.findViewById(R.id.tv_truck_brand_number);
		tv_nick_name_detail = (TextView) midView
				.findViewById(R.id.tv_nick_name_detail);
		tv_phone_number_detail = (TextView) midView
				.findViewById(R.id.tv_phone_number_detail);
		tv_area_detail = (TextView) midView.findViewById(R.id.tv_area_detail);
		tv_age_detail = (TextView) midView.findViewById(R.id.tv_age_detail);
		tv_drive_age_detail = (TextView) midView
				.findViewById(R.id.tv_drive_age_detail);
		tv_drive_license_certificate_detail = (TextView) midView
				.findViewById(R.id.tv_drive_license_certificate_detail);
		tv_bind_phone_detail = (TextView) midView
				.findViewById(R.id.tv_bind_phone_detail);
		iv_person_head = (ImageView) midView.findViewById(R.id.iv_person_head);
		// iv_person_head_icon=(ImageView)midView.findViewById(R.id.iv_person_head_icon);

		imageUrl = Config.REQUEST_URL + UtilTools.returnImageurlSmall(UserInfoPersist.personalInfo.LOGO_FILE);
		imageLoader.displayImage(imageUrl, iv_person_head, options,
				animateFirstListener);

		// imageLoader.displayImage(imageUrl, iv_person_head_icon, options,
		// animateFirstListener);

		Log.e("ImageURL", imageUrl);

		tv_my_credit = (TextView) midView.findViewById(R.id.tv_my_credit);
		tv_vip_grade = (TextView) midView.findViewById(R.id.tv_vip_grade);
		tv_birthday_detail = (TextView) midView
				.findViewById(R.id.tv_birthday_detail);

		tv_truck_brand_number.setText(UserInfoPersist.personalInfo.PLATE_NO);
		tv_nick_name_detail.setText(UserInfoPersist.personalInfo.NICK_NAME);
		tv_phone_number_detail.setText(UserInfoPersist.phoneNum);
		tv_area_detail.setText(UserInfoPersist.personalInfo.ADDRESS);
		tv_age_detail.setText(UserInfoPersist.personalInfo.AGE);
		tv_drive_age_detail.setText(UserInfoPersist.personalInfo.LICENSE_AGE);
		tv_drive_license_certificate_detail
				.setText(UserInfoPersist.personalInfo.LICENSE_AUTHENTICATED);
		tv_bind_phone_detail
				.setText(UserInfoPersist.personalInfo.MOBILE_BINDED);
		tv_my_credit.setText(UserInfoPersist.personalInfo.MEMBER_SCORE);
		tv_vip_grade.setText(UserInfoPersist.personalInfo.MEMBER_LEVEL);
		tv_birthday_detail.setText(UserInfoPersist.personalInfo.BORN_DATE);

		sv_personal_info = (ScrollView) midView
				.findViewById(R.id.sv_personal_info);
		sv_truck_info = (ScrollView) midView.findViewById(R.id.sv_truck_info);
		sv_gallery = (ScrollView) midView.findViewById(R.id.sv_gallery);
		rl_personal_info = (RelativeLayout) midView
				.findViewById(R.id.rl_personal_info);
		rl_truck_info = (RelativeLayout) midView
				.findViewById(R.id.rl_truck_info);
		gv_gallery = (CVFanGridView) midView.findViewById(R.id.gv_gallery);
		rl_my_credit = (RelativeLayout) midView.findViewById(R.id.rl_my_credit);

		rl_my_credit.setOnClickListener(this);

		btn_up = getResources().getDrawable(R.drawable.head_img11_1);
		btn_up.setBounds(0, 0, btn_up.getMinimumWidth(),
				btn_up.getMinimumHeight());
		btn_down = getResources().getDrawable(R.drawable.head_img11);
		btn_down.setBounds(0, 0, btn_down.getMinimumWidth(),
				btn_down.getMinimumHeight());

		btn_personal_info = (Button) midView
				.findViewById(R.id.btn_personal_info);
		btn_personal_info.setTextColor(getResources()
				.getColor(R.color.blue_btn));

		// btn_personal_info.setOnClickListener(this);
		// btn_truck_info = (Button) midView.findViewById(R.id.btn_truck_info);
		// btn_truck_info.setOnClickListener(this);
		// btn_gallery = (Button) midView.findViewById(R.id.btn_gallery);
		// btn_gallery.setOnClickListener(this);

		btn_commit = (Button) midView.findViewById(R.id.btn_commit);
		if (editStatus) {
			btn_commit.setVisibility(View.VISIBLE);
		} else {
			btn_commit.setVisibility(View.GONE);
		}
		iv_drive_license_certificate_detail = (ImageView) midView
				.findViewById(R.id.iv_drive_license_certificate_detail);
		rl_drive_license_certificate = (RelativeLayout) midView
				.findViewById(R.id.rl_drive_license_certificate);

		rl_drive_license_certificate.setOnClickListener(this);
		rl_bind_phone = (RelativeLayout) midView
				.findViewById(R.id.rl_bind_phone);
		rl_bind_phone.setOnClickListener(this);

		btn_right.setOnClickListener(this);

		btn_personal_info.setCompoundDrawables(null, btn_down, null, null);
		// btn_truck_info.setCompoundDrawables(null, btn_up, null, null);
		// btn_gallery.setCompoundDrawables(null, btn_up, null, null);
	}

	@Override
	public void onResume() {
		super.onResume();
		myProgressDialog = WaittingDialog.showHintDialog(
				PersonalPageActivity.this, R.string.query);
		myProgressDialog.show();
		ClientRequest.getPersonalInfo(handler);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// case R.id.btn_personal_info:
		// btn_personal_info.setCompoundDrawables(null, btn_down, null, null);
		// btn_truck_info.setCompoundDrawables(null, btn_up, null, null);
		// btn_gallery.setCompoundDrawables(null, btn_up, null, null);
		//
		// btn_personal_info.setTextColor(getResources().getColor(
		// R.color.blue_btn));
		// btn_truck_info.setTextColor(getResources().getColor(R.color.text_color));
		// btn_gallery.setTextColor(getResources().getColor(R.color.text_color));
		//
		// sv_personal_info.setVisibility(View.VISIBLE);
		// sv_truck_info.setVisibility(View.GONE);
		// sv_gallery.setVisibility(View.GONE);
		// break;
		// case R.id.btn_truck_info:
		// btn_personal_info.setCompoundDrawables(null, btn_up, null, null);
		// btn_truck_info.setCompoundDrawables(null, btn_down, null, null);
		// btn_gallery.setCompoundDrawables(null, btn_up, null, null);
		//
		// btn_personal_info.setTextColor(getResources().getColor(
		// R.color.text_color));
		// btn_truck_info.setTextColor(getResources().getColor(
		// R.color.blue_btn));
		// btn_gallery.setTextColor(getResources()
		// .getColor(R.color.text_color));
		//
		// sv_personal_info.setVisibility(View.GONE);
		// sv_truck_info.setVisibility(View.VISIBLE);
		// sv_gallery.setVisibility(View.GONE);
		//
		// myProgressDialog = WaittingDialog.showHintDialog(
		// PersonalPageActivity.this, R.string.query);
		// myProgressDialog.show();
		// // ClientRequest.getTruckInfo(handler, "鄂J8989");
		// ClientRequest.getMyTruckInfo(handler, UserInfoPersist.ownerID);
		//
		// break;
		// case R.id.btn_gallery:
		// btn_personal_info.setCompoundDrawables(null, btn_up, null, null);
		// btn_truck_info.setCompoundDrawables(null, btn_up, null, null);
		// btn_gallery.setCompoundDrawables(null, btn_down, null, null);
		//
		// btn_personal_info.setTextColor(getResources().getColor(
		// R.color.text_color));
		// btn_truck_info.setTextColor(getResources().getColor(R.color.text_color));
		// btn_gallery.setTextColor(getResources().getColor(R.color.blue_btn));
		//
		// sv_personal_info.setVisibility(View.GONE);
		// sv_truck_info.setVisibility(View.GONE);
		// sv_gallery.setVisibility(View.VISIBLE);
		//
		// initGalleryAdapter();
		// myProgressDialog = WaittingDialog.showHintDialog(
		// PersonalPageActivity.this, R.string.query);
		// myProgressDialog.show();
		// ClientRequest.getUserGallery(handler, UserInfoPersist.userID);
		//
		// break;
		case R.id.rl_drive_license_certificate:
			intent = new Intent();
			if(UserInfoPersist.personalInfo.LICENSE_AUTHENTICATED.equals("已认证"))
			{
				intent.setClass(PersonalPageActivity.this,
						DriveLicenseCertificationInfoActivity.class);
			}
			else
			{
				intent.setClass(PersonalPageActivity.this,
						DriveLicenseCertificationActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.rl_bind_phone:
			intent = new Intent();
			if (UserInfoPersist.personalInfo.MOBILE_BINDED.equals("未绑定")) {
				intent.putExtra("bindedFlag", false);
			} else if (UserInfoPersist.personalInfo.MOBILE_BINDED.equals("已绑定")) {
				intent.putExtra("bindedFlag", true);
			}
			intent.setClass(PersonalPageActivity.this,
					MobilePhoneCertificationActivity.class);
			startActivity(intent);
			break;

		case R.id.rl_my_credit:
			// intent = new Intent();
			// intent.setClass(PersonalPageActivity.this,
			// MyCreditActivity.class);
			// startActivity(intent);
			break;

		case R.id.btn_right:
			intent = new Intent();
			intent.setClass(PersonalPageActivity.this,
					ModifyPersonalInfoActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void initGalleryAdapter() {
		gAdapter = new GalleryAdapter(this);
		gv_gallery.setAdapter(gAdapter);
	}

	@Override
	public void onPause() {
		imageLoader.cancelDisplayTask(iv_person_head);
		imageLoader.stop();
		super.onPause();
	}
}
