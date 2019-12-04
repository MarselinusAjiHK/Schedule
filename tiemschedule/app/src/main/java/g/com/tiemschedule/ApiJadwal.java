package g.com.tiemschedule;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiJadwal {

    @POST("readByUser.php")
    @FormUrlEncoded
    Call<ValueJadwal> getJadwal(@Field("id") String id);

    @POST("getPrioritas.php")
    @FormUrlEncoded
    Call<ValueJadwal> getPrioritas(@Field("id") String id);


    @POST("getByDate.php")
    @FormUrlEncoded
    Call<ValueJadwal> getByDate(@Field("id") String id);


    @POST("getDateById.php")
    @FormUrlEncoded
    Call<ValueJadwal> getDateById(@Field("id") String id);


    @POST("create.php")
    @FormUrlEncoded
    Call<JadwalDAO> addJadwal(@Field("jadwal") String jadwal,
                          @Field("waktu") String waktu,
                          @Field("tanggal") String tanggal,
                            @Field("tempat") String tempat,
                            @Field("prioritas") int prioritas,
                            @Field("id") String id);
    @POST("update.php")
    @FormUrlEncoded
    Call<JadwalDAO> updateJadwal(@Field("jadwal") String jadwal,
                              @Field("waktu") String waktu,
                              @Field("tanggal") String tanggal,
                              @Field("tempat") String tempat,
                              @Field("prioritas") int prioritas,
                              @Field("id") String id);
    @POST("delete.php")
    @FormUrlEncoded
    Call<JadwalDAO> deleteJadwal(@Field("id") String id);


}
