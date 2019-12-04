package g.com.tiemschedule;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiCatatan {

    @POST("readByUser.php")
    @FormUrlEncoded
    Call<ValueCatatan> getCatatan( @Field("id") String id);

    @POST("getPrioritas.php")
    @FormUrlEncoded
    Call<ValueCatatan> getPrioritas( @Field("id") String id);


    @POST("create.php")
    @FormUrlEncoded
    Call<CatatanDAO> regCatatan(@Field("catatan") String nama,
                          @Field("prioritas") int prioritas,
                             @Field("id") String id);

    @POST("update.php")
    @FormUrlEncoded
    Call<CatatanDAO> updateCatatan(@Field("catatan") String nama,
                                @Field("prioritas") int prioritas,
                                @Field("id") String id);
    @POST("delete.php")
    @FormUrlEncoded
    Call<CatatanDAO> deleteCatatan(@Field("id") String id);

}
