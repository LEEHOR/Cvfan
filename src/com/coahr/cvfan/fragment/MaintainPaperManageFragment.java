package com.coahr.cvfan.fragment;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.adapter.MaintainRecorderAdapter;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class MaintainPaperManageFragment extends BaseFragment {
    
    private ListView lv_maintain_recorder;
    private MaintainRecorderAdapter mrAdapter;
    private ProgressDialog myProgressDialog;
    private TextView tv_click_search_msg;
	private ImageView iv_no_data;
	private ImageView iv_click_search;
    
    private ArrayList<GsonResponse.MaintainPaperItem> maintainPapers;
    
    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case Config.GET_MAINTAIN_PAPER_LIST_RESPONSE_TYPE:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                 GsonResponse.MaintainPaperList maintainPaperList = new Gson().fromJson(msg.obj.toString().trim(),
                            GsonResponse.MaintainPaperList.class);
                 for (int i = 0; i < maintainPaperList.data.length; i++) {
                     maintainPapers.add(maintainPaperList.data[i]);
                }
                 
             	if(maintainPapers.size()<=0)
				{
					iv_no_data.setVisibility(View.VISIBLE);
					iv_click_search.setVisibility(View.GONE);
					lv_maintain_recorder.setVisibility(View.GONE);
					tv_click_search_msg.setVisibility(View.VISIBLE);
					tv_click_search_msg.setText("暂无数据");
				}
				else
				{
					iv_no_data.setVisibility(View.GONE);
					iv_click_search.setVisibility(View.GONE);
					tv_click_search_msg.setVisibility(View.GONE);
					lv_maintain_recorder.setVisibility(View.VISIBLE);
				}	
                mrAdapter.notifyDataSetChanged();
                break;
            case Config.RESPONSE_TYPE_ERROR:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(getActivity(), headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
                break;
            case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				lv_maintain_recorder.setVisibility(View.GONE);
				iv_no_data.setVisibility(View.GONE);
				tv_click_search_msg.setVisibility(View.VISIBLE);
				tv_click_search_msg.setText("点击刷新");
				iv_click_search.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), getResources().getString(R.string.netconnect_exception),
	                     Toast.LENGTH_LONG).show();
				break;
				
            default:
                break;
            }
        };
    };
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {        
        super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return baseView;
    }
    
    private void initView(){
        setBelowContentView(R.layout.maintain_paper_manage_frag);
        setTitileName(R.string.maintail_recorder);
        setRightButtonGone();
        
        maintainPapers = new ArrayList<GsonResponse.MaintainPaperItem>();
        
        iv_no_data=(ImageView)midView.findViewById(R.id.iv_no_data);
		iv_click_search=(ImageView)midView.findViewById(R.id.iv_click_search);
		iv_click_search.setOnClickListener(this);
		tv_click_search_msg=(TextView)midView.findViewById(R.id.tv_click_search_msg);
        lv_maintain_recorder = (ListView)midView.findViewById(R.id.lv_maintain_recorder);
        mrAdapter = new MaintainRecorderAdapter(getActivity(), maintainPapers);
        lv_maintain_recorder.setAdapter(mrAdapter);
        /*lv_maintain_recorder.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MaintainPaperActivity.class);
                startActivity(intent);
            }
        });*/
        
        myProgressDialog = WaittingDialog.showHintDialog(getActivity(), R.string.query);
        myProgressDialog.show();
        ClientRequest.getMaintainPaperList(handler, UserInfoPersist.userID);
    }
    
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.iv_click_search:
			myProgressDialog = WaittingDialog.showHintDialog(getActivity(), R.string.query);
		    myProgressDialog.show();
		    ClientRequest.getMaintainPaperList(handler, UserInfoPersist.userID);
			break;  
        default:
            break;
        }
    }
}
