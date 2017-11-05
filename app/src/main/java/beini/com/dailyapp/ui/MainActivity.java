package beini.com.dailyapp.ui;

import android.util.Log;
import android.widget.EditText;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.ui.fragments.DailyEditFragment;
import beini.com.dailyapp.ui.fragments.LoginFragment;

public class MainActivity extends BaseActivity {




    @Override
    public void initView() {
//        replaceFragment(RegisterFragment.class);
        replaceFragment(LoginFragment.class);
    }
}
