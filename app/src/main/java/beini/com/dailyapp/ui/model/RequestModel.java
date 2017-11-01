package beini.com.dailyapp.ui.model;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

import beini.com.dailyapp.http.RxNetUtil;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by beini on 2017/10/19.
 */

public class RequestModel {

    /**
     * 统一接口
     */
    public void sendRequest(@NonNull final String url, @NonNull final Object request, @NonNull Scheduler scheduler, @NonNull final Consumer<ResponseBody> subscriber, Consumer<Throwable> consumer) {
        RxNetUtil.getSingleton().sendRequest(url, request).observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber, consumer);
    }

    public void sendRequest(@NonNull final String url, @NonNull final Object request, @NonNull Scheduler scheduler, FlowableSubscriber<ResponseBody> flowableSubscriber) {
        RxNetUtil.getSingleton().
                sendRequest(url, request).
                observeOn(scheduler).
                subscribeOn(Schedulers.io()).
                subscribe(flowableSubscriber);

    }

    //
    public void uploadSingleFile(@NonNull final String url, @NonNull final File file, @NonNull Scheduler scheduler, @NonNull final Consumer<ResponseBody> subscriber) {
        RxNetUtil.getSingleton().uploadFileSingle(url, file).observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber);
    }

    public void uploadMultiFile(@NonNull final String url, @NonNull List<File> files, @NonNull Scheduler scheduler, @NonNull final Consumer<ResponseBody> subscriber) {

        RxNetUtil.getSingleton().uploadMultiFile(url, files).observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber);
    }

    public void uploadSingleFileProcess(@NonNull final String url, @NonNull final Scheduler scheduler,
                                        @NonNull FlowableOnSubscribe<File> flowableOnSubscribe, @NonNull final Consumer<ResponseBody> subscriber) {

        Flowable.create(flowableOnSubscribe, BackpressureStrategy.BUFFER)
                .map(new Function<File, Flowable<ResponseBody>>() {
                    @Override
                    public Flowable<ResponseBody> apply(File file) throws Exception {
                        return RxNetUtil.getSingleton().uploadFileSingle(url, file);
                    }
                })
                .map(new Function<Flowable<ResponseBody>, Disposable>() {
                    @Override
                    public Disposable apply(Flowable<ResponseBody> responseBodyFlowable) throws Exception {
                        return responseBodyFlowable.observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber);
                    }
                }).subscribeOn(Schedulers.io()).subscribe();

    }

    public void uploadMultiFileProcess(@NonNull final String url, @NonNull final Scheduler scheduler,
                                       @NonNull FlowableOnSubscribe<List<File>> flowableOnSubscribe, @NonNull final Consumer<ResponseBody> subscriber) {

        Flowable.create(flowableOnSubscribe, BackpressureStrategy.BUFFER)
                .map(new Function<List<File>, Flowable<ResponseBody>>() {
                    @Override
                    public Flowable<ResponseBody> apply(List<File> files) throws Exception {
                        return RxNetUtil.getSingleton().uploadMultiFile(url, files);
                    }
                })
                .map(new Function<Flowable<ResponseBody>, Disposable>() {
                    @Override
                    public Disposable apply(Flowable<ResponseBody> responseBodyFlowable) throws Exception {
                        return responseBodyFlowable.observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber);
                    }
                }).subscribeOn(Schedulers.io()).subscribe();

    }

    public void downloadFile(final String url
            , @NonNull final Function<ResponseBody, Boolean> functionResponse, @NonNull Function<Boolean, Object> booleanObjectFunction) {

        RxNetUtil.getSingleton().downloadFile(url)
                .map(functionResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(booleanObjectFunction)
                .subscribe();

    }

    public void downloadBreakpoint(String rnag, String url
            , @NonNull final Function<ResponseBody, Boolean> functionResponse, @NonNull Function<Boolean, Object> booleanObjectFunction) {
        RxNetUtil.getSingleton().downloadBreakpoint(rnag, url)
                .map(functionResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(booleanObjectFunction)
                .subscribe();

    }

}
