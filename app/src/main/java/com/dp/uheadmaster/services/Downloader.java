package com.dp.uheadmaster.services;

/**
 * Created by DELL on 03/10/2017.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.BuildConfig;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader extends IntentService {
    private static final String AUTHORITY=
          "com.dp.uheadmaster.provider";
    private static int NOTIFY_ID=1337;
    private static int FOREGROUND_ID=1338;
    private String id;
    private String Auth;
    public Downloader() {
        super("Downloader");

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        id=intent.getStringExtra("Id");
        Auth=intent.getStringExtra("Authorization");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onHandleIntent(Intent i) {
        String filename=i.getData().getLastPathSegment();

        startForeground(FOREGROUND_ID,
                buildForegroundNotification(filename));

        try {
            File output=new File(getFilesDir(), filename);

            if (output.exists()) {
                output.delete();
            }

            URL url=new URL(i.getData().toString());
            HttpURLConnection c=(HttpURLConnection)url.openConnection();
            c.setRequestProperty("Key", ConfigurationFile.ConnectionUrls.HEAD_KEY);
            c.setRequestProperty("Id", id);
            c.setRequestProperty("Authorization", Auth);

            FileOutputStream fos=new FileOutputStream(output.getPath());
            BufferedOutputStream out=new BufferedOutputStream(fos);

            try {
                InputStream in=c.getInputStream();
                byte[] buffer=new byte[8192];
                int len=0;

                while ((len=in.read(buffer)) >= 0) {
                    out.write(buffer, 0, len);
                }

                out.flush();
            }
            finally {
                fos.getFD().sync();
                out.close();
                c.disconnect();
            }

            raiseNotification(i, output, null);
        }
        catch (IOException e2) {
            raiseNotification(i, null, e2);
        }
        finally {
            stopForeground(true);
        }
    }

    private void raiseNotification(Intent inbound, File output,
                                   Exception e) {
        NotificationCompat.Builder b=new NotificationCompat.Builder(this);

        b.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis());

        if (e == null) {
            b.setContentTitle(getString(R.string.download_complete))
                    .setContentText(getString(R.string.click_open))
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setTicker(getString(R.string.download_complete));

            Intent outbound=new Intent(Intent.ACTION_VIEW);
            Uri outputUri=
                    FileProvider.getUriForFile(this, AUTHORITY, output);

            outbound.setDataAndType(outputUri, inbound.getType());
            outbound.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            PendingIntent pi=PendingIntent.getActivity(this, 0,
                    outbound, PendingIntent.FLAG_UPDATE_CURRENT);

            b.setContentIntent(pi);
        }
        else {
            b.setContentTitle("Exception")
                    .setContentText(e.getMessage())
                    .setSmallIcon(android.R.drawable.stat_notify_error)
                    .setTicker("Exception");
        }

        NotificationManager mgr=
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        mgr.notify(NOTIFY_ID, b.build());
    }

    private Notification buildForegroundNotification(String filename) {
        NotificationCompat.Builder b=new NotificationCompat.Builder(this);

        b.setOngoing(true)
                .setContentTitle(getString(R.string.downloading))
                .setContentText(filename)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker(getString(R.string.downloading));

        return(b.build());
    }
}
