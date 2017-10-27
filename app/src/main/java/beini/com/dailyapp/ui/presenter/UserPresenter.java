package beini.com.dailyapp.ui.presenter;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.http.response.BaseResponseJson;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.fragments.RegisterFragment;
import beini.com.dailyapp.ui.model.RequestModel;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.util.GsonUtil;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by beini on 2017/10/25.
 */

public class UserPresenter {

    @Inject
    RequestModel dailyModel;

    @Inject
    public UserPresenter() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }

    public void registerUser(UserBean userBean, final RegisterFragment registerFragment) {
        dailyModel.sendRequest(NetConstants.URL_REGISTER_USER, userBean, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {

            }
        });

    }

    public void loginUser(UserBean userBean, final RegisterFragment registerFragment) {
        dailyModel.sendRequest(NetConstants.URL_LOGIN_USER, userBean, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                BaseResponseJson baseResponseJson = (BaseResponseJson) GsonUtil.getGsonUtil().fromJson(responseBody.string(), BaseResponseJson.class);
            }
        });

    }

    public void uploadSingleFile(final RegisterFragment registerFragment) {
        File file = null;
        dailyModel.uploadSingleFile(NetConstants.URL_UPLOAD_SINGLE_FILE, file, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {

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
        dailyModel.uploadMultiFile(NetConstants.URL_UPLOAD_MULTI_FILE, files, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {

            }
        });

    }

    public void uploadSingleFileProcess(final RegisterFragment registerFragment) {
        dailyModel.uploadSingleFileProcess(NetConstants.URL_UPLOAD_SINGLE_FILE, AndroidSchedulers.mainThread(),
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
                    public void accept(ResponseBody responseBody) throws Exception {
                    }
                });

    }
}
