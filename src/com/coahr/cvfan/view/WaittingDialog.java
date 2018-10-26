package com.coahr.cvfan.view;

import com.coahr.cvfan.R;

import android.app.ProgressDialog;
import android.content.Context;

public class WaittingDialog {
    public static ProgressDialog showHintDialog(Context context, int message) {
        ProgressDialog myProgressDialog = new ProgressDialog(context);
        myProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        myProgressDialog.setTitle(R.string.hint_title);
        myProgressDialog.setMessage(context.getResources().getString(message));
        myProgressDialog.setIndeterminate(true);
        myProgressDialog.setCancelable(true);
        //myProgressDialog.show();
        return myProgressDialog;
    }

    public static void cancelHintDialog(ProgressDialog myProgressDialog) {
        if (myProgressDialog != null && myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
    }
}
