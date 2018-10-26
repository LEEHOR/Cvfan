package com.coahr.cvfan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.coahr.cvfan.R;
import com.coahr.cvfan.util.UserInfoPersist;

public class MobilePhoneCertificationActivity extends BaseActivity {
    
	private ImageView iv_binded_img;
    private Intent intent;
    
    private TextView tv_verified_num;
    private Button   btn_modify_binded;
    
    private boolean bindedFlag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setBelowContentView(R.layout.mobile_auth_edit);
        
        setBackButtonVisibility();
        setTitileName(R.string.phone_certification);
        setRightButtonGone();
        initUI();
    }

    private void initUI(){
    	intent=getIntent();
    	bindedFlag=intent.getBooleanExtra("bindedFlag", true);
    	btn_modify_binded = (Button)midView.findViewById(R.id.btn_modify_binded);
    	btn_modify_binded.setOnClickListener(this);
    	iv_binded_img=(ImageView)midView.findViewById(R.id.iv_binded_img);
        tv_verified_num = (TextView) midView.findViewById(R.id.tv_verified_num);
        if(bindedFlag)
        {
        	tv_verified_num.setText(UserInfoPersist.phoneNum);
        	iv_binded_img.setBackground(getResources().getDrawable(R.drawable.mobile_binded));
        }
        else
        {
        	iv_binded_img.setBackground(getResources().getDrawable(R.drawable.mobile_not_binded));
        	tv_verified_num.setText(null);
        	btn_modify_binded.setText("绑定手机");
        }
    }
    
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.btn_modify_binded:
            intent = new Intent();
            intent.setClass(MobilePhoneCertificationActivity.this, ModifyBindPhoneActivity.class);
            startActivity(intent);
            finish();
            break;

        default:
            break;
        }
    }
}
