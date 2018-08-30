package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.models.AddCourseReview;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.response.CategoriesResponse;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by لا اله الا الله on 23/08/2017.
 */
public class CourseWriteReviewFragment extends Fragment {
    private TextView tvFname,tvLname,tvContent,tvProvider,tvInstructor;
    private EditText etFname,etLname,etComment;
    private Button btnPost,btnCancel;
    private Activity mActivity;
    private FontChangeCrawler fontChanger;
    private SharedPrefManager sharedPrefManager;
    private RatingBar contentRate,instructorRate,providerRate;
    private int courseId;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_write_review_layout, container, false);
        courseId=getActivity().getIntent().getIntExtra("CID",0);
        initializeUi(v);
        return v;
    }
    public void initializeUi(View v){
        sharedPrefManager=new SharedPrefManager(getActivity().getApplicationContext());
        contentRate=(RatingBar)v.findViewById(R.id.contentRate) ;
        instructorRate=(RatingBar)v.findViewById(R.id.instructorRate) ;
        providerRate=(RatingBar)v.findViewById(R.id.providerRate);
        tvFname=(TextView)v.findViewById(R.id.tv_fname);
        tvLname=(TextView)v.findViewById(R.id.tv_lname);
        tvContent=(TextView)v.findViewById(R.id.tv_content);
        tvProvider=(TextView)v.findViewById(R.id.tv_provider);
        tvInstructor=(TextView)v.findViewById(R.id.tv_instructor);

        etFname=(EditText)v.findViewById(R.id.et_fname);
        etLname=(EditText)v.findViewById(R.id.et_lname);
        etComment=(EditText)v.findViewById(R.id.et_comment);

        btnPost=(Button)v.findViewById(R.id.btn_post);
        btnCancel=(Button)v.findViewById(R.id.btn_cancel);

        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)){
            tvFname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvLname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvContent.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvProvider.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvInstructor.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            etFname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font2"));
            etLname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font2"));
            etComment.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            btnPost.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            btnCancel.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));

        }else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)){
            tvFname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvLname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvContent.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvProvider.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvInstructor.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            etFname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            etLname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            etComment.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            btnPost.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            btnCancel.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(providerRate.getRating()!=0.0 && instructorRate.getRating()!=0.0 && contentRate.getRating()!=0.0)
                             addCourseReview();
                else
                    Snackbar.make(getActivity().findViewById(R.id.content),getActivity().getResources().getString(R.string.fill_data), Snackbar.LENGTH_LONG).show();
            }
        });
    }


    public void addCourseReview() {
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext(),getActivity().findViewById(R.id.content))) {

            System.out.println("Comment Data 1:"+etComment.getText().toString());
            AddCourseReview addCourseReview=new AddCourseReview(courseId,contentRate.getRating(),instructorRate.getRating(),providerRate.getRating(),ConfigurationFile.isEmpty(etComment)? null:etComment.getText().toString());
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<DefaultResponse> call = apiService.addReview(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),addCourseReview);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    //   ConfigurationFile.hideDialog(progressDialog);
                    //  Toast.makeText(getActivity().getApplicationContext(), "Closing Dialog", Snackbar.LENGTH_LONG).show();
                    try {


                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)

                            getActivity().finish();
                            Snackbar.make(getActivity().findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                        } else {
                            // parse the response body …
                            System.out.println("Category /error Code message :" + response.body().getMessage());
                            Snackbar.make(getActivity().findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    }catch (NullPointerException ex){
                        System.out.println("Catch 1:"+ex.getMessage());
                        ex.printStackTrace();
                    }catch (Exception ex){
                        System.out.println("Catch 2:"+ex.getMessage());
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    //  Toast.makeText(getActivity().getApplicationContext(), "Failiure Closing Dialog", Snackbar.LENGTH_LONG).show();
                    // ConfigurationFile.hideDialog(progressDialog);

                    //  Snackbar.error(getContext(), "Failure:"+t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                    System.out.println("Category / Fialer :" + t.getMessage());
                }
            });

        } else {
            // Toast.makeText(getActivity().getApplicationContext(), "Network Closing Dialog", Snackbar.LENGTH_LONG).show();
            //  ConfigurationFile.hideDialog(progressDialog);
        }
    }
}
