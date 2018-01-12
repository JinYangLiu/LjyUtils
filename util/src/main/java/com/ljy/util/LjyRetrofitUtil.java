package com.ljy.util;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
    private static int mConnectTimeout = 10;
    private static int mReadTimeout = 30;
    private static int mWriteTimeout = 30;

    /**
     * 获得RetrofitManager单例
     */
    public static LjyRetrofitUtil getInstance() {
        if (retrofitManager == null) {
            retrofitManager = new LjyRetrofitUtil();
        }
        return retrofitManager;
    }

    /**
     * 构造方法中初始化apiService
     */
    private LjyRetrofitUtil() {
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
                            .connectTimeout(mConnectTimeout, TimeUnit.SECONDS)//设置连接超时
                            .readTimeout(mReadTimeout, TimeUnit.SECONDS)//设置读超时
                            .writeTimeout(mWriteTimeout, TimeUnit.SECONDS)//设置写超时
//                            .addInterceptor(commonInterceptor)//拦截器
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    public void get(String methodPath, Map<String, Object> params, final CallBack callBack) {
        Observable<Map<String, Object>> observable = apiService.get(methodPath, params);
        setCallBack(observable, callBack);
    }

    public void post(String methodPath, Map<String, Object> params, final CallBack callBack) {
        Observable<Map<String, Object>> observable = apiService.post(methodPath, params);
        setCallBack(observable, callBack);
    }



    private interface ApiService {

        @GET("{methodPath}")
        Observable<Map<String, Object>> get(@Path("methodPath") String methodPath, @QueryMap Map<String, Object> options);

        @Headers({"Content-Type: application/json", "Accept: application/json"})
        @POST("{methodPath}")
        Observable<Map<String, Object>> post(@Path("methodPath") String methodPath, @Body Map<String, Object> route);
    }

    /**
     *设置回调
     * @param observable
     * @param callBack
     */
    private void setCallBack(final Observable<Map<String, Object>> observable, final CallBack callBack) {
        observable.subscribeOn(Schedulers.io())//请求数据的事件发生在io线程
                .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程更显UI
                .subscribe(new Observer<Map<String, Object>>() {//订阅

                    @Override
                    public void onNext(Map<String, Object> parserData) {//调用接口成功，返回数据
                        if (callBack != null)
                            callBack.onSuccess(parserData);
                    }

                    @Override
                    public void onError(Throwable t) {//调用接口失败，提示错误
                        if (callBack != null)
                            callBack.onFail(t.getLocalizedMessage());
                    }

                    @Override
                    public void onCompleted() {//所以事件完成,若onError被调用了则不会再走这个方法

                    }


                });
    }

    /**
     * 成功失败的回调接口
     */
    public interface CallBack {
        void onSuccess(final Map<String, Object> parserData);

        void onFail(final String failInfo);
    }


    /**
     * 设置baseUrl，可以写在Application中
     *
     * @param baseUrl
     */
    public static void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    /**
     * 设置超时时长，可以写在Application中
     *
     * @param connectTimeout 连接超时
     * @param readTimeout    读取超时
     * @param writeTimeout   写入超时
     */
    public static void setTimeOut(int connectTimeout, int readTimeout, int writeTimeout) {
        mConnectTimeout = connectTimeout;
        mReadTimeout = readTimeout;
        mWriteTimeout = writeTimeout;
    }

}
