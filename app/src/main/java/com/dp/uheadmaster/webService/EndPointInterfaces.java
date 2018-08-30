package com.dp.uheadmaster.webService;

import com.dp.uheadmaster.models.AddAnswer;
import com.dp.uheadmaster.models.AddCourseReview;
import com.dp.uheadmaster.models.CourseContentModel;
import com.dp.uheadmaster.models.CourseIDRequest;
import com.dp.uheadmaster.models.request.SubScribeToCourse;
import com.dp.uheadmaster.models.request.AddAnnouncementCommentRequest;
import com.dp.uheadmaster.models.request.AddAnnouncementRequest;
import com.dp.uheadmaster.models.request.AddCourseQuestionRequest;
import com.dp.uheadmaster.models.request.AddQuestionAnswerRequest;
import com.dp.uheadmaster.models.request.AutomaticMessageReqest;
import com.dp.uheadmaster.models.request.ChangePasswordRequest;
import com.dp.uheadmaster.models.request.CourseLearnRequestModel;
import com.dp.uheadmaster.models.request.CoursePrerequirementsRequestModel;
import com.dp.uheadmaster.models.request.CourseTargetRequestModel;
import com.dp.uheadmaster.models.request.CreateCourseRequest;
import com.dp.uheadmaster.models.request.LastWatchedRequest;
import com.dp.uheadmaster.models.request.LoginRequest;
import com.dp.uheadmaster.models.request.RegisterRequest;
import com.dp.uheadmaster.models.request.ResetPasswordRequest;
import com.dp.uheadmaster.models.request.SubCategoryRequest;
import com.dp.uheadmaster.models.request.UpdateEmailRequest;
import com.dp.uheadmaster.models.request.UpdatePrivacyRequest;
import com.dp.uheadmaster.models.request.UpdateProfile;
import com.dp.uheadmaster.models.request.UserDataSocialMediaLogin;
import com.dp.uheadmaster.models.response.AnnounceMentResponse;
import com.dp.uheadmaster.models.response.AnswersResponse;
import com.dp.uheadmaster.models.response.CartListResponse;
import com.dp.uheadmaster.models.response.CategoriesResponse;
import com.dp.uheadmaster.models.response.CourseDetailsModel;
import com.dp.uheadmaster.models.response.CourseListResponse;
import com.dp.uheadmaster.models.response.CourseResponse;
import com.dp.uheadmaster.models.response.CourseReviewChartResponse;
import com.dp.uheadmaster.models.response.CreateCourseResponse;
import com.dp.uheadmaster.models.response.CreationProcessResponse;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.models.response.GetAllReviews;
import com.dp.uheadmaster.models.response.GetAnnouncementCommentsResponse;
import com.dp.uheadmaster.models.response.GetCourseNotification;
import com.dp.uheadmaster.models.response.InstructorCoursesResponse;
import com.dp.uheadmaster.models.response.InstructorResponse;
import com.dp.uheadmaster.models.response.InstructorReviewsResponse;
import com.dp.uheadmaster.models.response.LoginResponse;
import com.dp.uheadmaster.models.response.QuestionsResponse;
import com.dp.uheadmaster.models.response.RegisterResponse;
import com.dp.uheadmaster.models.response.ResetPasswordResponse;
import com.dp.uheadmaster.models.response.RoodMapResponse;
import com.dp.uheadmaster.models.response.SearchCoursesResponse;
import com.dp.uheadmaster.models.response.UserDataLoginResponse;
import com.dp.uheadmaster.models.response.WishListResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.google.gson.JsonElement;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by DELL on 04/04/2017.
 */

public interface EndPointInterfaces {
    @POST(ConfigurationFile.EndPoints.REGISTER)
    Call<RegisterResponse> register(@Header("Key") String key, @Header("Lang") String lang, @Body RegisterRequest registerRequest);

    @POST(ConfigurationFile.EndPoints.LOGIN)
    Call<LoginResponse> login(@Header("Key") String key, @Header("Lang") String lang, @Body LoginRequest loginRequest);

