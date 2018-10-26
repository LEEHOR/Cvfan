package com.coahr.cvfan.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coahr.cvfan.R;
import com.coahr.cvfan.activity.CustomProgressDialog;
import com.coahr.cvfan.slidingmenu.app.SlidingFragmentActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseFragment extends Fragment implements OnClickListener {

    private TextView titleName;
    private LinearLayout ll_btn_back;
    protected RelativeLayout contentView;
    protected LayoutInflater layoutInflater;
    public Button btn_right;
    public ProgressDialog myProgressDialog;
    public CustomProgressDialog  progressDialog;

    // 中间部分view
    public View midView;
    public View baseView;
    protected ImageLoader imageLoader=ImageLoader.getInstance();;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        baseView = inflater.inflate(R.layout.fragment_base_layout, null);
        initView();
        return baseView;
    }

    private void initView() {
        contentView = (RelativeLayout) baseView.findViewById(R.id.rl_content);
        titleName = (TextView) baseView.findViewById(R.id.tv_title);
        ll_btn_back = (LinearLayout) baseView.findViewById(R.id.ll_btn_back);
        btn_right = (Button) baseView.findViewById(R.id.btn_right);
        
        btn_right.setOnClickListener(this);
        ll_btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.ll_btn_back:
        	showMenu();
            break;

        default:
            break;
        }
    }    
    
    public void showMenu() {
        ((SlidingFragmentActivity) getActivity()).getSlidingMenu().showMenu();
        (new MenuFragment()).updateHead();
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
    	ll_btn_back.setVisibility(View.VISIBLE);
    }

    // 设置返回按钮不显示
    public void setBackButtonGone() {
    	ll_btn_back.setVisibility(View.GONE);
    }

    // 设置返回按钮背景图片
    public void setBackButtonBg(int resID) {
    	ll_btn_back.setBackgroundResource(resID);
    }

    // 设置右侧按钮显示
    public void setRightButtonVisibility() {
        btn_right.setVisibility(View.VISIBLE);
    }

    // 设置右侧按钮不显示
    public void setRightButtonGone() {
        btn_right.setVisibility(View.GONE);
    }

    // 设置右侧按钮背景图片
    public void setRightButtonBg(int resID) {
        btn_right.setBackgroundResource(resID);
    }

    // 设置标题栏右侧按钮文字
    public void setRightButtonText(int resID) {
        btn_right.setText(resID);
    }

    // 加载Title下的布局
    public void setBelowContentView(int resID) {
        if (contentView != null) {
            contentView.removeAllViews();
        } else {
            contentView = (RelativeLayout) baseView
                    .findViewById(R.id.rl_content);
        }
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(getActivity());
        }
        midView = layoutInflater.inflate(resID, null);

        contentView.addView(midView);
    }
}
