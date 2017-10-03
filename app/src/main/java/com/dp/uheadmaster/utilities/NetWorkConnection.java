package com.dp.uheadmaster.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dp.uheadmaster.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Dell on 13/08/2017.
 */

public class NetWorkConnection {



    public static  boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        showInterentMessage(context);
        return false;
    }


    public static void showInterentMessage(Context context) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getString(R.string.internet_message))
                .setContentText("")
                .setConfirmText(context.getString(R.string.close))
                .show();
    }
}