    @POST(ConfigurationFile.EndPoints.RESET_PASSWORD)
    Call<ResetPasswordResponse> resetPassword(@Header("Key") String key, @Header("Lang") String lang, @Body ResetPasswordRequest resetPasswordRequest);

    @POST(ConfigurationFile.EndPoints.LINKEDiN_LOGIN_URL)
    Call<UserDataLoginResponse> LinkedInLogin(@Header("Key") String key, @Body UserDataSocialMediaLogin userDataSocialMediaLogin);

    @POST(ConfigurationFile.EndPoints.FACEBOOK_LOGIN_URL)
    Call<UserDataLoginResponse> facebookLogin(@Header("Key") String key, @Body UserDataSocialMediaLogin userDataSocialMediaLogin);

    @POST(ConfigurationFile.EndPoints.TWITTER_LOGIN_URL)
    Call<UserDataLoginResponse> twitterLogin(@Header("Key") String key, @Body UserDataSocialMediaLogin userDataSocialMediaLogin);

    @POST(ConfigurationFile.EndPoints.GOOGLE_LOGIN_URL)
    Call<UserDataLoginResponse> googleLogin(@Header("Key") String key, @Body UserDataSocialMediaLogin userDataSocialMediaLogin);


    @POST(ConfigurationFile.EndPoints.CHANGE_PASSWORD_URL)
    Call<LoginResponse> changePassword(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body ChangePasswordRequest changePasswordRequest);

    @Multipart
    @POST(ConfigurationFile.EndPoints.CHANGE_PROFILE_IMAGE_URL)
    Call<JsonElement> uploadImages(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String auth, @Header("Id") int id, @Part MultipartBody.Part image);

    @POST(ConfigurationFile.EndPoints.UPDATE_PROFILE__URL)
    Call<LoginResponse> profileUpdate(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String auth, @Header("Id") int id, @Body UpdateProfile updateProfile);

    @GET(ConfigurationFile.EndPoints.CATEGORIES)
    Call<CategoriesResponse> getCategories(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Query("page") int page);

    @GET(ConfigurationFile.EndPoints.BEST_SELLERS)
    Call<CourseResponse> getBestSellers(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Query("page") int page);

    @GET(ConfigurationFile.EndPoints.EXPLORE_COURSES)
    Call<CourseResponse> getExploreCourses(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Query("page") int page);

    @GET(ConfigurationFile.EndPoints.COURSE_DETAILS + "{course_id}")
    Call<CourseDetailsModel> getCourseDetails(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Path("course_id") int CourseID);

    @GET(ConfigurationFile.EndPoints.WISH_LIST)
    Call<WishListResponse> getWishList(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Query("page") int page);

    @POST(ConfigurationFile.EndPoints.WISH_LIST)
    Call<DefaultResponse> addOrRemoveFromWishList(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body CourseIDRequest wishListRequest);

    @GET(ConfigurationFile.EndPoints.CART_LIST)
    Call<CartListResponse> getCartist(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Query("page") int page);

    @POST(ConfigurationFile.EndPoints.CART_LIST)
    Call<DefaultResponse> addOrRemoveFromCartList(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body CourseIDRequest wishListRequest);

    @POST(ConfigurationFile.EndPoints.SUB_CATEGORIES)
    Call<CategoriesResponse> getSubCategories(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body SubCategoryRequest subCategoryRequest, @Query("page") int page);

    @GET(ConfigurationFile.EndPoints.SUB_CATEGORIES_COURSES + "{id}")
    Call<CourseListResponse> getCoursesList(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Path("id") int sunCategoryID, @Query("page") int page);


    @GET(ConfigurationFile.EndPoints.SEARCH_FOR_COURSES_URL)
    Call<SearchCoursesResponse> coursesSearch(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id,
                                              @Query("keyword") String searchkeyword, @Query("category") int category, @Query("sub_category") int subCategory, @Query("page") int page);

