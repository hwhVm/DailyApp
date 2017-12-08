package beini.com.dailyapp.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bind.ViewInjectorImpl;
import beini.com.dailyapp.ui.BaseActivity;
import beini.com.dailyapp.ui.view.LoadingDailog;
import beini.com.dailyapp.util.BLog;
import beini.com.dailyapp.util.FragmentUtil;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by beini on 2017/10/19.
 */

public abstract class BaseFragment extends Fragment implements EasyPermissions.PermissionCallbacks {
    public BaseActivity baseActivity;

    @Override
    public void onAttach(Activity activity) {
        baseActivity = (BaseActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = ViewInjectorImpl.registerInstance(this, inflater, container);
        baseActivity.recoverToolbar();
        baseActivity.setBackListener(v -> FragmentUtil.removePreFragment());
        init();
        hiddenChanged(false);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            hiddenChanged(true);
        }
        super.onHiddenChanged(hidden);
    }

    public abstract void init();

    protected abstract void hiddenChanged(boolean hidden);

    public void showToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    private LoadingDailog loadingDailog;

    void showLoading() {
        if (loadingDailog == null) {
            loadingDailog = new LoadingDailog();
            loadingDailog.show(getFragmentManager(), "");
        }
    }

    void cancelLoading() {
        loadingDailog.dismiss();
    }

    //权限
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
            new AppSettingsDialog.Builder(this).setTitle(getString(R.string.permission_request)).setRationale(getString(R.string.permission_request_content)).build().show();//也可以自定义
        }
    }


}
