package com.coahr.cvfan.fragment;

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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.activity.AboutUsActivity;
import com.coahr.cvfan.activity.FeedBackActivity;
import com.coahr.cvfan.activity.LoginActivity;
import com.coahr.cvfan.activity.ModifyPasswordActivity;
import com.coahr.cvfan.activity.PushSettingActivity;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.DataCleanManager;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SettingsFragment extends BaseFragment {

	private RelativeLayout rl_push_function;
	private RelativeLayout rl_modify_pwd;
	private RelativeLayout rl_clear_cash;
	private RelativeLayout rl_appraise_us;
	private RelativeLayout rl_about_us;
	private RelativeLayout rl_version_update;
	private Intent intent;
	private Button btn_logout;
	private SharedPreferences accountData;

	private static final int PGSTOP = 2;
	/*private static final int PGSTART = 0;
	private static final int PGRUNNING = 1;
	private static final int DOWNLOAD_FINISH = -1;

	private static final String RETURNSUCCESS = "end";
	private static final String RETURNERROR = "error";
	private static final String RETURNNOFILE = "nofiles";*/

	/*
	 * 版本升级
	 */
	private int versionCode;
	private String versionName;

	private String versionNo;
	private String progressMsg;
	private String updateUrl;
	private ProgressDialog progressDialog;
	private long downLoadLength = 0;
	private long fileLength;
	private Message msg;
	private TextView tv_version_code;

	/*private ImageLoadingListener animateFirstListener;
	private DisplayImageOptions options;*/
	protected ImageLoader imageLoader;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.VERSION_UPDATE_RESPONSE_TYP:
				WaittingDialog.cancelHintDialog(myProgressDialog);
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
							doNewVersionUpdate(versionNo, progressMsg);
						} else if ("2"
								.endsWith(versionUpdateResponse.data.required)) {
							// 强制升级
							justNewVersionShow(versionNo, progressMsg);
						} 
					}
					else
					{
						// 无需升级 0
						Toast.makeText(getActivity(), "您现在已经是最新版本",
								Toast.LENGTH_SHORT).show();
					}
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
			case PGSTOP:
				progressDialog.dismiss();
				Toast.makeText(getActivity(), "下载完成", Toast.LENGTH_LONG).show();
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
		initView();

		imageLoader = ImageLoader.getInstance();
		/*animateFirstListener = new AnimateFirstDisplayListener();

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_station_logo)
				.showImageForEmptyUri(R.drawable.default_station_logo)
				.showImageOnFail(R.drawable.default_station_logo)
				.cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.NONE)
				// .displayer(new SimpleBitmapDisplayer())
				// .displayer(new CircularBitmapDisplayer()) // 圆形图片
				.displayer(new RoundedBitmapDisplayer(10)) // 圆角图片
				.build();*/

		return baseView;
	}

	private void initView() {

		setBelowContentView(R.layout.setting_frag);
		setTitileName(R.string.setting);
		setRightButtonGone();

		rl_push_function = (RelativeLayout) midView
				.findViewById(R.id.rl_push_function);
		rl_push_function.setOnClickListener(this);
		rl_modify_pwd = (RelativeLayout) midView
				.findViewById(R.id.rl_modify_pwd);
		rl_modify_pwd.setOnClickListener(this);
		rl_clear_cash = (RelativeLayout) midView
				.findViewById(R.id.rl_clear_cash);
		rl_clear_cash.setOnClickListener(this);
		rl_appraise_us = (RelativeLayout) midView
				.findViewById(R.id.rl_appraise_us);
		rl_appraise_us.setOnClickListener(this);
		rl_about_us = (RelativeLayout) midView.findViewById(R.id.rl_about_us);
		rl_about_us.setOnClickListener(this);
		btn_logout = (Button) midView.findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(this);
		rl_version_update = (RelativeLayout) midView
				.findViewById(R.id.rl_version_update);
		rl_version_update.setOnClickListener(this);
		tv_version_code = (TextView) midView.findViewById(R.id.tv_version_code);
		try {
			tv_version_code.setText("卡车之友V"
					+ getActivity().getPackageManager().getPackageInfo(
							"com.coahr.cvfan", 0).versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		super.onClick(v);
		intent = new Intent();

		switch (v.getId()) {
		case R.id.rl_push_function:
			intent.setClass(getActivity(), PushSettingActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_modify_pwd:
			intent.setClass(getActivity(), ModifyPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_clear_cash:
			imageLoader.clearDiscCache();
			imageLoader.clearMemoryCache();
			cleanCash();
			Toast.makeText(getActivity(), "清除缓存成功", Toast.LENGTH_SHORT).show();
			break;
		case R.id.rl_appraise_us:
			intent.setClass(getActivity(), FeedBackActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_about_us:
			intent.setClass(getActivity(), AboutUsActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_logout:

			final Dialog dialog = new AlertDialog.Builder(getActivity())
					.create();
			// 显示对话框
			dialog.show();
			Window window = dialog.getWindow();
			window.setContentView(R.layout.dialog);
			// 为确认按钮添加事件,执行退出应用操作
			Button ok = (Button) window.findViewById(R.id.dialog_button_ok);
			ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// save account user name and password
					accountData = getActivity().getSharedPreferences(
							Config.ACCOUNT_DATA_PREFERENCE,
							Context.MODE_PRIVATE);
					Editor editor = accountData.edit();
					editor.putString(Config.ACCOUNT_USERNAME, "");
					editor.putString(Config.ACCOUNT_PASSWORD, "");
					editor.putString(Config.ACCOUNT_USERID,
							UserInfoPersist.userID);
					editor.putBoolean(Config.LOGIN_FLAG, false);
					editor.commit();
					intent.setClass(getActivity(), LoginActivity.class);
					startActivity(intent);
					ClientRequest.clearJseeionID();
					getActivity().finish();
				}
			});
			Button cancel = (Button) window
					.findViewById(R.id.dialog_button_cancel);
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			break;
		case R.id.rl_version_update:
			try {
				versionCode = getActivity().getPackageManager().getPackageInfo(
						"com.coahr.cvfan", 0).versionCode;
				versionName = getActivity().getPackageManager().getPackageInfo(
						"com.coahr.cvfan", 0).versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (versionCode != -1) {
				ClientRequest.versionUpdate(handler, versionName + "");
			} else {
				Toast.makeText(
						getActivity(),
						getResources().getString(
								R.string.get_version_code_failed),
						Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}

	private void cleanCash() {
		DataCleanManager.cleanDatabases(getActivity());
		DataCleanManager.cleanExternalCache(getActivity());
		DataCleanManager.cleanFiles(getActivity());
		DataCleanManager.cleanInternalCache(getActivity());
	}

	private void justNewVersionShow(String newVersion, String msg) {

		Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle("软件更新")
				.setMessage(msg)// 设置内容
				.setPositiveButton("确定",// 设置确定按钮
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
		Dialog dialog = new AlertDialog.Builder(getActivity())
				.setTitle("软件更新")
				.setMessage(msg)
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								downFile(Config.REQUEST_URL + updateUrl);
							}
						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								dialog.dismiss();
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

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setTitle("正在下载更新");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				// msg = handler.obtainMessage(PGSTART);
				// handler.sendMessage(msg);

				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					fileLength = entity.getContentLength();
					progressDialog.setMax((int) fileLength);
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
							progressDialog.setProgress((int) downLoadLength);
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
}
