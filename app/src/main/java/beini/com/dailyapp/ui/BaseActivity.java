package beini.com.dailyapp.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.ViewInjectorImpl;
import beini.com.dailyapp.ui.fragments.BaseFragment;
import beini.com.dailyapp.util.ActivityResultListener;
import beini.com.dailyapp.util.FragmentUtil;
import beini.com.dailyapp.util.KeyBackListener;
import beini.com.dailyapp.util.ObjectUtil;

@ContentView(R.layout.activity_base)
public abstract class BaseActivity extends Activity {

    private FragmentManager customerFragmentManager;
    private KeyBackListener keyBackListener;
    private ActivityResultListener activityResultListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectorImpl.registerInstance(this);
        customerFragmentManager = getFragmentManager();
        initView();
    }

    public abstract void initView();

    public void replaceFragment(Class<?> fragment) {
        Fragment currentFragment = customerFragmentManager.findFragmentByTag(fragment.getName());
        if (currentFragment != null) {
            FragmentUtil.showFragment(currentFragment);
        } else {
            BaseFragment baseFragment = (BaseFragment) ObjectUtil.createInstance(fragment);
            FragmentUtil.addFragment(customerFragmentManager, baseFragment);
        }
    }

    public void replaceFragment(Class<?> fragment, Bundle args) {
        Fragment currentFragment = customerFragmentManager.findFragmentByTag(fragment.getName());
        if (currentFragment != null) {
            currentFragment.setArguments(args);
            FragmentUtil.showFragment(currentFragment);
        } else {
            BaseFragment baseFragment = (BaseFragment) ObjectUtil.createInstance(fragment);
            baseFragment.setArguments(args);
            FragmentUtil.addFragment(customerFragmentManager, baseFragment);
        }
    }

    public void remove(Class<?> fragment) {
        Fragment currentFragment = customerFragmentManager.findFragmentByTag(fragment.getName());
        FragmentUtil.removeFragment(currentFragment);
    }

    public void back() {
        FragmentUtil.removePreFragment(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyBackListener != null) {
                keyBackListener.keyBack();
            } else {
                onBackPressed();
            }
            return true;
        } else {
            if (keyBackListener != null) {
                keyBackListener.onKeyDown(event);
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (activityResultListener != null) {
            activityResultListener.resultCallback(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentUtil.removePreFragment(this);
    }


    public KeyBackListener getKeyBackListener() {
        return keyBackListener;
    }

    public void setKeyBackListener(KeyBackListener keyBackListener) {
        this.keyBackListener = keyBackListener;
    }


    public ActivityResultListener getActivityResultListener() {
        return activityResultListener;
    }

    public void setActivityResultListener(ActivityResultListener activityResultListener) {
        this.activityResultListener = activityResultListener;
    }

}
