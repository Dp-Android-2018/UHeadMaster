package com.dp.uheadmaster.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dp.uheadmaster.R;


import java.util.Locale;

/**
 * Created by Dell on 13/08/2017.
 */

public class ConfigurationFile {



    public static class GlobalVariables {
        public static final String USER_STUDENT_TYPE = "student";
        public static   String APP_LANGAUGE = "";
        public static   String APP_LANGAUGE_EN = "en";
        public static   String APP_LANGAUGE_AR = "ar";

    }


    public static class ShardPref {
        public static final String SHARD_PREF_NAME = "U_GEAD_MASTER";
        public static final String Langauge = "LANGAUGE";
        public static final String isFirstTime="ISFIRSTTIME";
        public static final String USER_TOKEN = "USER_TOKEN";
        public static final String USER_NAME = "USER_NAME";
        public static final String USER_EMAIL = "USER_EMAIL";
        public static final String USER_TYPE = "USER_TYPE";
        public static final String USER_MOBILE = "USER_MOBILE";
        public static final String USER_ID = "USER_ID";
        public static final String USER_IMAGE_URL = "IMAGE_URL";
        public static final String USER_COUNTRY_KEY = "COUNTRY_KEY";
        public static final String USER_Describtion = "Description";
        public static final String IS_SUBSCRIBED = "Subscribe";
        public static final String CONFIRMED = "Confirmed";
    }

    public static class ConnectionUrls {

        public static final String BASE_URL = "http://uheadmaster.com/api/";
       // public static final String BASE_URL = "http://192.168.1.22/master/uheadmaster/public/api/";
        public static final String HEAD_KEY = "lHmTZOLHUcDgWImY3fZDvMnCmsJg3IyCjYQolwDlVeFIxBdiXC4frsDXlR";


    }


    public static class EndPoints {
        public static final String LOGIN = "login";
        public static final String REGISTER = "register";
        public static final String RESET_PASSWORD = "password/sendLink";
        public static final String FACEBOOK_LOGIN_URL = "login/Facebook";
        public static final String GOOGLE_LOGIN_URL = "login/Google";
        public static final String TWITTER_LOGIN_URL = "login/Twitter";
        public static final String LINKEDiN_LOGIN_URL = "login/LinkedIn";
        public static final String CHANGE_PASSWORD_URL = "password/update";
        public static final String CHANGE_PROFILE_IMAGE_URL = "profile/image";
        public static final String UPDATE_PROFILE__URL = "profile/update";
        public static final String BEST_SELLERS = "courses/best-sellers";
        public static final String EXPLORE_COURSES = "courses/explore-courses";
        public static final String CATEGORIES = "categories";
        public static final String COURSE_DETAILS = "courses/";
        public static final String WISH_LIST = "wishlist";
        public static final String CART_LIST = "cart";
        public static final String SUB_CATEGORIES = "sub-categories";
        public static final String SUB_CATEGORIES_COURSES = "sub-categories/";
        public static final String SEARCH_FOR_COURSES_URL = "courses/search";
        public static final String FREE_COURSES = "courses/free-courses";
        public static final String PAID_COURSES = "courses/paid-courses";
        public static final String INSTRUCTOR_DATA_URL = "instructors";
        public static final String REVIEWS_DATA_URL = "courses/reviews";
        public static final String COUNTRY_CODES_URL = "countries_keys";
        public static final String REVIEW_URL = "courses/rates/";
        public static final String UPDATE_EMAIL = "profile/update_mail";
        public static final String UPDATE_PRIVACY = "profile/update_privacy";
        public static final String QUESTIONSURL = "courses/"+"{course_id}"+"/questions";
        public static final String QUESTIONSAnswersURL = "courses/{course_id}/questions/{question_id}/answers";
        public static final String ADD_QUESTION_URL = "questions/ask";
        public static final String ADD_ANSWER_URL = "questions/answer";
        public static final String COURSE_CONTENTS_URL = "courses/content/{id}";
        public static final String ANNOUNCEMENTS_DATA = "announcements/{course_id}";
        public static final String VIDEO_LINKS = "courses/videos/{course_id}/{video_file}";
        public static final String Add_ANNOUNCEMENTS = "announcements/create";
        public static final String Post_Announcement_Comment = "announcements/comments/comment";
        public static final String Get_Announcement_Comments = "announcements/{announcement_id}/comments";
        public static final String Get_Instructor_Courses = "instructor/{id}/courses/{status}";
        public static final String LAST_WATCHED = "courses/last-watched/{course_id}";
        public static final String ROOD_MAP = "roadmap";
        public static final String CRAETION_PROCESS = "creation_process";
        public static final String ADD_ANSWER="questions/answer";
        public static final String Get_Lang="langs/{lang}";
        public static final String Get_Reviews="courses/reviews/{course_id}";
        public static final String Become_Instructor="become-instructor";
        public static final String MyCourses="mycourses";
        public static final String Delete_User_Account="profile/delete";
        public static final String Add_To_Learn_GOALS="courses/add-to-learn";
        public static final String TARGET_GOALS="courses/add-target-students";
        public static final String PREREQUIREMENTS_GOALS="courses/add-prerequisites";
        public static final String Create_Course="courses/create";
        public static final String AUTOMATIC_MESSAGE = "courses/add-automatic-message";
        public static final String Get_Instructor_DahBoard = "instructor/dashboard_info";
        public static final String ENROLLED_COURSE = "courses/enroll";
        public static final String SUBSCRIBE_URL = "subscribe";
        public static final String UNSUBSCRIBE_URL = "un_subscribe";
        public static  final String COURSE_SUBSCRIBTION="subscription/set";
        public static  final String GET_COURSE_SUBSCRIBTION="subscription/{course_id}/get";
        public static  final String ADD_COURSE_REVIEW="courses/reviews/add";

