package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
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
import com.dp.uheadmaster.adapters.AnnouncementCommentsAdapter;
import com.dp.uheadmaster.adapters.ResponsesAdapter;
import com.dp.uheadmaster.models.Announcement;
import com.dp.uheadmaster.models.AnnouncementData;
import com.dp.uheadmaster.models.Answer;
import com.dp.uheadmaster.models.Comment;
import com.dp.uheadmaster.models.CourseData;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Question;
import com.dp.uheadmaster.models.request.AddAnnouncementCommentRequest;
import com.dp.uheadmaster.models.request.AddQuestionAnswerRequest;
import com.dp.uheadmaster.models.response.AnswersResponse;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.models.response.GetAnnouncementCommentsResponse;
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

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 29/10/2017.
 */

public class AnnouncementDetails extends AppCompatActivity {
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private RecyclerView recyclerQustions;
    private LinearLayoutManager mLayoutManager;
    private Question question;
    private CourseModel courseModel;
    private TextView emptyView;
    private AnnouncementCommentsAdapter announcementCommentsAdapter;
    private int courseId = -1;
    private Button btnAnswerPost;
    private EditText edAnswer;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private ArrayList<Comment>comments;
    private FontChangeCrawler fontChanger;
    private AnnouncementData announcementData;
    private String next_page="";
    private int pageId=0;
    private ImageView ivCouseName;
    private TextView tvCourseName;
    private RelativeLayout rlCourseInfo;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responses_layout);
        linearLayout=(LinearLayout)findViewById(R.id.content);
        sharedPrefManager = new SharedPrefManager(this);
        if((AnnouncementData) getIntent().getSerializableExtra("Announcement")!=null) {
            announcementData = (AnnouncementData) getIntent().getSerializableExtra("Announcement");
        }
        else {

           /* HashMap<String,String> hashMap = (HashMap<String,String>) getIntent().getSerializableExtra("ANNOUNCEMENT_DATA");
            Toast.makeText(this, "Data:"+hashMap.get("type"), Snackbar.LENGTH_LONG).show();*/


           try {
               rlCourseInfo=(RelativeLayout)findViewById(R.id.rl_course_info) ;
               ivCouseName=(ImageView)findViewById(R.id.iv_course_logo) ;
               tvCourseName=(TextView)findViewById(R.id.tv_course_name) ;
               rlCourseInfo.setVisibility(View.VISIBLE);
               String obj = getIntent().getStringExtra("ANNOUNCEMENT_DATA");
               JSONObject object=new JSONObject(obj);
               JsonParser parser = new JsonParser();
               JsonElement mJson = parser.parse(object.get("announcement").toString());
               JsonElement mJson1 = parser.parse(object.get("course").toString());
               System.out.println("Course Info :"+mJson1.toString());
               Gson gson = new Gson();
               announcementData = gson.fromJson(mJson, AnnouncementData.class);
               courseModel=gson.fromJson(mJson1,CourseModel.class);
               tvCourseName.setText(courseModel.getTitle());
               if(courseModel.getImagePath()!=null && !courseModel.getImagePath().equals(""))
               Picasso.with(getApplicationContext()).load(courseModel.getImagePath()).into(ivCouseName);
           }catch (JSONException ex){
               System.out.println("Exception Message :"+ex.getMessage());
               ex.printStackTrace();
           }
        }
       // Toast.makeText(this, "Data:"+announcementData.getId(), Snackbar.LENGTH_LONG).show();

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
       // getCourseQuestionsAnswers(false);
        initEventDriven();
    }

    private void initEventDriven() {
        btnAnswerPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = AnnouncementDetails.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                String comment = edAnswer.getText().toString();
                if (!comment.equals("") && comment != null) {
                    addAnnounceMentComment(comment);
                } else {
                   // Toasty.error(AnnouncementDetails.this,  getString(R.string.enter_all_fields), Snackbar.LENGTH_LONG, true).show();
                    Snackbar.make(linearLayout, getString(R.string.enter_all_fields), Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

   /* private void addAnswerAPI(String answer) {
        if (NetWorkConnection.isConnectingToInternet(AnnouncementDetails.this)) {
            progressDialog = ConfigurationFile.showDialog(AnnouncementDetails.this);

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
                            Toasty.success(AnnouncementDetails.this, defaultResponse.getMessage(), Snackbar.LENGTH_LONG, true).show();
                            edAnswer.setText("");
                            getCourseQuestionsAnswers(true);

                        } else {
                            // parse the response body …
                            System.out.println("Add Answer error Code message :" + response.body().getMessage());
                            Toasty.error(AnnouncementDetails.this, defaultResponse.getMessage(), Snackbar.LENGTH_LONG, true).show();
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

                    Toasty.error(AnnouncementDetails.this, t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }*/

    public void initializeUi() {
        comments=new ArrayList<>();

        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        recyclerQustions = (RecyclerView) findViewById(R.id.recycler1);
        emptyView = (TextView) findViewById(R.id.empty_view);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerQustions.setLayoutManager(mLayoutManager);
        recyclerQustions.setItemAnimator(new DefaultItemAnimator());

        edAnswer = (EditText) findViewById(R.id.ed_answer);
        btnAnswerPost = (Button) findViewById(R.id.btn_answer_post);
        getAnnouncementsComments(false);

        recyclerQustions.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading&&!next_page.equals(""))
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            getAnnouncementsComments(false);
                        }
                    }
                }
            }
        });


    }


    /*public void getCourseQuestionsAnswers(final boolean addAnswer) {
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
                                //    Toasty.success(getApplicationContext()," "+answers.size(),Snackbar.LENGTH_LONG).show();
                                notifyAdapter(addAnswer);
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                                emptyView.setText(R.string.no_responses);
                                recyclerQustions.setVisibility(View.GONE);
                            }

                        } else {
                            //Toasty.error(getActivity().getApplicationContext(),questionsResponse.getMessage(),Snackbar.LENGTH_LONG).show();
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
                    Toasty.error(getApplicationContext(), t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                }
            });


        } else {
            ConfigurationFile.hideDialog(progressDialog);
            Toasty.error(getApplicationContext().getApplicationContext(), getResources().getString(R.string.internet_message), Snackbar.LENGTH_LONG).show();
        }
    }*/

    public void getAnnouncementsComments(final boolean isAnswerAdded) {
      //  Toast.makeText(this, "anId:"+announcementData.getId(), Snackbar.LENGTH_LONG).show();
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog = ConfigurationFile.showDialog(this);


            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);

            Call<GetAnnouncementCommentsResponse> call = apiService.getAnnouncementComments(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), announcementData.getId(), pageId);
            call.enqueue(new Callback<GetAnnouncementCommentsResponse>() {
                @Override
                public void onResponse(Call<GetAnnouncementCommentsResponse> call, Response<GetAnnouncementCommentsResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    GetAnnouncementCommentsResponse getAnnouncementCommentsResponse = response.body();
                    try {



                        if (getAnnouncementCommentsResponse.getStatusCode() == 200) {

                           // Toast.makeText(AnnouncementDetails.this, ""+getAnnouncementCommentsResponse.getComments().size(), Snackbar.LENGTH_LONG).show();

                            if(isAnswerAdded){
                                comments.add(getAnnouncementCommentsResponse.getComments().get(getAnnouncementCommentsResponse.getComments().size()-1));
                            }else {
                                for (int i = 0; i < getAnnouncementCommentsResponse.getComments().size(); i++) {

                                    comments.add(getAnnouncementCommentsResponse.getComments().get(i));
                                }
                            }


                          loading=true;
                            if (!getAnnouncementCommentsResponse.getNextPageUrl().equals("")) {

                                next_page = getAnnouncementCommentsResponse.getNextPageUrl();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                                System.out.println("Page Id :"+pageId);
                            } else {
                                next_page = "";

                            }

                            notifyAdapter(isAnswerAdded);

                        }
                    } catch (NullPointerException ex) {
                        System.out.println("Catch Exception1:" + ex.getMessage());


                    } catch (Exception ex) {
                        System.out.println("Catch Exception2:" + ex.getMessage());
                    }


                }

                @Override
                public void onFailure(Call<GetAnnouncementCommentsResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    //Toasty.error(AnnouncementDetails.this, t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
         //   Toasty.warning(AnnouncementDetails.this, getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG).show();
            Snackbar.make(linearLayout, getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG).show();
        }
    }

    public void notifyAdapter(boolean isAnswerAdded) {
        if(isAnswerAdded){
            recyclerQustions.scrollToPosition(comments.size());
        }
        announcementCommentsAdapter = new AnnouncementCommentsAdapter(getApplicationContext(), announcementData, comments);
        recyclerQustions.setAdapter(announcementCommentsAdapter);

    }





    public void addAnnounceMentComment(String comment){
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog = ConfigurationFile.showDialog(AnnouncementDetails.this);

            AddAnnouncementCommentRequest addAnnouncementCommentRequest=new AddAnnouncementCommentRequest(announcementData.getId(),comment);
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);


            Call<DefaultResponse> call = apiService.addAnnouncementComment(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), addAnnouncementCommentRequest);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        DefaultResponse defaultResponse=response.body();
                        if (defaultResponse.getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                           // Toasty.success(AnnouncementDetails.this,getString(R.string.add_comment), Snackbar.LENGTH_LONG, true).show();
                            Snackbar.make(linearLayout,getString(R.string.add_comment), Snackbar.LENGTH_LONG).show();
                            edAnswer.setText("");
                            getAnnouncementsComments(true);

                        } else {
                            // parse the response body …
                            System.out.println("error Code message :" + defaultResponse.getMessage());


                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                   // Toasty.error(AnnouncementDetails.this, t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                    Snackbar.make(linearLayout,t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
          //  Toasty.warning(AnnouncementDetails.this,getString(R.string.check_internet_connection),Snackbar.LENGTH_LONG).show();
            Snackbar.make(linearLayout,getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG).show();
        }
    }

}
