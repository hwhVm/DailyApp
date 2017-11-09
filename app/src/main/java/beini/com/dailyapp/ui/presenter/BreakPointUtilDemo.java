package beini.com.dailyapp.ui.presenter;

import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.http.RxNetUtil;
import beini.com.dailyapp.http.RxReServer;
import beini.com.dailyapp.http.progress.CusNetworkInterceptor;
import beini.com.dailyapp.util.BLog;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by beini on 2017/11/8.
 */

public class BreakPointUtilDemo {

    private static BreakPointUtilDemo instance;
    private static OkHttpClient client;
    private static RxReServer rxReServer;

    public static BreakPointUtilDemo getSingleton() {
        if (instance == null) {
            synchronized (RxNetUtil.class) {
                instance = new BreakPointUtilDemo();
//                client = new OkHttpClient//添加头信息，cookie等
//                        .Builder()
//                        .build();
//                Retrofit retrofit = new Retrofit.Builder()
//                        .client(client)
//                        .baseUrl(NetConstants.ROOT_URL)
//                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                        .build();
//                rxReServer = retrofit.create(RxReServer.class);
            }
        }
        return instance;
    }

    private Disposable disposable;

    public void downFile(String range, CusNetworkInterceptor cusNetworkInterceptor) {

        instance = new BreakPointUtilDemo();
        client = new OkHttpClient//添加头信息，cookie等
                .Builder()
                .addNetworkInterceptor(cusNetworkInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(NetConstants.ROOT_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        rxReServer = retrofit.create(RxReServer.class);


        disposable = rxReServer.downloadBreakpoint(range, NetConstants.URL_BREAKPOINT_DOWNLOAD).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io()).subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
//                FileUtil.writeBytesToSD(Environment.getExternalStorageDirectory() + "/mm.mp3", responseBody.bytes());
                        BLog.e("             responseBody.contentLength()=" + responseBody.contentLength());
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
