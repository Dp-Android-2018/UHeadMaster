package com.dp.uheadmaster.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.dp.uheadmaster.R;

import es.dmoral.toasty.Toasty;


/**
 * Created by Dell on 13/08/2017.
 */

public class NetWorkConnection {



    public  static boolean isConnectingToInternet(Context context,View v) {
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
        showInterentMessage(context,v);
        return false;
    }


    public static void showInterentMessage(Context context,View v) {
      /*  new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getString(R.string.internet_message))
                .setContentText("")
                .setConfirmText(context.getString(R.string.close))
                .show();*/
       // Snackbar.make()
       // Toast.makeText(context, context.getString(R.string.internet_message), Toast.LENGTH_SHORT).show();
       // Toasty.info(context,context.getString(R.string.internet_message), Toast.LENGTH_SHORT).show();
        Snackbar.make(v,context.getString(R.string.internet_message), Snackbar.LENGTH_LONG).show();
    }
}
