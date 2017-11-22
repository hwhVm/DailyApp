package beini.com.dailyapp.ui;

import beini.com.dailyapp.ui.fragments.LoginFragment;

public class MainActivity extends BaseActivity {
//    private UserBeanDao userBeanDao;


    @Override
    public void initView() {
        replaceFragment(LoginFragment.class);
    }
}
