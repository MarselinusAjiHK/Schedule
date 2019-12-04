package g.com.tiemschedule;

import com.google.gson.annotations.JsonAdapter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiUser {


    @POST("create.php")
    @FormUrlEncoded
    Call<UserDAO> regUser(@Field("nama") String nama,
                          @Field("email") String email,
                          @Field("password") String password);

    @POST("update.php")
    @FormUrlEncoded
    Call<UserDAO> updateUser(@Field("nama") String nama,
                          @Field("id") String id);

    @POST("updatePasswordById2.php")
    @FormUrlEncoded
    Call<UserDAO> updatePasswordUser(@Field("password") String password,
                             @Field("id") String id);

    @POST("delete.php")
    @FormUrlEncoded
    Call<UserDAO> deleteUser(@Field("id") String id);

    @POST("login2.php")
    @FormUrlEncoded
    Call<UserDAO> loginUser(@Field("email") String email);

    @POST("emailResetPassword.php")
    @FormUrlEncoded
    Call<UserDAO> forgetPassword(@Field("email") String email);



}