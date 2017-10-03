package com.dp.uheadmaster.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.CoursesListAdapter;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.response.CourseListResponse;
import com.dp.uheadmaster.models.response.CourseResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 06/09/2017.
 */

public class CoursesListFrag extends Fragment {

    private SharedPrefManager sharedPrefManager;
    private static GridView gvCoursesList;
    private ProgressDialog progressDialog;
    private CoursesListAdapter coursesListAdapter;
    private static List<CourseModel> CoursesList = new ArrayList<>();
    private String subCategoryTitle = "";
    private int subCategoryID = -1;
    private int reqestType = -1;
    int myLastVisiblePos;// global variable of activity
    private String next_page;
    private int pageId = 0;
    private boolean isLoading;
    private static int position=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_course_list_layout,container,false);

        if(getArguments()!=null) {
            subCategoryID = getArguments().getInt("sub_category_id");
            reqestType = getArguments().getInt("reqest_type");
            subCategoryTitle = getArguments().getString("sub_category_title", "");
        }
        initializeUi(v);
        if (reqestType != -1) {
            getCoursesList();
        }
        return v;
    }
    public void initializeUi(View v){
        sharedPrefManager = new SharedPrefManager(getActivity());
        gvCoursesList = (GridView) v.findViewById(R.id.gridview_courses_list);
       myLastVisiblePos = gvCoursesList.getFirstVisiblePosition();
        gvCoursesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int currentFirstVisPos = view.getFirstVisiblePosition();
                if(currentFirstVisPos > myLastVisiblePos  ) {
                    if(firstVisibleItem + visibleItemCount >= totalItemCount-1) {
                        if (!isLoading && next_page != null) {
                            isLoading = true;
                            if (reqestType != -1) {
                                position=totalItemCount;
                                getCoursesList();
                            }
                        }

                    }
                }
                if(currentFirstVisPos < myLastVisiblePos) {
                    //scroll up
                    //Toast.makeText(getActivity().getApplicationContext(), "Scroll Up:"+totalItemCount, Toast.LENGTH_SHORT).show();
                }
                myLastVisiblePos = currentFirstVisPos;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        CoursesList = new ArrayList<>();
        myLastVisiblePos=0;
        next_page=null;
        pageId = 0;
        position=0;
        isLoading=false;
    }


    public void getCoursesList() {
        switch (reqestType) {
            case 1://best seller
                getBestSellerCourses();
                break;
            case 2://explore courses
                getExploreCourses();
                break;
            case 3://sub category
                if (subCategoryID != -1) {
                    getSubCategoryCourses();
                }
                break;
            case 4://free courses
                getFreeCourses();
                break;
            case 5://paid courses
                getPaidCourses();
                break;
        }
    }

    public void getFreeCourses() {
        if (NetWorkConnection.isConnectingToInternet(getActivity())) {
            progressDialog = ConfigurationFile.showDialog( getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CourseResponse> call = apiService.getFreeCourses(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),pageId);
            System.out.println("FreeCourses / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("FreeCourses / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CourseResponse>() {
                @Override
                public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            CourseResponse CourseResponse = response.body();
                            // Toasty.success(getContext(), "Done! cat.Num = " + CourseResponse.getCoursesList().size(), Toast.LENGTH_LONG, true).show();
                            for (int i=0;i<CourseResponse.getCoursesList().size();i++)
                                  CoursesList.add(CourseResponse.getCoursesList().get(i));
                            notifyAdapter();
                            gvCoursesList.setSelection(position);
                            isLoading = false;

                            if (CourseResponse.getNextPage() != null) {
                                next_page = CourseResponse.getNextPage();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = null;
                            }

                        } else {
                            // parse the response body …
                            System.out.println("FreeCourses /error Code message :" + response.body().getMessage());
                            Toasty.error( getActivity(), response.body().getMessage(), Toast.LENGTH_LONG, true).show();
                            getPaidCourses();
                        }


                    } catch (Exception ex) {
                        Toasty.error( getActivity(), ex.getMessage(), Toast.LENGTH_LONG, true).show();
                        getPaidCourses();
                    }
                }

                @Override
                public void onFailure(Call<CourseResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error( getActivity(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("FreeCourses / Fialer :" + t.getMessage());
                    getPaidCourses();

                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    public void getPaidCourses() {
        if (NetWorkConnection.isConnectingToInternet(getActivity())) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CourseResponse> call = apiService.getPaidCourses(ConfigurationFile.ConnectionUrls.HEAD_KEY,ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),pageId);
            System.out.println("PaidCourses / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("PaidCourses / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CourseResponse>() {
                @Override
                public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            CourseResponse CourseResponse = response.body();
                            for (int i=0;i<CourseResponse.getCoursesList().size();i++)
                                CoursesList.add(CourseResponse.getCoursesList().get(i));
                            notifyAdapter();
                            gvCoursesList.setSelection(position);
                            isLoading = false;

                            if (CourseResponse.getNextPage() != null) {
                                next_page = CourseResponse.getNextPage();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = null;
                            }
                        } else {
                            // parse the response body …
                            System.out.println("PaidCourses /error Code message :" + response.body().getMessage());
                            Toasty.error(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG, true).show();
                        }

                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CourseResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(getActivity(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("PaidCourses / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }
    public void getExploreCourses() {
        if (NetWorkConnection.isConnectingToInternet(getActivity())) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CourseResponse> call = apiService.getExploreCourses(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),pageId);
            System.out.println("ExploreCourses / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("ExploreCourses / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CourseResponse>() {
                @Override
                public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            CourseResponse CourseResponse = response.body();
                            // Toasty.success(getContext(), "Done! cat.Num = " + CourseResponse.getCoursesList().size(), Toast.LENGTH_LONG, true).show();
                            for (int i=0;i<CourseResponse.getCoursesList().size();i++)
                                CoursesList.add(CourseResponse.getCoursesList().get(i));
                            notifyAdapter();
                            gvCoursesList.setSelection(position);
                            isLoading = false;

                            if (CourseResponse.getNextPage() != null) {
                                next_page = CourseResponse.getNextPage();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = null;
                            }
                        } else {
                            // parse the response body …
                            System.out.println("ExploreCourses /error Code message :" + response.body().getMessage());
                            Toasty.error(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG, true).show();
                        }

                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CourseResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(getActivity(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("Explore Courses / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    public void getBestSellerCourses() {
        if (NetWorkConnection.isConnectingToInternet(getActivity())) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CourseResponse> call = apiService.getBestSellers(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),pageId);
            System.out.println("ExploreCourses / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("ExploreCourses / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CourseResponse>() {
                @Override
                public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            CourseResponse CourseResponse = response.body();
                            // Toasty.success(getContext(), "Done! cat.Num = " + CourseResponse.getCoursesList().size(), Toast.LENGTH_LONG, true).show();
                            for (int i=0;i<CourseResponse.getCoursesList().size();i++)
                                CoursesList.add(CourseResponse.getCoursesList().get(i));

                            notifyAdapter();
                           gvCoursesList.setSelection(position);
                            isLoading = false;

                            if (CourseResponse.getNextPage() != null) {
                                next_page = CourseResponse.getNextPage();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = null;
                            }

                        } else {
                            // parse the response body …
                            System.out.println("ExploreCourses /error Code message :" + response.body().getMessage());
                            Toasty.error(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG, true).show();
                        }

                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CourseResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(getActivity(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("Explore Courses / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    private void getSubCategoryCourses() {
        if (NetWorkConnection.isConnectingToInternet(getActivity())) {
            progressDialog = ConfigurationFile.showDialog(getActivity());
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CourseListResponse> call = apiService.getCoursesList(ConfigurationFile.ConnectionUrls.HEAD_KEY,ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), subCategoryID,pageId);
            call.enqueue(new Callback<CourseListResponse>() {
                @Override
                public void onResponse(Call<CourseListResponse> call, Response<CourseListResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {

                        System.out.println("Courses by Sub Category Body : " + response.body().getCoursesList());
                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            CourseListResponse CourseResponse = response.body();
                            CoursesList = CourseResponse.getCoursesList();
                            notifyAdapter();
                            gvCoursesList.setSelection(position);
                            isLoading = false;

                            if (CourseResponse.getNextPagePath() != null) {
                                next_page = CourseResponse.getNextPagePath();
                                pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
                            } else {
                                next_page = null;
                            }
                           // Toast.makeText(getActivity().getApplicationContext(), "Next:"+next_page, Toast.LENGTH_SHORT).show();

                        } else {
                            // parse the response body …
                            System.out.println("Courses by Sub Category /error Code message :" + response.body().getMessage());
                            Toasty.error(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG, true).show();
                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CourseListResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(getActivity(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("Courses by Sub Category / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    private void notifyAdapter() {
        coursesListAdapter = new CoursesListAdapter(getActivity(), CoursesList);
        gvCoursesList.setAdapter(coursesListAdapter);
    }

}
