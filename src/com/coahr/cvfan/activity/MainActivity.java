package com.coahr.cvfan.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.coahr.cvfan.MainApplication;
import com.coahr.cvfan.R;
import com.coahr.cvfan.fragment.HomeFragment;
import com.coahr.cvfan.fragment.MenuFragment;
import com.coahr.cvfan.interfa.ChangeFrag;
import com.coahr.cvfan.slidingmenu.SlidingMenu;
import com.coahr.cvfan.slidingmenu.app.SlidingFragmentActivity;
import com.coahr.cvfan.util.Config;

public class MainActivity extends SlidingFragmentActivity implements ChangeFrag {

	private long exTime = 0L;
    private Fragment mContent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setBehindContentView(R.layout.menu_frame);
		//locationAction();
        setContentView(R.layout.layout_main);

        // check if the content frame contains the menu frame
        if (findViewById(R.id.menu_frame) == null) {
            setBehindContentView(R.layout.menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu()
                    .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            // add a dummy view
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

        // set the Above View Fragment
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        }

        if (mContent == null) {
            mContent = new HomeFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mContent).commit();

        // set the Behind View Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new MenuFragment()).commit();

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeEnabled(false);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.35f);
        sm.setMode(SlidingMenu.LEFT);

        // sm.setBackgroundImage(R.drawable.img_frame_background);
        sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, -canvas.getWidth() / 2,
                        canvas.getHeight() / 2);
            }
        });

        sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (1 - percentOpen * 0.25);
                canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
            }
        });
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
   /* private void locationAction() {
		locationClient = new LocationClient(getApplicationContext());
		// 设置定位条件
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型。国测局经纬度坐标系:gcj02   百度墨卡托坐标系:bd09   百度经纬度坐标系:bd09ll
		option.setProdName("cvfan"); // 设置产品线名称。
		option.setScanSpan(UPDATE_TIME); // 设置定时定位的时间间隔。单位毫秒
		option.setAddrType("all");
		locationClient.setLocOption(option);

		// 注册位置监听器
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null) {
					return;
				}
				StringBuffer sb = new StringBuffer(256);
				sb.append("Time : ");
				sb.append(location.getTime());
				sb.append("\nError code : ");
				sb.append(location.getLocType());
				sb.append("\nLatitude : ");
				sb.append(location.getLatitude());
				sb.append("\nLontitude : ");
				sb.append(location.getLongitude());
				sb.append("\nRadius : ");
				sb.append(location.getRadius());
				if (location.getLocType() == BDLocation.TypeGpsLocation) {
					sb.append("\nSpeed : ");
					sb.append(location.getSpeed());
					sb.append("\nSatellite : ");
					sb.append(location.getSatelliteNumber());
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
					sb.append("\nAddress : ");
					sb.append(location.getAddrStr());
				}
				LOCATION_COUTNS++;
				sb.append("\n检查位置更新次数：");
				sb.append(String.valueOf(LOCATION_COUTNS));

				Config.latitude = location.getLatitude() + "";
				Config.longitude = location.getLongitude() + "";

				ZLog.e(sb.toString() + "");
			}
		});

		if (locationClient == null) {
			return;
		}
		if (locationClient.isStarted()) {
			locationClient.stop();
		} else {
			locationClient.start();
			
			 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation(
			 * )后，每隔设定的时间，定位SDK就会进行一次定位。如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
			 * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
			 * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
			 
			locationClient.requestLocation();
		}
	}*/


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(
                R.id.content_frame, fragment);
    }

    /**
     * 显示内容
     */
    @Override
    public void showContent() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                getSlidingMenu().showContent();
            }
        }, 50);
    }

    /**
     * 显示侧滑菜单
     */
    public void showMenu() {
        getSlidingMenu().showMenu();
        (new MenuFragment()).updateHead();
    }

    @Override
    public void changeFragment(Fragment frag) {
        getSupportFragmentManager().beginTransaction().replace(
                R.id.content_frame, frag);
        showContent();
    }

    /**
     * 切换显示
     * 
     * @param fragment
     */
    public void switchContent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
        getSlidingMenu().showContent();
    }

    public void exit() {
    	
    	if((System.currentTimeMillis() - exTime) > 2000){
			Toast.makeText(this, getResources().getString(R.string.exit_hint), Toast.LENGTH_SHORT).show();
			exTime = System.currentTimeMillis();
		}else{
			this.finish();
			Message msg = MainApplication.mHandler.obtainMessage();
			msg.what = Config.EXIT_APP_FLAG;
			MainApplication.mHandler.sendMessage(msg);
		}
    }

    @Override
	public void onDestroy() {
		super.onDestroy();
		/*if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}*/
	}
}
