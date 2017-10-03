package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.QuestionAnswerAdapter;
import com.dp.uheadmaster.adapters.ResourcesAdapter;
import com.dp.uheadmaster.adapters.ResponsesAdapter;
import com.dp.uheadmaster.models.Answer;
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

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responses_layout);
        question = (Question) getIntent().getSerializableExtra("Question");
        courseId = getIntent().getIntExtra("CourseId", -1);
        sharedPrefManager = new SharedPrefManager(this);

        initializeUi();
        getCourseQuestionsAnswers(false);
        initEventDriven();


    }

    private void initEventDriven() {
        btnAnswerPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = edAnswer.getText().toString();
                if (!answer.equals("") && answer != null) {
                    addAnswerAPI(answer);
                } else {
                    Toasty.error(ResponsesAct.this,  getString(R.string.enter_all_fields), Toast.LENGTH_LONG, true).show();
                }

            }
        });
    }

    private void addAnswerAPI(String answer) {
        if (NetWorkConnection.isConnectingToInternet(ResponsesAct.this)) {
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
                            Toasty.success(ResponsesAct.this, defaultResponse.getMessage(), Toast.LENGTH_LONG, true).show();
                            edAnswer.setText("");
                            getCourseQuestionsAnswers(true);

                        } else {
                            // parse the response body …
                            System.out.println("Add Answer error Code message :" + response.body().getMessage());
                            Toasty.error(ResponsesAct.this, defaultResponse.getMessage(), Toast.LENGTH_LONG, true).show();
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

                    Toasty.error(ResponsesAct.this, t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println(" Fialer :" + t.getMessage());
                 }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    public void initializeUi() {
         sharedPrefManager = new SharedPrefManager(getApplicationContext());
        recyclerQustions = (RecyclerView) findViewById(R.id.recycler1);
        emptyView = (TextView) findViewById(R.id.empty_view);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerQustions.setLayoutManager(mLayoutManager);
        recyclerQustions.setItemAnimator(new DefaultItemAnimator());

        edAnswer = (EditText) findViewById(R.id.ed_answer);
        btnAnswerPost = (Button) findViewById(R.id.btn_answer_post);

    }


    public void getCourseQuestionsAnswers(final boolean addAnswer) {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext())) {
            progressDialog = ConfigurationFile.showDialog(this);
            answers = new ArrayList<>();
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

                            if (!answersResponse.getAnswerResponse().isEmpty()) {
                                for (int i = 0; i < answersResponse.getAnswerResponse().size(); i++) {
                                    answers.add(answersResponse.getAnswerResponse().get(i));
                                }
                                //    Toasty.success(getApplicationContext()," "+answers.size(),Toast.LENGTH_LONG).show();
                                notifyAdapter(addAnswer);
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                                emptyView.setText(R.string.no_responses);
                                recyclerQustions.setVisibility(View.GONE);
                            }

                        } else {
                            //Toasty.error(getActivity().getApplicationContext(),questionsResponse.getMessage(),Toast.LENGTH_LONG).show();
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
                    Toasty.error(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                }
            });


        } else {
            ConfigurationFile.hideDialog(progressDialog);
            Toasty.error(getApplicationContext().getApplicationContext(), getResources().getString(R.string.internet_message), Toast.LENGTH_LONG).show();
        }
    }

    public void notifyAdapter(boolean addAnswer) {
        if(addAnswer){
            recyclerQustions.scrollToPosition(answers.size());
        }
        responsesAdapter = new ResponsesAdapter(getApplicationContext(), question, answers);
        recyclerQustions.setAdapter(responsesAdapter);

    }
}
