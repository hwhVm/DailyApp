package beini.com.dailyapp.ui.fragments;


import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;

import javax.inject.Inject;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.Event;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.inter.GlobalApplicationListener;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.UserPresenter;
import beini.com.dailyapp.ui.view.GlobalEditText;

/**
 * Create by beini 2017/10/25
 */
@ContentView(R.layout.fragment_register)
public class RegisterFragment extends BaseFragment implements GlobalApplicationListener {
    @Inject
    UserPresenter userPresenter;
    @ViewInject(R.id.et_re_email)
    GlobalEditText et_re_email;
    @ViewInject(R.id.et_re_password)
    GlobalEditText et_re_password;
    @ViewInject(R.id.et_re_username)
    GlobalEditText et_re_username;
    @ViewInject(R.id.rb_main)
    RadioButton rb_main;
    @ViewInject(R.id.rb_woman)
    RadioButton rb_woman;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void init() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }

    @Override
    protected void hiddenChanged(boolean hidden) {
        baseActivity.setToolbarTitle(getString(R.string.register_text));
    }


    @Event({R.id.btn_register})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                UserBean userBean = returnUserBean();
                if (userBean != null) {
                    userPresenter.registerUser(userBean, this);
                }
                break;

        }
    }

    public UserBean returnUserBean() {
        UserBean userBean = new UserBean();
        String email = et_re_email.getText().toString();
        String username = et_re_username.getText().toString();
        String password = et_re_password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            showToast(getString(R.string.error_email));
            return null;
        }
        if (TextUtils.isEmpty(username)) {
            showToast(getString(R.string.error_username));
            return null;
        }
        if (TextUtils.isEmpty(password)) {
            showToast(getString(R.string.error_password));
            return null;
        }
        userBean.setEmail(email);
        int id = 0;
        if (rb_main.isChecked()) {
            id = 0;
        }
        if (rb_woman.isChecked()) {
            id = 1;
        }
        userBean.setPassword(password);
        userBean.setSex(id);
        userBean.setUsername(username);
        return userBean;
    }

    @Override
    public void onResult(boolean aBoolen) {
        if (aBoolen) {
            showToast(getString(R.string.register_success));
            baseActivity.back();
        } else {
            showToast(getString(R.string.register_failed));
        }
    }
}
