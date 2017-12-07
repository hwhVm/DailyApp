package beini.com.dailyapp.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.ViewInject;
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
    @ViewInject(R.id.image_base_add)
    ImageView image_base_add;
    @ViewInject(R.id.global_toolbar)
    Toolbar global_toolbar;
    @ViewInject(R.id.global_title)
    TextView global_title;

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

    public void setAddVisibility(int visibility) {
        image_base_add.setVisibility(visibility);
    }

    public void setAddImageDrawable(Drawable drawable) {
        image_base_add.setImageDrawable(drawable);
    }

    public void setAddOnClickListener(View.OnClickListener onClickListener) {
        image_base_add.setOnClickListener(onClickListener);
    }

    public void setToolbarVisibility(int visibility) {
        global_toolbar.setVisibility(visibility);
    }

    public void setToolbarTitle(String title) {
        global_title.setVisibility(View.VISIBLE);
        global_title.setText(title);
    }

    public void recoverToolbar() {
        setAddVisibility(View.GONE);
    }

}
