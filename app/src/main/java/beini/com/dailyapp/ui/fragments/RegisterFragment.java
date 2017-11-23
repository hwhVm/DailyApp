package beini.com.dailyapp.ui.fragments;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import javax.inject.Inject;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.Event;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.UserPresenter;

/**
 * Create by beini 2017/10/25
 */
@ContentView(R.layout.fragment_register)
public class RegisterFragment extends BaseFragment {
    @Inject
    UserPresenter userPresenter;
    @ViewInject(R.id.et_re_email)
    EditText et_re_email;
    @ViewInject(R.id.et_re_password)
    EditText et_re_password;
    @ViewInject(R.id.et_re_username)
    EditText et_re_username;
    @ViewInject(R.id.rb_main)
    RadioButton rb_main;
    @ViewInject(R.id.rb_woman)
    RadioButton rb_woman;

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
            Toast.makeText(getActivity(), "邮箱错误", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getActivity(), "用户名错误", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "密码错误", Toast.LENGTH_SHORT).show();
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

    public void onSuccess() {
        Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
        baseActivity.replaceFragment(LoginFragment.class);
    }

    public void onFailed() {
        Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
    }

}
