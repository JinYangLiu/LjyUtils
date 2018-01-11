package com.ljy.util;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Mr.LJY on 2018/1/11.
 */

public class LjyRetrofitUtil {
    private static LjyRetrofitUtil retrofitManager;
    private static String mBaseUrl;
    private ApiService apiService;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    public static void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    /**
     * 获得RetrofitManager单例
     */
    public static LjyRetrofitUtil getInstance() {
        if (retrofitManager == null) {
            retrofitManager = new LjyRetrofitUtil();
        }
        return retrofitManager;
    }

    public LjyRetrofitUtil() {
        if (apiService == null) {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(mBaseUrl)
                        .client(getOkHttpClient())//配置okhttp
                        .addConverterFactory(GsonConverterFactory.create())//支持gson
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//增加返回值为Oservable<T>的支持,RxJava
                        .build();
            }
            apiService = retrofit.create(ApiService.class);
        }
    }

    /**
     * OkHttpClient配置
     */
    private OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (LjyRetrofitUtil.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时
                            .readTimeout(30, TimeUnit.SECONDS)//设置读超时
                            .writeTimeout(30, TimeUnit.SECONDS)//设置写超时
//                            .addInterceptor(commonInterceptor)//拦截器
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    public <T> void getJsonMap(String methodPath, Map<String, String> params, final CallBack<T> callBack) {
        Observable<ParserDataBase<HashMap<String, Object>>> observable = apiService.getJsonMap(methodPath, params);
        setCallBack(observable, callBack);
    }

    public interface ApiService {
        @GET("{methodPath}")
        Observable<ParserDataBase<HashMap<String, Object>>> getJsonMap(@Path("methodPath") String methodPath, @QueryMap Map<String, String> options);
    }

    private <T> void setCallBack(final Observable<T> observable, final CallBack callBack) {
        observable.subscribeOn(Schedulers.io())//请求数据的事件发生在io线程
                .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程更显UI
                .subscribe(new Observer<T>() {//订阅

                    @Override
                    public void onNext(T parserData) {//调用接口成功，返回数据
                        if (callBack != null)
                            callBack.onSuccess(parserData);
                    }

                    @Override
                    public void onError(Throwable t) {//调用接口失败，提示错误
                        if (callBack != null)
                            callBack.onFail(t.getLocalizedMessage());
                    }

                    @Override
                    public void onCompleted() {//所以事件完成
                        LjyLogUtil.i("onCompleted");
                    }


                });
    }


    public interface CallBack<T> {
        void onSuccess(final T parserData);
        void onFail(final String failInfo );
    }

    public interface FailureCallBack {

    }

    public static class ParserDataBase<T> {

        @SerializedName(value = "code", alternate = {"errorNo"})
        private int code;

        private String message;

        @SerializedName(value = "body", alternate = {"data", "list"})
        private T body;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public T getBody() {
            return body;
        }
    }

}
