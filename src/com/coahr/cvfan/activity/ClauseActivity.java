package com.coahr.cvfan.activity;

import com.coahr.cvfan.R;

import android.os.Bundle;

public class ClauseActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        setBelowContentView(R.layout.clause);
        
        setBackButtonVisibility();
        setTitileName(R.string.register_protocal);
        setRightButtonGone();
    }
}
