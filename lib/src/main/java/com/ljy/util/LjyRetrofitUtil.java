package com.ljy.util;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.ljy.bean.DownloadBean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Mr.LJY on 2018/1/11.
 * <p>
 * 网络请求工具类
 */
public class LjyRetrofitUtil {
    private static String mBaseUrl;
    private ApiService apiService;
    private Retrofit retrofit;
    private static int mConnectTimeout = 10;
    private static int mReadTimeout = 30;
    private static int mWriteTimeout = 30;

    /**
     * 获得RetrofitManager单例
     */
    public static LjyRetrofitUtil getInstance() {
        return LjyRetrofitUtilHolder.retrofitUtil;
    }

    private static class LjyRetrofitUtilHolder {
        private static final LjyRetrofitUtil retrofitUtil = new LjyRetrofitUtil();
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
                        .addConverterFactory(SimpleXmlConverterFactory.create())//支持xml
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//增加返回值为Oservable<T>的支持,RxJava
                        .build();
            }
            apiService = retrofit.create(ApiService.class);
        }
    }

    /**
     * OkHttpClient配置
     */
    private OkHttpClient getOkHttpClient() {
        return OkHttpClentHolder.okHttpClient;
    }

    private static class OkHttpClentHolder {
        private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(mConnectTimeout, TimeUnit.SECONDS)//设置连接超时
                .readTimeout(mReadTimeout, TimeUnit.SECONDS)//设置读超时
                .writeTimeout(mWriteTimeout, TimeUnit.SECONDS)//设置写超时
                .build();
    }

    public void get(String methodPath, Map<String, Object> params, final CallBack callBack) {
        Observable<Map<String, Object>> observable = apiService.get(methodPath, params);
        setCallBack(observable, callBack);
    }

    public void postBody(String methodPath, Map<String, Object> params, final CallBack callBack) {
        Observable<Map<String, Object>> observable = apiService.postBody(methodPath, params);
        setCallBack(observable, callBack);
    }

    public void postFieldMap(String methodPath, Map<String, String> params, final CallBack callBack) {
        Observable<Map<String, Object>> observable = apiService.postFieldMap(methodPath, params);
        setCallBack(observable, callBack);
    }

    public void upload(String url, Map<String, String> params, final UpLoadCallBack upLoadCallBack) {


        HashMap<String, RequestBody> partMap = new HashMap<>();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            RequestBody body = RequestBody.create(
                    MediaType.parse("multipart/form-data"), entry.getValue());
            partMap.put(entry.getKey(), body);
        }
        Observable<ResponseBody> observable = apiService.upload(url, partMap);
        observable.subscribeOn(Schedulers.io())//请求数据的事件发生在io线程
                .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程更显UI
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onError(Throwable e) {
                        upLoadCallBack.onFail(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String info = new String(responseBody.bytes(), "utf-8");
                            LjyLogUtil.i(info);
                            upLoadCallBack.onSuccess(info);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    Disposable mDisposable;
    public Disposable download(final DownloadBean bean, final ProgressListener progressListener, final DownloadCallBack callBack) {
        ApiService apiService = bean.getApiService();
        if (apiService == null) {
            apiService = getApiServiceProgress(progressListener);
            bean.setApiService(apiService);
        }
        long currentLen = 0;
        if (bean.getProgress() > 0)
            currentLen = bean.getProgress();
        Observable<Boolean> observable = apiService.download("bytes=" + currentLen + "-", bean.getLoadUrll())
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws Exception {
                        return LjyFileUtil.writeResponseBodyToDisk(responseBody, bean);
                    }
                });

        Observer<Boolean> mDownLoadSubscriber = new Observer<Boolean>() {

            @Override
            public void onError(Throwable e) {
                callBack.onCompleted(false);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable=d;
            }

            @Override
            public void onNext(Boolean b) {
                callBack.onCompleted(b);
            }
        };
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(mDownLoadSubscriber);
        bean.setDisposable(mDisposable);
        return mDisposable;
    }

    private ApiService getApiServiceProgress(final ProgressListener progressListener) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(mConnectTimeout, TimeUnit.SECONDS)//设置连接超时
                .readTimeout(mReadTimeout, TimeUnit.SECONDS)//设置读超时
                .writeTimeout(mWriteTimeout, TimeUnit.SECONDS)//设置写超时
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }
                })//拦截器
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(client)//配置okhttp
                .addConverterFactory(GsonConverterFactory.create())//支持gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//增加返回值为Oservable<T>的支持,RxJava
                .build();
        return retrofit.create(ApiService.class);
    }


    public interface ApiService {

        /**
         * get
         */
        @GET("{methodPath}")
        Observable<Map<String, Object>> get(@Path("methodPath") String methodPath, @QueryMap Map<String, Object> options);

        /**
         * method：网络请求的方法（区分大小写）
         * path：网络请求地址路径
         * hasBody：是否有请求体
         */
        @HTTP(method = "GET", path = "{methodPath}", hasBody = false)
        Observable<ResponseBody> get2(@Path("methodPath") String methodPath);
        // {id} 表示是一个变量
        // method 的值 retrofit 不会做处理，所以要自行保证准确

        /**
         * post
         * <p>
         * # @Body:用于POST请求体，将实例对象根据转换方式转换为对应的json字符串参数， 这个转化方式是GsonConverterFactory定义的。
         */
        @Headers({"Content-Type: application/json", "Accept: application/json"})
        @POST("{methodPath}")
        Observable<Map<String, Object>> postBody(@Path("methodPath") String methodPath, @Body Map<String, Object> route);

        /**
         * post
         * <p>
         * # @FieldMapPost方式传递简单的键值对,
         * 需要添加@FormUrlEncoded表示表单提交
         */
        @FormUrlEncoded
        @POST("{methodPath}")
        Observable<Map<String, Object>> postFieldMap(@Path("methodPath") String methodPath,@FieldMap Map<String, String> maps);

        /**
         * 断点续传下载接口
         */
        @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
        @GET
        Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);

        /**
         * 上传文件
         */
        @Multipart
        @POST
        Observable<ResponseBody> uploadFile(@Url() String url, @PartMap() Map<String, RequestBody> partMap,
                                            @Part("file") MultipartBody.Part file);

        /**
         * 提交表单（可上传文件）
         *
         * #@Part，@PartMap：用于POST文件上传 其中@Part MultipartBody.Part代表文件，@Part("key") RequestBody代表参数
         * 需要添加@Multipart表示支持文件上传的表单，Content-Type: multipart/form-data
         * 例如:
         * #@Multipart
         * #@POST("upload") /
         * Call<ResponseBody> upload(@Part("description") RequestBody description,@Part MultipartBody.Part file);
         */
        @Multipart
        @POST
        Observable<ResponseBody> upload(@Url() String url, @PartMap() Map<String, RequestBody> partMap);

    }

    /**
     * 设置回调
     */
    private void setCallBack(final Observable<Map<String, Object>> observable, final CallBack callBack) {
        observable.subscribeOn(Schedulers.io())//请求数据的事件发生在io线程
                .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程更显UI
                .subscribe(new Observer<Map<String,Object>>() {//订阅

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

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
                    public void onComplete() {

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

    public interface DownloadCallBack {
        void onCompleted(boolean isSuccess);
    }

    public interface UpLoadCallBack {
        void onSuccess(final String successInfo);

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


    static class ProgressResponseBody extends ResponseBody {
        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }


        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.onProgress(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

    public interface ProgressListener {
        /**
         * @param progress 已经下载或上传字节数
         * @param total    总字节数
         * @param done     是否完成
         */
        void onProgress(long progress, long total, boolean done);
    }

}
