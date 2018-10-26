package com.coahr.cvfan.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.zipper.framwork.core.ZLog;

import com.coahr.cvfan.R;
import com.coahr.cvfan.adapter.AreaListAdapter;
import com.coahr.cvfan.adapter.CityListAdapter;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.net.GsonResponse.AreaData;
import com.coahr.cvfan.util.CircleBitmapDisplayer;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.DateUtils;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.util.UtilTools;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ModifyPersonalInfoActivity extends BaseActivity {

	private EditText et_nick_name_detail;
	private EditText et_detail_add;
	private RelativeLayout rl_birthday;
	private RelativeLayout rl_license_date;
	private RelativeLayout rl_area;
	private RelativeLayout rl_head_icon;
	private TextView tv_birthday_detail;
	private TextView tv_area_detail;
	private TextView tv_license_date_detail;
	private ImageView iv_head_icon;
	private Button btn_finish_detail_add;

	private Intent intent;

	private PopupWindow pw_photo;
	private PopupWindow province_pop;
	private PopupWindow city_pop;
	private PopupWindow area_pop;
	private PopupWindow detail_add_pop;
	private ListView areaChose;
	private Button btn_ok;
	private LayoutInflater inflater;

	private int year;
	private int month;
	private int day;
	private String headIcon;
	private String detailAddress;
	
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_GALLERY = 0;
	private final int IMAGE_CAMERA = 1;
	//private static int lastChoseCityPosition = -1;
	private static boolean areChoseFlag = false;

	private CityListAdapter plAdapter;
	private CityListAdapter cAdapter;
	private AreaListAdapter aAdapter;
	private ArrayList<GsonResponse.AreaData> provinceList = new ArrayList<GsonResponse.AreaData>();
	private ArrayList<GsonResponse.AreaData> cityList = new ArrayList<GsonResponse.AreaData>();
	private ArrayList<GsonResponse.AreaData> areaList = new ArrayList<GsonResponse.AreaData>();
	
	 // 使用开源的webimageloader
 	private DisplayImageOptions options;
 	private String imageUrl;
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.RESPONSE_TYPE_ERROR:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(ModifyPersonalInfoActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;

			case Config.GET_PROVINCE_LIST_RESPONSE_TYPE:
				if (msg != null) {
					GsonResponse.AreaListResponse areaListResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.AreaListResponse.class);
					if (areaListResponse.status_code.equals("0")) {
						ZLog.e("query Province list succeed!");
						if (areaListResponse.data != null) {
							provinceList.clear();
							for (int i = 0; i < areaListResponse.data.length; i++) {
								provinceList.add(areaListResponse.data[i]);
							}
							plAdapter.notifyDataSetChanged();
						}
					}
				}
				break;
			case Config.GET_CITY_LIST_RESPONSE_TYPE:
				if (msg != null) {
					GsonResponse.AreaListResponse areaListResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.AreaListResponse.class);
					if (areaListResponse.status_code.equals("0")) {
						ZLog.e("query City list succeed!");
						if (areaListResponse.data != null) {
							if (!areChoseFlag) {
								cityList.clear();
								// lastChoseCityPosition = -1;
								for (int i = 0; i < areaListResponse.data.length; i++) {
									cityList.add(areaListResponse.data[i]);
								}
								cAdapter.notifyDataSetChanged();
							} else {
								areaList.clear();
								// lastChoseCityPosition = -1;
								for (int i = 0; i < areaListResponse.data.length; i++) {
									areaList.add(areaListResponse.data[i]);
								}
								aAdapter.notifyDataSetChanged();
							}
						}
					}
				}
				break;
			case Config.MODIFY_PERSONAL_INFO_RESPONSE_TYPE:
				if (msg != null) {
					GsonResponse.ModifyPersonalInfoResponse modifyPersonalInfoResponse = new Gson()
							.fromJson(
									msg.obj.toString(),
									GsonResponse.ModifyPersonalInfoResponse.class);
					if (modifyPersonalInfoResponse.status_code.equals("0")) {
						ZLog.e("modify personal info succeed!");
						Toast.makeText(
								ModifyPersonalInfoActivity.this,
								getResources().getString(R.string.modify_personal_info_succeed),
								Toast.LENGTH_SHORT).show();
			            ModifyPersonalInfoActivity.this.finish();
					}
				}
				break;
			case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(ModifyPersonalInfoActivity.this, getResources().getString(R.string.netconnect_exception),
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

		setBelowContentView(R.layout.activity_modify_personal_info_layout);

		setBackButtonVisibility();
		setTitileName(R.string.edit_personinfo);
		
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_driver_logo)
				.showImageForEmptyUri(R.drawable.default_driver_logo)
				.showImageOnFail(R.drawable.default_driver_logo)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.NONE)
				.displayer(new CircleBitmapDisplayer()) // 圆形图片
				// .displayer(new SimpleBitmapDisplayer())
