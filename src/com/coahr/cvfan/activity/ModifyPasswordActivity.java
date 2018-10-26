package com.coahr.cvfan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class ModifyPasswordActivity extends BaseActivity {

	private Button btn_ok;

	private EditText et_old_password, et_new_password, et_new_password_repeat;
	private SharedPreferences accountData;
	private Intent intent;
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.MODIFY_PASSWORD_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg != null) {
					GsonResponse.ModifyPasswordResponse modifyPasswordResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.ModifyPasswordResponse.class);
					if (modifyPasswordResponse.status_code.equals("0")) {
						Toast.makeText(ModifyPasswordActivity.this, "修改密码成功",
								Toast.LENGTH_SHORT).show();
						intent = new Intent();
						accountData = getSharedPreferences(Config.ACCOUNT_DATA_PREFERENCE,
			                    Context.MODE_PRIVATE);
			            Editor editor = accountData.edit();
			            editor.putString(Config.ACCOUNT_USERNAME, "");
			            editor.putString(Config.ACCOUNT_PASSWORD, "");
			            editor.putString(Config.ACCOUNT_USERID, UserInfoPersist.userID);
			            editor.commit();
			        	
			        	intent.setClass(ModifyPasswordActivity.this, LoginActivity.class);
			            startActivity(intent);
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
                        Toast.makeText(ModifyPasswordActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;
			 case Config.NET_CONNECT_EXCEPTION:
					WaittingDialog.cancelHintDialog(myProgressDialog);
					Toast.makeText(ModifyPasswordActivity.this, getResources().getString(R.string.netconnect_exception),
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

		setBelowContentView(R.layout.activity_modify_password_layout);

		initUI();
	}

	private void initUI() {
		setBackButtonVisibility();
		setTitileName(R.string.modify_pwd);
		setRightButtonGone();

		btn_ok = (Button) midView.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);

		et_old_password = (EditText) midView.findViewById(R.id.et_old_password);
		et_new_password = (EditText) midView.findViewById(R.id.et_new_password);
		et_new_password_repeat = (EditText) midView
				.findViewById(R.id.et_new_password_repeat);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_ok:
			String oldPassWord = et_old_password.getText().toString().trim();
			String newPassword = et_new_password.getText().toString().trim();
			String newPasswordRepeat =et_new_password_repeat.getText().toString().trim();
			
			if(oldPassWord.isEmpty())
			{
				Toast.makeText(ModifyPasswordActivity.this, "旧密码不能为空",
						Toast.LENGTH_LONG).show();
				break;
			}
			
			if(newPassword.isEmpty())
			{
				Toast.makeText(ModifyPasswordActivity.this, "新码不能为空",
						Toast.LENGTH_LONG).show();
				break;
			}
			
			if(newPasswordRepeat.isEmpty())
			{
				Toast.makeText(ModifyPasswordActivity.this, "确认密码不能为空",
						Toast.LENGTH_LONG).show();
				break;
			}
			
			if ((oldPassWord != null) && (newPassword != null)
					&& (newPasswordRepeat != null)) {
				if (newPassword.equals(newPasswordRepeat)) {
					myProgressDialog = WaittingDialog.showHintDialog(
							ModifyPasswordActivity.this, R.string.modify_password);
					myProgressDialog.show();
					ClientRequest.modifyPassword(handler,
							UserInfoPersist.userID, oldPassWord, newPassword);
				}
				else
				{
					Toast.makeText(ModifyPasswordActivity.this, "两次输入密码输入不一致",
							Toast.LENGTH_LONG).show();
					break;
				}
			}
			break;

		default:
			break;
		}
	}
}
