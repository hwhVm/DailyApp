package beini.com.dailyapp.ui.presenter;

import android.os.Environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.http.RxReServer;
import beini.com.dailyapp.http.progress.ProgressListener;
import beini.com.dailyapp.http.progress.ProgressResponseBody;
import beini.com.dailyapp.util.BLog;
import beini.com.dailyapp.util.FileUtil;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by beini on 2017/11/8.
 */

public class BreakPointUtilDemo {

    private long cullentSize = 0L;
    private String strUrl = "http://120.76.41.61/source/sound/sleep/Sleep_Bird_Chirping.mp3";
    String strPath = Environment.getExternalStorageDirectory() + "/str.zip";
    String destPath = Environment.getExternalStorageDirectory() + "/temp.zip";
    String sumPath = Environment.getExternalStorageDirectory() + "/sum.zip";

    private ProgressListener progressListener = new ProgressListener() {
        @Override
        public void update(long bytesRead, long contentLength, boolean done) {
            BLog.e("      bytesRead=" + bytesRead + "   contentLength=" + contentLength + "  done=" + done + "  " + ((100 * bytesRead) / contentLength));
            cullentSize = bytesRead;
        }
    };
    RxReServer rxReServer;

    public void initNet() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                BLog.h("      http log :  " + message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//日志级别,NONE BASIC HEADERS BODY

        OkHttpClient client = new OkHttpClient//添加头信息，cookie等
                .Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        BLog.e("     intercept     ");
                        Response originalResponse = chain.proceed(chain.request());
//                        String contentLength = originalResponse.header(" Content-Length");
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }
                }).addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(NetConstants.ROOT_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        rxReServer = retrofit.create(RxReServer.class);
    }

    public void downFileTest() {
        disposable = rxReServer.downloadFile(strUrl).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io()).subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        BLog.e("   dddddddddddddd");
                        FileUtil.writeBytesToSD(Environment.getExternalStorageDirectory() + "/mm.mp3", responseBody.bytes());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        BLog.e("  throwable=" + throwable);
                    }
                });

    }


    Disposable disposable;

    public void downFile() {
        BLog.e("             cullentSize=" + cullentSize);
        rxReServer.downloadBreakpoint(String.valueOf(cullentSize), NetConstants.URL_BREAKPOINT_DOWNLOAD).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io()).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                BLog.e("      downFile        ");
                // size>0   copy   other file   and  new file;
                if (cullentSize > 0) {
                    FileUtil.copyFile(strPath, destPath);
                    FileUtil.writeBytesToSD(strPath, responseBody.bytes());
                    //合并
                    List<String> allFile = new ArrayList<>();
                    allFile.add(strPath);
                    allFile.add(destPath);
                    FileUtil.merge(allFile, sumPath);
                } else {
                    FileUtil.writeBytesToSD(strPath, responseBody.bytes());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                BLog.e("        throwable=" + throwable);
            }
        });

    }

    public void stopDownFile() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

}
