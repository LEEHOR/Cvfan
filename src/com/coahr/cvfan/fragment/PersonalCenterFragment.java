package com.coahr.cvfan.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.activity.DriveLicenseCertificationActivity;
import com.coahr.cvfan.activity.LoginActivity;
import com.coahr.cvfan.activity.PersonalPageActivity;
import com.coahr.cvfan.listener.AnimateFirstDisplayListener;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.CircleBitmapDisplayer;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.util.UtilTools;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class PersonalCenterFragment extends BaseFragment {

	private RelativeLayout rl_personal_page;
	/*
	 * private RelativeLayout rl_personal_info; private RelativeLayout
	 * rl_personal_gallery;
	 */
	private RelativeLayout rl_my_credit;
	private RelativeLayout rl_vip_grade;
	private Button btn_identification;

	private ImageView iv_personal_fag_head;

	private TextView tv_personal_fag_credit;
	private TextView tv_personal_fag_level;

	// 使用开源的webimageloader
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener;
	private String imageUrl;
	private Intent intent;

	private SharedPreferences accountData;
	private Context context;
	private boolean isLogin = false;

	// private ProgressDialog myProgressDialog;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.GET_DRIVER_INFO_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				break;

			case Config.GET_PERSONAL_INFO_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.GetPersonalInfoResponse getPersonalInfoResponse = new Gson()
							.fromJson(msg.obj.toString().trim(),
									GsonResponse.GetPersonalInfoResponse.class);
					UserInfoPersist.personalInfo = getPersonalInfoResponse.data;

					imageUrl = Config.REQUEST_URL
							+ UtilTools
									.returnImageurlSmall(UserInfoPersist.personalInfo.LOGO_FILE);
					imageLoader.displayImage(imageUrl, iv_personal_fag_head,
							options, animateFirstListener);
				}
				break;

			case Config.RESPONSE_TYPE_ERROR:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
					GsonResponse.HeadResponse headResponse = new Gson()
							.fromJson(msg.obj.toString().trim(),
									GsonResponse.HeadResponse.class);
					Toast.makeText(getActivity(), headResponse.status_text,
							Toast.LENGTH_LONG).show();
				}
				break;
			case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(
						getActivity(),
						getResources().getString(R.string.netconnect_exception),
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		context = getActivity();
		accountData = context.getSharedPreferences(
				Config.ACCOUNT_DATA_PREFERENCE, getActivity().MODE_PRIVATE);

		isLogin = accountData.getBoolean(Config.LOGIN_FLAG, false);
		/*
		 * myProgressDialog = WaittingDialog.showHintDialog(getActivity(),
		 * R.string.query); myProgressDialog.show();
		 * 
		 * ClientRequest.getDriverInfo(handler, UserInfoPersist.userID);
		 */
		animateFirstListener = new AnimateFirstDisplayListener();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_driver_logo)
				.showImageForEmptyUri(R.drawable.default_driver_logo)
				.showImageOnFail(R.drawable.default_driver_logo)
				.cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new CircleBitmapDisplayer()) // 圆形图片
				// .displayer(new SimpleBitmapDisplayer())
				// .displayer(new RoundedBitmapDisplayer(10)) //圆角图片
				.build();
		initView();
		return baseView;
	}

	private void initView() {

		setBelowContentView(R.layout.personal_center_frag);
		setTitileName(R.string.personal_center);
		setRightButtonGone();

		iv_personal_fag_head = (ImageView) midView
				.findViewById(R.id.iv_personal_fag_head);

		tv_personal_fag_credit = (TextView) midView
				.findViewById(R.id.tv_personal_fag_credit);
		tv_personal_fag_level = (TextView) midView
				.findViewById(R.id.tv_personal_fag_level);

		rl_personal_page = (RelativeLayout) midView.findViewById(R.id.rl_personal_page);
		rl_my_credit = (RelativeLayout) midView.findViewById(R.id.rl_my_credit);
		rl_vip_grade = (RelativeLayout) midView.findViewById(R.id.rl_vip_grade);
		btn_identification = (Button) midView.findViewById(R.id.btn_identification);
		
		rl_personal_page.setOnClickListener(this);
		rl_my_credit.setOnClickListener(this);
		rl_vip_grade.setOnClickListener(this);
		// iv_personal_fag_head.setImageBitmap(UtilTools.returnBitMap(UserInfoPersist.headIconUrl));

		if (isLogin) {
			if (UserInfoPersist.personalInfo != null) {
				imageUrl = Config.REQUEST_URL + UtilTools.returnImageurlSmall(UserInfoPersist.personalInfo.LOGO_FILE);
			}
			imageLoader.displayImage(imageUrl, iv_personal_fag_head, options, animateFirstListener);
			
			if (UserInfoPersist.personalInfo != null) {
				tv_personal_fag_credit.setText(UserInfoPersist.personalInfo.MEMBER_SCORE + "");
				tv_personal_fag_level.setText(UserInfoPersist.personalInfo.MEMBER_LEVEL + "");
				btn_identification.setText("会员认证(" + UserInfoPersist.personalInfo.LICENSE_AUTHENTICATED + ")");
				
				if (UserInfoPersist.personalInfo.LICENSE_AUTHENTICATED.equals(getResources().getString(R.string.uncertificated))) {
					btn_identification.setOnClickListener(this);
				}
			} else {
				tv_personal_fag_credit.setText(0 + "");
				tv_personal_fag_level.setText(null);
			}
			
			/*
			   rl_personal_info = (RelativeLayout) midView.findViewById(R.id.rl_personal_info);
			   rl_personal_info.setOnClickListener(this); 
			   rl_personal_gallery = (RelativeLayout) midView.findViewById(R.id.rl_personal_gallery);
			   rl_personal_gallery.setOnClickListener(this);
			 */
		}else{
			String noLogin = getResources().getString(R.string.no_login);
			tv_personal_fag_credit.setText(noLogin);
			tv_personal_fag_level.setText(noLogin);
			btn_identification.setText(noLogin);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isLogin) {
			myProgressDialog = WaittingDialog.showHintDialog(getActivity(),
					R.string.query);
			myProgressDialog.show();
			ClientRequest.getPersonalInfo(handler);
		}
	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		intent = new Intent();
		switch (v.getId()) {
		case R.id.rl_personal_page:
			if (isLogin) {
				intent.setClass(getActivity(), PersonalPageActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(getActivity(), "暂未登录，请登录", Toast.LENGTH_LONG).show();
				
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.rl_my_credit:
			Toast.makeText(getActivity(), "尚未开放，尽请期待", Toast.LENGTH_LONG)
					.show();
			break;
		case R.id.rl_vip_grade:
			Toast.makeText(getActivity(), "尚未开放，尽请期待", Toast.LENGTH_LONG)
					.show();
			break;
		case R.id.btn_identification:
			intent.setClass(getActivity(),
					DriveLicenseCertificationActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onPause() {
		imageLoader.cancelDisplayTask(iv_personal_fag_head);
		imageLoader.stop();
		super.onPause();
	}
}
