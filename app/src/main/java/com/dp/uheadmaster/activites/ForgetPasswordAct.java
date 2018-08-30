package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.request.ResetPasswordRequest;
import com.dp.uheadmaster.models.response.ResetPasswordResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordAct extends AppCompatActivity {


    private EditText edEmail;
    private Button btnReset;
    private String email = "";
    private ProgressDialog progressDialog;
    private FontChangeCrawler fontChanger;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        }
        //hidden keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);   // open with animation
        initView();
        initEventDriven();
    }

    private void initEventDriven() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edEmail.getText().toString().trim();
                 if (checkData(email)) {
                    resetPassword();
                }
             }
        });
    }

    private void resetPassword() {
        try{


        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog = ConfigurationFile.showDialog(ForgetPasswordAct.this);

            ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(email);
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<ResetPasswordResponse> call = apiService.resetPassword(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, resetPasswordRequest);
            call.enqueue(new Callback<ResetPasswordResponse>() {
                @Override
                public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {


                        System.out.println("Response : " + response.body());
                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            Snackbar.make(linearLayout, "Done!", Snackbar.LENGTH_LONG).show();
                            System.out.println("Response : " + response.body().getMessage());
                            finish();
                        } else {
                            // parse the response body …
                            System.out.println("error Code message :" + response.body().getMessage());
                            Snackbar.make(linearLayout, response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        }else {
            ConfigurationFile.hideDialog(progressDialog);
        }
        }catch (Exception e){
            System.out.println("Error in try : "+ e.getMessage());
            ConfigurationFile.hideDialog(progressDialog);

        }
    }

    private void initView() {
        linearLayout=(LinearLayout) findViewById(R.id.content);
        edEmail = (EditText) findViewById(R.id.ed_reset_email);
         btnReset= (Button) findViewById(R.id.btn_reset_password);

    }
    private boolean checkData(String email) {
        if (!email.equals("")) {
            if (isEmailValid(email)) {
                    return true;
            } else {
                edEmail.setError(getString(R.string.email_format));
                Snackbar.make(linearLayout, getString(R.string.email_format), Snackbar.LENGTH_LONG).show();

            }
        } else {
            edEmail.setError(getString(R.string.email_required));
            Snackbar.make(linearLayout, getString(R.string.email_required), Snackbar.LENGTH_LONG).show();
        }

        return false;
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
         overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // overridePendingTransition(R.anim.left_in, R.anim.left_out);   // open with animation

    }

}
