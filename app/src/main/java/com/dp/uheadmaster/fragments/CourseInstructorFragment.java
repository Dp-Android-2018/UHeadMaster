package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseDetailAct;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.interfaces.OnInstructorValueChanged;
import com.dp.uheadmaster.models.response.InstructorResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by لا اله الا الله on 23/08/2017.
 */
public class CourseInstructorFragment extends Fragment implements OnInstructorValueChanged {
    private static TextView tvInstructorName, tvInstructorSpecialization, tvInstructorDescription;
    private static ImageView ivInstructorImage;
    private ProgressDialog progressDialog;
    public static OnInstructorValueChanged delegate = null;
    public SharedPrefManager sharedPrefManager;
    private Activity mHostActivity;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_instructor_layout, container, false);
        initializeUi(v);
        if(sharedPrefManager.getIntegerFromSharedPrederances("InstructorId")!=0 && sharedPrefManager.getIntegerFromSharedPrederances("InstructorId")!= -1){
            getDataOfInstructor(getActivity().getApplicationContext(),sharedPrefManager.getIntegerFromSharedPrederances("InstructorId"));
        }

        return v;
    }

    public void initializeUi(View v) {
        sharedPrefManager =new SharedPrefManager(getActivity().getApplicationContext());
        ivInstructorImage = (ImageView) v.findViewById(R.id.iv_instructor_image);
        tvInstructorName = (TextView) v.findViewById(R.id.tv_instructor_name);
        tvInstructorSpecialization = (TextView) v.findViewById(R.id.tv_instructor_specialization);
        tvInstructorDescription = (TextView) v.findViewById(R.id.tv_instructor_description);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
            tvInstructorName.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvInstructorSpecialization.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font2"));
            tvInstructorDescription.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font2"));


        } else if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)) {
            tvInstructorName.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvInstructorSpecialization.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvInstructorDescription.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));

        }
    }

    public void getDataOfInstructor(final Context context, int instructord_id) {
        if (NetWorkConnection.isConnectingToInternet(context)) {
            progressDialog = ConfigurationFile.showDialog(mHostActivity);


            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);

            System.out.println("instructord_id : "+instructord_id);
            Call<InstructorResponse> call = apiService.getInstructorData(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, instructord_id);
            call.enqueue(new Callback<InstructorResponse>() {
                @Override
                public void onResponse(Call<InstructorResponse> call, Response<InstructorResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    InstructorResponse instructorResponse = response.body();
                    try {
                        //Toasty.error(getActivity().getApplicationContext(),"Success :"+response.body().getStatus(), Toast.LENGTH_LONG, true).show();
                        System.out.println("Response Instructor:" + new Gson().toJson(response));

                        if (instructorResponse.getStatus() == 200) {
                            showData(instructorResponse , context);

                        } else if (instructorResponse.getStatus() == -302) {
                            Toasty.error(context, instructorResponse.getMessage(), Toast.LENGTH_LONG, true).show();
                        } else if (response.body().getStatus() == -201) {
                            Toasty.error(context, instructorResponse.getMessage(), Toast.LENGTH_LONG, true).show();
                        }
                    } catch (NullPointerException ex) {
                        System.out.println("Catch Exception1:" + ex.getMessage());


                    } catch (Exception ex) {
                        System.out.println("Catch Exception2:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<InstructorResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(context, t.getMessage(), Toast.LENGTH_LONG, true).show();
                }
            });

        } else {
            Toasty.warning(context, getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    private void showData(InstructorResponse instructorResponse, Context context) {

        CourseInstructorFragment.tvInstructorName.setText(instructorResponse.getInstructor().getName());
        if (instructorResponse.getInstructor().getHeadline() != null)
            CourseInstructorFragment.tvInstructorSpecialization.setText(instructorResponse.getInstructor().getHeadline());
        else
            CourseInstructorFragment. tvInstructorSpecialization.setText("");

        if (instructorResponse.getInstructor().getAbout() != null)
            CourseInstructorFragment. tvInstructorDescription.setText(instructorResponse.getInstructor().getAbout());
        else
            CourseInstructorFragment. tvInstructorDescription.setText("");


        if (!instructorResponse.getInstructor().getImage().equals(""))
            Picasso.with(context).load(instructorResponse.getInstructor().getImage()).into(CourseInstructorFragment.ivInstructorImage);

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHostActivity=activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void getInstructorValue(Context context, int instructorId) {
        if (instructorId != -1 && instructorId != 0) {


            getDataOfInstructor(context, instructorId);

        }
    }
}
