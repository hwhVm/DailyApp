package beini.com.dailyapp.ui.presenter;

import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.net.response.BaseResponseJson;
import beini.com.dailyapp.net.response.LoginResponse;
import beini.com.dailyapp.ui.inter.ResultListener;
import beini.com.dailyapp.util.GsonUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by beini on 2017/10/25.
 */

public class UserPresenter extends BasePresenter {

    public void registerUser(UserBean userBean, final ResultListener<Boolean> listener) {
        requestModel.sendRequest(NetConstants.URL_REGISTER_USER, userBean, AndroidSchedulers.mainThread(), responseBody -> {
                    BaseResponseJson baseResponseJson = (BaseResponseJson) GsonUtil.getGsonUtil().fromJson(responseBody.string(), BaseResponseJson.class);
                    if (baseResponseJson.getReturnCode() == NetConstants.IS_SUCCESS) {
                        listener.onSuccessd(true);
                    } else {
                        listener.onFailed();
                    }
                }, throwable -> listener.onFailed()
        );
    }

    public void loginUser(UserBean userBean, final ResultListener<Boolean> loginListener) {
        requestModel.sendRequest(NetConstants.URL_LOGIN_USER, userBean, AndroidSchedulers.mainThread(), responseBody -> {
            LoginResponse baseResponseJson = (LoginResponse) GsonUtil.getGsonUtil().fromJson(responseBody.string(), LoginResponse.class);
            if (baseResponseJson.getReturnCode() == NetConstants.IS_SUCCESS) {
                loginListener.onSuccessd(true);
                UserBean userBeanFromNet = baseResponseJson.getUserBean();
                storageModel.saveUserBeanToDb(userBeanFromNet);
            } else {
                loginListener.onFailed();
            }
        }, throwable -> loginListener.onFailed());

    }


    public void logout(UserBean currentUser) {
        requestModel.sendRequest(NetConstants.URL_LOGOUT, currentUser, AndroidSchedulers.mainThread(), responseBody -> {

        }, throwable -> {

        });
    }


}
