package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.TempAct;
import com.dp.uheadmaster.adapters.ProfileSpinnerAdapter;
import com.dp.uheadmaster.models.ProgressRequestBody;
import com.dp.uheadmaster.models.request.UpdateProfile;
import com.dp.uheadmaster.models.response.LoginResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by DELL on 27/08/2017.
 */

public class BasicProfileFrag extends Fragment implements ProgressRequestBody.UploadCallbacks , AdapterView.OnItemSelectedListener{
    private ImageView ivUserProfile;
    private Button btnUploadImage,btnSaveData;
    private String picturePath=null;
    private ProgressDialog progressDialog,progressDialog2;
    private SharedPrefManager sharedPrefManager;
    private Activity mHostActivity;
    private static final int request_code=123;
    private EditText etUserName,etMobileNumber,etDescription;
    private Spinner spCountryCodes;
    private ArrayList<String>keys;
    private ArrayList<String>values;
    private ProfileSpinnerAdapter profileSpinnerAdapter;
    private String selectedKey=null;
    private String selectedValue=null;
    private int selectedPosition=-1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.basic_profile_layout,container,false);
        initializeUi(v);
     //
        return v;
    }

    public void initializeUi(View v)
    {
        sharedPrefManager =new SharedPrefManager(getActivity().getApplicationContext());
        ivUserProfile=(ImageView)v.findViewById(R.id.iv_user_profile);
        spCountryCodes=(Spinner) v.findViewById(R.id.sp_country_code);
        spCountryCodes.setOnItemSelectedListener(this);
        keys=new ArrayList<>();
        values=new ArrayList<>();
        profileSpinnerAdapter=new ProfileSpinnerAdapter(mHostActivity,values);
        spCountryCodes.setAdapter(profileSpinnerAdapter);
        btnUploadImage=(Button)v.findViewById(R.id.btn_upload);
        btnSaveData=(Button)v.findViewById(R.id.btn_save);
        etUserName=(EditText)v.findViewById(R.id.et_user_name);
        etMobileNumber=(EditText)v.findViewById(R.id.et_mobile_number);
       // etCountryCode=(EditText)v.findViewById(R.id.et_country_code);
        etDescription=(EditText)v.findViewById(R.id.et_description);
        ivUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(hasPermission())
                {
                  openGallery();
                }
                else {
                    reqpermissions();
                }


            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picturePath==null)
                {
                    Toasty.error(mHostActivity,getString(R.string.choose_image),Toast.LENGTH_LONG).show();
                }
                else {

                    uploadImage(picturePath);
                }
            }
        });

        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ConfigurationFile.isEmpty(etUserName)&& !ConfigurationFile.isEmpty(etMobileNumber)) {
                    if(!(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_NAME).equals(etUserName.getText().toString().trim()) &&
                            sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_MOBILE).equals(etMobileNumber.getText().toString().trim())   &&
                            sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_COUNTRY_KEY).equals(selectedValue) &&
                            sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_Describtion).equals(etDescription.getText().toString().trim())) ) {
                        updateProfile();
                    }else {
                        Toasty.error(mHostActivity,getString(R.string.no_changes),Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toasty.error(mHostActivity,getString(R.string.fill_data),Toast.LENGTH_LONG).show();
                }
            }
        });
        geContryCodes();
    }

    public void  geContryCodes(){
        if (NetWorkConnection.isConnectingToInternet(mHostActivity)) {
            progressDialog2 = ConfigurationFile.showDialog(mHostActivity);

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
                                         if(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_COUNTRY_KEY).equals(value))
                                         {
                                                selectedPosition=i;
                                         }
                                         values.add(value);
                                     } catch (JSONException e) {
                                         // Something went wrong!
                                     }
                                     i++;
                                 }
                                profileSpinnerAdapter.notifyDataSetChanged();
                                 spCountryCodes.setSelection(selectedPosition);
                                 //Toasty.success(mHostActivity," "+values.size()+"\n"+keys.size(),Toast.LENGTH_LONG).show();
                                 setDataToUi();
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

                    Toasty.error(mHostActivity, t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            Toasty.warning(mHostActivity,getString(R.string.check_internet_connection),Toast.LENGTH_LONG).show();
        }
    }

    public void setDataToUi(){
        if(!sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_IMAGE_URL).equals("")){
            Picasso.with(getActivity().getApplicationContext()).load(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_IMAGE_URL)).into(ivUserProfile);
        }
        etUserName.setText(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_NAME));
        etMobileNumber.setText(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_MOBILE));
       // etCountryCode.setText(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_COUNTRY_KEY));
        etDescription.setText(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_Describtion));

    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            ivUserProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }



    private void uploadImage(String path)
    {
        if (NetWorkConnection.isConnectingToInternet(mHostActivity)) {
        File originalFile= new File(path);
        ProgressRequestBody fileBody = new ProgressRequestBody(originalFile, this);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), originalFile);
        progressDialog=new ProgressDialog(mHostActivity);
        progressDialog.setMessage("Uploading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", originalFile.getName(), fileBody);
        final EndPointInterfaces apiService =
                ApiClient.getClient().create(EndPointInterfaces.class);
        Call<JsonElement> call = apiService.uploadImages(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE,sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN),sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),body);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                ConfigurationFile.hideDialog(progressDialog);
                try {
                JsonElement element = response.body();
                System.out.println(element.toString());

                    JSONObject object = new JSONObject(element.toString());
                    int status = object.getInt("status");
                    if (status == 200) {
                        Toast.makeText(getActivity().getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        if(!(object.getString("url")==null && object.getString("url").equals("")))
                        sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_IMAGE_URL , object.getString("url"));
                        picturePath=null;
                    }
                }catch (JSONException ex){
                    ex.getMessage();
                }catch (NullPointerException ex){
                ex.printStackTrace();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                ConfigurationFile.hideDialog(progressDialog);
                Toast.makeText(getActivity().getApplicationContext(), "Error:"+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        } else {
            Toasty.warning(mHostActivity,getString(R.string.check_internet_connection),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        progressDialog.setProgress(100);
        progressDialog.dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHostActivity=activity;
    }


    public void updateProfile()
    {
        if (NetWorkConnection.isConnectingToInternet(mHostActivity)) {
            progressDialog2 = ConfigurationFile.showDialog(mHostActivity);

            UpdateProfile updateProfile=new UpdateProfile(etUserName.getText().toString().trim(),etMobileNumber.getText().toString().trim(),selectedKey,etDescription.getText().toString().trim());
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);


            Call<LoginResponse> call = apiService.profileUpdate(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE,sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN),sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),updateProfile);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog2);

                    try {


                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            Toasty.success(mHostActivity, getString(R.string.profile_updated), Toast.LENGTH_LONG, true).show();
                            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_NAME,etUserName.getText().toString().trim());
                            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_MOBILE,etMobileNumber.getText().toString().trim());
                            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_COUNTRY_KEY,selectedValue);
                            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_Describtion,etDescription.getText().toString().trim());
                        } else {
                            // parse the response body …
                            System.out.println("error Code message :" + response.body().getMessage());
                            Toasty.error(mHostActivity, response.body().getMessage(), Toast.LENGTH_LONG, true).show();


                        }
                    }catch (NullPointerException ex){
                    ex.printStackTrace();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog2);

                    Toasty.error(mHostActivity, t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            Toasty.warning(mHostActivity,getString(R.string.check_internet_connection),Toast.LENGTH_LONG).show();
        }
    }


    private boolean hasPermission()
    {
        int res=0;
        String[]permissions=new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for(String perms:permissions)
        {
            res=mHostActivity.checkCallingOrSelfPermission(perms);
            if(!(res== PackageManager.PERMISSION_GRANTED))
            {
                return  false;
            }
        }
        return true;
    }

    private void  reqpermissions(){

        String[]permissions=new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
           requestPermissions(permissions,request_code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed=true;
        switch (requestCode)
        {
            case 123:
                for(int res:grantResults){
                    allowed=allowed&&(res== PackageManager.PERMISSION_GRANTED);
                }
                break;


            default:
                allowed=false;
                //allowedvideo=false;


        }
        if(allowed)
        {
            openGallery();
        }
        else {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getActivity(), "Storage Permission Denided", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void openGallery(){
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       /* String selectedItem=values.get(position);
        System.out.println("Spinnners:"+selectedItem);
        Toasty.success(getActivity().getApplicationContext()," "+position,Toast.LENGTH_LONG).show();*/
         selectedKey=keys.get(position);
        selectedValue=values.get(position);
      //  Toasty.success(getActivity().getApplicationContext()," "+position,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
