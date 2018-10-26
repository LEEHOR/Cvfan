package com.coahr.cvfan.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coahr.cvfan.MainApplication;
import com.coahr.cvfan.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseActivity extends FragmentActivity implements OnClickListener {


    private TextView titleName,tv_return_msg;
    private Button backButton;
    protected RelativeLayout contentView;
    protected LayoutInflater layoutInflater;
    public Button btn_right;
    private RelativeLayout actionBar;
    
    protected ImageLoader imageLoader= ImageLoader.getInstance();;
    
    public ProgressDialog myProgressDialog;
    public CustomProgressDialog  progressDialog;

    // 中间部分view
    public View midView;

    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        MainApplication mApplication = MainApplication.getAppInstance();
        
        setContentView(R.layout.activity_base_layout);
        
        MainApplication.getAppInstance().addActivity(this);

        contentView = (RelativeLayout) findViewById(R.id.rl_content);
        titleName = (TextView) findViewById(R.id.tv_title);
        tv_return_msg=(TextView)findViewById(R.id.tv_return_msg);
        backButton = (Button) findViewById(R.id.btn_back);
        btn_right = (Button) findViewById(R.id.btn_right);
        actionBar = (RelativeLayout)findViewById(R.id.rl_actionbar);

        backButton.setOnClickListener(this);
        tv_return_msg.setOnClickListener(this);
        //btn_right.setOnClickListener(this);

    }
    
    public void setTitleBarbg(int resID){
        actionBar.setBackgroundColor(getResources().getColor(resID));
    }

    // 点击事件监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_back:// 返回按钮
            this.finish();
            break;
        /*case R.id.btn_right:// 跳转至设置页面
            intent = new Intent(settings_action);
            startActivity(intent);
            break;*/
        case R.id.tv_return_msg:// 返回
            this.finish();
            break;
        default:
            break;
        }
    }

    // 设置页面标题
    public void setTitileName(int resID) {
        titleName.setText(getResources().getString(resID));
    }
    // 设置页面标题
    public void setTitileName(String resID) {
        titleName.setText(resID);
    }

    // 设置返回按钮显示
    public void setBackButtonVisibility() {
    	tv_return_msg.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
    }

    // 设置返回按钮不显示
    public void setBackButtonGone() {
    	tv_return_msg.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
    }

    // 设置设置按钮显示
    public void setRightButtonVisibility() {
        btn_right.setVisibility(View.VISIBLE);
    }

    // 设置设置按钮不显示
    public void setRightButtonGone() {
        btn_right.setVisibility(View.GONE);
    }
    
    //设置标题栏右侧按钮文字
    public void setRightButtonText(int resID){
        btn_right.setText(resID);
    }

    // 加载Title下的布局
    public void setBelowContentView(int resID) {
        contentView.removeAllViews();
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(this);
        }
        midView = layoutInflater.inflate(resID, null);
        /*contentView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));*/
        contentView.addView(midView);
    }
    
    public void setBelowContentView(View view) {
        contentView.removeAllViews();
        if (view == null) {
            return;
        }else{
            midView = view;
            contentView.addView(midView);
        }
        /*contentView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));*/
    }
}
