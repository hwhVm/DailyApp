package beini.com.dailyapp.ui.presenter;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import javax.inject.Inject;

import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.http.response.BaseResponseJson;
import beini.com.dailyapp.http.response.LoginSuccessResponse;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.fragments.RegisterFragment;
import beini.com.dailyapp.ui.model.DailyModel;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.util.BLog;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by beini on 2017/10/25.
 */

public class UserPresenter {

    @Inject
    DailyModel dailyModel;

    @Inject
    public UserPresenter() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }

    public void registerUser(UserBean userBean, final RegisterFragment registerFragment) {
        dailyModel.insertDaily(NetConstants.URL_REGISTER_USER, userBean, new Subscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Object o) {
                BaseResponseJson baseResponseJson = (BaseResponseJson) o;
                BLog.e("  registerUser    baseResponseJson.getReturnCode()=" + baseResponseJson.getReturnCode() + "  " + baseResponseJson.getReturnMessage());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        }, AndroidSchedulers.mainThread());

    }

    public void loginUser(UserBean userBean, final RegisterFragment registerFragment) {
        dailyModel.insertDaily(NetConstants.URL_LOGIN_USER, userBean, new Subscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Object object) {
                LoginSuccessResponse loginSuccessResponse = (LoginSuccessResponse) object;
                BLog.e("  loginUser    baseResponseJson.getReturnCode()=" + loginSuccessResponse.getReturnCode() + "  " + loginSuccessResponse.getReturnMessage());
            }

            @Override
            public void onError(Throwable t) {
                BLog.e(" loginUser   t==" + t);
            }

            @Override
            public void onComplete() {

            }
        }, AndroidSchedulers.mainThread());


    }
}
