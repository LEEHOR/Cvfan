package com.coahr.cvfan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.coahr.cvfan.MainApplication;
import com.coahr.cvfan.R;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.DesUtil;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class LoginActivity extends Activity{

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;
    //private TextView tv_error;
    private Intent intent;
    private TextView tv_forget_password;
    

    public ProgressDialog myProgressDialog;

    private SharedPreferences accountData;

    private String userName, password, userid;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case Config.LOGIN_RESPONSE_TYPE:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg != null) {
                    GsonResponse.UserAccountResponse loginResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.UserAccountResponse.class);
                    if (loginResponse.status_code.equals("0")) {

                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                        UserInfoPersist.driverId = loginResponse.data.userId;
                        UserInfoPersist.userID = loginResponse.data.userId;
                        UserInfoPersist.ownerID = loginResponse.data.ownerId;
                        UserInfoPersist.ownerRole = loginResponse.data.ownerRole;
                        UserInfoPersist.phoneNum = loginResponse.data.owner.mobileNo;

                        Toast.makeText(LoginActivity.this, "登录成功",
                                Toast.LENGTH_SHORT).show();
                        
                        //save account user name and password
                        Editor editor = accountData.edit();
                        
                        editor.putBoolean(Config.LOGIN_FLAG, true);
                        editor.putString(Config.ACCOUNT_USERNAME, userName);
                        editor.putString(Config.ACCOUNT_USERID, loginResponse.data.userId);
                        editor.putString(Config.ACCOUNT_OWNEROLE, loginResponse.data.ownerRole);
                        editor.putString(Config.ACCOUNT_MOBILENO, loginResponse.data.owner.mobileNo);
                        
                        try
                        {
                        	editor.putString(Config.ACCOUNT_PASSWORD, DesUtil.encrypt(password));
                        }
                        catch (Exception e) 
                        {
                        	Toast.makeText(LoginActivity.this, "密码异常",
                                    Toast.LENGTH_SHORT).show();
						}
                        editor.putString(Config.ACCOUNT_USERID, UserInfoPersist.userID);
                        editor.commit();
                        
                        LoginActivity.this.finish();
                        //ClientRequest.getDriverInfo(handler, MainApplication.userID);
                        
                    }
                }
                break;
            case Config.RESPONSE_TYPE_ERROR:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(LoginActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
                break;
            case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(LoginActivity.this, getResources().getString(R.string.netconnect_exception),
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
        MainApplication.getAppInstance().addActivity(this);
		setContentView(R.layout.login);
        initView();
    }

    private void initView() {
        intent = new Intent();
        accountData = getSharedPreferences(Config.ACCOUNT_DATA_PREFERENCE,
                MODE_PRIVATE);
        userName = accountData.getString(Config.ACCOUNT_USERNAME, "");
        if(accountData.getString(Config.ACCOUNT_PASSWORD, "")!=null && accountData.getString(Config.ACCOUNT_PASSWORD, "")!="")
        {
        	try
        	{
				password =  DesUtil.decrypt(accountData.getString(Config.ACCOUNT_PASSWORD, ""));
			} catch (Exception e) {
				Toast.makeText(LoginActivity.this, "密码异常",
                        Toast.LENGTH_SHORT).show();
			}
        }
        else
        {
        	password="";
        }
        
        if (!userName.equals("") && !password.equals("")) {
			myProgressDialog = WaittingDialog.showHintDialog(
					LoginActivity.this, R.string.loginning);
			myProgressDialog.show();
			
			ClientRequest.userLogin(handler, userName, password);
		}
        
		et_username = (EditText) this.findViewById(R.id.et_username);
        et_username.setText(userName);
        et_username.setSelection(userName.length());
        et_password = (EditText) this.findViewById(R.id.et_password);
        et_password.setText(password);
        et_password.setSelection(password.length());
        et_password.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_DONE)
				{
					 if (et_username.getText() != null && et_password.getText() != null) {
			                userName = et_username.getText().toString().trim();
			                password = et_password.getText().toString().trim();
			                if(userName.isEmpty())
			                {
			                	  Toast.makeText(LoginActivity.this, "登录账户不能为空",
			                              Toast.LENGTH_SHORT).show();
			                }
			                else if(password.isEmpty())
			                {
			                	 Toast.makeText(LoginActivity.this, "密码不能为空",
			                             Toast.LENGTH_SHORT).show();
			                }
			                else
			                {
			                	ClientRequest.clearJseeionID();
			                	myProgressDialog = WaittingDialog.showHintDialog(LoginActivity.this, R.string.loginning);
			                	myProgressDialog.show();
			                	ClientRequest.userLogin(handler, userName, password);
			                }
			                
			            }else{
			                Toast.makeText(LoginActivity.this, "登录账户名与密码不能为空", Toast.LENGTH_SHORT).show();
			            }
				}
				return false;
			}
		});
        tv_forget_password=(TextView)this.findViewById(R.id.tv_forget_password);
        
        tv_forget_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(LoginActivity.this,ForgetPasswordStepOneAcvitity.class);
	            startActivity(intent);
			}
		});
        
        
        btn_login = (Button) this.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 if (et_username.getText() != null && et_password.getText() != null) {
		                userName = et_username.getText().toString().trim();
		                password = et_password.getText().toString().trim();
		                if(userName.isEmpty())
		                {
		                	  Toast.makeText(LoginActivity.this, "登录账户不能为空",
		                              Toast.LENGTH_SHORT).show();
		                }
		                else if(password.isEmpty())
		                {
		                	 Toast.makeText(LoginActivity.this, "密码不能为空",
		                             Toast.LENGTH_SHORT).show();
		                }
		                else
		                {
		                	ClientRequest.clearJseeionID();
		                	myProgressDialog = WaittingDialog.showHintDialog(LoginActivity.this, R.string.loginning);
		                	myProgressDialog.show();
		                	ClientRequest.userLogin(handler, userName, password);
		                }
		                
		            }else{
		                Toast.makeText(LoginActivity.this, "登录账户名与密码不能为空", Toast.LENGTH_SHORT).show();
		            }
			}
		});
        btn_register=(Button)this.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(LoginActivity.this,
						RegisterAcvitity.class);
	            startActivity(intent);
			}
		});
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
    		/*final Dialog dialog = new AlertDialog.Builder(LoginActivity.this).create();
		       // 显示对话框
  		   dialog.show();
			   Window window = dialog.getWindow();
			   window.setContentView(R.layout.dialog);
		        // 为确认按钮添加事件,执行退出应用操作
			   TextView msg=(TextView)window.findViewById(R.id.tv_promote_msg);
			   msg.setText("确定退出应用？");
			   Button ok = (Button) window.findViewById(R.id.dialog_button_ok);
			   ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainApplication.getAppInstance().exit();
				}
			});
			Button  cancel=(Button) window.findViewById(R.id.dialog_button_cancel);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});*/
    		
    		LoginActivity.this.finish();
    		
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
}
