package com.coahr.cvfan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
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

public class ForgetPasswordStepOneAcvitity extends BaseActivity {
    
    private EditText et_phone_number;
    private EditText et_verification_code;
    private Button   btn_gain_verification_code;
    private Button   btn_next;
    private Intent intent ;    
    private String mobileNo;
    private String userId;
    private TimeCount time;
    
    Handler handler = new Handler() {
        @Override
		public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case Config.GAIN_VERIFITCATION_CODE_RESPONSE_TYPE:
                if (msg.obj != null) {
                	GsonResponse.ForgetPasswordResponse codeResponse = new Gson().fromJson(msg.obj.toString().trim(),GsonResponse.ForgetPasswordResponse.class);
                    if (codeResponse.status_code.equals("0")&&codeResponse.data!=null) {
                    	userId=codeResponse.data.userId;
                    	Log.e("userId", userId);
                    }
                    else
                    {
                    	 Toast.makeText(ForgetPasswordStepOneAcvitity.this, "未找到此手机号注册过的账号",
                                 Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case Config.RESPONSE_TYPE_ERROR:
                if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(ForgetPasswordStepOneAcvitity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
                break;
            case Config.NET_CONNECT_EXCEPTION:
				Toast.makeText(ForgetPasswordStepOneAcvitity.this, getResources().getString(R.string.netconnect_exception),
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
        
        setBelowContentView(R.layout.forget_password_first_activity);
        setTitileName(R.string.forget_password_title);
        initView();
    }
    
    private void initView(){
        et_phone_number = (EditText)midView.findViewById(R.id.et_phone_number);
        
        et_verification_code = (EditText)midView.findViewById(R.id.et_verification_code);
        
        btn_gain_verification_code = (Button)midView.findViewById(R.id.btn_gain_verification_code);
        btn_gain_verification_code.setOnClickListener(this);
        
        btn_next = (Button)midView.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        
        time=new TimeCount(60000, 1000);
    }
    
    @Override
    public void onClick(View v) {
        intent = new Intent();
        super.onClick(v);
        switch (v.getId()) {
        case R.id.btn_gain_verification_code://获取验证码
        	String phone_number = et_phone_number.getText().toString().trim();
        	if (!phone_number.isEmpty()) {
        		time.start();
				ClientRequest.gainVerificationCode(handler, phone_number);
			}
        	else
        	{
        		 Toast.makeText(ForgetPasswordStepOneAcvitity.this, "请您输入手机号",
                         Toast.LENGTH_LONG).show();
        	}
			break;
        case R.id.btn_next://下一步
            mobileNo = et_phone_number.getText().toString().trim();
            String vcode = et_verification_code.getText().toString().trim();
            if(mobileNo.isEmpty())
            {
            	 Toast.makeText(ForgetPasswordStepOneAcvitity.this, "请您输入手机号",
                         Toast.LENGTH_LONG).show();
            	 break;
            }
            else if(vcode.isEmpty())
            {
            	Toast.makeText(ForgetPasswordStepOneAcvitity.this, "验证码不能为空",
                        Toast.LENGTH_LONG).show();
            	break;
            }
            else if(userId==null)
            {
            	Log.e("userId", userId+"");
            	Toast.makeText(ForgetPasswordStepOneAcvitity.this, "未找到此手机号注册过的账号",
                        Toast.LENGTH_LONG).show();
            }
            else
            {
            	 intent = new Intent(ForgetPasswordStepOneAcvitity.this, ForgetPasswordSecondAcvitity.class);
                 intent.putExtra("userId", userId);
            	 startActivity(intent);
                 ForgetPasswordStepOneAcvitity.this.finish();
            }
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
			btn_gain_verification_code.setText("重新验证");
			btn_gain_verification_code.setClickable(true);
		}
	
		@Override
		public void onTick(long millisUntilFinished) {
			btn_gain_verification_code.setClickable(false);
			btn_gain_verification_code.setText(millisUntilFinished /1000+"秒");
		}
   }
}
