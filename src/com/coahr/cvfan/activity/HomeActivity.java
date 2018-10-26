package com.coahr.cvfan.activity;

import com.coahr.cvfan.R;
import com.coahr.cvfan.slidingmenu.SlidingMenu;
import com.coahr.cvfan.slidingmenu.app.SlidingActivity;

import android.graphics.Canvas;
import android.os.Bundle;

@Deprecated
public class HomeActivity extends SlidingActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.layout_main);
        
        SlidingMenu sm = new SlidingMenu(this);
        sm.setMode(SlidingMenu.LEFT);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        sm.setMenu(R.layout.layout_menu);        
        
        sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, -canvas.getWidth() / 2,
                        canvas.getHeight() / 2);
            }
        });

        sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (1 - percentOpen * 0.25);
                canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
            }
        });
    }
}
