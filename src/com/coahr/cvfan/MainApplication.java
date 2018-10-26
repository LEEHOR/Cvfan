package com.coahr.cvfan;

import java.util.LinkedList;
import java.util.UUID;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.baidu.mapapi.SDKInitializer;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainApplication extends Application{
    private static MainApplication mInstance = null;
    private LinkedList<Activity> activityList = new LinkedList<Activity>(); 
    private LinkedList<Fragment> fragmentList = new LinkedList<Fragment>();
    
    private static UserInfoPersist userInfo;

	public static Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.EXIT_APP_FLAG: {
				exitApp();
				break;
			}
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();

		// ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
		SDKInitializer.initialize(this);

		// 创建默认的ImageLoader配置参数
		// ImageLoaderConfiguration configuration =
		// ImageLoaderConfiguration.createDefault(this);

		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);
		
		 //设置该CrashHandler为程序的默认处理器    
        /*UnCeHandler catchExcep = new UnCeHandler(this);  
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);  */ 

	}

	public static MainApplication getAppInstance() {
		getUUID();
		return mInstance = (mInstance != null ? mInstance
				: (new MainApplication()));
	}

	public static void getUUID() {
		userInfo = UserInfoPersist.getInstance();
		UserInfoPersist.uuid = UUID.randomUUID().toString().replace("-", "");
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	public void cleanAllActivity() {
		for (Activity activity : activityList) {
			if (activity != null) {
				activity.finish();
			}

		}
	}

	public void addFragment(Fragment fragment) {
		fragmentList.add(fragment);
	}

	public void removeFragment(Fragment fragment) {
		fragmentList.remove(fragment);
	}

	public void exit() {
		if (activityList != null && activityList.size() > 0) {
			for (Activity activity : activityList) {
				activity.finish();
			}
		}
	}

	private static void exitApp() {
		System.exit(0);
	}
	
	 /** 
     * 关闭Activity列表中的所有Activity*/  
    public void finishActivity(){  
        for (Activity activity : activityList) {    
            if (null != activity) {    
                activity.finish();    
            }    
        }  
        
        for (Fragment fragment : fragmentList) {
            if (null != fragmentList) {
            	fragment.onDestroy();  
            }    
        }
        
        //杀死该应用进程  
       android.os.Process.killProcess(android.os.Process.myPid());    
    }  
}
