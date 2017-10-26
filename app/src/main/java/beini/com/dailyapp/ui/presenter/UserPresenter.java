package beini.com.dailyapp.ui.presenter;

import java.io.File;
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

    public void uploadSingleFile(File file, final RegisterFragment registerFragment) {
        dailyModel.uploadSingleFile(NetConstants.URL_UPLOAD_SINGLE_FILE, file, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {

            }
        });
    }

    public void uploadMultiFile(List<File> files, final RegisterFragment registerFragment) {

        dailyModel.uploadMultiFile(NetConstants.URL_UPLOAD_MULTI_FILE, files, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {

            }
        });
    }
}
