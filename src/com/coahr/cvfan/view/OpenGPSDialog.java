package com.coahr.cvfan.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.coahr.cvfan.R;

public class OpenGPSDialog extends AlertDialog implements OnClickListener {

    private Button btn_call_ok, btn_call_cancle;
    private Context context;

    public OpenGPSDialog(Context context) {
        super(context);
        this.context = context;
    }
    
    /*public OpenGPSDialog(Context context, String phoneNumber) {
        super(context);
        this.phoneNumber = phoneNumber;
        this.context = context;
    }*/

    public OpenGPSDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_gps_dialog);

        btn_call_ok = (Button) findViewById(R.id.btn_call_ok);
        btn_call_cancle = (Button) findViewById(R.id.btn_call_cancle);

        btn_call_ok.setOnClickListener(this);
        btn_call_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_call_ok:            
        	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);   
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
