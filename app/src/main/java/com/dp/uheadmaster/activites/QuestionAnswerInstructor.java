package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.InstructorQuestionAnswerAdapter;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Question;
import com.dp.uheadmaster.models.response.QuestionsResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 18/10/2017.
 */

public class QuestionAnswerInstructor extends AppCompatActivity {
   @Nullable @BindView(R.id.recycler_question_answers)
    RecyclerView questionsRecycler;


    private ProgressDialog progressDialog;
    private FontChangeCrawler fontChanger;
    private SharedPrefManager sharedPrefManager;
    private ArrayList<Question>questions;
    private InstructorQuestionAnswerAdapter adapter;
    private int courseId;
    private ImageView ivEmptyView;
    private LinearLayout linearLayout;
    Slide slide;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /* if(Build.VERSION.SDK_INT>21){
            slide=new Slide();

            slide.setDuration(2000);
            getWindow().setEnterTransition(slide);
        }*/
        setContentView(R.layout.instructor_question_answer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ButterKnife.bind(this);

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)findViewById(android.R.id.content));
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)findViewById(android.R.id.content));
        }
        initializeUi();
        getCourseQuestions();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initializeUi(){
        questions=new ArrayList<>();
        linearLayout=(LinearLayout) findViewById(R.id.content);
        ivEmptyView=(ImageView)findViewById(R.id.iv_question_empty) ;
        courseId=getIntent().getIntExtra("cid",0);
        questionsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        questionsRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter=new InstructorQuestionAnswerAdapter(getApplicationContext(),questions,this);
        questionsRecycler.setAdapter(adapter);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());

    }

    public void getCourseQuestions(){

       // Snackbar.make(this, ""+courseId, Snackbar.LENGTH_LONG).show();
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog = ConfigurationFile.showDialog(this);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<QuestionsResponse> call = apiService.getCourseQuestions(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),courseId,0);
            call.enqueue(new Callback<QuestionsResponse>() {
                @Override
                public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    QuestionsResponse questionsResponse=response.body();
                    try {
                        //Snackbar.make(QuestionAnswerInstructor.this, " status:"+questionsResponse.getStatus(), Snackbar.LENGTH_LONG).show();
                        if (questionsResponse.getStatus() == 200) {
                          //  Snackbar.make(QuestionAnswerInstructor.this, "SIZE"+questionsResponse.getQuestions().size(), Snackbar.LENGTH_LONG).show();
                            if(!questionsResponse.getQuestions().isEmpty()) {


                                for(Question question:questionsResponse.getQuestions()){
                                    questions.add(question);
                                }
                                adapter.notifyDataSetChanged();
                            }else {
                                ivEmptyView.setVisibility(View.VISIBLE);
                                questionsRecycler.setVisibility(View.GONE);
                            }

                        }else {

                            Snackbar.make(linearLayout, ""+questionsResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                            //Snackbar.error(getActivity().getApplicationContext(),questionsResponse.getMessage(),Snackbar.LENGTH_LONG).show();
                            /*emptyView.setVisibility(View.VISIBLE);
                            emptyView.setText(questionsResponse.getMessage());
                            recyclerQustions.setVisibility(View.GONE);*/
                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<QuestionsResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);
                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });




        } else {
            ConfigurationFile.hideDialog(progressDialog);
            Snackbar.make(linearLayout,getResources().getString(R.string.internet_message),Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        questions=null;
        adapter=null;
        slide=null;
    }
}
