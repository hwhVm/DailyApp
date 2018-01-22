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
    private BaseActivity baseActivity;
    private Bundle args;

    public static RouteService getInstance() {
        return ourInstance;
    }

    private RouteService() {
    }

    public final static String FRAGMENT_LOGIN = "FRAGMENT_LOGIN";
    public final static String FRAGMENT_DAILYEDIT = "FRAGMENT_DAILYEDIT";
    public final static String FRAGMENT_DAILYSHOW = "FRAGMENT_DAILYSHOW";
    public final static String FRAGMENT_REGISTER = "FRAGMENT_REGISTER";

    public void jumpToAnyWhere(String tag) {
        switch (tag) {
            case FRAGMENT_LOGIN:
                baseActivity.replaceFragment(LoginFragment.class, getArgs(),tag);
                break;
            case FRAGMENT_DAILYEDIT:
                baseActivity.replaceFragment(DailyEditFragment.class, getArgs(),tag);
                break;
            case FRAGMENT_DAILYSHOW:
                baseActivity.remove(LoginFragment.class);
                baseActivity.replaceFragment(DailyShowFragment.class, getArgs(),tag);
                break;
            case FRAGMENT_REGISTER:
                baseActivity.replaceFragment(RegisterFragment.class, getArgs(),tag);
                break;
        }

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

    public void setBaseActivity(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }
}
