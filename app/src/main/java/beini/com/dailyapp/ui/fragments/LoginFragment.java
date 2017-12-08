package beini.com.dailyapp.ui.fragments;


import android.Manifest;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.R;
import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.Event;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.constant.Constants;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.inter.GlobalApplicationListener;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.UserPresenter;
import beini.com.dailyapp.ui.route.RouteService;
import beini.com.dailyapp.ui.view.GlobalEditText;
import beini.com.dailyapp.util.BLog;
import io.objectbox.Box;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * create by beini 2017/11/4
 */
@ContentView(R.layout.fragment_login)
public class LoginFragment extends BaseFragment implements GlobalApplicationListener {
    GlobalEditText et_email;
    GlobalEditText et_password;
    @ViewInject(R.id.text_input_layout_email)
    TextInputLayout text_input_layout_email;
    @ViewInject(R.id.text_input_layout_password)
    TextInputLayout text_input_layout_password;

    @Inject
    UserPresenter userPresenter;

    @Override
    public void init() {
        et_email = (GlobalEditText) text_input_layout_email.getEditText();
        et_password = (GlobalEditText) text_input_layout_password.getEditText();
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
        Box<UserBean> userBeanBox = GlobalApplication.getInstance().getBoxStore().boxFor(UserBean.class);
        List<UserBean> userBeans = userBeanBox.getAll();
        UserBean userBean;
        if (userBeans != null && userBeans.size() > 0) {
            userBean = userBeans.get(userBeans.size() - 1);
            if (userBean != null) {
                login();
            }
        }
    }

    @Override
    protected void hiddenChanged(boolean hidden) {
        baseActivity.setBackVisibility(View.GONE);
        baseActivity.setToolbarTitle(getString(R.string.login_text));
    }


    @Event({R.id.text_register, R.id.btn_login})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_login://进行权限检查
                strorageTask();
                break;
            case R.id.text_register:
                RouteService.getInstance().jumpToRegister(baseActivity);
                break;
        }
    }

    @AfterPermissionGranted(Constants.READ_EXTERNAL_STORAGE)
    public void strorageTask() {
        if (hasExternalStoragePermission()) {
            login();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.daily_need_read_write_permission),
                    Constants.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    public void login() {
        showLoading();
        UserBean userBeanLogin = returnUserBean();
        userPresenter.loginUser(userBeanLogin, this);
    }


    public UserBean returnUserBean() {
        UserBean userBean = new UserBean();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            text_input_layout_email.setError(getString(R.string.account_not_empty));
            return null;
        }
        userBean.setEmail(email);
        if (TextUtils.isEmpty(password)) {
            text_input_layout_password.setError(getString(R.string.password_not_empty));
            return null;
        }
        userBean.setPassword(password);
        return userBean;
    }

    @Override
    public void onResult(boolean aBoolen) {
        if (aBoolen) {
            showToast(getString(R.string.login_success));
            RouteService.getInstance().jumpToDailyShow(baseActivity);
        } else {
            showToast(getString(R.string.login_faild));
        }
        cancelLoading();
    }

    private boolean hasExternalStoragePermission() {
        return EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // 从设置返回，判定是否开启了权限
            if (hasExternalStoragePermission()) {
                login();
            } else {
                BLog.e("    没有进行任何操作     ");
            }
        }
    }


}
