package beini.com.dailyapp.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.bind.ViewInjectorImpl;
import beini.com.dailyapp.util.ActivityResultListener;
import beini.com.dailyapp.util.FragmentUtil;
import beini.com.dailyapp.util.KeyBackListener;

@ContentView(R.layout.activity_base)
public abstract class BaseActivity extends AppCompatActivity {

    private FragmentManager customerFragmentManager;
    private KeyBackListener keyBackListener;
    private ActivityResultListener activityResultListener;
    @ViewInject(R.id.image_base_add)
    ImageView image_base_add;
    @ViewInject(R.id.global_toolbar)
    Toolbar global_toolbar;
    @ViewInject(R.id.global_title)
    TextView global_title;
    @ViewInject(R.id.navigation)
    BottomNavigationView navigation;
    @ViewInject(R.id.image_base_back)
    ImageView image_base_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectorImpl.registerInstance(this);
        customerFragmentManager = getFragmentManager();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnClickListener(v -> {

        });
        init();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
//                replaceFragment(DailyShowFragment.class, null);
                return true;
            case R.id.navigation_dashboard:
//                replaceFragment(SquareFragment.class, null);
                return true;
            case R.id.navigation_notifications:
//                replaceFragment(MineFragment.class, null);
                return true;
        }
        return false;
    };

    public abstract void init();

    public void replaceFragment(Class fragment, Bundle args, String tag) {
        Fragment fragment1 = FragmentUtil.addFragment(customerFragmentManager, fragment, tag);
        fragment1.setArguments(args);
    }

    public void remove(Class<?> fragment) {
        Fragment currentFragment = customerFragmentManager.findFragmentByTag(fragment.getName());
        FragmentUtil.removeFragment(currentFragment);
    }

    public void back() {
        FragmentUtil.removePreFragment();
    }

    //设置监听
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
        FragmentUtil.removePreFragment();
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

    //image_base_back
    public void setBackListener(View.OnClickListener onClickListener) {
        image_base_back.setVisibility(View.VISIBLE);
        image_base_back.setOnClickListener(onClickListener);
    }

    public void setBackVisibility(int visibility) {
        image_base_back.setVisibility(visibility);
    }

    //设置UI
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

    public void setNavigationVisibility(int visibility) {
        navigation.setVisibility(visibility);
    }

    public void recoverToolbar() {
        setBackVisibility(View.GONE);
        setAddVisibility(View.GONE);
        setNavigationVisibility(View.GONE);
    }

}
