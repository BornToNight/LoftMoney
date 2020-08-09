package ru.borntonight.loftmoney.remote;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MoneyApi {

    @GET("./items")
    Single<List<MoneyItem>> getItems(@Query("auth-token") String token, @Query("type") String type);

    @POST("./items/add")
    @FormUrlEncoded
    Completable addItem(@Field("auth-token") String token,
                        @Field("name") String name,
                        @Field("price") String price,
                        @Field("type") String type);
}
