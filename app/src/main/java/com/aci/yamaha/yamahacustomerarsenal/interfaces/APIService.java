package com.aci.yamaha.yamahacustomerarsenal.interfaces;


import com.aci.yamaha.yamahacustomerarsenal.model.ChassisValidationResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.CommonResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.LocationResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.LoginResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.Post;
import com.aci.yamaha.yamahacustomerarsenal.model.RegistrationResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.ServiceResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.ServiceStatResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.UpgradeResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.UploadResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.UserComplainResponse;
import com.aci.yamaha.yamahacustomerarsenal.model.UserInquiryResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by aburasel on 9/24/2017.
 */

public interface APIService {
    @FormUrlEncoded
    @POST("authenticate/loginservice")
    Call<LoginResponse> userLogIn(@Field("userid") String userId,
                                  @Field("password") String password);

    @Multipart
    @POST("upload.php")
    Call<UploadResponse> updateProfile(@Part("full_name") RequestBody fullName,
                                       @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("home/register")
    Call<RegistrationResponse> userRegistration(@Field("data") String data);

    @FormUrlEncoded
    @POST("home/service_in")
    Call<CommonResponse> serviceTicketRaiseCall(@Field("data") String data);

    @FormUrlEncoded
    @POST("home/get_details_by_chassis")
    Call<ChassisValidationResponse> checkValidChassis(@Field("data") String data);


    @FormUrlEncoded
    @POST("home/get_service_count")
    Call<ServiceStatResponse> serviceStatus(@Field("data") String data);


    @FormUrlEncoded
    @POST("home/get_services")
    Call<ServiceResponse> getServices(@Field("data") String data);

    @FormUrlEncoded
    @POST("home/inquiry_in")
    Call<CommonResponse> userInquiry(@Field("data") String data);

    @FormUrlEncoded
    @POST("home/complain_in")
    Call<CommonResponse> userComplain(@Field("data") String data);

    @POST("post.php")
    Call<String> sendComplain(@Body Post post);


    @POST("apps")
    Call<UpgradeResponse> getAppDownloadUri();

    @FormUrlEncoded
    @POST("home/get_inquiry")
    Call<UserInquiryResponse> getUserInquiry(@Field("data") String data);

    @FormUrlEncoded
    @POST("home/get_complain")
    Call<UserComplainResponse> getUserComplain(@Field("data") String data);

    @FormUrlEncoded
    @POST("home/locationinfo")
    Call<LocationResponse> fetchLocations(@Field("data") String data);
}