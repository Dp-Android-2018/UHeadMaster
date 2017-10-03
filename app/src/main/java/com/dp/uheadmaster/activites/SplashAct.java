package com.dp.uheadmaster.activites;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;

import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SplashAct extends AppCompatActivity {


    private VideoView videoView;
    private SharedPrefManager sharedPrefManager;
    private int userID = -1;
    private String userToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        userID = sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID);
        userToken = sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN);
        System.out.println("Splash / id : " + userID);
        System.out.println("Splash / authro : " + userToken);
        if(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.isFirstTime).equals(""))
        {
            setLocale(Locale.getDefault().getLanguage());
            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.Langauge,Locale.getDefault().getLanguage());
            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.isFirstTime,"123");
            ConfigurationFile.GlobalVariables.APP_LANGAUGE = Locale.getDefault().getLanguage();
        }else {
            setLocale(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.Langauge));
            ConfigurationFile.GlobalVariables.APP_LANGAUGE = sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.Langauge);

        }
        videoHandler();
    }

    private void videoHandler() {
        playVideo();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });
                if (userID != -1 && !userToken.equals("")) {
                    startActivity(new Intent(SplashAct.this, MainAct.class));

                } else {
                    startActivity(new Intent(SplashAct.this, MoodAct.class));
                }
                finish();
            }
        }, 5000);
    }

    private void playVideo() {
        try {
            videoView = (VideoView) findViewById(R.id.splash_video);
            videoView.setVisibility(View.VISIBLE);
            String path = "android.resource://" + getPackageName() + "/" + R.raw.splash_video;
            videoView.setVideoURI(Uri.parse(path));
            // videoView.setZOrderOnTop(true);
            videoView.start();
        } catch (Exception e) {
            System.out.println("Error / splash Screen : " + e.getMessage());
        }
    }


    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

}
