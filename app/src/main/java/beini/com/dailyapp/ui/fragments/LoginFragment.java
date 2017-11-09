package beini.com.dailyapp.ui.fragments;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.Event;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.http.progress.CusNetworkInterceptor;
import beini.com.dailyapp.http.progress.ProgressListener;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.BreakPointUtilDemo;
import beini.com.dailyapp.ui.presenter.FilePresenter;
import beini.com.dailyapp.ui.presenter.UserPresenter;
import beini.com.dailyapp.util.BLog;

/**
 * create by beini 2017/11/4
 */
@ContentView(R.layout.fragment_login)
public class LoginFragment extends BaseFragment {
    @ViewInject(R.id.et_email)
    EditText et_email;
    @ViewInject(R.id.et_password)
    EditText et_password;
    @Inject
    UserPresenter userPresenter;
    @Inject
    FilePresenter filePresenter;

    @Override
    public void initData() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }

    @Override
    public void initView() {

    }

    @Event({R.id.text_register, R.id.btn_login, R.id.btn_downfile, R.id.btn_stop_downfile})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                UserBean userBean = returnUserBean();
                if (userBean != null) {
                    userPresenter.loginUser(userBean, this);
                }
                break;
            case R.id.text_register:
                BLog.e("   " + (baseActivity == null));
                baseActivity.replaceFragment(RegisterFragment.class);
                break;
            case R.id.btn_downfile:
//                String urlDownLoad = "http://120.76.41.61/source/sound/sleep/Sleep_Bird_Chirping.mp3";
//                filePresenter.downloadFile(urlDownLoad);
//                filePresenter.uploadSingleFile();
                BreakPointUtilDemo.getSingleton().downFile("2000000", new CusNetworkInterceptor(new ProgressListener() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        BLog.e("          bytesRead= " + bytesRead + "  contentLength=" + contentLength + "  done=" + done);
                    }
                }));
                break;
            case R.id.btn_stop_downfile:
                break;
        }
    }


    public UserBean returnUserBean() {
        UserBean userBean = new UserBean();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "账号不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }
        userBean.setUsername(email);
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }
        userBean.setPassword(password);
        return userBean;
    }

    public void OnSuccess() {//登录成功
        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
    }

    public void onFalied() {//登录失败
        Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();

    }
}