    @GET(ConfigurationFile.EndPoints.FREE_COURSES)
    Call<CourseResponse> getFreeCourses(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Query("page") int page);

    @GET(ConfigurationFile.EndPoints.PAID_COURSES)
    Call<CourseResponse> getPaidCourses(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Query("page") int page);


    @GET(ConfigurationFile.EndPoints.INSTRUCTOR_DATA_URL + "/{id}")
    Call<InstructorResponse> getInstructorData(@Header("Key") String key, @Header("Lang") String lang, @Path("id") int Id);


    @GET(ConfigurationFile.EndPoints.COUNTRY_CODES_URL)
    Call<JsonElement> getCountryCodes(@Header("Key") String key, @Header("Lang") String lang);

    @GET(ConfigurationFile.EndPoints.REVIEWS_DATA_URL + "/{course_id}")
    Call<GetAllReviews> getAllReviews(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Ids, @Path("course_id") int Id, @Query("page") int page);

    @GET(ConfigurationFile.EndPoints.REVIEW_URL + "{course_id}")
    Call<CourseReviewChartResponse> getReviewChart(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Path("course_id") int couseId);

    @POST(ConfigurationFile.EndPoints.UPDATE_EMAIL)
    Call<DefaultResponse> updateEmail(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body UpdateEmailRequest updateEmailRequest);

    @POST(ConfigurationFile.EndPoints.UPDATE_PRIVACY)
    Call<LoginResponse> updatePrivacy(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body UpdatePrivacyRequest updatePrivacyRequest);

    @GET(ConfigurationFile.EndPoints.QUESTIONSURL)
    Call<QuestionsResponse> getCourseQuestions(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Path("course_id") int cId, @Query("page") int page);


    @GET(ConfigurationFile.EndPoints.QUESTIONSAnswersURL)
    Call<AnswersResponse> getCourseQuestionsAnswers(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Path("course_id") int cId, @Path("question_id") int QId, @Query("page") int page);

    @POST(ConfigurationFile.EndPoints.ADD_QUESTION_URL)
    Call<DefaultResponse> addCourseQuestions(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body AddCourseQuestionRequest addCourseQuestionRequest);

    @POST(ConfigurationFile.EndPoints.ADD_ANSWER_URL)
    Call<DefaultResponse> addQuestionsAnswer(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body AddQuestionAnswerRequest addQuestionAnswerRequest);


    @GET(ConfigurationFile.EndPoints.COURSE_CONTENTS_URL)
    Call<CourseContentModel> getCourseContent(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Path("id") int CId);

    @GET(ConfigurationFile.EndPoints.ANNOUNCEMENTS_DATA)
    Call<AnnounceMentResponse> getAnnouncements(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Path("course_id") int CId, @Query("page") int page);


    @GET(ConfigurationFile.EndPoints.VIDEO_LINKS)
    Call<JsonElement> getVideoLinksUrl(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Path("course_id") int CId, @Path("video_file") String videoFile);

    @POST(ConfigurationFile.EndPoints.Add_ANNOUNCEMENTS)
    Call<DefaultResponse> addAnnouncements(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body AddAnnouncementRequest addAnnouncementRequest);

    @POST(ConfigurationFile.EndPoints.Post_Announcement_Comment)
    Call<DefaultResponse> addAnnouncementComment(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body AddAnnouncementCommentRequest addAnnouncementCommentRequest);

    @GET(ConfigurationFile.EndPoints.Get_Announcement_Comments)
    Call<GetAnnouncementCommentsResponse> getAnnouncementComments(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Ids, @Path("announcement_id") int Id, @Query("page") int page);

    @GET(ConfigurationFile.EndPoints.Get_Instructor_Courses)
    Call<InstructorCoursesResponse> getInstructorCourses(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Ids, @Path("id") int Id, @Path("status") String status, @Query("page") int page);

    @POST(ConfigurationFile.EndPoints.LAST_WATCHED)
    Call<DefaultResponse> setLastVideoWatched(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Path("course_id") int courseID, @Body LastWatchedRequest lastWatchedRequest);

