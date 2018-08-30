package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.ProfileSpinnerAdapter;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.request.RegisterRequest;
import com.dp.uheadmaster.models.request.UserDataSocialMediaLogin;
import com.dp.uheadmaster.models.response.RegisterResponse;
import com.dp.uheadmaster.models.response.UserDataLoginResponse;
import com.dp.uheadmaster.notification.MyFirebaseInstanceIdService;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.gson.JsonElement;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpAct extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener , AdapterView.OnItemSelectedListener{

    private EditText edName, edEmail, edPassword, edRePassword;
    private Button btnSignUp;
    private ArrayList<String>keys;
    private TextView tvSignin;
    private ImageView imgFB, imgGoogle, imgTwt, imgLinked;
    private String name = "", email = "", password = "", repassword = "";
    private ProgressDialog progressDialog,progressDialog2;
    private GoogleApiClient mGoogleApiClient;
    private TwitterLoginButton btnTwitterLogin;
    private LoginButton btnFacebookLogin;
    private CallbackManager callbackManager;
    private Spinner spCountryCodes;
    public static final String TWITTER_KEY = "gYfcyWDt04iRU0zVP0Wt6NgnV";
    public static final String TWITTER_SECRET = "2c6VHDYGabPnM7y6bzhckkaKU2FNjsD8blvVdCjNCnf8c5q3za";
    public static int checker = 1;
    private String deviceToken = null;
    public static Retrofit retrofit;
    TwitterSession session ;
    private String  userName;
    private FontChangeCrawler fontChanger;
    private ArrayList<String> values;
    private ProfileSpinnerAdapter profileSpinnerAdapter;
    private String selectedKey=null;
    private String selectedValue=null;
    private int selectedPosition=-1;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(getApplicationContext(), new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_up);

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)findViewById(android.R.id.content));
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)findViewById(android.R.id.content));
        }
        final MyFirebaseInstanceIdService mfs = new MyFirebaseInstanceIdService();
        FirebaseApp.initializeApp(getApplicationContext());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                mfs.onTokenRefresh();


            }
        });

        deviceToken = mfs.TOKEN;
        //hidden keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);   // open with animation


        initView();
        initializeGoogleSettings();
        initEventDriven();
    }

    public void initializeGoogleSettings() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initEventDriven() {
        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                startActivity(new Intent(getApplicationContext(), LoginAct.class));
                //finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edName.getText().toString().trim();
                email = edEmail.getText().toString().trim();
                password = edPassword.getText().toString().trim();
                repassword = edRePassword.getText().toString().trim();
                if (checkData(name, email, password, repassword,selectedKey)) {
                    register();
                }

            }
        });


        imgLinked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = 1;
                handleLinkedInLogin();

            }


        });

        imgTwt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = 2;
                btnTwitterLogin.performClick();
            }
        });
        btnTwitterLogin.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                session = result.data;
                userName = session.getUserName();


                System.out.println("Twitter Data userName:" + userName);
                System.out.println("Twitter Data Id:" + session.getUserId());


                TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new com.twitter.sdk.android.core.Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        System.out.println("Twitter Data Email:" + result.data);

                        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
                            progressDialog = ConfigurationFile.showDialog(SignUpAct.this);
                            twitterMediaLogin(String.valueOf(session.getUserId()), result.data,userName, deviceToken);
                        } else {
                            Snackbar.make(linearLayout, R.string.internet_message, Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        System.out.println("Error:" + exception.getMessage());
                    }
                });


            }

            @Override
            public void failure(TwitterException e) {
                System.out.println("Exception" + e.getMessage());
            }
        });


        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = 4;
                googleSignIn();
            }
        });

        imgFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = 3;
                btnFacebookLogin.performClick();

            }
        });

        btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                try {
                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                    String id = object.getString("id");

                                    System.out.println("Facebook Data name:" + name);
                                    System.out.println("Facebook Data email:" + email);
                                    System.out.println("Facebook Data birthday:" + birthday);
                                    System.out.println("Facebook Data id:" + id);

                                    if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
                                        progressDialog = ConfigurationFile.showDialog(SignUpAct.this);

                                        facebookMediaLogin(id, email,name, deviceToken);
                                    } else {
                                        Snackbar.make(linearLayout, R.string.internet_message, Snackbar.LENGTH_LONG).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                System.out.println("Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("Facebook Error:" + error.getMessage());

            }
        });
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 4);
    }

    private boolean checkData(String name, String email, String password, String repassword,String selectedKey) {
        if (!name.equals("")) {
            if(! selectedKey.equals("")) {
                if (!email.equals("")) {
                    if (isEmailValid(email)) {
                        if (!password.equals("")) {
                            if (password.equals(repassword)) {
                                return true;
                            } else {
                                edRePassword.setText("");
                                edPassword.setError(getString(R.string.password_not_match));
                                Snackbar.make(linearLayout, getString(R.string.password_not_match), Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            edPassword.setError(getString(R.string.password_required));
                            Snackbar.make(linearLayout, getString(R.string.password_required), Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        edEmail.setError(getString(R.string.email_format));
                        Snackbar.make(linearLayout, getString(R.string.email_format), Snackbar.LENGTH_LONG).show();

                    }
                } else {
                    edEmail.setError(getString(R.string.email_required));
                    Snackbar.make(linearLayout, getString(R.string.email_required), Snackbar.LENGTH_LONG).show();
                }
            }else {
                Snackbar.make(linearLayout,"Please Enter Country Code", Snackbar.LENGTH_LONG).show();
            }
        } else {
            edName.setError(getString(R.string.name_required));
            Snackbar.make(linearLayout, getString(R.string.name_required), Snackbar.LENGTH_LONG).show();
        }
        return false;
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void  geContryCodes(){
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog2 = ConfigurationFile.showDialog(this);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);


            Call<JsonElement> call = apiService.getCountryCodes(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    ConfigurationFile.hideDialog(progressDialog2);
                    JsonElement element=response.body();

                    try {

                        JSONObject object=new JSONObject(element.toString());
                        int status=object.getInt("status");
                        if (status == 200) {
                            JSONObject json=object.getJSONObject("countries_keys");
                            Iterator<String> iter = json.keys();
                            int i=0;
                            while (iter.hasNext()) {
                                String key = iter.next();
                                keys.add(key);
                                try {
                                    String value = json.getString(key);
                                   /* if(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_COUNTRY_KEY).equals(value))
                                    {
                                        selectedPosition=i;
                                    }*/
                                    values.add(value);
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                                i++;
                            }
                            profileSpinnerAdapter.notifyDataSetChanged();
                            //   spCountryCodes.setSelection(selectedPosition);
                            //Snackbar.success(this," "+values.size()+"\n"+keys.size(),Snackbar.LENGTH_LONG).show();
                            // setDataToUi();
                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog2);

                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            Snackbar.make(linearLayout,getString(R.string.check_internet_connection),Snackbar.LENGTH_LONG).show();
        }
    }

    private void initView() {
        linearLayout=(LinearLayout) findViewById(R.id.content);
        keys=new ArrayList<>();
        values=new ArrayList<>();
        spCountryCodes=(Spinner) findViewById(R.id.sp_country_code);
        spCountryCodes.setOnItemSelectedListener(this);
        profileSpinnerAdapter=new ProfileSpinnerAdapter(getApplicationContext(),values);
        spCountryCodes.setAdapter(profileSpinnerAdapter);
        edName = (EditText) findViewById(R.id.ed_sign_up_name);
        edEmail = (EditText) findViewById(R.id.ed_sign_up_email);
        edPassword = (EditText) findViewById(R.id.ed_sign_up_password);
        edRePassword = (EditText) findViewById(R.id.ed_sign_up_re_password);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        tvSignin = (TextView) findViewById(R.id.tv_signUp_signIn);
        imgFB = (ImageView) findViewById(R.id.img_fb);
        imgGoogle = (ImageView) findViewById(R.id.img_google);
        imgTwt = (ImageView) findViewById(R.id.img_twt);
        imgLinked = (ImageView) findViewById(R.id.img_linked);
        btnTwitterLogin = (TwitterLoginButton) findViewById(R.id.twitter_login);
        imgLinked = (ImageView) findViewById(R.id.img_linked);
        btnFacebookLogin = (LoginButton) findViewById(R.id.facebook_login_button);
        callbackManager = CallbackManager.Factory.create();
        btnFacebookLogin.setReadPermissions(Arrays.asList(
                "email"));

        geContryCodes();
    }


    public void register() {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {

            progressDialog = ConfigurationFile.showDialog(SignUpAct.this);
          //  Snackbar.make(this, ""+selectedKey

              //      , Snackbar.LENGTH_LONG).show();
            RegisterRequest registerRequest = new RegisterRequest(name, email, password,deviceToken,selectedKey);
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<RegisterResponse> call = apiService.register(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, registerRequest);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {


                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                           // Snackbar.success(SignUpAct.this, "Done!", Snackbar.LENGTH_LONG, true).show();
                            RegisterResponse registerResponse = response.body();
                            System.out.println("Response : " + registerResponse.getEmail());
                            ConfigurationFile.saveUserData(getApplicationContext(), registerResponse.getName(), registerResponse.getEmail(), registerResponse.getToken(),registerResponse.getMobile(), registerResponse.getType(),registerResponse.getId(),registerResponse.getImage(),registerResponse.getCountryKey(),registerResponse.getAbout(),registerResponse.getIsSubScribed(),0);
                            startActivity(new Intent(getApplicationContext(), MainAct.class));
                            finishAffinity();
                        } else {
                            // parse the response body …
                            System.out.println("error Code  message :" + response.body().getMessage());
                            Snackbar.make(linearLayout, response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }

                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                    ConfigurationFile.hideDialog(progressDialog);
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //overridePendingTransition(R.anim.left_in, R.anim.left_out);   // open with animation

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("Connection Failed");
    }


    public void handleLinkedInLogin() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                fetchLinkedInPersonalInfo();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
                System.out.println("Error" + error.toString());
            }
        }, true);
    }


    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE, Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Linked In
        if (checker == 1)
            LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);

        //Twitter Login
        if (checker == 2)
            btnTwitterLogin.onActivityResult(requestCode, resultCode, data);


        //FaceBook Login
        if (checker == 3)
            callbackManager.onActivityResult(requestCode, resultCode, data);


        //Google Login
        if (requestCode == 4) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            System.out.println("Google Data Name:" + acct.getDisplayName());
            System.out.println("Google Data Id:" + acct.getId());
            System.out.println("Google Data Email:" + acct.getEmail());
            System.out.println("Google Data Photo:" + acct.getPhotoUrl());

            if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
                progressDialog = ConfigurationFile.showDialog(SignUpAct.this);
                googleMediaLogin(acct.getId(), acct.getEmail(),acct.getDisplayName(), deviceToken);
            } else {
                Snackbar.make(linearLayout, R.string.internet_message, Snackbar.LENGTH_LONG).show();
            }

        } else {
            // Signed out, show unauthenticated UI.
            //   updateUI(false);
        }
    }

    public void fetchLinkedInPersonalInfo() {
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,public-profile-url,picture-url,email-address,picture-urls::(original))";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(getApplicationContext(), url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                System.out.println("Linked In Data:" + jsonObject.toString());
                try {
                    String fname = jsonObject.getString("firstName");
                    String lname = jsonObject.getString("lastName");
                    String pictureUrl = jsonObject.getString("pictureUrl");
                    String emailAddress = jsonObject.getString("emailAddress");
                    String id = jsonObject.getString("id");

                    System.out.println("Linked In id:" + id);
                    System.out.println("Linked In Fname:" + fname);
                    System.out.println("Linked In lname:" + lname);
                    System.out.println("Linked In pictureUrl:" + pictureUrl);
                    System.out.println("Linked In Email:" + emailAddress);

                    if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
                        progressDialog = ConfigurationFile.showDialog(SignUpAct.this);
                        linkedinMediaLogin(id, emailAddress, fname + " " + lname, deviceToken);
                    } else {
                        Snackbar.make(linearLayout, R.string.internet_message, Snackbar.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Success!
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
                System.out.println("Error:" + liApiError.toString());
            }
        });

    }

    public void linkedinMediaLogin(String id, String email, String name, String deviceToken) {
        retrofit = ApiClient.getClient();

        EndPointInterfaces apiServices = retrofit.create(EndPointInterfaces.class);
        UserDataSocialMediaLogin userDataSocialMediaLogin = new UserDataSocialMediaLogin(id, email, name, deviceToken);
        Call<UserDataLoginResponse> call = apiServices.LinkedInLogin(getResources().getString(R.string.server_key), userDataSocialMediaLogin);
        call.enqueue(new retrofit2.Callback<UserDataLoginResponse>() {
            @Override
            public void onResponse(Call<UserDataLoginResponse> call, Response<UserDataLoginResponse> response) {

                UserDataLoginResponse user = response.body();
                // Snackbar.make(SignUpAct.this, ""+user.getStatus(), Snackbar.LENGTH_LONG).show();
                try {

                    if(user.getStatus()==200) {
                        //Snackbar.success(SignUpAct.this, "Done!", Snackbar.LENGTH_LONG, true).show();
                        System.out.println("User Response token:" + user.getToken());
                        System.out.println("User Response name:" + user.getName());
                        System.out.println("User Response email:" + user.getEmail());
                        System.out.println("User Response type:" + user.getType());
                        System.out.println("User Response Id:" + user.getId());
                        ConfigurationFile.saveUserData(getApplicationContext(), user.getName(), user.getEmail(), user.getToken(), user.getMobile(), user.getType(),user.getId(),user.getImage(),user.getCountryKey(),user.getAbout(),user.getIsSubScribed(),0);
                        startActivity(new Intent(getApplicationContext(), MainAct.class));
                        finishAffinity();
                    }else {
                        Snackbar.make(linearLayout,user.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                    //  Snackbar.make(SignUpAct.this, "Success", Snackbar.LENGTH_LONG).show();


                    ConfigurationFile.hideDialog(progressDialog);
                }catch (NullPointerException ex){
                    ex.printStackTrace();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<UserDataLoginResponse> call, Throwable t) {
                System.out.println("Server Error");
                ConfigurationFile.hideDialog(progressDialog);

            }
        });
    }




    public void facebookMediaLogin(String id, String email, String name, String deviceToken) {
        retrofit = ApiClient.getClient();

        EndPointInterfaces apiServices = retrofit.create(EndPointInterfaces.class);
        UserDataSocialMediaLogin userDataSocialMediaLogin = new UserDataSocialMediaLogin(id, email, name, deviceToken);
        Call<UserDataLoginResponse> call = apiServices.facebookLogin(getResources().getString(R.string.server_key), userDataSocialMediaLogin);
        call.enqueue(new retrofit2.Callback<UserDataLoginResponse>() {
            @Override
            public void onResponse(Call<UserDataLoginResponse> call, Response<UserDataLoginResponse> response) {

                UserDataLoginResponse user = response.body();
                // Snackbar.make(SignUpAct.this, ""+user.getStatus(), Snackbar.LENGTH_LONG).show();
                try {

                    if(user.getStatus()==200) {
                       // Snackbar.success(SignUpAct.this, "Done!", Snackbar.LENGTH_LONG, true).show();
                        System.out.println("User Response token:" + user.getToken());
                        System.out.println("User Response name:" + user.getName());
                        System.out.println("User Response email:" + user.getEmail());
                        System.out.println("User Response type:" + user.getType());
                        ConfigurationFile.saveUserData(getApplicationContext(), user.getName(), user.getEmail(), user.getToken(), user.getMobile(), user.getType(),user.getId(),user.getImage(),user.getCountryKey(),user.getAbout(),user.getIsSubScribed(),0);
                        startActivity(new Intent(getApplicationContext(), MainAct.class));
                        finishAffinity();
                    }else {
                        Snackbar.make(linearLayout,user.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                    //  Snackbar.make(SignUpAct.this, "Success", Snackbar.LENGTH_LONG).show();


                    ConfigurationFile.hideDialog(progressDialog);
                }catch (NullPointerException ex){
                    ex.printStackTrace();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<UserDataLoginResponse> call, Throwable t) {
                System.out.println("Server Error");
                ConfigurationFile.hideDialog(progressDialog);

            }
        });
    }



    public void googleMediaLogin(String id, String email, String name, String deviceToken) {
        retrofit = ApiClient.getClient();

        EndPointInterfaces apiServices = retrofit.create(EndPointInterfaces.class);
        UserDataSocialMediaLogin userDataSocialMediaLogin = new UserDataSocialMediaLogin(id, email, name, deviceToken);
        Call<UserDataLoginResponse> call = apiServices.googleLogin(getResources().getString(R.string.server_key), userDataSocialMediaLogin);
        call.enqueue(new retrofit2.Callback<UserDataLoginResponse>() {
            @Override
            public void onResponse(Call<UserDataLoginResponse> call, Response<UserDataLoginResponse> response) {

                UserDataLoginResponse user = response.body();
                try {


                    // Snackbar.make(SignUpAct.this, ""+user.getStatus(), Snackbar.LENGTH_LONG).show();
                    if(user.getStatus()==200) {
                      //  Snackbar.success(SignUpAct.this, "Done!", Snackbar.LENGTH_LONG, true).show();
                        System.out.println("User Response token:" + user.getToken());
                        System.out.println("User Response name:" + user.getName());
                        System.out.println("User Response email:" + user.getEmail());
                        System.out.println("User Response type:" + user.getType());
                        ConfigurationFile.saveUserData(getApplicationContext(), user.getName(), user.getEmail(), user.getToken(),user.getMobile(), user.getType(),user.getId(),user.getImage(),user.getCountryKey(),user.getAbout(),user.getIsSubScribed(),0);
                        Intent i=new Intent(getApplicationContext(), MainAct.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(i);
                        finishAffinity();
                    }else {
                        Snackbar.make(linearLayout,user.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                    //  Snackbar.make(SignUpAct.this, "Success", Snackbar.LENGTH_LONG).show();


                    ConfigurationFile.hideDialog(progressDialog);

                }catch (NullPointerException ex){
                    ex.printStackTrace();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<UserDataLoginResponse> call, Throwable t) {
                System.out.println("Server Error");
                ConfigurationFile.hideDialog(progressDialog);

            }
        });
    }





    public void twitterMediaLogin(String id, String email, String name, String deviceToken) {
        retrofit = ApiClient.getClient();

        EndPointInterfaces apiServices = retrofit.create(EndPointInterfaces.class);
        UserDataSocialMediaLogin userDataSocialMediaLogin = new UserDataSocialMediaLogin(id, email, name, deviceToken);
        Call<UserDataLoginResponse> call = apiServices.twitterLogin(getResources().getString(R.string.server_key), userDataSocialMediaLogin);
        call.enqueue(new retrofit2.Callback<UserDataLoginResponse>() {
            @Override
            public void onResponse(Call<UserDataLoginResponse> call, Response<UserDataLoginResponse> response) {

                UserDataLoginResponse user = response.body();
                // Snackbar.make(SignUpAct.this, ""+user.getStatus(), Snackbar.LENGTH_LONG).show();
                try {


                    if(user.getStatus()==200) {
                     //   Snackbar.success(SignUpAct.this, "Done!", Snackbar.LENGTH_LONG, true).show();
                        System.out.println("User Response token:" + user.getToken());
                        System.out.println("User Response name:" + user.getName());
                        System.out.println("User Response email:" + user.getEmail());
                        System.out.println("User Response type:" + user.getType());
                        ConfigurationFile.saveUserData(getApplicationContext(), user.getName(), user.getEmail(), user.getToken(), user.getMobile(), user.getType(),user.getId(),user.getImage(),user.getCountryKey(),user.getAbout(),user.getIsSubScribed(),0);
                        startActivity(new Intent(getApplicationContext(), MainAct.class));
                        finishAffinity();
                    }else {
                        Snackbar.make(linearLayout,user.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                    //  Snackbar.make(SignUpAct.this, "Success", Snackbar.LENGTH_LONG).show();


                    ConfigurationFile.hideDialog(progressDialog);

                }catch (NullPointerException ex){
                    ex.printStackTrace();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<UserDataLoginResponse> call, Throwable t) {
                System.out.println("Server Error");
                ConfigurationFile.hideDialog(progressDialog);

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedKey=keys.get(position);
        selectedValue=values.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
