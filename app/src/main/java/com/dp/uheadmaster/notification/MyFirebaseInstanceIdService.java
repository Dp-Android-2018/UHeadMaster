package com.dp.uheadmaster.notification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by DELL on 08/04/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    public static String TOKEN = null ;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        TOKEN=refreshedToken;

    }
}
