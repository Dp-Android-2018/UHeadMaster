package com.dp.uheadmaster.holders;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Browser;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.dp.uheadmaster.R;

import com.dp.uheadmaster.fragments.LecturesFrag;
import com.dp.uheadmaster.interfaces.OnItemClickInterface;
import com.dp.uheadmaster.models.Resource;
import com.dp.uheadmaster.services.Downloader;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;

import java.io.File;
import java.sql.SQLOutput;

/**
 * Created by DELL on 27/09/2017.
 */

public class ResourcesHolder extends RecyclerView.ViewHolder  {

    private TextView tvResourceName;
    private ImageView ivDownload;
    private Context context;
    private String documentPath;
    private String documentType;
    private SharedPrefManager sharedPrefManager;
    private Activity activity;
    public ResourcesHolder(View itemView, Context context,Activity activity) {
        super(itemView);

        this.context=context;
        tvResourceName=(TextView)itemView.findViewById(R.id.tv_resource_name);
        ivDownload=(ImageView)itemView.findViewById(R.id.iv_download);
        sharedPrefManager=new SharedPrefManager(context);
        this.activity=activity;
    }

    public void onBind(final Resource resource, final OnItemClickInterface listener){

        documentPath= LecturesFrag.documentPath+""+resource.getResourcePath();
        System.out.println("Document Path Download :"+documentPath);

        documentType="application/"+resource.getResourcePath().substring(resource.getResourcePath().indexOf('.')+1);
        System.out.println("Document Path Download :"+documentType);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickedItem(resource);

            }
        });
        tvResourceName.setText(resource.getLectureTitle()+" - "+resource.getResourceTitle());
        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               System.out.println("Document path:"+documentPath +" Document Type:"+documentType);

               Intent i=new Intent(context, Downloader.class);
                i.putExtra("Id",String.valueOf(sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID)));
                i.putExtra("Authorization", sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
                i.setDataAndType(Uri.parse(documentPath),
                        getMimeType(documentPath));

                context.startService(i);

            }
        });
    }


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

}
