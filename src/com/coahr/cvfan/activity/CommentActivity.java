package com.coahr.cvfan.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.adapter.CommentAdapter;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class CommentActivity extends BaseActivity {

    private CommentAdapter cAdapter;

    private ListView lv_comment_list;

    private String stationID;

    private ArrayList<GsonResponse.StationCommentItem> commentList;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case Config.GET_STATION_COMMENT_LIST_RESPONSE_TYPE:
                WaittingDialog.cancelHintDialog(myProgressDialog);

                GsonResponse.GetStationCommentListResponse response = new Gson()
                        .fromJson(
                                msg.obj.toString().trim(),
                                GsonResponse.GetStationCommentListResponse.class);
                for (int i = 0; i < response.data.length; i++) {
                    commentList.add(response.data[i]);
                }
                cAdapter.notifyDataSetChanged();

                break;
            case Config.RESPONSE_TYPE_ERROR:
                WaittingDialog.cancelHintDialog(myProgressDialog);
                if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(CommentActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
                break;
        	case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(CommentActivity.this, getResources().getString(R.string.netconnect_exception),
	                     Toast.LENGTH_LONG).show();
				break;
				
            default:
                break;
            }
        };
    };

    private ProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBelowContentView(R.layout.exp_exchange);
        initUI();

    }

    private void initUI() {
        stationID = getIntent().getStringExtra("station_id");

        setBackButtonVisibility();
        setTitileName(R.string.friend_appraise);
        setRightButtonGone();
        
        commentList = new ArrayList<GsonResponse.StationCommentItem>();
        lv_comment_list = (ListView) midView.findViewById(R.id.lv_comment_list);
        cAdapter = new CommentAdapter(this, commentList);

        lv_comment_list.setAdapter(cAdapter);

        myProgressDialog = WaittingDialog.showHintDialog(CommentActivity.this,
                R.string.query);
        myProgressDialog.show();

        ClientRequest.getStationCommentList(handler, stationID, "0", "20");
    }
}
