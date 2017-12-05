package beini.com.dailyapp.ui.presenter;

import android.os.Environment;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.bean.FileRequestBean;
import beini.com.dailyapp.constant.Constants;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.net.progress.CusNetworkInterceptor;
import beini.com.dailyapp.net.progress.ProgressListener;
import beini.com.dailyapp.net.response.FileResponse;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.fragments.DailyEditFragment;
import beini.com.dailyapp.ui.fragments.LoginFragment;
import beini.com.dailyapp.ui.fragments.RegisterFragment;
import beini.com.dailyapp.ui.model.RequestModel;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.util.BLog;
import beini.com.dailyapp.util.FileUtil;
import beini.com.dailyapp.util.GsonUtil;
import beini.com.dailyapp.util.SPUtils;
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

    public void uploadMultiFile(List<File> files, final DailyEditFragment dailyEditFragment) {

        requestModel.uploadMultiFile(NetConstants.URL_UPLOAD_MULTI_FILE, files, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                FileResponse fileResponse = (FileResponse) GsonUtil.getGsonUtil().fromJson(responseBody.string(), FileResponse.class);
                dailyEditFragment.insertDaily(fileResponse);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                BLog.e(" uploadMultiFile  throwable= " + throwable.getLocalizedMessage());
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

    public void getFileInfo(FileRequestBean fileRequestBean, final LoginFragment loginFragment) {
        requestModel.sendRequest(NetConstants.URL_GET_FILE_INFO, fileRequestBean, AndroidSchedulers.mainThread(),
                new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String string = responseBody.string();
                        FileRequestBean fileRequestBean1 = (FileRequestBean) GsonUtil.getGsonUtil().fromJson(string, FileRequestBean.class);
                        loginFragment.onStartDownload(fileRequestBean1);
                    }
                },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        BLog.e("        throwable=" + throwable);
                        loginFragment.onFailedDownload();
                    }
                });
    }

    //断点
    public void downloadFileBreakPoint(final FileRequestBean fileRequestBean, final ProgressBar progressBar) {
        //初始化
        final float progressCurrent = (float) Long.parseLong(fileRequestBean.getRange()) / fileRequestBean.getFileSize() * 100;
        BLog.e("           progress=" + progressCurrent);
        progressBar.setProgress((int) progressCurrent);
        requestModel.downloadFileBreakPoint(fileRequestBean, new CusNetworkInterceptor(new ProgressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                float progress = (float) bytesRead / contentLength * 100;
                BLog.e("                   progress=" + progress);
                progressBar.setProgress((int) ((int) progressCurrent + progress + progress));
            }

            @Override
            public void onStop() {

            }

            @Override
            public void onError(Exception e) {

            }
        }), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                long range = Long.parseLong(fileRequestBean.getRange());
                InputStream inputStream = responseBody.byteStream();
                if (range > 0 && range < fileRequestBean.getFileSize()) {
                    FileUtil.writeInputStreamToSD(Constants.EXTEND_STORAGE_PATH + "temp.zip", inputStream);//一般文件有个唯一的id
                    FileUtil.appendFile(Constants.EXTEND_STORAGE_PATH + "sum.zip", Constants.EXTEND_STORAGE_PATH + "temp.zip");
                    File file = new File(Constants.EXTEND_STORAGE_PATH + "temp.zip");
                    file.delete();
                } else {
                    FileUtil.writeInputStreamToSD(Constants.EXTEND_STORAGE_PATH + "sum.zip", inputStream);//一般文件有个唯一的id
                }
                File file = new File(Constants.EXTEND_STORAGE_PATH + "sum.zip");
                SPUtils.put(GlobalApplication.getInstance().getApplicationContext(), Constants.DEMO_RANGE, file.length());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

    public void cancelDownloadFileBreakPoint() {
        requestModel.cancelDownloadFileBreakPoint();
    }


    public void uploadFile(ProgressBar progressBar) {
        File file = new File(Constants.EXTEND_STORAGE_PATH + "sum.zip");
        BLog.e(" ------------>file.exists()=" + file.exists());
        requestModel.uploadFile("0", NetConstants.URL_BREAKPOINT_UPLOAD, file, new ProgressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                BLog.e(" ------------ > ");
            }

            @Override
            public void onStop() {

            }

            @Override
            public void onError(Exception e) {

            }
        }, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                BLog.e("   accept     ");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                BLog.e("          throwable=" + throwable.getLocalizedMessage());
            }
        });

    }
}
