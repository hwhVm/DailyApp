package beini.com.dailyapp.ui.model;

import android.support.annotation.NonNull;

import org.reactivestreams.Subscriber;

import java.io.IOException;

import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.http.response.BaseResponseJson;
import beini.com.dailyapp.http.RxNetUtil;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by beini on 2017/10/19.
 */

public class DailyModel {

    /**
     * 统一接口
     *
     * @param url
     * @param dailyRequest
     * @param subscriber
     * @param scheduler
     */
    public void insertDaily(@NonNull final String url, @NonNull final Object dailyRequest, @NonNull final Subscriber<BaseResponseJson> subscriber, @NonNull Scheduler scheduler) {
        Flowable.create(new FlowableOnSubscribe<BaseResponseJson>() {
            @Override
            public void subscribe(FlowableEmitter<BaseResponseJson> baseResponseJson) {
                try {
                    Response baseResponseJsonResponse = RxNetUtil.getSingleton().insertDaily(url, dailyRequest);
                    if (baseResponseJsonResponse.body() == null) {
                        baseResponseJson.onError(new Throwable(NetConstants.EXCTPTION_RESPONSE_NULL));
                    } else {
                        baseResponseJson.onNext((BaseResponseJson) baseResponseJsonResponse.body());
                        baseResponseJson.onComplete();
                    }
                } catch (IOException e) {
                    baseResponseJson.onError(new Throwable(e.getLocalizedMessage()));
//                    e.printStackTrace();
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(scheduler)
                .subscribe(subscriber);
    }

}