//					.displayer(new RoundedBitmapDisplayer(10)) //圆角图片
				.build();
		initUI();
	}

	private void initUI() {
		setRightButtonVisibility();
		setRightButtonText(R.string.finish);

		et_nick_name_detail = (EditText) midView
				.findViewById(R.id.et_nick_name_detail);
		rl_birthday = (RelativeLayout) midView.findViewById(R.id.rl_birthday);
		rl_license_date = (RelativeLayout) midView
				.findViewById(R.id.rl_license_date);
		rl_area = (RelativeLayout) midView.findViewById(R.id.rl_area);
		rl_head_icon = (RelativeLayout) midView.findViewById(R.id.rl_head_icon);
		tv_birthday_detail = (TextView) midView
				.findViewById(R.id.tv_birthday_detail);
		tv_license_date_detail = (TextView) midView
				.findViewById(R.id.tv_license_date_detail);
		iv_head_icon = (ImageView) midView.findViewById(R.id.iv_head_icon);
		
	 	imageUrl = Config.REQUEST_URL + UtilTools.returnImageurlSmall(UserInfoPersist.personalInfo.LOGO_FILE);
	    imageLoader.displayImage(imageUrl, iv_head_icon, options);
	    Log.e("ImageURL", imageUrl);
		
	    tv_area_detail = (TextView) midView.findViewById(R.id.tv_area_detail);
		
		et_nick_name_detail.setText(UserInfoPersist.personalInfo.NICK_NAME);
		tv_birthday_detail.setText(UserInfoPersist.personalInfo.BORN_DATE);
		tv_license_date_detail.setText(UserInfoPersist.personalInfo.LICENSE_DATE+"");
		tv_area_detail.setText(UserInfoPersist.personalInfo.ADDRESS);

		rl_area.setOnClickListener(this);
		rl_head_icon.setOnClickListener(this);

		initDatePickerData(UserInfoPersist.personalInfo.BORN_DATE);
		rl_birthday.setOnClickListener(this);
		rl_license_date.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_nick_name:
			intent = new Intent();
			intent.setClass(ModifyPersonalInfoActivity.this,
					ModifyNickNameActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_birthday:
			// 创建DatePickerDialog对象
			DatePickerDialog dpd = new DatePickerDialog(
					ModifyPersonalInfoActivity.this, birthdayListener, year,
					month, day);
			dpd.show();// 显示DatePickerDialog组件
			break;
		case R.id.rl_area:
			initProvinceChose();

			ClientRequest.getProvinceList(handler);
			province_pop.showAtLocation(findViewById(R.id.rl_modify_info),
					Gravity.CENTER, 0, 0);
			break;
		case R.id.rl_head_icon:
			initPhotoChoice();
			pw_photo.showAtLocation(findViewById(R.id.rl_modify_info),
					Gravity.BOTTOM, 0, 0);
			break;
		case R.id.btn_from_camera:
			dismissPop(pw_photo);
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, IMAGE_CAMERA);
			break;
		case R.id.btn_from_pictures:
			dismissPop(pw_photo);
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
			getAlbum.setType(IMAGE_TYPE);
			startActivityForResult(getAlbum, IMAGE_GALLERY);
			break;
		case R.id.btn_cancel:
			dismissPop(pw_photo);
			break;
		case R.id.iv_province_pop_close:
			UserInfoPersist.choseProvince = new AreaData();
			dismissPop(province_pop);
			break;
		case R.id.iv_city_pop_close:
			UserInfoPersist.choseProvince = new AreaData();
			UserInfoPersist.choseCity = new AreaData();
			dismissPop(city_pop);
			break;
		case R.id.btn_next_step:
			dismissPop(province_pop);
			initCityChose();
			areChoseFlag = false;
			ClientRequest
					.getCityList(handler, UserInfoPersist.choseProvince.ID);
			city_pop.showAtLocation(findViewById(R.id.rl_modify_info),
					Gravity.CENTER, 0, 0);
			break;
		case R.id.btn_next_step_city:
			dismissPop(city_pop);
			initAreaChose();
			areChoseFlag = true;
			ClientRequest.getCityList(handler, UserInfoPersist.choseCity.ID);
			area_pop.showAtLocation(findViewById(R.id.rl_modify_info),
					Gravity.CENTER, 0, 0);

			break;
		case R.id.iv_detail_add_pop_close:
			dismissPop(detail_add_pop);
			break;
		case R.id.btn_finish_detail_add:
			dismissPop(detail_add_pop);
			detailAddress = et_detail_add.getText().toString();
			UserInfoPersist.personalInfo.ADDRESS = UserInfoPersist.choseProvince.LABEL
					+ UserInfoPersist.choseCity.LABEL
					+ UserInfoPersist.choseArea.LABEL + detailAddress + "";
			tv_area_detail.setText(UserInfoPersist.personalInfo.ADDRESS);
			break;
		case R.id.btn_right:
			if(!checkDate(tv_birthday_detail.getText().toString()))
			{
				Toast.makeText(
						ModifyPersonalInfoActivity.this,"出生日期填写不规范",
						Toast.LENGTH_SHORT).show();
				break;
			}
			else if(!checkDate(tv_license_date_detail.getText().toString()))
			{
				Toast.makeText(
						ModifyPersonalInfoActivity.this,"驾照日期填写不规范",
						Toast.LENGTH_SHORT).show();
				break;
			}
			myProgressDialog = WaittingDialog.showHintDialog(
					ModifyPersonalInfoActivity.this, R.string.comitting);
			myProgressDialog.show();
			ClientRequest.modifyPersonalInfo(handler, headIcon,
					et_nick_name_detail.getText().toString(),
					tv_birthday_detail.getText().toString(),
					UserInfoPersist.choseProvince.LABEL,
					UserInfoPersist.choseCity.LABEL,
					UserInfoPersist.choseArea.LABEL, detailAddress,
					tv_license_date_detail.getText().toString());
			break;
		case R.id.btn_next_step_area:
			dismissPop(area_pop);
			initDetailAddress();
			detail_add_pop.showAtLocation(findViewById(R.id.rl_modify_info),
					Gravity.CENTER, 0, 0);

			break;
		case R.id.iv_area_pop_close:
			dismissPop(area_pop);
			break;
		case R.id.rl_license_date:
			initDatePickerData(UserInfoPersist.personalInfo.LICENSE_DATE);
			DatePickerDialog ldd = new DatePickerDialog(
					ModifyPersonalInfoActivity.this, licenseDateListener, year,
					month, day);
			ldd.show();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		et_nick_name_detail.setText(UserInfoPersist.personalInfo.NICK_NAME);
		tv_birthday_detail.setText(UserInfoPersist.personalInfo.BORN_DATE);
		tv_license_date_detail.setText(UserInfoPersist.personalInfo.LICENSE_DATE);
		tv_area_detail.setText(UserInfoPersist.personalInfo.ADDRESS);
	}

	private void initProvinceChose() {
		inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.province_choice_pop, null);
		province_pop = new PopupWindow(view, 600, 800, true);
		province_pop.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.del_dot_big));
		view.findViewById(R.id.iv_province_pop_close).setOnClickListener(this);
		view.findViewById(R.id.btn_next_step).setOnClickListener(this);
		areaChose = (ListView) view.findViewById(R.id.lv_province);
		plAdapter = new CityListAdapter(ModifyPersonalInfoActivity.this,
				provinceList);
		areaChose.setAdapter(plAdapter);
		areaChose.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				plAdapter.setSelectItem(position);
				plAdapter.notifyDataSetInvalidated();
				UserInfoPersist.choseProvince = provinceList.get(position);
			}
		});
	}

	private void initCityChose() {
		inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.city_choice_pop, null);
		city_pop = new PopupWindow(view, 600, 800, true);
		city_pop.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.del_dot_big));
		view.findViewById(R.id.iv_city_pop_close).setOnClickListener(this);
		btn_ok = (Button) view.findViewById(R.id.btn_next_step_city);
		btn_ok.setText(R.string.next_step);
		btn_ok.setOnClickListener(this);
		areaChose = (ListView) view.findViewById(R.id.lv_city);
		cAdapter = new CityListAdapter(ModifyPersonalInfoActivity.this,
				cityList);
		areaChose.setAdapter(cAdapter);
		areaChose.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserInfoPersist.choseCity = cityList.get(position);
				cAdapter.setSelectItem(position);
				cAdapter.notifyDataSetInvalidated();
			}
		});
	}

	private void initAreaChose() {
		inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.area_choice_pop, null);
		area_pop = new PopupWindow(view, 600, 800, true);
		area_pop.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.del_dot_big));
		view.findViewById(R.id.iv_area_pop_close).setOnClickListener(this);
		btn_ok = (Button) view.findViewById(R.id.btn_next_step_area);
		btn_ok.setText(R.string.finish);
		btn_ok.setOnClickListener(this);
		areaChose = (ListView) view.findViewById(R.id.lv_area);
		aAdapter = new AreaListAdapter(ModifyPersonalInfoActivity.this,
				areaList);
		areaChose.setAdapter(aAdapter);
		areaChose.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserInfoPersist.choseArea = areaList.get(position);
				aAdapter.setSelectItem(position);
				aAdapter.notifyDataSetInvalidated();
			}
		});
	}

	private void initDetailAddress() {
		inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.write_detail_address, null);
		detail_add_pop = new PopupWindow(view, 700, 350, true);
		detail_add_pop.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.del_dot_big));
		view.findViewById(R.id.iv_detail_add_pop_close)
				.setOnClickListener(this);
		btn_finish_detail_add = (Button) view
				.findViewById(R.id.btn_finish_detail_add);
		btn_finish_detail_add.setText(R.string.finish);
		btn_finish_detail_add.setOnClickListener(this);
		et_detail_add = (EditText) view.findViewById(R.id.et_detail_add);
	}

	private void initDatePickerData(String dateString) {
		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate;
		if (!dateString.isEmpty()) {
			mydate = DateUtils.stringToDate(dateString,
					DateUtils.DATE_FORMAT_NORMAL_1);
		} else{
			mydate = new Date();
		}
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期

		year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
		month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
		day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天
		//tv_birthday_detail.setText("" + year + "-" + (month + 1) + "-" + day); // 显示当前的年月日
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

	private void dismissPop(PopupWindow pop) {
		if (pop != null && pop.isShowing()) {
			pop.dismiss();
		}
	}

	private DatePickerDialog.OnDateSetListener birthdayListener = new DatePickerDialog.OnDateSetListener() {
		/**
		 * params：view：该事件关联的组件 params：myyear：当前选择的年 params：monthOfYear：当前选择的月
		 * params：dayOfMonth：当前选择的日
		 */
		@Override
		public void onDateSet(DatePicker view, int myyear, int monthOfYear,
				int dayOfMonth) {
			// 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
			year = myyear;
			month = monthOfYear;
			day = dayOfMonth;
			// 更新日期
			updateDate();
		}

		// 当DatePickerDialog关闭时，更新日期显示
		private void updateDate() {
			// 在TextView上显示日期
			tv_birthday_detail.setText(year + "-" + (month + 1) + "-" + day);
		}
	};

	private DatePickerDialog.OnDateSetListener licenseDateListener = new DatePickerDialog.OnDateSetListener() {
		/**
		 * params：view：该事件关联的组件 params：myyear：当前选择的年 params：monthOfYear：当前选择的月
		 * params：dayOfMonth：当前选择的日
		 */
		@Override
		public void onDateSet(DatePicker view, int myyear, int monthOfYear,
				int dayOfMonth) {
			// 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
			year = myyear;
			month = monthOfYear;
			day = dayOfMonth;
			// 更新日期
			updateDate();
		}

		// 当DatePickerDialog关闭时，更新日期显示
		private void updateDate() {
			// 在TextView上显示日期
			tv_license_date_detail
					.setText(year + "-" + (month + 1) + "-" + day);
		}
	};

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
				headIcon = cursor.getString(column_index);
				iv_head_icon.setImageBitmap(bm);
			} catch (IOException e) {
				Log.e("TAG-->Error", e.toString());
			}
			break;
		case IMAGE_CAMERA:
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.v("TestFile",
						"SD card is not avaiable/writeable right now.");
				Toast.makeText(ModifyPersonalInfoActivity.this, "SD card 未挂载",
						Toast.LENGTH_LONG).show();
				return;
			}
			Bitmap bitmap = (Bitmap) data.getExtras().get("data");
			FileOutputStream b = null;
			File file = new File(Config.LOCAL_IMAGE_URL);
			file.mkdirs();// 创建文件夹
			headIcon = Config.LOCAL_IMAGE_URL + "/"
					+ System.currentTimeMillis() + ".jpg";

			iv_head_icon.setImageBitmap(bitmap);

			try {
				b = new FileOutputStream(headIcon);
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
    	imageLoader.cancelDisplayTask(iv_head_icon);
    	imageLoader.stop();
    	super.onPause();
    }
    
    private  boolean checkDate(String date)
    {
    	String arr[];
    	Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
    	Date mydate=new Date();
    	int year,month,day;
    	mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
    	year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
    	month = mycalendar.get(Calendar.MONTH)+1;// 获取Calendar对象中的月
    	day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天
    	 
    	if(date!=null&&!"".equals(date))
    	{
    		arr=date.split("-");
    		if(Integer.parseInt(arr[0])>year||Integer.parseInt(arr[0])<1900)
    		{
    			return false;
    		}
    		else if(Integer.parseInt(arr[0])==year&&Integer.parseInt(arr[1])>month)
    		{
    			return false;
    		}
    		else if(Integer.parseInt(arr[0])==year&&Integer.parseInt(arr[1])==month&&Integer.parseInt(arr[2])>day)
    		{
    			return false;
    		}
    		return true;
    	}
    	return false;
    }
}
