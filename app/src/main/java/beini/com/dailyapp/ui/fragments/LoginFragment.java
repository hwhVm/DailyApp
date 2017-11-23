package beini.com.dailyapp.ui.fragments;


import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.R;
import beini.com.dailyapp.bean.FileRequestBean;
import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.Event;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.constant.Constants;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.FilePresenter;
import beini.com.dailyapp.ui.presenter.UserPresenter;
import beini.com.dailyapp.util.BLog;
import beini.com.dailyapp.util.MD5Util;
import beini.com.dailyapp.util.SPUtils;
import io.objectbox.Box;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * create by beini 2017/11/4
 */
@ContentView(R.layout.fragment_login)
public class LoginFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    @ViewInject(R.id.et_email)
    EditText et_email;
    @ViewInject(R.id.et_password)
    EditText et_password;
    @Inject
    UserPresenter userPresenter;
    @Inject
    FilePresenter filePresenter;
    @ViewInject(R.id.pro_bar)
    ProgressBar pro_bar;


    @Override
    public void initData() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
        //
        Box<UserBean> userBeanBox = GlobalApplication.getInstance().getBoxStore().boxFor(UserBean.class);
        List<UserBean> userBeans = userBeanBox.getAll();
        UserBean userBean = null;
        if (userBeans != null && userBeans.size() > 0) {
            userBean = userBeans.get(userBeans.size() - 1);
            if (userBean != null) {
//                baseActivity.replaceFragment(DailyShowFragment.class);
//                baseActivity.remove(LoginFragment.class);
                login();
            }

        }
    }

    @Override
    public void initView() {

    }

    @Event({R.id.text_register, R.id.btn_login, R.id.btn_downfile, R.id.btn_stop_downfile})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //进行权限检查
                strorageTask();
                break;
            case R.id.text_register:
                baseActivity.replaceFragment(RegisterFragment.class);
                break;
            case R.id.btn_downfile:
//                filePresenter.getFileInfo(new FileRequestBean(), this);
                filePresenter.uploadFile(pro_bar);
                break;
            case R.id.btn_stop_downfile:
                filePresenter.cancelDownloadFileBreakPoint();
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
        UserBean userBeanLogin = returnUserBean();
        userPresenter.loginUser(userBeanLogin, this);
    }

    public UserBean returnUserBean() {
        UserBean userBean = new UserBean();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "账号不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }
        userBean.setEmail(email);
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }
        userBean.setPassword(password);
        return userBean;
    }

    public void onStartDownload(FileRequestBean fileRequestBean) {
        //根据文件id/username 返回文件的相关信息：大小，
        //首先验证本地文件的完整性
        //服务器端是否修改
        //数据库保存
        File file = new File(Constants.EXTEND_STORAGE_PATH + "sum.zip");
        if (file.exists()) {
            String fileMd5 = MD5Util.file2Md5(file);
            if (fileMd5.equals(fileRequestBean.getFileMd5())) {
                //已经下载
            } else {//文件不完整
                long localRange = (long) SPUtils.get(GlobalApplication.getInstance().getApplicationContext(), Constants.DEMO_RANGE, 0L);
                BLog.e("   localRange=" + localRange);
                fileRequestBean.setRange(String.valueOf(localRange));
                filePresenter.downloadFileBreakPoint(fileRequestBean, pro_bar);
            }
        } else {
            long localRange = (long) SPUtils.get(GlobalApplication.getInstance().getApplicationContext(), Constants.DEMO_RANGE, 0L);
            BLog.e("   localRange=" + localRange);
            fileRequestBean.setRange(String.valueOf(localRange));
            filePresenter.downloadFileBreakPoint(fileRequestBean, pro_bar);
        }

    }

    public void onFailedDownload() {
        Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_SHORT).show();
    }

    public void OnSuccess() {//登录成功
        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
        baseActivity.replaceFragment(DailyShowFragment.class);
        baseActivity.remove(LoginFragment.class);
    }

    public void onFalied() {//登录失败
        Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
    }

    //
    private boolean hasExternalStoragePermission() {
        return EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BLog.e("      onRequestPermissionsResult      requestCode=" + requestCode + "   permissions=" + permissions.length);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        BLog.e("onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        BLog.e("onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).setTitle("title").setRationale("setRationale").build().show();//也可以自定义
        }
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
