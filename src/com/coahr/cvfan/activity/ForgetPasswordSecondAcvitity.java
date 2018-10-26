package com.coahr.cvfan.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.MainApplication;
import com.coahr.cvfan.R;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class ForgetPasswordSecondAcvitity extends BaseActivity {

    private EditText et_password;
    private EditText et_repeat_password;
    private Button btn_next;
    private String userId;
    Intent intent;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case Config.RESET_PASSWORD_RESPONSE_TYP:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) 
                {
                	GsonResponse.HeadResponse headResponse = new Gson().fromJson(msg.obj.toString().trim(),GsonResponse.HeadResponse.class);
                    if (headResponse.status_code.equals("0")) {
                    	Toast.makeText(ForgetPasswordSecondAcvitity.this, "密码重置成功",
                                Toast.LENGTH_LONG).show();
                    	intent=new Intent();
                    	intent.setClass(ForgetPasswordSecondAcvitity.this, LoginActivity.class);
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
                        Toast.makeText(ForgetPasswordSecondAcvitity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
                break; 
            case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(ForgetPasswordSecondAcvitity.this, getResources().getString(R.string.netconnect_exception),
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
        
        setBelowContentView(R.layout.forget_password_second_activity);
        setTitileName(R.string.reset_password);
        intent=getIntent();
        userId=intent.getStringExtra("userId");
        initView();
    }

    private void initView() {
        et_password = (EditText) midView.findViewById(R.id.et_password);
        et_repeat_password = (EditText) midView
                .findViewById(R.id.et_repeat_password);


        btn_next = (Button) midView.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        btn_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        intent = new Intent();
        super.onClick(v);
        switch (v.getId()) {
        case R.id.btn_next:

            String password = et_password.getText().toString().trim();
            String password_repeat = et_repeat_password.getText().toString()
                    .trim();
            if(password.isEmpty())
            {
            	Toast.makeText(ForgetPasswordSecondAcvitity.this, "密码不能为空",
                        Toast.LENGTH_LONG).show();
            	break;
            }
            else if(password.length()<6)
            {
            	Toast.makeText(ForgetPasswordSecondAcvitity.this, "密码长度至少应为6位以上",
                        Toast.LENGTH_LONG).show();
            	break;
            }
            else if(!password.equals(password_repeat))
            {
            	Toast.makeText(ForgetPasswordSecondAcvitity.this, "两次密码不一致",
                        Toast.LENGTH_LONG).show();
            	break;
            }
            else
            {
            	myProgressDialog = WaittingDialog.showHintDialog(
            			ForgetPasswordSecondAcvitity.this, R.string.loginning);
            	myProgressDialog.show();
            	ClientRequest.resetPassword(handler, userId, password);
            }
            break;
            
        default:
            break;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
    		final Dialog dialog = new AlertDialog.Builder(ForgetPasswordSecondAcvitity.this).create();
		       // 显示对话框
  		   dialog.show();
			   Window window = dialog.getWindow();
			   window.setContentView(R.layout.dialog);
		        // 为确认按钮添加事件,执行退出应用操作
			   TextView msg=(TextView)window.findViewById(R.id.tv_promote_msg);
			   msg.setText("确定放弃密码重置？");
			   Button ok = (Button) window.findViewById(R.id.dialog_button_ok);
			   ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					intent=new Intent();
                	intent.setClass(ForgetPasswordSecondAcvitity.this, LoginActivity.class);
                	startActivity(intent);
                	finish();
				}
			});
			Button  cancel=(Button) window.findViewById(R.id.dialog_button_cancel);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
