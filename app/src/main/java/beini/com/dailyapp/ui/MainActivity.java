package beini.com.dailyapp.ui;

import beini.com.dailyapp.ui.fragments.LoginFragment;

public class MainActivity extends BaseActivity {




    @Override
    public void initView() {
        replaceFragment(LoginFragment.class);
    }
}
