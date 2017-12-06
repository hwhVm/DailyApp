package beini.com.dailyapp.ui.route;

import android.os.Bundle;

import beini.com.dailyapp.ui.BaseActivity;
import beini.com.dailyapp.ui.fragments.DailyEditFragment;
import beini.com.dailyapp.ui.fragments.DailyShowFragment;
import beini.com.dailyapp.ui.fragments.LoginFragment;
import beini.com.dailyapp.ui.fragments.RegisterFragment;

/**
 * Created by beini on 2017/12/1.
 */

public class RouteService {
    private static final RouteService ourInstance = new RouteService();

    public static RouteService getInstance() {
        return ourInstance;
    }

    private Bundle args;

    private RouteService() {
    }

    public void jumpToLogin(BaseActivity baseActivity) {
        baseActivity.replaceFragment(LoginFragment.class);
    }

    public void jumpToDailyEdit(BaseActivity baseActivity) {
        baseActivity.replaceFragment(DailyEditFragment.class, getArgs());
    }


    public void jumpToDailyShow(BaseActivity baseActivity) {
        baseActivity.remove(LoginFragment.class);
        baseActivity.replaceFragment(DailyShowFragment.class);
    }

    public void jumpToRegister(BaseActivity baseActivity) {
        baseActivity.replaceFragment(RegisterFragment.class);
    }

    /**
     * get set
     */
    public Bundle getArgs() {
        return args;
    }

    public RouteService setArgs(Bundle args) {
        this.args = args;
        return this;
    }

}
