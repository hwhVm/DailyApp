package beini.com.dailyapp.http;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.constant.NetConstants;
import io.reactivex.Flowable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
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

    public static RxNetUtil getSingleton() {
        if (instance == null) {
            synchronized (RxNetUtil.class) {
                if (instance == null) {
                    instance = new RxNetUtil();
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
                              /*
              这里可以添加一个HttpLoggingInterceptor，因为Retrofit封装好了从Http请求到解析，
            出了bug很难找出来问题，添加HttpLoggingInterceptor拦截器方便调试接口
             */
//                            .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//                                @Override
//                                public void log(String message) {
//
//                                }
//                            }).setLevel(HttpLoggingInterceptor.Level.BASIC))
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                            .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())//信任所有证书
                            .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(GlobalApplication.getInstance().getApplicationContext())
                                    , SSLSocketFactoryUtils.createTrustAllManager())
                            .hostnameVerifier(new SSLSocketFactoryUtils.TrustAllHostnameVerifier())
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
        }
        return instance;
    }

    /**
     * 通用方法
     * @param url
     * @param object
     * @return
     * @throws InterruptedException
     */
    public Flowable<ResponseBody> sendRequest(@NonNull final String url, @NonNull final Object object) throws InterruptedException {
        return rxReServer.sendRequestReturnResponseBody(url, object);
    }
}
