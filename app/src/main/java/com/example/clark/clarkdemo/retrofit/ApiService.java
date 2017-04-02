package com.example.clark.clarkdemo.retrofit;

import com.example.clark.clarkdemo.bean.UpdateAppInfo;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Clark on 2017/4/2.
 */
public interface  ApiService {

    // 实际开发过程可能的接口方式
    @GET("update") Observable<UpdateAppInfo> getUpdateInfo(
            @Query("appname") String appname,
            @Query("serverVersion") String appVersion);

    // 以下方便版本更新接口测试
    @GET("update") Observable<UpdateAppInfo> getUpdateInfo();
}
