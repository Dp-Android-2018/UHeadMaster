package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.QuestionAnswerAdapter;
import com.dp.uheadmaster.adapters.ResourcesAdapter;
import com.dp.uheadmaster.adapters.ResponsesAdapter;
import com.dp.uheadmaster.models.AnnouncementData;
import com.dp.uheadmaster.models.Answer;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Question;
import com.dp.uheadmaster.models.request.AddQuestionAnswerRequest;
import com.dp.uheadmaster.models.response.AnswersResponse;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.models.response.QuestionsResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 26/09/2017.
 */

public class ResponsesAct extends AppCompatActivity {
    private RecyclerView recyclerQustions;
    private LinearLayoutManager mLayoutManager;
    private Question question;

     private TextView emptyView;
    private ArrayList<Answer> answers;
    private ResponsesAdapter responsesAdapter;
    private int courseId = -1;
    private Button btnAnswerPost;
    private EditText edAnswer;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private FontChangeCrawler fontChanger;
    private String type="";
    private Answer answer;
    private ImageView ivCouseName;
    private TextView tvCourseName;
    private RelativeLayout rlCourseInfo;
    private CourseModel courseModel;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responses_layout);
        if(getIntent().getSerializableExtra("Question")!=null) {
            question = (Question) getIntent().getSerializableExtra("Question");
            courseId = getIntent().getIntExtra("CourseId", -1);
        }else {
            try {

                rlCourseInfo=(RelativeLayout)findViewById(R.id.rl_course_info) ;
                ivCouseName=(ImageView)findViewById(R.id.iv_course_logo) ;
                tvCourseName=(TextView)findViewById(R.id.tv_course_name) ;
                rlCourseInfo.setVisibility(View.VISIBLE);

                if(getIntent().getStringExtra("QUESTION_DATA")!=null) {

                    String obj = getIntent().getStringExtra("QUESTION_DATA");
                    JSONObject object = new JSONObject(obj);
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = parser.parse(object.get("question").toString());
                    JsonElement mJson1 = parser.parse(object.get("course").toString());
                    Gson gson = new Gson();
                    question = gson.fromJson(mJson, Question.class);
                    System.out.println("Q8uestion Obj :"+question.toString());
                    courseModel=gson.fromJson(mJson1,CourseModel.class);
                    tvCourseName.setText(courseModel.getTitle());
                    if(courseModel.getImagePath()!=null && !courseModel.getImagePath().equals(""))
                        Picasso.with(getApplicationContext()).load(courseModel.getImagePath()).into(ivCouseName);
                    courseId = question.getCourseId();
                }else if(getIntent().getStringExtra("ANSWER_DATA")!=null){
                  // Snackbar.make(linearLayout, "Fired", Snackbar.LENGTH_LONG).show();
                    answer=new Answer();
                    String obj = getIntent().getStringExtra("ANSWER_DATA");
                //    String obj="{\"answer\":\"{\\\"updated_at\\\":\\\"2018-02-25 08:56:52\\\",\\\"user_id\\\":23,\\\"created_at\\\":\\\"2018-02-25 08:56:52\\\",\\\"id\\\":87,\\\"question_id\\\":\\\"77\\\",\\\"user\\\":{\\\"private\\\":0,\\\"role\\\":\\\"instructor\\\",\\\"confirmation_code\\\":\\\"d570b2e3f36504571ebebcf47deb9a33\\\",\\\"about\\\":null,\\\"mobile\\\":null,\\\"created_at\\\":\\\"2018-02-21 08:36:05\\\",\\\"confirmed\\\":0,\\\"deleted_at\\\":null,\\\"thumbLink\\\":\\\"http:\\\\/\\\\/uheadmaster.com\\\\/assets\\\\/site\\\\/images\\\\/default.jpg\\\",\\\"imageLink\\\":\\\"\\\",\\\"country_key\\\":\\\"1264\\\",\\\"updated_at\\\":\\\"2018-02-24 10:49:28\\\",\\\"device_token\\\":null,\\\"name\\\":\\\"لن\\\",\\\"id\\\":23,\\\"banned\\\":0,\\\"headline\\\":null,\\\"email\\\":\\\"test@test.test\\\"},\\\"content\\\":\\\"b bbbb\\\"}\",\"course\":\"{\\\"image\\\":\\\"http:\\\\/\\\\/uheadmaster.com\\\\/media\\\\/images\\\\/Kraken-Adds-Newest-Cryptocurrency-For-Trading_15171324046217.jpg\\\",\\\"title\\\":\\\"Cryptocurrency Trading Course 2018: Make Profits Dail\\\"}\",\"question\":\"{\\\"course_id\\\":8,\\\"updated_at\\\":\\\"2018-02-25 08:56:22\\\",\\\"user_id\\\":28,\\\"created_at\\\":\\\"2018-02-25 08:56:22\\\",\\\"id\\\":77,\\\"title\\\":\\\"why3\\\",\\\"answer_id\\\":null,\\\"user\\\":{\\\"private\\\":0,\\\"role\\\":\\\"student\\\",\\\"confirmation_code\\\":\\\"ece20ee3606ec8caae1b93cd9776ca1c\\\",\\\"about\\\":null,\\\"mobile\\\":null,\\\"created_at\\\":\\\"2018-02-25 08:54:35\\\",\\\"confirmed\\\":0,\\\"deleted_at\\\":null,\\\"thumbLink\\\":\\\"http:\\\\/\\\\/uheadmaster.com\\\\/assets\\\\/site\\\\/images\\\\/default.jpg\\\",\\\"imageLink\\\":\\\"\\\",\\\"country_key\\\":\\\"1268\\\",\\\"updated_at\\\":\\\"2018-02-25 08:54:35\\\",\\\"device_token\\\":\\\"dY9GG6WvIaA:APA91bF6sNO59Ok5ywIPn3D7zWOYzFYPBphDVOtUb-sin8NRWXibD8hrm7ZD5I8PctcEIJ-qw1VK5KIrgwtdCCTbnZ9IWLH_zWhFiwq6KgwFFgcscSKE6Y6qKKgZn-nbuIcs-dKXQVyP\\\",\\\"name\\\":\\\"test\\\",\\\"id\\\":28,\\\"banned\\\":0,\\\"headline\\\":null,\\\"email\\\":\\\"test@android.com\\\"},\\\"content\\\":\\\"why3\\\"}\",\"type\":\"answer\"}";
                    System.out.println("Object Data :"+obj);
                    JSONObject object = new JSONObject(obj);
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = parser.parse(object.get("question").toString());
                    JsonElement mJson1 = parser.parse(object.get("course").toString());
                    JsonElement mJson2 = parser.parse(object.get("answer").toString());
                    Gson gson = new Gson();
                    question = gson.fromJson(mJson, Question.class);
                    answer=gson.fromJson(mJson2, Answer.class);
                    courseModel=gson.fromJson(mJson1,CourseModel.class);
               //    Snackbar.make(this, "Answer :"+answer.getContent(), Snackbar.LENGTH_LONG).show();
                    courseId = question.getCourseId();
                    System.out.println("Answer Obj :"+mJson2.toString());
                    tvCourseName.setText(courseModel.getTitle());
                    if(courseModel.getImagePath()!=null && !courseModel.getImagePath().equals(""))
                        Picasso.with(getApplicationContext()).load(courseModel.getImagePath()).into(ivCouseName);

                    //System.out.println("Question Title :"+mJson.toString());
                }
              /*  responsesAdapter = new ResponsesAdapter(getApplicationContext(), question, answers);
                recyclerQustions.setAdapter(responsesAdapter);
               Snackbar.make(this, "question :"+question.getId(), Snackbar.LENGTH_LONG).show();*/

             //   System.out.println("Notification Course Id :"+courseId);
                //System.out.println("Notification Question Id :"+courseId);
            }catch (JSONException ex){
                System.out.println("Exception Message :"+ex.getMessage());
                ex.printStackTrace();
            }
        }
        sharedPrefManager = new SharedPrefManager(this);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        }
        initializeUi();

        initEventDriven();


    }

    private void initEventDriven() {
        btnAnswerPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = ResponsesAct.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                String answer = edAnswer.getText().toString();
                if (!answer.equals("") && answer != null) {
                    addAnswerAPI(answer);
                } else {
                    Snackbar.make(linearLayout,  getString(R.string.enter_all_fields), Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    private void addAnswerAPI(String answer) {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog = ConfigurationFile.showDialog(ResponsesAct.this);

            AddQuestionAnswerRequest addAnswerRequest = new AddQuestionAnswerRequest(question.getId(),answer);
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<DefaultResponse> call = apiService.addQuestionsAnswer(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), addAnswerRequest);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        DefaultResponse defaultResponse = response.body();
                        if (defaultResponse.getStatus() == 200) {
                            Snackbar.make(linearLayout, defaultResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                            edAnswer.setText("");
                            getCourseQuestionsAnswers(true);

                        } else {
                            // parse the response body …
                            System.out.println("Add Answer error Code message :" + response.body().getMessage());
                            Snackbar.make(linearLayout, defaultResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                        }

                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                 }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                 }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    public void initializeUi() {
       answers = new ArrayList<>();
        if(answer!=null)
            answers.add(answer);
        linearLayout=(LinearLayout) findViewById(R.id.content);
         sharedPrefManager = new SharedPrefManager(getApplicationContext());
        recyclerQustions = (RecyclerView) findViewById(R.id.recycler1);
        emptyView = (TextView) findViewById(R.id.empty_view);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerQustions.setLayoutManager(mLayoutManager);
        recyclerQustions.setItemAnimator(new DefaultItemAnimator());

        edAnswer = (EditText) findViewById(R.id.ed_answer);
        btnAnswerPost = (Button) findViewById(R.id.btn_answer_post);

        if(getIntent().getSerializableExtra("Question")==null) {

            notifyAdapter(false);
        }else {
            getCourseQuestionsAnswers(false);
        }
    }


    public void getCourseQuestionsAnswers(final boolean addAnswer) {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog = ConfigurationFile.showDialog(this);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<AnswersResponse> call = apiService.getCourseQuestionsAnswers(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), courseId, question.getId(), 0);
            call.enqueue(new Callback<AnswersResponse>() {
                @Override
                public void onResponse(Call<AnswersResponse> call, Response<AnswersResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    AnswersResponse answersResponse = response.body();
                    try {

                        if (answersResponse.getStatus() == 200) {
                            answers.clear();
                            if (!answersResponse.getAnswerResponse().isEmpty()) {
                                for (int i = 0; i < answersResponse.getAnswerResponse().size(); i++) {
                                    answers.add(answersResponse.getAnswerResponse().get(i));
                                }
                                //    Snackbar.success(getApplicationContext()," "+answers.size(),Snackbar.LENGTH_LONG).show();
                                notifyAdapter(addAnswer);
                                emptyView.setVisibility(View.GONE);
                                emptyView.setText(R.string.no_responses);
                                recyclerQustions.setVisibility(View.VISIBLE);
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                                emptyView.setText(R.string.no_responses);
                                recyclerQustions.setVisibility(View.GONE);
                            }

                        } else {
                            //Snackbar.error(getActivity().getApplicationContext(),questionsResponse.getMessage(),Snackbar.LENGTH_LONG).show();
                            emptyView.setVisibility(View.VISIBLE);
                            emptyView.setText(answersResponse.getMessage());
                            recyclerQustions.setVisibility(View.GONE);
                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<AnswersResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);
                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });


        } else {
            ConfigurationFile.hideDialog(progressDialog);
            Snackbar.make(linearLayout, getResources().getString(R.string.internet_message), Snackbar.LENGTH_LONG).show();
        }
    }

    public void notifyAdapter(boolean addAnswer) {
       // System.out.println("Notification Adapter Question :"+question.getId());
        //System.out.println("Notification Adapter Answer :"+answers.size());
        if(addAnswer){
            recyclerQustions.scrollToPosition(answers.size());
        }
      // Snackbar.make(this, "Answer :"+answers.get(0).getContent(), Snackbar.LENGTH_LONG).show();
        responsesAdapter = new ResponsesAdapter(getApplicationContext(), question, answers);
        recyclerQustions.setAdapter(responsesAdapter);

    }


}
