package com.coahr.cvfan.activity;

import android.os.Bundle;

import com.coahr.cvfan.R;

public class CreditRuleActivity extends BaseActivity{
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setBelowContentView(R.layout.credit_rule);
        
        setBackButtonVisibility();
        setTitileName(R.string.credit_rule);
        setRightButtonGone();
        
    }

}
