package com.coahr.cvfan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coahr.cvfan.R;
import com.coahr.cvfan.net.GsonResponse.AreaData;
import com.coahr.cvfan.net.GsonResponse.BrandType;
import com.coahr.cvfan.util.UserInfoPersist;

public class SearchStationActivity extends BaseActivity {

    private EditText et_search;
    private InputMethodManager imm;
    private Button btn_search;
    private TextView tv_province_city;
    private TextView tv_brand;
    private ImageView iv_delete_one,iv_delete_two,iv_clean_conten,iv_brand_direction,iv_area_direction;
    private RelativeLayout ll_search_brand;
    private RelativeLayout ll_search_city;
    private Intent intent;
    private String stationName;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBelowContentView(R.layout.search_station_frag);
		initUI();
	}
    
    private void initUI(){
    	setBackButtonVisibility();
		setTitileName(R.string.search_station);
		setRightButtonGone();
    
        ll_search_brand = (RelativeLayout)midView.findViewById(R.id.ll_search_brand);
        ll_search_brand.setOnClickListener(this);
        ll_search_city = (RelativeLayout)midView.findViewById(R.id.ll_search_city);
        ll_search_city.setOnClickListener(this);
        tv_province_city = (TextView)midView.findViewById(R.id.tv_province_city);
        tv_brand = (TextView)midView.findViewById(R.id.tv_brand);
        iv_brand_direction=(ImageView)midView.findViewById(R.id.iv_brand_direction);
        iv_area_direction=(ImageView)midView.findViewById(R.id.iv_area_direction);
        iv_delete_one=(ImageView)midView.findViewById(R.id.iv_delete_one);
        iv_delete_two=(ImageView)midView.findViewById(R.id.iv_delete_two);
        iv_clean_conten=(ImageView)midView.findViewById(R.id.iv_clean_conten);
        et_search = (EditText) midView.findViewById(R.id.et_search);
        
        iv_delete_one.setOnClickListener(this);
        iv_delete_two.setOnClickListener(this);
        iv_clean_conten.setOnClickListener(this);
        btn_search = (Button) midView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        
        et_search.setOnFocusChangeListener(new OnFocusChangeListener() {
            
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(!focus){
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
            }
        });
        
        et_search.addTextChangedListener(new TextWatcher (){

			@Override
			public void afterTextChanged(Editable arg0) {
				iv_clean_conten.setVisibility(View.VISIBLE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
        });
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	if (UserInfoPersist.choseProvince.LABEL != null && UserInfoPersist.choseCity.LABEL != null) {
			tv_province_city.setText(UserInfoPersist.choseProvince.LABEL + "" + " "
					+ UserInfoPersist.choseCity.LABEL + "");
			iv_area_direction.setVisibility(View.GONE);
    		iv_delete_one.setVisibility(View.VISIBLE);
		}
    	else if(UserInfoPersist.choseProvince.LABEL !=null)
    	{
    		iv_area_direction.setVisibility(View.GONE);
    		tv_province_city.setText(UserInfoPersist.choseProvince.LABEL + "");
    		iv_delete_one.setVisibility(View.VISIBLE);
    	}
    	else
    	{
			iv_area_direction.setVisibility(View.VISIBLE);
    	}
		if (UserInfoPersist.choseBrandData.label != null) {
			tv_brand.setText(UserInfoPersist.choseBrandData.label + "");
			iv_brand_direction.setVisibility(View.GONE);
			iv_delete_two.setVisibility(View.VISIBLE);
		}
		else
		{
			iv_brand_direction.setVisibility(View.VISIBLE);
		}
    }
    
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.btn_search:  
        	stationName = et_search.getText().toString().trim();
            intent = new Intent();
            intent.setClass(SearchStationActivity.this, SearchResultActivity.class);
            intent.putExtra("stationname", stationName);
            startActivity(intent);
            break;
        case R.id.ll_search_brand:
        	intent = new Intent();
        	intent.setClass(SearchStationActivity.this, BrandListActivity.class);
			startActivity(intent);
        	break;
        case R.id.ll_search_city:
        	intent = new Intent();
        	intent.setClass(SearchStationActivity.this, AreaChoseActivity.class);
			startActivity(intent);
        	break;
        case R.id.iv_delete_one:
        	tv_province_city.setText(null);
    		iv_delete_one.setVisibility(View.GONE);
    		iv_area_direction.setVisibility(View.VISIBLE);
        	UserInfoPersist.choseProvince=new AreaData();
        	UserInfoPersist.choseCity=new AreaData();
        	break;
        case R.id.iv_delete_two:
        	tv_brand.setText(null);
    		iv_delete_two.setVisibility(View.GONE);
    		iv_brand_direction.setVisibility(View.VISIBLE);
        	UserInfoPersist.choseBrandData = new BrandType();
        	break;
        case R.id.iv_clean_conten:
        	et_search.setText(null);
        	et_search.setHint(getResources().getString(R.string.input_station_name));
        	iv_clean_conten.setVisibility(View.GONE);
        	break;
        default:
            break;
        }
    }
    
    @Override
    protected void onDestroy() {
    	UserInfoPersist.choseProvince=new AreaData();
    	UserInfoPersist.choseCity=new AreaData();
    	super.onDestroy();
    }
}
