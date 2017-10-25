package beini.com.dailyapp.ui;

import beini.com.dailyapp.ui.fragments.RegisterFragment;

public class MainActivity extends BaseActivity {

    @Override
    public void initView() {
        replaceFragment(RegisterFragment.class);
    }
}
