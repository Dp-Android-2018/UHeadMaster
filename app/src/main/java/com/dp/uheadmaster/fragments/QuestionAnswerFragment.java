package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseLearn;
import com.dp.uheadmaster.adapters.QuestionAnswerAdapter;
import com.dp.uheadmaster.dialogs.AddQuestionDialog;
import com.dp.uheadmaster.interfaces.RefreshRecyclerListener;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Question;
import com.dp.uheadmaster.models.response.QuestionsResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.sql.SQLOutput;
import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 24/09/2017.
 */

public class QuestionAnswerFragment extends Fragment implements RefreshRecyclerListener{
    private RecyclerView recyclerQustions;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton fab;
    private Context mcontext;
     private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private ArrayList<Question>questions;
    private QuestionAnswerAdapter questionAnswerAdapter;
    private Activity mActivity;
    private ImageView ivEmptyView;

    private View v;

    private FontChangeCrawler fontChanger;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup) this.getView());
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup) this.getView());
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.frag_questions_answers_layout,container,false);

        initializeUi(v);
       // getQuestions(0);
            return v;
    }

    public void getQuestions(int checker){
        if(CourseLearn.enrolled) {
            getCourseQuestions(checker);
        }else {
            recyclerQustions.setVisibility(View.GONE);
            ivEmptyView.setImageResource(R.drawable.ic_not_enrolled);
            ivEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getQuestions(0);
      //  Toast.makeText(mcontext, "Resumed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
       //    Toast.makeText(mcontext, "Paused", Toast.LENGTH_SHORT).show();
    }

    /*public void refresh(){
        System.out.println("Success");
//        Toast.makeText(mcontext, "Refresh>>>", Toast.LENGTH_SHORT).show();
       //initializeUi(v);
        getCourseQuestions();
    }*/
    public void initializeUi(View v){

         //Snackbar.success(getActivity().getApplicationContext()," "+CourseLearn.courseID, Toast.LENGTH_LONG).show();
        sharedPrefManager = new SharedPrefManager(mcontext);
        questions=new ArrayList<>();
        recyclerQustions = (RecyclerView) v.findViewById(R.id.recycler1);
        ivEmptyView=(ImageView) v.findViewById(R.id.iv_resources_empty) ;
        fab=(FloatingActionButton)v.findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddQuestionDialog addResponseDialog=new AddQuestionDialog(mcontext, CourseLearn.courseID,mActivity,QuestionAnswerFragment.this);
                addResponseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addResponseDialog.show();

            }
        });

        if(!CourseLearn.enrolled)
            fab.setVisibility(View.GONE);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerQustions.setLayoutManager(mLayoutManager);
        recyclerQustions.setItemAnimator(new DefaultItemAnimator());
        questionAnswerAdapter=new QuestionAnswerAdapter(mcontext,questions,CourseLearn.courseID);
        recyclerQustions.setAdapter(questionAnswerAdapter);

    }

    public void getCourseQuestions(final int check){
        if (NetWorkConnection.isConnectingToInternet(mActivity.getApplicationContext(),mActivity.findViewById(R.id.content))) {
            questions.clear();
           // progressDialog = ConfigurationFile.showDialog(getActivity());
            System.out.println("User Id Data :"+sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<QuestionsResponse> call = apiService.getCourseQuestions(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),CourseLearn.courseID,0);
            call.enqueue(new Callback<QuestionsResponse>() {
                @Override
                public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
                 //   ConfigurationFile.hideDialog(progressDialog);
                    QuestionsResponse questionsResponse=response.body();
                    try {

                        if (questionsResponse.getStatus() == 200) {

                            if(!questionsResponse.getQuestions().isEmpty()) {

                               /* for (int i = 0; i < questionsResponse.getQuestions().size(); i++) {
                                    questions.add(questionsResponse.getQuestions().get(i));
                                }*/
                               for(Question question:questionsResponse.getQuestions())
                                   questions.add(question);

                                questionAnswerAdapter.notifyDataSetChanged();
                                if(check==1)
                                recyclerQustions.scrollToPosition(0);
                            }else {
                                recyclerQustions.setVisibility(View.GONE);
                                ivEmptyView.setImageResource(R.drawable.ic_question_empty);
                                ivEmptyView.setVisibility(View.VISIBLE);
                               // ivEmptyView.setText(R.string.no_responses);

                            }

                        }else {
                                //Snackbar.error(getActivity().getApplicationContext(),questionsResponse.getMessage(),Toast.LENGTH_LONG).show();
                            ivEmptyView.setVisibility(View.VISIBLE);
                            ivEmptyView.setImageResource(R.drawable.ic_question_empty);
                          //  ivEmptyView.setText(questionsResponse.getMessage());
                            recyclerQustions.setVisibility(View.GONE);
                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<QuestionsResponse> call, Throwable t) {
                 //   ConfigurationFile.hideDialog(progressDialog);
                    Snackbar.make(mActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });




        } else {
          //  ConfigurationFile.hideDialog(progressDialog);
           // Snackbar.make(v,getActivity().getResources().getString(R.string.internet_message),Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext=context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity=activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity=null;
        mcontext=null;
    }

    @Override
    public void refreshRecycler() {
       // Toast.makeText(mcontext, "Refresh Data", Toast.LENGTH_SHORT).show();
       // System.out.println("Add Question Fragment Data ");
        getQuestions(1);
    }
}
