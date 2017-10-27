package beini.com.dailyapp.ui.fragments;


import android.view.View;

import javax.inject.Inject;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.Event;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.UserPresenter;
import beini.com.dailyapp.util.BLog;

/**
 * Create by beini 2017/10/25
 */
@ContentView(R.layout.fragment_register)
public class RegisterFragment extends BaseFragment {
    @Inject
    UserPresenter userPresenter;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initData() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }

    @Override
    public void initView() {

    }

    @Event({R.id.btn_register, R.id.btn_login, R.id.btn_upload, R.id.btn_mutil_upload})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                BLog.e("  btn_register ");
                UserBean userBean = new UserBean();
                userBean.setEmail("22@qq.com");
                userBean.setPassword("123456");
                userBean.setSex(1);
                userBean.setUsername("beini");
                BLog.e("  btn_register  " + userBean.toString());
                userPresenter.registerUser(userBean, this);
                break;
            case R.id.btn_login:
                UserBean userBeanLogin = new UserBean();
                userBeanLogin.setEmail("874140704@qq.com");
                userBeanLogin.setPassword("123456");
                userPresenter.loginUser(userBeanLogin, this);
                break;
            case R.id.btn_upload:
                userPresenter.uploadSingleFileProcess(this);
                break;
            case R.id.btn_mutil_upload:
                userPresenter.uploadMultiFile(this);

                break;
        }
    }

}
