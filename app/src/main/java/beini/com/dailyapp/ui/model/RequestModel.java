package beini.com.dailyapp.ui.model;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

import beini.com.dailyapp.bean.FileRequestBean;
import beini.com.dailyapp.net.BreakPointUtil;
import beini.com.dailyapp.net.RxNetUtil;
import beini.com.dailyapp.net.progress.CusNetworkInterceptor;
import beini.com.dailyapp.net.progress.ProgressListener;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
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
    //普通post请求
    public void sendRequest(@NonNull final String url, @NonNull final Object request, @NonNull Scheduler scheduler,
                            @NonNull final Consumer<ResponseBody> subscriber, Consumer<Throwable> consumer) {
        RxNetUtil.getSingleton().sendRequest(url, request).observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber, consumer);
    }

    //上传单个文件
    public void uploadSingleFile(@NonNull final String url, @NonNull final File file, @NonNull Scheduler scheduler,
                                 @NonNull final Consumer<ResponseBody> subscriber, Consumer<Throwable> consumer) {
        RxNetUtil.getSingleton().uploadFileSingle(url, file).observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber, consumer);
    }


    //上传多文件
    public void uploadMultiFile(@NonNull final String url, @NonNull List<File> files, @NonNull Scheduler scheduler,
                                @NonNull final Consumer<ResponseBody> subscriber, Consumer<Throwable> consumer) {

        RxNetUtil.getSingleton().uploadMultiFile(url, files).observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber, consumer);
    }

    //上传单个文件,结合获取文件数据
    public void uploadSingleFileProcess(@NonNull final String url, @NonNull final Scheduler scheduler,
                                        @NonNull FlowableOnSubscribe<File> flowableOnSubscribe, @NonNull final Consumer<ResponseBody> subscriber, final Consumer<Throwable> consumer) {

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
                        return responseBodyFlowable.observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber, consumer);
                    }
                }).subscribeOn(Schedulers.io()).subscribe();

    }

    //上传多个文件,结合获取文件数据
    public void uploadMultiFileProcess(@NonNull final String url, @NonNull final Scheduler scheduler,
                                       @NonNull FlowableOnSubscribe<List<File>> flowableOnSubscribe, @NonNull final Consumer<ResponseBody> subscriber, final Consumer<Throwable> consumer) {

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
                        return responseBodyFlowable.observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber, consumer);
                    }
                }).subscribeOn(Schedulers.io()).subscribe();

    }

    //文件下载
    public void downloadFile(final String url,
                             @NonNull final Function<ResponseBody, Boolean> functionResponse,
                             @NonNull final Consumer<Boolean> subscriber, Consumer<Throwable> consumer) {
        RxNetUtil.getSingleton().downloadFile(url)
                .map(functionResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber, consumer);
    }

    //断点续传
    public void downloadFileBreakPoint(FileRequestBean fileRequestBean, CusNetworkInterceptor cusNetworkInterceptor,
                                       Consumer<ResponseBody> consumer, Consumer<Throwable> throwableConsumer) {
        BreakPointUtil.getSingleton().downFile(fileRequestBean, cusNetworkInterceptor, consumer, throwableConsumer);
    }

    public void cancelDownloadFileBreakPoint() {
        BreakPointUtil.getSingleton().cancelDownFile();
    }

    public void uploadFile(@NonNull String rang, @NonNull String url, @NonNull File file, ProgressListener progressListener,
                           @NonNull Scheduler scheduler, @NonNull final Consumer<ResponseBody> subscriber, Consumer<Throwable> consumer) {
        RxNetUtil.getSingleton().uploadFile(rang, url, file, progressListener).observeOn(scheduler).subscribeOn(Schedulers.io()).subscribe(subscriber, consumer);
    }
    //断点多线程下载
    //  5   个       sum/fileSize=Progress  重试机制（3次）

}
