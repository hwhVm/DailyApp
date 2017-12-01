package beini.com.dailyapp.net;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.net.progress.ProgressListener;
import beini.com.dailyapp.util.BLog;
import beini.com.dailyapp.util.NetUtil;
import io.reactivex.Flowable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    private final static long maxSize = 1024 * 1024 * 100;//maxSize>0  1024 * 1024 * 10   10M


    public static RxNetUtil getSingleton() {
        synchronized (RxNetUtil.class) {
            if (instance == null) {
                instance = new RxNetUtil();
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        BLog.h(message);
                    }
                });
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//日志级别,NONE BASIC HEADERS BODY
                File directory = new File(NetConstants.DIRECTORY_CACHE);
                Cache cache = new Cache(directory, maxSize);
                OkHttpClient client = new OkHttpClient//添加头信息，cookie等
                        .Builder()
//                      .retryOnConnectionFailure()//重试机制
                        .connectTimeout(8, TimeUnit.SECONDS) // 设置连接超时时间
//                      .writeTimeout(8, TimeUnit.SECONDS)// 设置写入超时时间
//                      .readTimeout(8, TimeUnit.SECONDS)// 设置读取数据超时时间
                        .cache(cache)
//                      .cookieJar(returnCookieJar())
//                      .addInterceptor(returnGeneralHeadInterceptor())// 添加通用的Header
//                        .addInterceptor(returnCacheInterceptor())
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                      .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())//信任所有证书
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
//                      .addConverterFactory(ScalarsConverterFactory.create())//普通类型
                        .build();

                rxReServer = retrofit.create(RxReServer.class);
            }
        }
        return instance;
    }

    private static Interceptor returnGeneralHeadInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("token", "123");
                return chain.proceed(builder.build());
            }
        };

    }

    private static Interceptor returnCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
//                BLog.e("------------->" + NetUtil.checkNet(GlobalApplication.getInstance().getApplicationContext()));
                if (!NetUtil.checkNet(GlobalApplication.getInstance().getApplicationContext())) {//离线缓存控制  总的缓存时间=在线缓存时间+设置离线缓存时间

                    int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周,单位:秒
                    CacheControl tempCacheControl = new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(maxStale, TimeUnit.SECONDS)
                            .build();
                    request = request.newBuilder()
                            .cacheControl(tempCacheControl)
                            .build();
                }
                return chain.proceed(request);
            }
        };
    }

    private static CookieJar returnCookieJar() {
        //缓存 ,考虑到安全和不同服务器的访问，暂时不做持久化处理，仅做服务器测试。
        return new CookieJar() {
            private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {//服务端给客户端发送Cookie时调用
                BLog.e("------------>saveFromResponse   url=" + url + "     cookies=" + cookies.size() + "   url.host()=" + url.host());
                cookieStore.put(HttpUrl.parse(url.host()), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {//发送给服务器
                BLog.e("------------>loadForRequest   url=" + url);
                List<Cookie> cookies = cookieStore.get(HttpUrl.parse(url.host()));
                if (cookies == null) {
                    BLog.e(" 没有加载 cookies ");
                }
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };

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
        return rxReServer.sendRequestReturnResponseBody("public,max-age=3600", url, object);
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
