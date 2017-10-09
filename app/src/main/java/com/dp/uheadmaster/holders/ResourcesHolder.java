package com.dp.uheadmaster.holders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.interfaces.OnItemClickInterface;
import com.dp.uheadmaster.models.Resource;
import com.dp.uheadmaster.services.Downloader;

/**
 * Created by DELL on 27/09/2017.
 */

public class ResourcesHolder extends RecyclerView.ViewHolder  {

    private TextView tvResourceName;
    private ImageView ivDownload;
    private Context context;
    public ResourcesHolder(View itemView, Context context) {
        super(itemView);

        this.context=context;
        tvResourceName=(TextView)itemView.findViewById(R.id.tv_resource_name);
        ivDownload=(ImageView)itemView.findViewById(R.id.iv_download);
    }

    public void onBind(final Resource resource, final OnItemClickInterface listener){
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

                Intent i=new Intent(context, Downloader.class);

                i.setDataAndType(Uri.parse("https://commonsware.com/Android/Android-1_0-CC.pdf"),
                        "application/pdf");

                context.startService(i);
            }
        });
    }


}
