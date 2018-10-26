package com.coahr.cvfan.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BaiduNaviManager;
import com.coahr.cvfan.R;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.NetworkStateManager;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class SplashActivity extends Activity {

	private static int SPLASH_DISPLAY_TIME = 3000;
	private static final int PGSTOP = 2;
	private SharedPreferences accountData;
	private boolean loginFlag;
	private boolean firstLogin;
	private String ownerRole;
	private String mobileNo;
	private String userId;

	private String versionNo;
	private String progressMsg;
	private String updateUrl;
	private long downLoadLength = 0;
	private long fileLength;
	private ProgressDialog downLoadProgressDialog;
	private Message msg;
	private int versionCode;
	private String versionName;

	public Intent intent;

	private static final String TAG = "SPLASH_ACTIVITY";

	private boolean mIsEngineInitSuccess = false;
	private boolean ifDownloadNewVersion = false;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.VERSION_UPDATE_RESPONSE_TYP:
				if (msg != null) {
					GsonResponse.VersionUpdateResponse versionUpdateResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.VersionUpdateResponse.class);
					if (versionUpdateResponse.data.message != null
							&& !"".equals(versionUpdateResponse.data.message)) {
						versionNo = versionUpdateResponse.data.versionNo;
						updateUrl = versionUpdateResponse.data.versionPath;
						progressMsg = versionUpdateResponse.data.message;

						if ("1".endsWith(versionUpdateResponse.data.required)) {
							// 可选升级
							jumpLogicCotrol();
							doNewVersionUpdate(versionNo, progressMsg);
						} else if ("2".endsWith(versionUpdateResponse.data.required)) {
							// 强制升级
							jumpLogicCotrol();
							justNewVersionShow(versionNo, progressMsg);
						} else {
							jumpLogicCommon();
						}
					} else {
						jumpLogicCommon();
					}
				}
				break;
			case Config.NET_CONNECT_EXCEPTION:
				jumpLogicCommon();
				break;

			case Config.RESPONSE_TYPE_ERROR:
				jumpLogicCommon();
				break;

			case PGSTOP:
				downLoadProgressDialog.dismiss();
				Toast.makeText(SplashActivity.this, "下载完成", Toast.LENGTH_LONG)
						.show();
				break;
			default:
				break;
			}
		};
	};

	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
		public void engineInitSuccess() {
			// 导航初始化是异步的，需要一小段时间，以这个标志来识别引擎是否初始化成功，为true时候才能发起导航
			mIsEngineInitSuccess = true;
			Log.e(TAG, "ENGIN INIT SUCCESS");
		}

		public void engineInitStart() {
			Log.e(TAG, "ENGIN INIT START");
		}

		public void engineInitFail() {
			Log.e(TAG, "ENGIN INIT FAIL");
		}
	};

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash_layout);

		// 初始化导航引擎
		// BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(),
		// mNaviEngineInitListener, ACCESS_KEY, mKeyVerifyListener);
		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(),
				mNaviEngineInitListener, new LBSAuthManagerListener() {
					@Override
					public void onAuthResult(int status, String msg) {
						String str = null;
						if (0 == status) {
							str = "key校验成功!";
						} else {
							str = "key校验失败, " + msg;
						}
						Log.e(TAG, str);
					}
				});

		accountData = getSharedPreferences(Config.ACCOUNT_DATA_PREFERENCE,
				MODE_PRIVATE);
		loginFlag = accountData.getBoolean(Config.LOGIN_FLAG, false);
		firstLogin = accountData.getBoolean(Config.FIRST_LOGIN, true);
		NetworkStateManager.instance().init(getApplicationContext());
		checkForUpdate();
	}

	private void checkForUpdate() {
		try {
			versionCode = getPackageManager().getPackageInfo("com.coahr.cvfan",
					0).versionCode;
			versionName = getPackageManager().getPackageInfo("com.coahr.cvfan",
					0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (versionCode != -1) {
			ClientRequest.versionUpdate(handler, versionName + "");
		} else {
			Toast.makeText(SplashActivity.this,
					getResources().getString(R.string.get_version_code_failed),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myUid());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void justNewVersionShow(String newVersion, String msg) {

		Dialog dialog = new AlertDialog.Builder(SplashActivity.this)
				.setTitle("软件更新").setMessage(msg)// 设置内容
				.setCancelable(false).setPositiveButton("确定",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								downFile(Config.REQUEST_URL + updateUrl);
							}
						}).create();// 创建
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		// 设置透明度为0.3
		lp.alpha = 0.6f;
		window.setAttributes(lp);
		// 显示对话框
		dialog.show();
	}

	private void doNewVersionUpdate(String newVersion, String msg) {
		Dialog dialog = new AlertDialog.Builder(SplashActivity.this)
				.setTitle("软件更新")
				.setMessage(msg)
				.setCancelable(false)
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								ifDownloadNewVersion = true;
								downFile(Config.REQUEST_URL + updateUrl);
							}
						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								dialog.dismiss();
								SplashActivity.this.startActivity(intent);
								SplashActivity.this.finish();
								ifDownloadNewVersion = true;
							}
						}).create();// 创建
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		// 设置透明度为0.3
		lp.alpha = 0.6f;
		window.setAttributes(lp);
		// 显示对话框
		dialog.show();
	}

	void downFile(final String url) {

		downLoadProgressDialog = new ProgressDialog(SplashActivity.this);
		downLoadProgressDialog.setTitle("正在下载更新");
		downLoadProgressDialog
				.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		downLoadProgressDialog.setCancelable(false);
		downLoadProgressDialog.setCanceledOnTouchOutside(false);
		downLoadProgressDialog.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;

				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					fileLength = entity.getContentLength();
					downLoadProgressDialog.setMax((int) fileLength);
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File fileDir = new File(Config.EXTERNAL_STORAGE_PATH);
						if (!fileDir.exists()) {
							fileDir.mkdir();
						}

						Log.e("apk restore path", Config.EXTERNAL_STORAGE_PATH
								+ "/" + Config.UPDATE_SAVENAME);
						File file = new File(Config.EXTERNAL_STORAGE_PATH + "/"
								+ Config.UPDATE_SAVENAME);
						if (file.exists()) {
							file.delete();
						}
						fileOutputStream = new FileOutputStream(file);
						// fileOutputStream =
						// getActivity().getApplicationContext().openFileOutput(file.getName(),getActivity().getApplicationContext().MODE_WORLD_READABLE
						// );
						Log.e("文件名", file.getName());
						byte[] buf = new byte[1024];
						int ch = -1;
						downLoadLength = 0;
						// Log.e("fileLength", fileLength + "");

						while ((ch = is.read(buf)) != -1) {
							// Log.e("ch", ch + "");
							// Log.e("download percent", downLoadLength * 100 /
							// fileLength + "");
							fileOutputStream.write(buf, 0, ch);
							downLoadLength += ch;
							downLoadProgressDialog
									.setProgress((int) downLoadLength);
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					msg = handler.obtainMessage(PGSTOP);
					handler.sendMessage(msg);
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void down() {
		handler.post(new Runnable() {
			public void run() {
				update();
			}
		});
	}

	void update() {
		chmod("777", Config.EXTERNAL_STORAGE_PATH);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(
				Config.EXTERNAL_STORAGE_PATH, Config.UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	// 设置文件夹权限
	void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void jumpLogicCommon() {
		if (firstLogin) {
			intent = new Intent(SplashActivity.this, FirstAnimActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (loginFlag) {
						ownerRole = accountData.getString(
								Config.ACCOUNT_OWNEROLE, "");
						mobileNo = accountData.getString(
								Config.ACCOUNT_MOBILENO, "");
						userId = accountData.getString(Config.ACCOUNT_USERID,
								"");
						UserInfoPersist.driverId = userId;
						UserInfoPersist.userID = userId;
						UserInfoPersist.ownerID = userId;
						UserInfoPersist.ownerRole = ownerRole;
						UserInfoPersist.phoneNum = mobileNo;
						/*intent = new Intent(SplashActivity.this,
								MainActivity.class);*/
					} else {
						/*intent = new Intent(SplashActivity.this,
								LoginActivity.class);*/
						
					}
					intent = new Intent(SplashActivity.this,
							MainActivity.class);
					SplashActivity.this.startActivity(intent);
					SplashActivity.this.finish();
				}
			}, SPLASH_DISPLAY_TIME);
		}
	}

	private void jumpLogicCotrol() {
		if (firstLogin) {
			intent = new Intent(SplashActivity.this, FirstAnimActivity.class);
		} else {
			intent = new Intent(SplashActivity.this, MainActivity.class);
			//不经过登录流程
			/*if (loginFlag) {
				ownerRole = accountData.getString(Config.ACCOUNT_OWNEROLE, "");
				mobileNo = accountData.getString(Config.ACCOUNT_MOBILENO, "");
				userId = accountData.getString(Config.ACCOUNT_USERID, "");
				UserInfoPersist.driverId = userId;
				UserInfoPersist.userID = userId;
				UserInfoPersist.ownerID = userId;
				UserInfoPersist.ownerRole = ownerRole;
				UserInfoPersist.phoneNum = mobileNo;
				intent = new Intent(SplashActivity.this, MainActivity.class);
			} else {
				intent = new Intent(SplashActivity.this, LoginActivity.class);
			}*/
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(ifDownloadNewVersion){
			intent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			this.finish();
		}
	}
}
