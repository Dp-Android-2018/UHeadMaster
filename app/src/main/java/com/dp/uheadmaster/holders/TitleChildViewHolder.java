package com.dp.uheadmaster.holders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseLearn;
import com.dp.uheadmaster.fragments.LecturesFrag;
import com.dp.uheadmaster.interfaces.VideoLinksInterface;
import com.dp.uheadmaster.models.TitleChild;
import com.dp.uheadmaster.services.Downloader;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;

/**
 * Created by DELL on 16/09/2017.
 */

public class TitleChildViewHolder extends ChildViewHolder {

    private TextView tvLectureName;
    private TextView tvLectureType;
    private TextView tvVideoDuration;
    private Context context;
    private VideoLinksInterface videoLinksInterface;
    private String lastWatched;
    private ImageView ivPlayVideo;
    private ImageView ivDownloadLecture;
    private String documentPath;
    private String documentType;
    //public ListView listView;
    private SharedPrefManager sharedPrefManager;


    public TitleChildViewHolder(Context context, View itemView, VideoLinksInterface videoLinksInterface,String lastWatched) {
        super(itemView);
        this.context=context;
        this.lastWatched=lastWatched;
        this.videoLinksInterface=videoLinksInterface;
        tvLectureName=(TextView)itemView.findViewById(R.id.tv_lecture_name);
        tvLectureType=(TextView)itemView.findViewById(R.id.tv_lecture_type);
        tvVideoDuration=(TextView)itemView.findViewById(R.id.tv_lecture_duration);
        ivPlayVideo=(ImageView)itemView.findViewById(R.id.iv_play_lecture);
        ivDownloadLecture=(ImageView)itemView.findViewById(R.id.iv_download_lecture);

        sharedPrefManager=new SharedPrefManager(context);
    }



    public void bind(final TitleChild titleChild, final int position){
       // Toasty.success(context,"Content Type >>>> "+titleChild.getContentType(), Toast.LENGTH_LONG).show();
        tvLectureName.setText(titleChild.getLectureTitle());
        if(lastWatched!=null  && !lastWatched.equals("")) {
            if (titleChild.getContentPath().equals(lastWatched)) {
                LecturesFrag.VIDEO_POSTION=CourseLearn.pdfs.indexOf(titleChild.getContentPath());
                tvLectureName.setTextColor(context.getResources().getColor((R.color.child_active_colr)));
                ivPlayVideo.setColorFilter(new LightingColorFilter(Color.GREEN, Color.GREEN));
            }else{
                tvLectureName.setTextColor(context.getResources().getColor((R.color.black)));
                ivPlayVideo.setColorFilter(new LightingColorFilter(Color.BLACK, Color.BLACK));

            }
        }
       /* tvLectureName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getChildAdapterPosition();
                tvLectureName.setTextColor(context.getResources().getColor((R.color.child_active_colr)));

            }
        });*/


        if(titleChild.getContentType().equals("doc")) {
          ivPlayVideo.setImageResource(R.mipmap.ic_document);
            tvLectureType.setText("Doc");
            tvVideoDuration.setVisibility(View.GONE);
            ivDownloadLecture.setVisibility(View.VISIBLE);
            documentPath=LecturesFrag.documentPath+""+titleChild.getContentPath();
          /*  if((titleChild.getContentPath().substring(titleChild.getContentPath().indexOf('.')+1).equals("pdf")))
                  documentType="application/pdf";
            else if((titleChild.getContentPath().substring(titleChild.getContentPath().indexOf('.')+1).equals("doc")))
                documentType="application/msword";
            else if((titleChild.getContentPath().substring(titleChild.getContentPath().indexOf('.')+1).equals("docx")))
                documentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            else if((titleChild.getContentPath().substring(titleChild.getContentPath().indexOf('.')+1).equals("docx")))
                documentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document";*/

        }else if(titleChild.getContentType().equals("video")) {
            ivPlayVideo.setImageResource(R.mipmap.ic_play);
            tvLectureType.setText("Video");
            tvVideoDuration.setText(titleChild.getVideoDuration());
            tvVideoDuration.setVisibility(View.VISIBLE);
            ivDownloadLecture.setVisibility(View.GONE);

        }else if(titleChild.getContentType().equals("question")) {

                tvLectureType.setText("Quiz");
                ivPlayVideo.setImageResource(R.mipmap.ic_lighting);
                tvVideoDuration.setText(titleChild.getCourseData().getContents().size()+" "+ context.getString(R.string.questions));
                tvVideoDuration.setVisibility(View.VISIBLE);
                ivDownloadLecture.setVisibility(View.GONE);


        }

        String lastWord = titleChild.getContentPath().substring(titleChild.getContentPath().lastIndexOf(" ")+1);
        System.out.println("Type:"+lastWord.substring(lastWord.indexOf('.')+1));


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CourseLearn.enrolled) {
                    if (titleChild.getContentType().equals("video")) {
                        if (CourseLearn.pdfs.indexOf(titleChild.getContentPath()) == (CourseLearn.pdfs.size() - 1)) {
                            CourseLearn.isCourseFinished = true;
                            // Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show();
                        }

                        videoLinksInterface.getVideoName(titleChild.getContentPath(), null, CourseLearn.pdfs.indexOf(titleChild.getContentPath()), null, 1);
                    } else if (titleChild.getContentType().equals("doc")) {

                        if (CourseLearn.pdfs.indexOf(titleChild.getContentPath()) == (CourseLearn.pdfs.size() - 1)) {
                            CourseLearn.isCourseFinished = true;
                            // Toast.makeText(context, "Finished 2", Toast.LENGTH_SHORT).show();
                        }
                        documentType = getMimeType(documentPath);
                        Intent i = new Intent(context, Downloader.class);
                        System.out.println("Document path:" + documentPath + " Document Type:" + documentType);
                        System.out.println("Document Name:" + titleChild.getContentName() + " Document Type:" + documentType);
                        i.putExtra("Id", String.valueOf(sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID)));
                        i.putExtra("Authorization", sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));

                        i.setDataAndType(Uri.parse(documentPath), documentType);
                        context.startService(i);
                 /*   Intent i=new Intent(context, ExperiementActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);*/
                        videoLinksInterface.getVideoName(titleChild.getContentPath(), null, CourseLearn.pdfs.indexOf(titleChild.getContentPath()), null, 2);
                    } else if (titleChild.getContentType().equals("question")) {
                        if(titleChild.getLectureTitle()!=null && titleChild.getCourseData().getContents()!=null && titleChild.getContentName()!=null && titleChild.getCourseData().getContents().size()!=0)
                        videoLinksInterface.getVideoName(titleChild.getLectureTitle(), titleChild.getCourseData().getContents(), -1, titleChild.getContentName(), 3);

                    }

             /*   else if(titleChild.getContentType().equals("doc")){

                }*/


                }else {
                 //   Toast.makeText(context, "You Are Not Enrolled in This Course", Toast.LENGTH_LONG).show();
                    Snackbar.make(itemView,context.getResources().getString(R.string.not_enrolled), Snackbar.LENGTH_LONG).show();
                }
            }


        });

        //Toast.makeText(context, "Pdfs :"+CourseLearn.pdfs.size(), Toast.LENGTH_SHORT).show();

        /*ivDownloadLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Document path:"+documentPath +" Document Type:"+documentType);
                Intent i=new Intent(context, Downloader.class);
                i.putExtra("Id",String.valueOf(sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID)));
                i.putExtra("Authorization", sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));

                i.setDataAndType(Uri.parse(documentPath),documentType);
                context.startService(i);

            }
        });*/

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
