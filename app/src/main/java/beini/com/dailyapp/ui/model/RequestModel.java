package beini.com.dailyapp.ui.model;

import android.support.annotation.NonNull;

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
        try {
            RxNetUtil.getSingleton().sendRequest(url, request).observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
