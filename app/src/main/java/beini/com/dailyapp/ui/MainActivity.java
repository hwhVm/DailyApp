package beini.com.dailyapp.ui;

import beini.com.dailyapp.ui.fragments.DailyEditFragment;

public class MainActivity extends BaseActivity {

    @Override
    public void initView() {
//        replaceFragment(RegisterFragment.class);
        replaceFragment(DailyEditFragment.class);
    }
}
