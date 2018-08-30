package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseLearn;

import com.dp.uheadmaster.activites.StartQuizAct;
import com.dp.uheadmaster.adapters.ExpandedAdapter;
import com.dp.uheadmaster.dialogs.WelcomeMessageDialog;
import com.dp.uheadmaster.interfaces.ParentPositionChecker;
import com.dp.uheadmaster.interfaces.VideoLinksInterface;
import com.dp.uheadmaster.jwplayer.JWPlayerViewExample;
import com.dp.uheadmaster.models.Content;
import com.dp.uheadmaster.models.CourseContentModel;
import com.dp.uheadmaster.models.CourseData;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Resource;
import com.dp.uheadmaster.models.Section;
import com.dp.uheadmaster.models.TitleChild;
import com.dp.uheadmaster.models.TitleParent;
import com.dp.uheadmaster.models.request.LastWatchedRequest;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 24/09/2017.
 */

public class LecturesFrag extends Fragment implements ParentPositionChecker {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private  ExpandedAdapter expandedAdapter;
    private SharedPrefManager sharedPrefManager;
    public static ArrayList<Resource> resources;
    private ArrayList<Content> quizes;
    private Context mcContext;
    private int courseId;
    static String VIDEO_UNIQUE_TITLE = "";
    public static int VIDEO_POSTION = -1;
    private FontChangeCrawler fontChanger;
    private String lastWatched=null;
    private int parentPosition=-1;
    public static String documentPath;
    private ImageView ivEmptyView;
    private String finalMessage;
    private Activity mActivity;
    private boolean addReview;
    private HashMap<String,String>subTitles;
    private String videoKey;
    private HashMap<String, HashMap<String,String>> mapVideoSubtitle=new HashMap<>();
    View v;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
            fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup) this.getView());
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)) {
            fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup) this.getView());
        }
       // Toast.makeText(getActivity().getApplicationContext(), "Restated", Toast.LENGTH_SHORT).show();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.frag_lecture_layout, container, false);
       // Toast.makeText(getActivity().getApplicationContext(), "Restated", Toast.LENGTH_SHORT).show();
        initializeUi(v);
        documentPath="http://uhm.findandfix.com/api/"+courseId+"/";
        subTitles=new HashMap<>();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(CourseLearn.isCourseFinished) {
          //  Toast.makeText(getActivity().getApplicationContext(), "Congratulations", Toast.LENGTH_SHORT).show();
            if(!addReview) {
                WelcomeMessageDialog welcomeMessageDialog = new WelcomeMessageDialog(mcContext, finalMessage, 2, courseId);
                welcomeMessageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                welcomeMessageDialog.setCancelable(false);
                welcomeMessageDialog.show();
                CourseLearn.isCourseFinished = false;
            }
        }
    }

    @Override
    public void getParentPosition(Context context, int position) {


        try {


          // System.out.println("Adapters Count:" + expandedAdapter.getItemCount() + " " + position);
            for (int i = 0; i < expandedAdapter.getItemCount(); i++) {
                if (i != position)
                    expandedAdapter.collapseParent(i);
            }
        }catch (NullPointerException ex) {
            ex.printStackTrace();
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }

    }

    public void initializeUi(View v) {

        resources = new ArrayList<>();
       // quizes = new ArrayList<>();
        courseId = getActivity().getIntent().getIntExtra("course_id", 0);
        finalMessage=getActivity().getIntent().getStringExtra("Final_Message");
        //Toast.makeText(mcContext, "Cid LEC :"+courseId, Toast.LENGTH_SHORT).show();
        ivEmptyView=(ImageView) v.findViewById(R.id.iv_resources_empty) ;
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        getCourseContents();
    }


    public void getCourseContents() {
        if (NetWorkConnection.isConnectingToInternet(mActivity.getApplicationContext(),mActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(getActivity());


            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);


            Call<CourseContentModel> call = apiService.getCourseContent(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), CourseLearn.courseID);
            call.enqueue(new Callback<CourseContentModel>() {
                @Override
                public void onResponse(Call<CourseContentModel> call, Response<CourseContentModel> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {

                        System.out.println("Full Course Response :"+new Gson().toJson(response));
                        CourseContentModel courseContentModel = response.body();
                     //   Toast.makeText(getActivity().getApplicationContext(), " "+courseContentModel.getStatus()+"\n message:"+sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), Toast.LENGTH_SHORT).show();
                        if (courseContentModel.getStatus() == 200) {
                            lastWatched=courseContentModel.getLastWatched();
                            addReview=courseContentModel.isAddedReview();
                            if(lastWatched!=null && !lastWatched.equals("")){
                                System.out.println("Last Watched :"+lastWatched);
                            }
                            //initiateData();
                            expandedAdapter = new ExpandedAdapter(getActivity().getApplicationContext(), initiateData(courseContentModel.getSections()), new VideoLinksInterface() {
                                @Override
                                public void getVideoName(String name, ArrayList<Content> contents , int position,String lecturename,int type) {
                                    if (contents == null) {
                                        System.out.println("Content Content Sub :"+name);
                                        if(type==1)
                                            getVideoLinks(name, position);

                                        if (LecturesFrag.VIDEO_POSTION < position) {
                                                LecturesFrag.VIDEO_UNIQUE_TITLE = name;
                                                LecturesFrag.VIDEO_POSTION = position;
                                                //Toast.makeText(getActivity().getApplicationContext(), "Pos :"+position, Toast.LENGTH_SHORT).show();
                                                 setLastWatched();
                                            }
                                    } else {
                                        Intent i = new Intent(getActivity(), StartQuizAct.class);
                                        i.putExtra("quiz", name);
                                         i.putExtra("lecture",lecturename);
                                        i.putExtra("questions_num", contents.size());
                                        i.putExtra("Answers", contents);

                                        getActivity().startActivity(i);

                                    }
                                }
                            },lastWatched);
                            recyclerView.setAdapter(expandedAdapter);
                            if(parentPosition!=-1)
                            expandedAdapter.expandParent(parentPosition);
                        }else {
                            //Toast.makeText(getActivity().getApplicationContext(), "Error :"+courseContentModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                     //   Toast.makeText(getActivity().getApplicationContext(), "nULL eXCEPTION", Toast.LENGTH_SHORT).show();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CourseContentModel> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(mActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            //Snackbar.make(v, getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG).show();
        }
    }

    public List<TitleParent> initiateData(ArrayList<Section> sections) {
      //  Toast.makeText(mcContext, "Data :"+sections.size(), Toast.LENGTH_SHORT).show();
        ArrayList<TitleParent> parents = new ArrayList<>();
        ArrayList<TitleChild> children = new ArrayList<>();
        if(!sections.isEmpty()) {

            for (int i = 0; i < sections.size(); i++) {
                Section section = sections.get(i);
                children = new ArrayList<>();

                for (int j = 0; j < section.getCourseDatas().size(); j++) {
                    CourseData courseData = section.getCourseDatas().get(j);
                    if (courseData.getType().equals("quiz")) {
                        //  Toast.makeText(mcContext, "Course Data:"+courseData.getContents().size(), Toast.LENGTH_SHORT).show();
                        //     quizes.add(content);
                        //   children.add(new TitleChild(courseData.getId(), courseData.getTitle(), content.getId(), content.getType(), null, content.getName(), content.getDuration(), courseData));
                        children.add(new TitleChild(courseData.getId(), courseData.getTitle(), 0, "question", "", section.getTitle(), "", courseData));

                    }
                    for (int z = 0; z < courseData.getContents().size(); z++) {
                        System.out.println("Content Srt Size :"+courseData.getContents().size());
                        Content content = courseData.getContents().get(z);
                        TitleChild titleChild=null;
                        if (courseData.getType().equals("lecture")) {

                            if (!content.getType().equals("resource")) {


                                if (content.getType().equals("video")) {
                                    videoKey=content.getContent();
                                    System.out.println("Content Content Sub 2:"+content.getName()+" "+content.getContent());
                                    System.out.println("Content Content Video");
                                    if (lastWatched != null && !lastWatched.equals("")) {
                                        if (content.getContent().equals(lastWatched)) {

                                            parentPosition = i;
                                        }
                                    }
                                    titleChild=new TitleChild(courseData.getId(), courseData.getTitle(), content.getId(), content.getType(), content.getContent(), content.getName(), content.getDuration(), null);
                                    children.add(titleChild);
                                    CourseLearn.pdfs.add(content.getContent());
                                }else if (content.getType().equals("srt")){
                                   subTitles.put(content.getName(),content.getContent());
                                }
                            }else {
                                ///  public Resource(int lectureId, String lectureTitle, int resourceId, String resourceTitle, String resourcePath)
                                resources.add(new Resource(courseData.getId(), courseData.getTitle(), content.getId(), content.getName(), content.getContent()));
                            }

                        }

                        if (titleChild!=null) {
                            mapVideoSubtitle.put(videoKey, subTitles);
                        }


                    }


                }
                parents.add(new TitleParent(section.getId(), section.getTitle(), children));
            }


        }else {
            recyclerView.setVisibility(View.GONE);
            ivEmptyView.setVisibility(View.VISIBLE);

        }

       //Toast.makeText(getActivity().getApplicationContext(), "Pdfs :"+CourseLearn.pdfs.size(), Toast.LENGTH_SHORT).show();
        return parents;
    }



    public void getVideoLinks(final String name , final int position) {
        HashMap<String, String> map = new HashMap<String, String>();
        System.out.println("Video Path :"+"http://uheadmaster.com/"+courseId+"/courses/videos/source/"+name+"?authorization="+ URLEncoder.encode(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN)));
        map.put("source","http://uheadmaster.com/"+courseId+"/courses/videos/source/"+name+"?authorization="+ URLEncoder.encode(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN)));
        Intent i = new Intent(getActivity().getApplicationContext(), JWPlayerViewExample.class);
        i.putExtra("Links", map);
        i.putExtra("SubTiltes",mapVideoSubtitle.get(name));
        System.out.println("Map Video SubTitle Size :"+mapVideoSubtitle.size());
        System.out.println("Map Video SubTitle Size cHILD:"+mapVideoSubtitle.get(name).size());
        getActivity().startActivity(i);}


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcContext = context;
    }

    @Override
    public void onDetach() {

        //call web service for set last watched video
       //
        super.onDetach();

    }

    public void setLastWatched() {
        if (NetWorkConnection.isConnectingToInternet(mActivity.getApplicationContext(),mActivity.findViewById(R.id.content))) {


            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);

            LastWatchedRequest lastWatchedRequest = new LastWatchedRequest(LecturesFrag.VIDEO_UNIQUE_TITLE);

            Call<DefaultResponse> call = apiService.setLastVideoWatched(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), courseId, lastWatchedRequest);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {

                        DefaultResponse element = response.body();
                     //   Toast.makeText(getActivity().getApplicationContext(), " "+element.getStatus(), Toast.LENGTH_SHORT).show();
                        if (element.getStatus() != 200) {
                            Snackbar.make(mActivity.findViewById(R.id.content), element.getStatus() + "\n" + element.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    } catch (NullPointerException ex) {
                        System.out.println("Error in Last Watched Video 2: " + ex.getMessage());

                    } catch (Exception ex) {
                        System.out.println("Error in Last Watched Video 2: " + ex.getMessage());

                    }
                }



                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
           // Snackbar.make(v, getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;}

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity=null;}
}
