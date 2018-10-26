package com.coahr.cvfan.activity;

import com.coahr.cvfan.R;

import android.os.Bundle;

public class PushSettingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setBelowContentView(R.layout.activity_push_setting_layout);
        
        initUI();
    }
    
    private void initUI(){
        
        setBackButtonVisibility();
        setTitileName(R.string.push_setting);
        setRightButtonGone();
    }
}
