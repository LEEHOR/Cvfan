package com.coahr.cvfan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.coahr.cvfan.R;

public class MyCreditActivity extends BaseActivity {
	
	private RelativeLayout rl_exchange_rule;
	private Intent intent;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setBelowContentView(R.layout.my_credit);
        
        setBackButtonVisibility();
        setTitileName(R.string.my_credit);
        setRightButtonGone();        
        initUI();
        
        
    }
    
    private void initUI(){
    	
    	 rl_exchange_rule = (RelativeLayout) midView.findViewById(R.id.rl_exchange_rule);
         rl_exchange_rule.setOnClickListener(this);
    	
    	
    }
        
    public void onClick(View v) {
         super.onClick(v);         

         switch (v.getId()) {
         
         case R.id.rl_exchange_rule:
             intent = new Intent(MyCreditActivity.this, CreditRuleActivity.class);             
             startActivity(intent);
             break;
             
         default:
        	 break;
         	
         	
            
            }
            
            
            
    }
}
