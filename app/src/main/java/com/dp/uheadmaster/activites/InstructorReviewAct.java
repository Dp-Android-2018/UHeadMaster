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
import android.transition.Explode;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.InstructorQuestionAnswerAdapter;
import com.dp.uheadmaster.adapters.InstructorReviewAdapter;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.ReviewModel;
import com.dp.uheadmaster.models.request.AddAnnouncementRequest;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.models.response.InstructorReviewsResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 18/10/2017.
 */

public class InstructorReviewAct extends AppCompatActivity {

    @Nullable @BindView(R.id.recycler_reviews)
    RecyclerView reviewsRecycler;
    private FontChangeCrawler fontChanger;
    private int pageId=0;

    private ProgressDialog progressDialog;
    private int courseId;
    private SharedPrefManager sharedPrefManager;
    private ArrayList<ReviewModel>reviews;
    private InstructorReviewAdapter adapter;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    LinearLayoutManager mLayoutManager;
    private String next_page="";
    private int position=0;
    private ImageView ivEmptyView;
    Slide slide;
    private LinearLayout linearLayout;

    public class HandleScrolling extends RecyclerView.OnScrollListener{
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
                        position=totalItemCount;
                        getInstructorReviews();
                    }
                }
            }
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /*if(Build.VERSION.SDK_INT>21){
            slide=new Slide();
            slide.setDuration(2000);
            getWindow().setEnterTransition(slide);
        }*/
        setContentView(R.layout.instructor_reviews_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ButterKnife.bind(InstructorReviewAct.this);

        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        courseId=getIntent().getIntExtra("cid",0);
        //courseId=23;
        System.out.println("Review Course Id :"+courseId);
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
        reviews=new ArrayList<>();
        linearLayout=(LinearLayout)findViewById(R.id.content);
        ivEmptyView=(ImageView)findViewById(R.id.iv_reviews_empty) ;
        mLayoutManager=new LinearLayoutManager(getApplicationContext());
        reviewsRecycler.setLayoutManager(mLayoutManager);
        reviewsRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter=new InstructorReviewAdapter(getApplicationContext(),reviews);
        reviewsRecycler.setAdapter(adapter);
        reviewsRecycler.setOnScrollListener(new HandleScrolling());
        getInstructorReviews();
    }


    public void getInstructorReviews(){
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog = ConfigurationFile.showDialog(InstructorReviewAct.this);


            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);


            Call<InstructorReviewsResponse> call = apiService.getInstructorReviews(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), courseId,pageId);
            call.enqueue(new Callback<InstructorReviewsResponse>() {
                @Override
                public void onResponse(Call<InstructorReviewsResponse> call, Response<InstructorReviewsResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        InstructorReviewsResponse instructorReviewsResponse=response.body();
                        if (instructorReviewsResponse.getStatus() == 200) {

                            for(ReviewModel reviewModel:instructorReviewsResponse.getRates())
                                reviews.add(reviewModel);

                            if(reviews.isEmpty()){
                                ivEmptyView.setVisibility(View.VISIBLE);
                                reviewsRecycler.setVisibility(View.GONE);
                            }else {
                                ivEmptyView.setVisibility(View.GONE);
                                reviewsRecycler.setVisibility(View.VISIBLE);
                            }

                            loading = true;
                            if (!instructorReviewsResponse.getNextPageUrl().equals("")) {
                                next_page = instructorReviewsResponse.getNextPageUrl();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = "";

                            }

                            adapter.notifyDataSetChanged();

                        } else {
                            // parse the response body …
                            Snackbar.make(linearLayout, instructorReviewsResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                            System.out.println("error Code message :" + instructorReviewsResponse.getMessage());


                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<InstructorReviewsResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            Snackbar.make(linearLayout,getString(R.string.check_internet_connection),Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reviews=null;
        adapter=null;
        slide=null;
    }
}
