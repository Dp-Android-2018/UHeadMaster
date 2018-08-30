package com.dp.uheadmaster.activites;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.Locale;
import java.util.Set;

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
        if(getIntent()!=null){
            System.out.println("Firebase Success Splash");
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
                    if(getIntent().getExtras()!=null){

                        JSONObject json = new JSONObject();
                        Set<String> keys = getIntent().getExtras().keySet();

                        for (String key : keys) {
                            try {

                                System.out.println("Bundle Data :"+getIntent().getExtras().get(key));
                                // json.put(key, bundle.get(key)); see edit below
                                json.put(key, JSONObject.wrap(getIntent().getExtras().get(key)));
                            } catch(JSONException e) {
                                //Handle exception here
                                System.out.println("Json Error :"+e.getMessage());
                            }
                        }
                       // System.out.println("Bundle Data :"+getIntent().getExtras().getString("type"));
                        if( getIntent().getExtras().containsKey("type")) {
                            if (getIntent().getExtras().getString("type").equals("announcement")) {
                            //    Toast.makeText(SplashAct.this, "Moved", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), AnnouncementDetails.class);
                                i.putExtra("ANNOUNCEMENT_DATA",json.toString());
                                startActivity(i);
                            }else if (getIntent().getExtras().getString("type").equals("question")) {
                               // Toast.makeText(SplashAct.this, "Moved", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), ResponsesAct.class);
                                i.putExtra("QUESTION_DATA",json.toString());
                                startActivity(i);
                            }else if (getIntent().getExtras().getString("type").equals("answer")) {
                                // Toast.makeText(SplashAct.this, "Moved", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), ResponsesAct.class);
                                i.putExtra("ANSWER_DATA",json.toString());
                                startActivity(i);
                            }
                        }else if(getIntent().getExtras().containsKey("title")){
                            if(getIntent().getExtras().getString("title").equals("Account Activated")) {
                                System.out.println("In Account Activated splash");
                                finishAffinity();
                            }
                        }else
                            startActivity(new Intent(SplashAct.this, MainAct.class));
                            finishAffinity();

                    } else {
                        Intent i=new Intent(SplashAct.this, MainAct.class);
                        startActivity(i);
                        finishAffinity();
                      //  Toast.makeText(SplashAct.this, "Started", Toast.LENGTH_SHORT).show();
                        if(getIntent().getExtras()!=null)
                              getIntent().getExtras().clear();
                    }

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
