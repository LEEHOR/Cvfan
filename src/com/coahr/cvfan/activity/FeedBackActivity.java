package com.coahr.cvfan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coahr.cvfan.R;
import com.coahr.cvfan.net.ClientRequest;
import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.UserInfoPersist;
import com.coahr.cvfan.view.WaittingDialog;
import com.google.gson.Gson;

public class FeedBackActivity extends BaseActivity {

	private EditText et_suggestion;
	private EditText et_contact;
	private Button btn_submit_suggestion;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Config.FEED_BACK_RESPONSE_TYPE:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg != null) {
                	GsonResponse.FeedBackResponse feedBackResponse = new Gson()
							.fromJson(msg.obj.toString(),
									GsonResponse.FeedBackResponse.class);
					if (feedBackResponse.status_code.equals("0")) {
						FeedBackActivity.this.finish();
						Toast.makeText(FeedBackActivity.this, getResources().getString(R.string.commit_succeed),
								Toast.LENGTH_LONG).show();
					}
				}
				break;
			case Config.RESPONSE_TYPE_ERROR:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				if (msg.obj != null) {
                    GsonResponse.HeadResponse headResponse = new Gson()
                            .fromJson(msg.obj.toString().trim(),
                                    GsonResponse.HeadResponse.class);
                        Toast.makeText(FeedBackActivity.this, headResponse.status_text,
                                Toast.LENGTH_LONG).show();
                }
				break;

			case Config.NET_CONNECT_EXCEPTION:
				WaittingDialog.cancelHintDialog(myProgressDialog);
				Toast.makeText(FeedBackActivity.this, getResources().getString(R.string.netconnect_exception),
	                     Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setBelowContentView(R.layout.activity_feed_back_layout);

		initUI();
	}

	private void initUI() {

		setBackButtonVisibility();
		setTitileName(R.string.feed_back_title);
		setRightButtonGone();

		et_suggestion = (EditText) midView.findViewById(R.id.et_suggestion);
		et_contact = (EditText) midView.findViewById(R.id.et_contact);
		btn_submit_suggestion = (Button) midView
				.findViewById(R.id.btn_submit_suggestion);

		btn_submit_suggestion.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_submit_suggestion:
			String content = et_suggestion.getText().toString().trim();
			String contact = et_contact.getText().toString().trim();
			if(content.isEmpty())
			{
				Toast.makeText(FeedBackActivity.this, "请输入您的建议",
						Toast.LENGTH_LONG).show();
				break;
			}
			myProgressDialog = WaittingDialog.showHintDialog(
					FeedBackActivity.this, R.string.comitting);
			myProgressDialog.show();
			if (content != null) {
				if (contact == null) {
					contact = UserInfoPersist.phoneNum;
				}
				ClientRequest.feedBack(handler, content, contact);
			}
			break;

		default:
			break;
		}
	}
}
