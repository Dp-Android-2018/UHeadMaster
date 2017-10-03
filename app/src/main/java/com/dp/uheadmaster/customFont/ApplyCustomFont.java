package com.dp.uheadmaster.customFont;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by DELL on 05/09/2017.
 */

public class ApplyCustomFont {
    public static ApplyCustomFont instance=null;
    private Typeface face1,face2,face3;
    private ApplyCustomFont(Context context)
    {
        face1=Typeface.createFromAsset(context.getAssets(),"font/Roboto-Bold.ttf");
        face2=Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
        face3=Typeface.createFromAsset(context.getAssets(),"font/GE_SS_Two_Medium.otf");

    }

    public static ApplyCustomFont getInstance(Context context)
    {
        if(instance==null) {
            instance= new ApplyCustomFont(context);
        }

        return instance;
    }

    public Typeface chooseFont(String fontName){
        if(fontName.equals("en_font1"))
            return face1;
        else if(fontName.equals("en_font2"))
            return face2;
        else if(fontName.equals("ar_font"))
            return face3;


        return null;
    }
}
