package com.coahr.cvfan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class RegisterAcvitity extends BaseActivity {
    
    private EditText et_phone_number;
    private EditText et_verification_code;
    private Button   btn_gain_verification_code;
    private EditText et_login_account;
    private EditText et_password;
    private EditText et_repeat_password;
    private TextView tv_reg_cluse;
    private Button btn_reg;
    private Intent intent ;    
    private String mobileNo;
    private TimeCount time;
    private String username;
    private String password;
    
    
    
    Handler handler = new Handler() {
        @Override
		public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case Config.CDOE_VALIDATION_RESPONSE_TYPE:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) {
                    GsonResponse.ForgetPasswordResponse codeResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.ForgetPasswordResponse.class);
                    if (codeResponse.status_code.equals("0")) {
                       myProgressDialog = WaittingDialog.showHintDialog(
                                RegisterAcvitity.this, R.string.loginning);
                       myProgressDialog.show();
                       ClientRequest.userRegister(handler, username, password,
                    		   mobileNo);
                    }
                    else
                    {
                    	Toast.makeText(RegisterAcvitity.this, codeResponse.status_text,
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case Config.USER_REGISTER_RESPONSE_TYPE:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) {
                    GsonResponse.UserAccountResponse registerResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.UserAccountResponse.class);
                    UserInfoPersist.userID = registerResponse.data.userId;
                    UserInfoPersist.driverId = registerResponse.data.userId;
                    
                    UserInfoPersist.ownerID = registerResponse.data.ownerId;
                    UserInfoPersist.ownerRole = registerResponse.data.ownerRole;
                    UserInfoPersist.phoneNum = registerResponse.data.owner.mobileNo;
                    intent.setClass(RegisterAcvitity.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            case Config.RESPONSE_TYPE_ERROR:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(RegisterAcvitity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
                break;
            case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(RegisterAcvitity.this, getResources().getString(R.string.netconnect_exception),
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
        setBelowContentView(R.layout.register_activity);
        setTitileName(R.string.register);
        setRightButtonText(R.string.login);
        initView();
    }
    
    private void initView(){
    	 et_login_account = (EditText) midView.findViewById(R.id.et_nick_name);
         et_password = (EditText) midView.findViewById(R.id.et_password);
         et_repeat_password = (EditText) midView.findViewById(R.id.et_repeat_password);

         tv_reg_cluse = (TextView) midView.findViewById(R.id.tv_reg_cluse);
         tv_reg_cluse.setOnClickListener(this);

         btn_reg = (Button) midView.findViewById(R.id.btn_reg);
         btn_reg.setOnClickListener(this);
         btn_right.setOnClickListener(this);
         
        et_phone_number = (EditText)midView.findViewById(R.id.et_phone_number);
        
        et_verification_code = (EditText)midView.findViewById(R.id.et_verification_code);
        
        btn_gain_verification_code = (Button)midView.findViewById(R.id.btn_gain_verification_code);
        btn_gain_verification_code.setOnClickListener(this);
        
        btn_right.setOnClickListener(this);
        time=new TimeCount(60000, 1000);
    }
    
    @Override
    public void onClick(View v) {
        intent = new Intent();
        super.onClick(v);
        switch (v.getId()) {
        case R.id.btn_reg:// 注册成功跳转至登录页面
        	
        	username = et_login_account.getText().toString().trim();
            password = et_password.getText().toString().trim();
            String password_repeat = et_repeat_password.getText().toString()
                    .trim();
            mobileNo = et_phone_number.getText().toString().trim();
            String vcode = et_verification_code.getText().toString().trim();
            if(mobileNo.isEmpty())
            {
            	 Toast.makeText(RegisterAcvitity.this, "请您输入手机号",
                         Toast.LENGTH_LONG).show();
            	 break;
            }
            else if(vcode.isEmpty())
            {
            	Toast.makeText(RegisterAcvitity.this, "验证码不能为空",
                        Toast.LENGTH_LONG).show();
            	break;
            }
            else if(username.isEmpty())
            {
            	Toast.makeText(RegisterAcvitity.this, "登陆账号不能为空",
                        Toast.LENGTH_LONG).show();
            	break;
            }
            else if(password.isEmpty())
            {
            	Toast.makeText(RegisterAcvitity.this, "密码不能为空",
                        Toast.LENGTH_LONG).show();
            	break;
            }
            else if(password.length()<6)
            {
            	Toast.makeText(RegisterAcvitity.this, "密码长度至少为6位及以上",
                        Toast.LENGTH_LONG).show();
            	break;
            }
            else if(!password.equals(password_repeat))
            {
            	Toast.makeText(RegisterAcvitity.this, "两次密码不一致",
                        Toast.LENGTH_LONG).show();
            	break;
            }
            else
            {
            	myProgressDialog = WaittingDialog.showHintDialog(
            			RegisterAcvitity.this, R.string.comitting);
            	myProgressDialog.show();
            	ClientRequest.validateSendCode(handler, mobileNo, vcode);
            }
            break;
        case R.id.tv_reg_cluse:// 注册条款
            intent = new Intent(RegisterAcvitity.this,
                    ClauseActivity.class);
            startActivity(intent);
            break;
       
        case R.id.btn_gain_verification_code://获取验证码
        	String phone_number = et_phone_number.getText().toString().trim();
        	if (!phone_number.isEmpty()) {
        		time.start();
				ClientRequest.gainVerificationCode(handler, phone_number);
			}
        	else
        	{
        		 Toast.makeText(RegisterAcvitity.this, "请您输入手机号",
                         Toast.LENGTH_LONG).show();
        	}
			break;
        case R.id.btn_right:// 跳转至登录页面
            intent = new Intent(RegisterAcvitity.this, LoginActivity.class);
            startActivity(intent);
            break;
        default:
            break;
        }
    }
    
    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer 
    {
	    public TimeCount(long millisInFuture, long countDownInterval) {
	    super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
	    }
	
		@Override
		public void onFinish() {
			btn_gain_verification_code.setText("重新获取");
			btn_gain_verification_code.setClickable(true);
		}
	
		@Override
		public void onTick(long millisUntilFinished) {
			btn_gain_verification_code.setClickable(false);
			btn_gain_verification_code.setText(millisUntilFinished /1000+"秒");
		}
   }
}
