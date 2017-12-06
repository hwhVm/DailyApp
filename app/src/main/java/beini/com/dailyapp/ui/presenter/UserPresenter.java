package beini.com.dailyapp.ui.presenter;

import javax.inject.Inject;

import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.net.response.BaseResponseJson;
import beini.com.dailyapp.net.response.LoginResponse;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.fragments.LoginFragment;
import beini.com.dailyapp.ui.fragments.RegisterFragment;
import beini.com.dailyapp.ui.model.RequestModel;
import beini.com.dailyapp.ui.model.StorageModel;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.util.BLog;
import beini.com.dailyapp.util.GsonUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by beini on 2017/10/25.
 */

public class UserPresenter {

    @Inject
    RequestModel requestModel;
    @Inject
    StorageModel storageModel;

    @Inject
    public UserPresenter() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }

    public void registerUser(UserBean userBean, final RegisterFragment registerFragment) {
        requestModel.sendRequest(NetConstants.URL_REGISTER_USER, userBean, AndroidSchedulers.mainThread(), responseBody -> {
            BaseResponseJson baseResponseJson = (BaseResponseJson) GsonUtil.getGsonUtil().fromJson(responseBody.string(), BaseResponseJson.class);
            if (baseResponseJson.getReturnCode() == NetConstants.IS_SUCCESS) {
                registerFragment.onSuccess();
            } else {
                registerFragment.onFailed();
            }
        }, throwable -> {
            BLog.e("   Throwable       " + throwable.getLocalizedMessage());
            registerFragment.onFailed();
        });
    }

    public void loginUser(UserBean userBean, final LoginFragment loginFragment) {
        requestModel.sendRequest(NetConstants.URL_LOGIN_USER, userBean, AndroidSchedulers.mainThread(), responseBody -> {
            LoginResponse baseResponseJson = (LoginResponse) GsonUtil.getGsonUtil().fromJson(responseBody.string(), LoginResponse.class);
            if (baseResponseJson.getReturnCode() == NetConstants.IS_SUCCESS) {
                loginFragment.OnSuccess();
                UserBean userBeanFromNet = baseResponseJson.getUserBean();
                storageModel.saveUserBeanToDb(userBeanFromNet);
            } else {
                loginFragment.onFalied();
            }
        }, throwable -> loginFragment.onFalied());

    }


    public void logout(UserBean currentUser) {
        requestModel.sendRequest(NetConstants.URL_LOGOUT, currentUser, AndroidSchedulers.mainThread(), responseBody -> {

        }, throwable -> {

        });
    }


}
