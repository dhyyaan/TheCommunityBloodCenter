package com.think360.alexbloodbank.api.interfaces;
import com.think360.alexbloodbank.api.data.Responce;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;


/**
 * Created by think360 on 18/04/17.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("log_in")
    Call<Responce.LoginResponce> login(@Field("devicetype") String devicetype, @Field("deviceid") String deviceid,
                                           @Field("Unamemail") String user_name, @Field("Upassword") String password);


    @Multipart
    @POST("register")
    Call<Responce.RegistrationResponce> registration(@Part("first_name") RequestBody first_name, @Part("last_name") RequestBody last_name, @Part("username") RequestBody username,
                                                         @Part("dob") RequestBody dob, @Part("email") RequestBody email,
                                                         @Part("phone") RequestBody phone, @Part("sms") RequestBody sms, @Part("password") RequestBody password,
                                                         @Part("confirm_pass") RequestBody confirm_pass, @Part("school") RequestBody school,
                                                         @Part("schoolyear") RequestBody schoolyear, @Part MultipartBody.Part profile_pic,
                                                         @Part("deviceid") RequestBody deviceid, @Part("devicetype") RequestBody devicetype,@Part("otherschool") RequestBody otherschool);

  @GET("select_school")
  Call<Responce.CountryResponce> getSchools();

    @FormUrlEncoded
    @POST("addonation")
    Call<Responce.AddDonationResponce> addonation(@Field("useridBD") String useridBD, @Field("locationBD") String locationBD,
                                                                                           @Field("dateBD") String dateBD);
     @FormUrlEncoded
    @POST("addvolunteer")
    Call<Responce.AddVolunteerResponce> addvolunteer(@Field("useridBD") String useridBD, @Field("vdateBD") String vdateBD, @Field("vhoursrBD") String vhoursrBD,
                                                     @Field("vfacultyBD") String vfacultyBD, @Field("vlocationBD") String vlocationBD);
  @FormUrlEncoded
  @POST("addrecruit")
  Call<Responce.RecruitResponce> addrecruit(@Field("useridBD") String useridBD, @Field("donor_first") String donor_first,
                                            @Field("donor_last") String donor_last
                                                   );
    @FormUrlEncoded
    @POST("addhost")
    Call<Responce.BloodResponce> addhost(@Field("useridBD") String useridBD, @Field("Hlocation") String Hlocation,
                                         @Field("HdateBD") String HdateBD, @Field("HcbcName") String HcbcName);
    @FormUrlEncoded
    @POST("hostlist")
    Call<Responce.HostListResponce> hostlist(@Field("useridBD") String useridBD);

  @FormUrlEncoded
  @POST("donorslist")
  Call<Responce.DonorsListResponce> donorslist(@Field("useridBD") String useridBD);

  @FormUrlEncoded
  @POST("volunteerlist")
  Call<Responce.HostListResponce> volunteerlist(@Field("useridBD") String useridBD);

  @FormUrlEncoded
  @POST("recruitlist")
  Call<Responce.RecruitListResponce> recruitlist(@Field("useridBD") String useridBD);

  @FormUrlEncoded
  @POST("notificationlist")
  Call<Responce.NotificationListResponce> notificationlist(@Field("useridBD") String useridBD,@Field("deviceid") String deviceid);

  @FormUrlEncoded
  @POST("changepassword")
  Call<Responce.ChangePasswordResponce> changepassword(@Field("userid") String userid,
                                                         @Field("changepass") String changepass, @Field("confirmpass") String confirmpass);
  @FormUrlEncoded
  @POST("Needhelp")
  Call<Responce.NeedHelpResponce> needhelp(@Field("useridBD") String useridBD,
                                           @Field("Firstname") String Firstname, @Field("Lastname") String Lastname,
                                           @Field("Uemail") String Uemail,
                                           @Field("Selschool") String Selschool, @Field("Comment") String Comment, @Field("otherschool") String otherschool);

  @Multipart
  @POST("editprofile")
  Call<Responce.EditProfileResponce> editprofile(@Part("useridBD") RequestBody  useridBD,
                                                 @Part("firstname") RequestBody firstname,
                                                 @Part("lastname") RequestBody lastname,
                                                 @Part("userdob") RequestBody userdob,
                                                 @Part("sms") RequestBody sms, @Part("schoolname") RequestBody schoolname,
                                                 @Part("schoolyear") RequestBody schoolyear, @Part MultipartBody.Part image,@Part("otherschool") RequestBody otherschool);

  @FormUrlEncoded
  @POST("showprofile")
  Call<Responce.ShowProfileResponce> showProfile(@Field("useridBD") String useridBD);

  @FormUrlEncoded
  @POST("forget_password")
  Call<Responce.ForgetPasswordResponce> forgetPassword(@Field("Uemail") String Uemail);

  @FormUrlEncoded
  @POST("UserCounts")
  Call<Responce.UserCountsResponce> userCounts(@Field("useridBD") String useridBD);
}
