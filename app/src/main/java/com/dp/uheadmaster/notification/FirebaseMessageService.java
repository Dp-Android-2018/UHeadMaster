package com.dp.uheadmaster.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.AnnouncementDetails;
import com.dp.uheadmaster.activites.MainAct;
import com.dp.uheadmaster.activites.ResponsesAct;
import com.dp.uheadmaster.activites.SplashAct;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by DELL on 08/04/2017.
 */

public class FirebaseMessageService extends FirebaseMessagingService {

    Context context;

    // Handler handler =null;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            context = this;
            //handler=(Handler)context;
         //  System.out.println("Notification Type:" + remoteMessage.getData().get("type"));
          /*  String title = remoteMessage.getNotification().getTitle();
            remoteMessage.getNotification().getTitle();
            System.out.println("Notification Title:" + remoteMessage.getNotification().getBody());
*/
            System.out.println("Notification Title:" + remoteMessage.getNotification().getTitle());
            if(remoteMessage.getNotification().getTitle().equals("Account Activated")){
             /*   Intent i=new Intent(this, SplashAct.class);
                Notify(i,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());*/

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.package.ACCOUNT_VERIFIED");
                sendBroadcast(broadcastIntent);
            }else if(remoteMessage.getData().get("type").equals("announcement")){
              Intent i=new Intent(this, AnnouncementDetails.class);
              i.putExtra("ANNOUNCEMENT_DATA",new Gson().toJson(remoteMessage.getData()).toString());
              Notify(i,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getTitle());
          }else if(remoteMessage.getData().get("type").equals("question")){
              Intent i=new Intent(this, ResponsesAct.class);
            //  System.out.println("Data Content :"+new Gson().toJson(remoteMessage.getData()));
              i.putExtra("QUESTION_DATA",new Gson().toJson(remoteMessage.getData()).toString());
              Notify(i,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getTitle());
          }else if(remoteMessage.getData().get("type").equals("answer")){
              Intent i=new Intent(this, ResponsesAct.class);
            //  System.out.println("Data Content :"+new Gson().toJson(remoteMessage.getData()));
              i.putExtra("ANSWER_DATA",new Gson().toJson(remoteMessage.getData()).toString());
              Notify(i,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getTitle());
          }




        } catch (Exception e) {
            System.out.println("Error in firebaseMesage : " + e.getMessage());
        }





    }





    public void Notify(Intent intent, String messageTitle, String nb) {
        System.out.println("In Account Activated Notified");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500, 500, 500, 500, 500};

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(messageTitle)
                .setContentText(nb)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationManager.IMPORTANCE_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}


