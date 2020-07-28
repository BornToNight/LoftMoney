package ru.borntonight.loftmoney;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.borntonight.loftmoney.remote.MoneyApi;

public class LoftApp extends Application {

    private MoneyApi moneyApi;

    public MoneyApi getMoneyApi() {
        return moneyApi;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        configureNetwork();
    }

    private void configureNetwork() {
        // Логгирование
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Body - полное

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://verdant-violet.glitch.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // создание API класса через ретрофит
        moneyApi = retrofit.create(MoneyApi.class);
    }
}
