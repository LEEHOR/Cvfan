package com.coahr.cvfan.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.coahr.cvfan.R;
import com.coahr.cvfan.util.Config;

public class FirstAnimActivity extends Activity{
	private ViewFlipper fliper;
	private float startX;
	private float clickY;
	private float endX;
	private int flag=1;
	private Animation rInAnim;
	private Animation rOutAnim;
	private Animation lInAnim;
	private Animation lOutAnim;
    private Intent intent;
    private int screenHeigh;
    private SharedPreferences accountData;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.activity_first_anim_layout);
		rInAnim = AnimationUtils.loadAnimation(this, R.anim.push_right_in);  // 向右滑动左侧进入的渐变效果（alpha  0.1 -> 1.0）  
        rOutAnim = AnimationUtils.loadAnimation(this, R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）  
    	lInAnim = AnimationUtils.loadAnimation(this, R.anim.push_left_in);       // 向左滑动左侧进入的渐变效果（alpha 0.1  -> 1.0）  
        lOutAnim = AnimationUtils.loadAnimation(this, R.anim.push_left_out);     // 向左滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）  
		fliper=(ViewFlipper)findViewById(R.id.viewfliper);
		accountData = getSharedPreferences(Config.ACCOUNT_DATA_PREFERENCE,
	                MODE_PRIVATE);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenHeigh = dm.heightPixels;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		flag=fliper.getDisplayedChild();
		//MotionEvent.ACTION_MOVE 移动事件
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			startX=event.getX();
			clickY=event.getY();
			
			if(flag==2)
			{
				if(clickY/screenHeigh>0.75)
				{
					Editor editor = accountData.edit();
					editor.putBoolean(Config.FIRST_LOGIN, false);
					editor.commit();
					//跳过登录流程
					//intent = new Intent(FirstAnimActivity.this, LoginActivity.class);
					intent = new Intent(FirstAnimActivity.this, MainActivity.class);
					FirstAnimActivity.this.startActivity(intent);
					FirstAnimActivity.this.finish();
				}
			}
		}
		else if(event.getAction()==MotionEvent.ACTION_UP)
		{
			endX=event.getX();
			if(startX-endX<=-120.0)//从左向右滑动（左进右出）
			{
				
				if(flag==1)
				{
					fliper.setInAnimation(rInAnim);  
					fliper.setOutAnimation(rOutAnim);  
					fliper.showPrevious();
				}
			}
			else if(startX-endX>=120.0)//从右向左滑动（右进左出）
			{
				if(flag==0||flag==1)
				{
					fliper.setInAnimation(lInAnim);  
					fliper.setOutAnimation(lOutAnim);  
					fliper.showNext();
				}
			}
			return true;
		}
		return false;
	}
}
