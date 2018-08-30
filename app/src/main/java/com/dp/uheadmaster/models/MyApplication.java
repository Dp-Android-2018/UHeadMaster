package com.dp.uheadmaster.models;

import android.app.Application;
import android.content.Context;



/**
 * Created by DELL on 01/01/2018.
 */

public class  MyApplication extends Application {
    private String mDescription;
    private String mRequirements;
   // private RefWatcher refWatcher;

    /*public static  RefWatcher getRefWatcher(Context context){
        MyApplication myApplication=(MyApplication)context.getApplicationContext();
        return myApplication.refWatcher;

    }*/

    @Override
    public void onCreate() {
        super.onCreate();
       /* new Instabug.Builder(this, "a93c49d31435780845c076d146608d1e")
                .setInvocationEvent(InstabugInvocationEvent.SHAKE)
                .build();*/
      /*  if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher= LeakCanary.install(this);*/
    }

   public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmRequirements() {
        return mRequirements;
    }

    public void setmRequirements(String mRequirements) {
        this.mRequirements = mRequirements;
    }
}
