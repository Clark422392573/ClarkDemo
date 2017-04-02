package com.example.clark.clarkdemo.retrofit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Clark on 2017/4/2.
 */
public class ServiceFactory {

    private static final String BASEURL = "http://www.chinaliankeji.com/";

    public static <T> T createServiceFrom(final Class<T> serviceClass) {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .build();
        return adapter.create(serviceClass);
    }
}
