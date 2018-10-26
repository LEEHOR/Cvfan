package com.coahr.cvfan.activity;

import android.os.Bundle;

import com.coahr.cvfan.R;

public class AboutUsActivity extends BaseActivity {
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setBelowContentView(R.layout.activity_about_us);
	        initUI();

	    }

	    private void initUI() {

	        setBackButtonVisibility();
	        setTitileName(R.string.about_us);
	        setRightButtonGone();
	    }
}
