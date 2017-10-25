package beini.com.dailyapp.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.ViewInjectorImpl;
import beini.com.dailyapp.ui.fragments.BaseFragment;
import beini.com.dailyapp.util.FragmentUtil;
import beini.com.dailyapp.util.ObjectUtil;

@ContentView(R.layout.activity_base)
public abstract class BaseActivity extends Activity {

    private FragmentManager customerFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectorImpl.registerInstance(this);
        customerFragmentManager = getFragmentManager();
        initView();
    }

    public abstract void initView();

    public void replaceFragment(Class<?> fragment) {
        BaseFragment baseFragment = (BaseFragment) ObjectUtil.createInstance(fragment);
        Fragment newFragment = customerFragmentManager.findFragmentByTag(fragment.getName());
        if (newFragment != null) {
            FragmentUtil.showFragment(newFragment);
        } else {
            FragmentUtil.addFragment(customerFragmentManager, baseFragment);
        }
    }
}
