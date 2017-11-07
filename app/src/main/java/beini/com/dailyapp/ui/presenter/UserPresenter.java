package beini.com.dailyapp.ui.presenter;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.http.progress.ProgressResponseBody;
import beini.com.dailyapp.http.response.BaseResponseJson;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.fragments.LoginFragment;
import beini.com.dailyapp.ui.fragments.RegisterFragment;
import beini.com.dailyapp.ui.model.RequestModel;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.util.BLog;
import beini.com.dailyapp.util.FileUtil;
import beini.com.dailyapp.util.GsonUtil;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by beini on 2017/10/25.
 */

public class UserPresenter {

    @Inject
    RequestModel requestModel;

    @Inject
    public UserPresenter() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }

    public void registerUser(UserBean userBean, final RegisterFragment registerFragment) {
        requestModel.sendRequest(NetConstants.URL_REGISTER_USER, userBean, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                BLog.e("  accept    ");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                BLog.e("   Throwable       " + throwable.getLocalizedMessage());
            }
        });
//        requestModel.sendRequest(NetConstants.URL_REGISTER_USER, userBean, AndroidSchedulers.mainThread(), new FlowableSubscriber<ResponseBody>() {
//            @Override
//            public void onSubscribe(Subscription s) {
//                s.request(Integer.MAX_VALUE);
//            }
//
//            @Override
//            public void onNext(ResponseBody responseBody) {
//                BLog.e(" onNext  responseBody     ");
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                BLog.e("           " + t.getLocalizedMessage());
//            }
//
//            @Override
//            public void onComplete() {
//                BLog.e(" onComplete  responseBody     ");
//            }
//        });

    }

    public void loginUser(UserBean userBean, final LoginFragment registerFragment) {
        requestModel.sendRequest(NetConstants.URL_LOGIN_USER, userBean, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                BaseResponseJson baseResponseJson = (BaseResponseJson) GsonUtil.getGsonUtil().fromJson(responseBody.string(), BaseResponseJson.class);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });

    }

    public void uploadSingleFile(final RegisterFragment registerFragment) {
        File file = null;
        requestModel.uploadSingleFile(NetConstants.URL_UPLOAD_SINGLE_FILE, file, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

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

    public void logout(UserBean currentUser) {
        requestModel.sendRequest(NetConstants.URL_LOGOUT, currentUser, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {

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
                        BLog.e("  downloadFleWithPro     "+throwable.getLocalizedMessage());
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
