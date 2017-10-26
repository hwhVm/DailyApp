package beini.com.dailyapp.ui.model;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

import beini.com.dailyapp.http.RxNetUtil;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by beini on 2017/10/19.
 */

public class RequestModel {

    /**
     * 统一接口
     */
    public void sendRequest(@NonNull final String url, @NonNull final Object request, @NonNull Scheduler scheduler, @NonNull final Consumer<ResponseBody> subscriber) {
        RxNetUtil.getSingleton().sendRequest(url, request).observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber);
    }

    public void uploadSingleFile(@NonNull final String url, @NonNull File file, @NonNull Scheduler scheduler, @NonNull final Consumer<ResponseBody> subscriber) {
        RxNetUtil.getSingleton().uploadFileSingle(url, file).observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber);
    }

    public void uploadMultiFile(@NonNull final String url, @NonNull List<File> files, @NonNull Scheduler scheduler, @NonNull final Consumer<ResponseBody> subscriber) {
        RxNetUtil.getSingleton().uploadMultiFile(url, files).observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber);
    }
}
