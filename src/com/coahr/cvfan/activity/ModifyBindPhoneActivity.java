package com.coahr.cvfan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class ModifyBindPhoneActivity extends BaseActivity {

    private EditText et_phone_number;
    private EditText et_verification_code;
    private Button btn_gain_verification_code;
    private Button btn_modify_phone_ok;
    private String mobileNo;
    private TimeCount time;
    private Intent intent;


    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
           switch (msg.what) {
            case Config.CDOE_VALIDATION_RESPONSE_TYPE:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) {
                    GsonResponse.ForgetPasswordResponse codeResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.ForgetPasswordResponse.class);
                    if (codeResponse.status_code.equals("0")) {
                        Toast.makeText(ModifyBindPhoneActivity.this, codeResponse.status_text,
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case Config.BIND_PHONE_RESPONSE_TYPE:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                    if (headResponse.status_code.equals("0")) {
                        Toast.makeText(ModifyBindPhoneActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                        UserInfoPersist.phoneNum = mobileNo;
                        intent=new Intent();
                        intent.setClass(ModifyBindPhoneActivity.this, PersonalPageActivity.class);
                        startActivity(intent);
                    }
                }
                break;

            case Config.RESPONSE_TYPE_ERROR:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(ModifyBindPhoneActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
                break;
            case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(ModifyBindPhoneActivity.this, getResources().getString(R.string.netconnect_exception),
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

        setBelowContentView(R.layout.mobile_authentication);

        setBackButtonVisibility();
        setTitileName(R.string.modify_bind);
        setRightButtonGone();
        initUI();
    }

    private void initUI() {
        et_phone_number = (EditText) midView.findViewById(R.id.et_phone_number);
        et_verification_code = (EditText) midView
                .findViewById(R.id.et_verification_code);
        btn_gain_verification_code = (Button) midView
                .findViewById(R.id.btn_gain_verification_code);
        btn_gain_verification_code.setOnClickListener(this);
        btn_modify_phone_ok = (Button) midView
                .findViewById(R.id.btn_modify_phone_ok);
        btn_modify_phone_ok.setOnClickListener(this);
        time=new TimeCount(60000, 1000);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.btn_gain_verification_code:
        	String phone_number = et_phone_number.getText().toString().trim();
        	if (!phone_number.isEmpty()) {
        		time.start();
				ClientRequest.gainVerificationCode(handler, phone_number);
			}
        	else
        	{
        		 Toast.makeText(ModifyBindPhoneActivity.this, "请您输入手机号",
                         Toast.LENGTH_LONG).show();
        	}
			break;
        case R.id.btn_modify_phone_ok:
        	 mobileNo = et_phone_number.getText().toString().trim();
             String vcode = et_verification_code.getText().toString().trim();
             if(mobileNo.isEmpty())
             {
             	 Toast.makeText(ModifyBindPhoneActivity.this, "请您输入手机号",
                          Toast.LENGTH_LONG).show();
             	 break;
             }
             else if(vcode.isEmpty())
             {
             	Toast.makeText(ModifyBindPhoneActivity.this, "验证码不能为空",
                         Toast.LENGTH_LONG).show();
             	break;
             }
             else
             {
            	 myProgressDialog = WaittingDialog.showHintDialog(
                         ModifyBindPhoneActivity.this, R.string.comitting);
                 myProgressDialog.show();
                 ClientRequest.bindAndChangePhoneNumber(handler, mobileNo, vcode);
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
