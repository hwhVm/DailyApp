package beini.com.dailyapp.http;

import beini.com.dailyapp.bean.FileRequestBean;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.http.progress.CusNetworkInterceptor;
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

public class BreakPointUtil {

    private RxReServer rxReServer;
    private Disposable disposable;
    private OkHttpClient client;
    private Retrofit retrofit;
    private static BreakPointUtil breakPointUtil;

    public static BreakPointUtil getSingleton() {
        if (breakPointUtil == null) {
            breakPointUtil = new BreakPointUtil();
        }
        return breakPointUtil;
    }

    public void downFile(FileRequestBean fileRequestBean, CusNetworkInterceptor cusNetworkInterceptor, Consumer<ResponseBody> consumer, Consumer<Throwable> throwableConsumer) {
        client = new OkHttpClient
                .Builder()
                .addNetworkInterceptor(cusNetworkInterceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(NetConstants.ROOT_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        rxReServer = retrofit.create(RxReServer.class);
        disposable = rxReServer.downloadBreakpoint(fileRequestBean.getRange(), NetConstants.URL_BREAKPOINT_DOWNLOAD)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(consumer, throwableConsumer);
    }

    public void cancelDownFile() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

}
