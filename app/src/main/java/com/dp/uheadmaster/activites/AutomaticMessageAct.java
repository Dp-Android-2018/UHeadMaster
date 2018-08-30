package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.fragments.LecturesFrag;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.request.AutomaticMessageReqest;
import com.dp.uheadmaster.models.request.LastWatchedRequest;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 18/10/2017.
 */

public class AutomaticMessageAct extends AppCompatActivity {
    private FontChangeCrawler fontChanger;
     @BindView(R.id.et_instructor_answer)
    EditText edWelcomeMessage;
    @BindView(R.id.et_congratulations_message)
    EditText edCongratsMessage;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    private int courseID = -1;
    private LinearLayout linearLayout;
    Slide slide;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*if(Build.VERSION.SDK_INT>21){
           slide=new Slide();
            slide.setDuration(2000);
            getWindow().setEnterTransition(slide);
        }*/

        setContentView(R.layout.act_automatic_message_layout);
        linearLayout=(LinearLayout)findViewById(R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ButterKnife.bind(this);

        if(getIntent().getExtras() != null){
            courseID = getIntent().getExtras().getInt("cid");

        }
        sharedPrefManager = new SharedPrefManager(getApplicationContext());

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)findViewById(android.R.id.content));
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)findViewById(android.R.id.content));
        }
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

    @OnClick(R.id.btn_automatic_save)
    public void sendAutomaticMesagesToServer() {

        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {

            String welecomeMessage = edWelcomeMessage.getText().toString();
            String congratsMessage = edCongratsMessage.getText().toString();
            if (!welecomeMessage.equals("") && !congratsMessage.equals("")) {
                progressDialog = ConfigurationFile.showDialog(AutomaticMessageAct.this);

                final EndPointInterfaces apiService = ApiClient.getClient().create(EndPointInterfaces.class);
                AutomaticMessageReqest messageReqest = new AutomaticMessageReqest(courseID, welecomeMessage, congratsMessage);

                Call<DefaultResponse> call = apiService.setAutoMaticMessage(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), messageReqest);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        ConfigurationFile.hideDialog(progressDialog);
                        try {

                            DefaultResponse element = response.body();
                            if (element.getStatus() == 200) {
                               // Toasty.success(AutomaticMessageAct.this, element.getMessage(), Snackbar.LENGTH_LONG, true).show();
                                Snackbar.make(linearLayout,element.getMessage(), Snackbar.LENGTH_LONG).show();
                                finish();
                            } else {
                              //  Toasty.error(AutomaticMessageAct.this, element.getMessage(), Snackbar.LENGTH_LONG, true).show();
                                Snackbar.make(linearLayout,element.getMessage(), Snackbar.LENGTH_LONG).show();

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

               // Toasty.error(AutomaticMessageAct.this, getString(R.string.enter_all_message), Snackbar.LENGTH_LONG, true).show();
                Snackbar.make(linearLayout,getString(R.string.enter_all_message), Snackbar.LENGTH_LONG).show();

            }
        } else {
           // Toasty.warning(AutomaticMessageAct.this, getString(R.string.internet_message), Snackbar.LENGTH_LONG, true).show();
            Snackbar.make(linearLayout,getString(R.string.internet_message), Snackbar.LENGTH_LONG).show();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        slide=null;
    }
}