    @GET(ConfigurationFile.EndPoints.ROOD_MAP)
    Call<RoodMapResponse> getRoodMap(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id);

    @GET(ConfigurationFile.EndPoints.CRAETION_PROCESS)
    Call<CreationProcessResponse> getCreationProcess(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id);


    @POST(ConfigurationFile.EndPoints.ADD_ANSWER)
    Call<DefaultResponse> AddAnswer(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body AddAnswer addAnswer);

   /* @GET(ConfigurationFile.EndPoints.Get_Lang)
    Call<JsonElement> getLanguages(@Header("Key") String key, @Header("Lang") String lang,@Path("lang") String langs);*/

    @GET(ConfigurationFile.EndPoints.Get_Lang)
    Call<JsonElement> getLanguages(@Header("Key") String key, @Header("Lang") String lang, @Path("lang") String langs);


    @GET(ConfigurationFile.EndPoints.Get_Reviews)
    Call<InstructorReviewsResponse> getInstructorReviews(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Ids, @Path("course_id") int Cid, @Query("page") int page);

    @POST(ConfigurationFile.EndPoints.Become_Instructor)
    Call<DefaultResponse> becomeInstructor(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id);


    @GET(ConfigurationFile.EndPoints.MyCourses)
    Call<WishListResponse> getMyCourses(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Ids, @Query("page") int page);


    @DELETE(ConfigurationFile.EndPoints.Delete_User_Account)
    Call<DefaultResponse> deleteUserAccount(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Ids);


    @POST(ConfigurationFile.EndPoints.Add_To_Learn_GOALS)
    Call<DefaultResponse> addToLearn(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Ids, @Body CourseLearnRequestModel requestModel);

    @POST(ConfigurationFile.EndPoints.TARGET_GOALS)
    Call<DefaultResponse> addTarget(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Ids, @Body CourseTargetRequestModel requestModel);

    @POST(ConfigurationFile.EndPoints.PREREQUIREMENTS_GOALS)
    Call<DefaultResponse> addPreReqyirements(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Ids, @Body CoursePrerequirementsRequestModel requestModel);

    @POST(ConfigurationFile.EndPoints.Create_Course)
    Call<CreateCourseResponse> createCourse(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Ids, @Body CreateCourseRequest createCourseRequest);


    @POST(ConfigurationFile.EndPoints.AUTOMATIC_MESSAGE)
    Call<DefaultResponse> setAutoMaticMessage(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body AutomaticMessageReqest messageReqest);


    @GET(ConfigurationFile.EndPoints.Get_Instructor_DahBoard)
    Call<JsonElement> getInstructorDashboard(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Ids);

    @POST(ConfigurationFile.EndPoints.ENROLLED_COURSE)
    Call<DefaultResponse> courseEnrolled(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body CourseIDRequest request);

    @POST(ConfigurationFile.EndPoints.SUBSCRIBE_URL)
    Call<DefaultResponse> subScribe(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id);


    @POST(ConfigurationFile.EndPoints.UNSUBSCRIBE_URL)
    Call<DefaultResponse> unsubScribe(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id);


    @POST("clear_token")
    Call<DefaultResponse>clearToken(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String auth, @Header("Id") int Id);

    @POST(ConfigurationFile.EndPoints.COURSE_SUBSCRIBTION)
    Call<DefaultResponse> setCourseNotification(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id,@Body SubScribeToCourse request);

    @GET(ConfigurationFile.EndPoints.GET_COURSE_SUBSCRIBTION)
    Call<GetCourseNotification> getCourseNotification(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Path("course_id") int Cid);

    @POST(ConfigurationFile.EndPoints.ADD_COURSE_REVIEW)
    Call<DefaultResponse> addReview(@Header("Key") String key, @Header("Lang") String lang, @Header("Authorization") String Authorization, @Header("Id") int Id, @Body AddCourseReview addCourseReview);

}
