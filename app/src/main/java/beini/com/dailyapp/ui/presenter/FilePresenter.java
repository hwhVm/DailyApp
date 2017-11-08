package beini.com.dailyapp.ui.presenter;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.fragments.RegisterFragment;
import beini.com.dailyapp.ui.model.RequestModel;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.util.BLog;
import beini.com.dailyapp.util.FileUtil;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by beini on 2017/11/8.
 */

public class FilePresenter {

    @Inject
    RequestModel requestModel;

    @Inject
    public FilePresenter() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }

    public void uploadSingleFile() {
        String path1 = Environment.getExternalStorageDirectory() + File.separator + "0.zip";
        File file = new File(path1);
        BLog.e("    file.exists()=" + file.exists());
        requestModel.uploadSingleFile(NetConstants.URL_UPLOAD_SINGLE_FILE, file, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                BLog.e("  uploadSingleFile ");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                BLog.e("   throwable=" + throwable.getLocalizedMessage());
            }
        });
    }

    public void uploadMultiFile(final RegisterFragment registerFragment) {
        List<File> files = new ArrayList<>();
        String path1 = Environment.getExternalStorageDirectory() + File.separator + "aa.xml";
        File file1 = new File(path1);
        String path2 = Environment.getExternalStorageDirectory() + File.separator + "bb.ver";
        File file2 = new File(path2);
        files.add(file1);
        files.add(file2);
        requestModel.uploadMultiFile(NetConstants.URL_UPLOAD_MULTI_FILE, files, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });

    }

    public void uploadSingleFileProcess(final RegisterFragment registerFragment) {
        requestModel.uploadSingleFileProcess(NetConstants.URL_UPLOAD_SINGLE_FILE, AndroidSchedulers.mainThread(),
                new FlowableOnSubscribe<File>() {
                    @Override
                    public void subscribe(FlowableEmitter<File> e) throws Exception {
                        String path = Environment.getExternalStorageDirectory() + File.separator + "aa.xml";
                        File file = new File(path);
                        e.onNext(file);
                        e.onComplete();
                    }
                },
                new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    public void downloadFile(String url) {
        requestModel.downloadFile(url,
                new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws IOException {
                        FileUtil.writeBytesToSD(Environment.getExternalStorageDirectory() + "/mm.mp3", responseBody.bytes());
                        return true;
                    }
                }, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        BLog.e(" aBoolean = " + aBoolean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        BLog.e("    downloadFile        throwable=" + throwable.getLocalizedMessage());
                    }
                });

    }

    public void downloadFleWithPro(String url) {
        requestModel.downloadFleWithPro(url,
                new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws Exception {
                        BLog.e(" ----------->ProgressResponseBody");
//                        FileUtil.writeBytesToSD(Environment.getExternalStorageDirectory() + "/mm.mp3", responseBody.bytes());
                        return true;
                    }
                }, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        BLog.e(" ---------->");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        BLog.e("  downloadFleWithPro     " + throwable.getLocalizedMessage());
                    }
                });

    }


    public void downloadBreakpoint(String url) {

        requestModel.downloadBreakpoint("200", url,
                new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws Exception {
                        FileUtil.writeBytesToSD(Environment.getExternalStorageDirectory() + "/gg.mp3", responseBody.bytes());
                        return true;
                    }
                },
                new Function<Boolean, Object>() {
                    @Override
                    public Object apply(Boolean aBoolean) throws Exception {
                        BLog.e("  download break point ");
                        return true;
                    }
                });
    }

}
