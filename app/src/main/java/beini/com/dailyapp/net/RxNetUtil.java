package beini.com.dailyapp.net;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.net.progress.ProgressListener;
import beini.com.dailyapp.util.BLog;
import io.reactivex.Flowable;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by beini on 2017/4/14.
 */

public class RxNetUtil {
    private static RxNetUtil instance;
    private static Retrofit retrofit;
    private static RxReServer rxReServer;
    private static int DEFAULT_TIMEOUT = 5;
    private final static long maxSize = 0L;
    private final static String directroyCache = "";//Android/data/

    public static RxNetUtil getSingleton() {
        synchronized (RxNetUtil.class) {
            if (instance == null) {
                instance = new RxNetUtil();
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        BLog.h("      http log :  " + message);
                    }
                });
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//日志级别,NONE BASIC HEADERS BODY
                //缓存
                File directory = new File(directroyCache);
                Cache cache = new Cache(directory, maxSize);
                OkHttpClient client = new OkHttpClient//添加头信息，cookie等
                        .Builder()
                        // 添加通用的Header
//                            .addInterceptor(new Interceptor() {
//                                @Override
//                                public okhttp3.Response intercept(Chain chain) throws IOException {
//                                    Request.Builder builder = chain.request().newBuilder();
//                                    builder.addHeader("token", "123");
//                                    return chain.proceed(builder.build());
//                                }
//                            })
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Response originResponse = chain.proceed(chain.request());
                                //设置缓存时间为，并移除了pragma消息头，移除它的原因是因为pragma也是控制缓存的一个消息头属性
                                return originResponse.newBuilder().removeHeader("pragma")
                                        .header("Cache-Control", "max-age=10")//设置10秒
                                        .header("Cache-Control", "max-stale=30").build();
                            }
                        })
//                      .retryOnConnectionFailure()//重试机制
                        .connectTimeout(8, TimeUnit.SECONDS) // 设置连接超时时间
//                        .writeTimeout(8, TimeUnit.SECONDS)// 设置写入超时时间
//                        .readTimeout(8, TimeUnit.SECONDS)// 设置读取数据超时时间
                        .cache(cache)
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Response originalResponse = chain.proceed(chain.request());
                                return originalResponse.newBuilder()
                                        .build();
                            }
                        })
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                          .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())//信任所有证书
                        .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(GlobalApplication.getInstance().getApplicationContext())
                                , SSLSocketFactoryUtils.createTrustAllManager())
                        .hostnameVerifier(new SSLSocketFactoryUtils.TrustAllHostnameVerifier())
                        .addInterceptor(httpLoggingInterceptor)
                        .build();
                retrofit = new Retrofit.Builder()
                        .client(client)
                        .baseUrl(NetConstants.ROOT_URL)
                        .addConverterFactory(GsonConverterFactory.create())//compile 'com.squareup.retrofit2:converter-gson:2.0.2'
                        // 添加Retrofit到RxJava的转换器
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                          .addConverterFactory(ScalarsConverterFactory.create())//普通类型
                        .build();

                rxReServer = retrofit.create(RxReServer.class);
            }
        }
        return instance;
    }

    /**
     * 通用方法
     *
     * @param url
     * @param object
     * @return
     * @throws InterruptedException
     */
    public Flowable<ResponseBody> sendRequest(@NonNull final String url, @NonNull final Object object) {
        return rxReServer.sendRequestReturnResponseBody(url, object);
    }

    /**
     * @param url
     * @param file
     * @return
     */
    public Flowable<ResponseBody> uploadFileSingle(@NonNull final String url, @NonNull File file) {
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // add another part within the multipart request
//        String descriptionString = "hello, this is description speaking";
//        RequestBody description =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), descriptionString);

        // finally, execute the request

        return rxReServer.uploadSingleFile(url, body);
    }

    public Flowable<ResponseBody> uploadMultiFile(@NonNull String url, @NonNull List<File> fileList) {

        List<MultipartBody.Part> parts = new ArrayList<>(fileList.size());
        for (File file : fileList) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        return rxReServer.uploadMultiFile(url, parts);
    }


    public Flowable<ResponseBody> downloadFile(@NonNull String url) {

        return rxReServer.downloadFile(url);
    }


    public Flowable<ResponseBody> uploadFile(@NonNull String rang, @NonNull String url, @NonNull File file, ProgressListener progressListener) {
//        ProgressRequestBody progressRequestBody = new ProgressRequestBody(file, progressListener);
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("file", file.getName(), progressRequestBody);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return rxReServer.uploadBreakpoint("0", url, body);
    }


}
