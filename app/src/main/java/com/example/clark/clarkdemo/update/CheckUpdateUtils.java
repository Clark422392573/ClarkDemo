package com.example.clark.clarkdemo.update;

import com.example.clark.clarkdemo.bean.UpdateAppInfo;
import com.example.clark.clarkdemo.retrofit.ApiService;
import com.example.clark.clarkdemo.retrofit.ServiceFactory;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Clark on 2017/4/2.
 */
public class CheckUpdateUtils {

    /**
     * 检查更新
     */
    @SuppressWarnings("unused")
    public static void checkUpdate(String appCode, String curVersion, final CheckCallBack updateCallback) {
        ApiService apiService = ServiceFactory.createServiceFrom(ApiService.class);
        apiService.getUpdateInfo()//测试使用
                //   .apiService.getUpdateInfo(appCode, curVersion)//开发过程中可能使用的
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateAppInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UpdateAppInfo updateAppInfo) {
                        if (updateAppInfo.error_code == 0 || updateAppInfo.data == null ||
                                updateAppInfo.data.updateurl == null) {
                            updateCallback.onError(); // 失败
                        } else {
                            updateCallback.onSuccess(updateAppInfo);
                        }
                    }
                });
    }

    public interface CheckCallBack {
        void onSuccess(UpdateAppInfo updateInfo);
        void onError();
    }
}
