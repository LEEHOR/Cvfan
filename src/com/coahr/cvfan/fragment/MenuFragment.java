package com.coahr.cvfan.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coahr.cvfan.R;
import com.coahr.cvfan.activity.MainActivity;
import com.coahr.cvfan.interfa.ChangeFrag;
import com.coahr.cvfan.listener.AnimateFirstDisplayListener;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.CircleBitmapDisplayer;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MenuFragment extends Fragment implements OnClickListener {

    ChangeFrag changeFrag;
    
    View view;

    RelativeLayout rl_head;
    RelativeLayout rl_personal_center;
    RelativeLayout rl_vehicle_manage;
    RelativeLayout rl_maintain_paper_manage;
    RelativeLayout rl_search_maintain_station;
    RelativeLayout rl_setting;
    private TextView  meau_nick_name;
    private ImageView meau_driver_logo; 
    private String imageUrl;

    
    // 使用开源的webimageloader
 	private DisplayImageOptions options1;
 	private ImageLoader imageLoader;
 	private ImageLoadingListener animateFirstListener;


    private SharedPreferences accountData;
	private Context context;
	private boolean isLogin = false;

    Handler handler = new Handler() {
	    public void handleMessage(android.os.Message msg) {
	        switch (msg.what) {
	        case Config.GET_PERSONAL_INFO_RESPONSE_TYPE:
	        	//去掉登录功能后引起的更新个人信息出错问题
	            /*if (msg.obj != null) {
	                GsonResponse.GetPersonalInfoResponse getPersonalInfoResponse = new Gson()
	                        .fromJson(msg.obj.toString().trim(), GsonResponse.GetPersonalInfoResponse.class);
	                UserInfoPersist.personalInfo = getPersonalInfoResponse.data; 
	                initUI();
	            }*/
	            break;
	        default:
	            break;
	        }
	    };
    };
 	
 	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
			
		context = getActivity();
		accountData = context.getSharedPreferences(
				Config.ACCOUNT_DATA_PREFERENCE, getActivity().MODE_PRIVATE);

		isLogin = accountData.getBoolean(Config.LOGIN_FLAG, false);

        view = inflater.inflate(R.layout.layout_menu, null);
        animateFirstListener = new AnimateFirstDisplayListener();
        imageLoader=ImageLoader.getInstance();
		options1 = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_driver_logo)
				.showImageForEmptyUri(R.drawable.default_driver_logo)
				.showImageOnFail(R.drawable.default_driver_logo)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new CircleBitmapDisplayer()) // 圆形图片
				// .displayer(new SimpleBitmapDisplayer())
//				.displayer(new RoundedBitmapDisplayer(10)) //圆角图片
				.build();
		ClientRequest.getPersonalInfo(handler);
        return view;
    }
    
    private void initUI(){
        changeFrag = new MainActivity();
        rl_head = (RelativeLayout) view.findViewById(R.id.rl_head);
        rl_head.setOnClickListener(this);
        meau_nick_name=(TextView)view.findViewById(R.id.meau_nick_name);
        if(UserInfoPersist.personalInfo!=null)
        {
        	meau_nick_name.setText(UserInfoPersist.personalInfo.NICK_NAME);
        	imageUrl = Config.REQUEST_URL + returnImageurlSmall(UserInfoPersist.personalInfo.LOGO_FILE);
        }
        meau_driver_logo=(ImageView)view.findViewById(R.id.meau_driver_logo);
        imageLoader.displayImage(imageUrl, meau_driver_logo, options1, animateFirstListener);
        
        rl_personal_center = (RelativeLayout) view
                .findViewById(R.id.rl_personal_cente);
        rl_personal_center.setOnClickListener(this);
        
        rl_vehicle_manage = (RelativeLayout) view
                .findViewById(R.id.rl_vehicle_manage);
        rl_vehicle_manage.setOnClickListener(this);
        
        rl_maintain_paper_manage = (RelativeLayout) view
                .findViewById(R.id.rl_maintain_paper_manage);
        rl_maintain_paper_manage.setOnClickListener(this);
        
        rl_search_maintain_station = (RelativeLayout) view
                .findViewById(R.id.rl_search_maintain_station);
        rl_search_maintain_station.setOnClickListener(this);
        
        rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        rl_setting.setOnClickListener(this);
    }

    public void updateHead() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.rl_head:
            switchFragment(new PersonalCenterFragment());
            break;
        case R.id.rl_personal_cente:
            switchFragment(new PersonalCenterFragment());
            break;
        case R.id.rl_vehicle_manage:
            switchFragment(new VehicleManageFragment());
            break;
        case R.id.rl_maintain_paper_manage:
            switchFragment(new MaintainPaperManageFragment());
            break;
        case R.id.rl_search_maintain_station:
            switchFragment(new HomeFragment());
            break;
        case R.id.rl_setting:
            switchFragment(new SettingsFragment());
            break;

        default:
            break;
        }
    }
    
    private void switchFragment(Fragment fragment) {
        if (getActivity() == null) {
            return;
        }
        if (getActivity() instanceof MainActivity) {
            MainActivity fca = (MainActivity) getActivity();
            fca.switchContent(fragment);
        }
    }
    @Override
    public void onResume() 
    {
    	super.onResume();
    	initUI();
    }
    @Override
    public void onStop() {
    	imageLoader.stop();
    	super.onStop();
    }
    
    public String returnImageurlSmall(String url)
    {
    	if(url==null||"".equals(url))
    	{
    		return "";
    	}
    	return url.replace(".", "-small.");
    }
}
