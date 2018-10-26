package com.coahr.cvfan.activity;

import com.coahr.cvfan.R;
import com.coahr.cvfan.util.UserInfoPersist;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ModifyNickNameActivity extends BaseActivity {
	private EditText tv_nickname_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setBelowContentView(R.layout.nickname_edit);
        
        setBackButtonVisibility();
        setTitileName(R.string.modify_nickname);
        initUI();
    }
    
    private void initUI(){
    	setRightButtonVisibility();
    	setRightButtonText(R.string.ok);
    	tv_nickname_edit = (EditText)midView.findViewById(R.id.tv_nickname_edit);
    	tv_nickname_edit.setText(UserInfoPersist.personalInfo.NICK_NAME);
    	btn_right.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
    	super.onClick(v);
    	switch (v.getId()) {
		case R.id.btn_right:
			UserInfoPersist.personalInfo.NICK_NAME = tv_nickname_edit.getText().toString();
			ModifyNickNameActivity.this.finish();
			break;

		default:
			break;
		}
    }
}
