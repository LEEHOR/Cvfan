package com.coahr.cvfan.activity;

import android.os.Bundle;
import android.widget.Button;

import com.coahr.cvfan.R;

public class HomepageActivity extends BaseActivity {
    
    private Button pbutton1;
     
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBelowContentView(R.layout.personal_page_activity);
        setTitileName(R.string.personal_page);

        
    }
    
}