        //add-to-learn
       // public static final String Get_Lang="langs";
    }

    public static ProgressDialog showDialog(Activity activity) {
        ProgressDialog dialog = null;
        try {
            dialog = new ProgressDialog(activity, R.style.full_screen_dialog) {
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.fill_dialog);
                  /*  getWindow().setLayout(LinearLayoutCompat.LayoutParams.FILL_PARENT,
                            LinearLayoutCompat.LayoutParams.FILL_PARENT);*/
                  /*  ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
                    Wave doubleBounce = new Wave();
                    progressBar.setIndeterminateDrawable(doubleBounce);*/
                }
            };

            dialog.setCancelable(false);
            dialog.show();


        } catch (Exception e) {
            System.out.println("Error in Loader : " + e.getMessage());
        }
        return dialog;
    }


    public static void hideDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            try {

                progressDialog.dismiss();
                progressDialog = null;
            }catch (IllegalArgumentException ex){
                ex.printStackTrace();
            }
        }
    }

    public static void saveUserData(Context context, String name, String email, String token, String mobile, String type, int id, String imgUrl, String countryKey, String description,String isSubScribed,
                                    int confirmed) {
        try {
            SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
            if (name != null && !name.equals("")) {
                sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_NAME, name);
            }
            if (email != null && !email.equals("")) {
                sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_EMAIL, email);
            }
            if (token != null && !token.equals("")) {
                sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN, token);
            }
            if (type != null && !type.equals("")) {
                sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_TYPE, type);
            }
            if (mobile != null && !mobile.equals("")) {
                sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_MOBILE, mobile);
            }

            if (imgUrl != null && !imgUrl.equals("")) {
                sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_IMAGE_URL, imgUrl);
            }

            if (countryKey != null && !countryKey.equals("")) {
                sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_COUNTRY_KEY, countryKey);
            }


            if (description != null && !description.equals("")) {
                sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.USER_Describtion, description);
            }

            if (isSubScribed != null && !isSubScribed.equals("")) {
                sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.IS_SUBSCRIBED, isSubScribed);
            }

            if (id != -1) {
                sharedPrefManager.addIntegerToSharedPrederances(ShardPref.USER_ID, id);
            }
            if (confirmed ==1 || confirmed ==0){
                sharedPrefManager.addIntegerToSharedPrederances(ShardPref.CONFIRMED, confirmed);
            }
        } catch (Exception e) {
            System.out.println("Error in save user data :" + e.getMessage());
        }

    }


    public static boolean isEmpty(EditText e) {
        if (e.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

}
