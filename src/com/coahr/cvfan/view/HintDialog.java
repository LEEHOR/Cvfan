package com.coahr.cvfan.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.coahr.cvfan.R;

public class HintDialog extends AlertDialog implements OnClickListener {

    private Button btn_call_ok, btn_call_cancle;
    private TextView tv_phone_number;
    
    private String phoneNumber;
    private Context context;

    public HintDialog(Context context) {
        super(context);
        this.context = context;
    }
    
    public HintDialog(Context context, String phoneNumber) {
        super(context);
        this.phoneNumber = phoneNumber;
        this.context = context;
    }

    public HintDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hint_dialog);

        btn_call_ok = (Button) findViewById(R.id.btn_call_ok);
        btn_call_cancle = (Button) findViewById(R.id.btn_call_cancle);
        tv_phone_number = (TextView)findViewById(R.id.tv_phone_number);
        tv_phone_number.setText(phoneNumber);

        btn_call_ok.setOnClickListener(this);
        btn_call_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_call_ok:            
            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + phoneNumber));  
            this.context.startActivity(intent);
            this.dismiss();
            break;

        case R.id.btn_call_cancle:
            this.dismiss();
            break;

        default:
            break;
        }
    }

}
