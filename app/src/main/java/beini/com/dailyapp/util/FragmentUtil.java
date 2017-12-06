package beini.com.dailyapp.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.R;
import beini.com.dailyapp.ui.BaseActivity;
import beini.com.dailyapp.ui.fragments.BaseFragment;

/**
 * Created by beini on 2017/10/19.
 */

public class FragmentUtil {

    public static List<String> tags = new ArrayList<>();
    private static FragmentManager fm;
    private static int id = R.id.frame_layout;

    /**
     * add fragment
     *
     * @param fragmentManager
     * @param fragment
     */
    public static void addFragment(FragmentManager fragmentManager, Fragment fragment) {
        if (fragmentManager != null && fragment != null) {
            if (fm == null) {
                fm = fragmentManager;
            }
            hideAllFragment();
            FragmentTransaction ft = fragmentManager.beginTransaction();
//            if (!fragment.isAdded()) {
//                ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit, R.anim.fragment_slide_left_enter,
//                        R.anim.fragment_slide_left_exit);//动画
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            }
            ft.add(id, fragment, fragment.getClass().getName());
            tags.add(fragment.getClass().getName());
            ft.commit();
        }
    }

    private static long mLastKeyDown = 0;

    public static void removePreFragment(BaseActivity baseActivity) {
        if (tags != null && tags.size() > 1) {
            String current = tags.get(tags.size() - 1);
            String old = tags.get(tags.size() - 2);
            removeFragment(fm.findFragmentByTag(current));
            BaseFragment oldFragment = (BaseFragment) fm.findFragmentByTag(old);
            if (oldFragment != null) {
                showFragment(oldFragment);
            }
        } else {
            ToastUtils.showShortToast(GlobalApplication.getInstance().getString(R.string.app_exit));
            long timeMillis = System.currentTimeMillis();
            if (timeMillis - mLastKeyDown >= 2000) {
                mLastKeyDown = timeMillis;
            } else {
                System.exit(0);
            }
        }
    }

    /**
     * remove between  fragmen
     */
    public static void removeBetweenFragment() {

    }

    /**
     * show fragment
     *
     * @param fragment
     */
    public static void showFragment(Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        hideAllFragment();
        if (fragment != null) {
//            ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit, R.anim.fragment_slide_left_enter,
//                    R.anim.fragment_slide_left_exit);//动画
            ft.show(fragment);
            ft.commit();
        }
    }

    /**
     * hide all fragment
     */
    public static void hideAllFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        for (String str : tags) {
            Fragment fragment = fm.findFragmentByTag(str);
            if (fragment != null && !fragment.isHidden()) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    /**
     * remove all fragment
     */
    public static void removeAllFragment() {
        for (String str : tags) {
            tags.remove(tags.size() - 1);
            Fragment fragment = fm.findFragmentByTag(str);
            removeFragment(fragment);
        }
    }

    /**
     * remove fragment
     *
     * @param fragment
     */
    public static void removeFragment(Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment != null) {
            if (tags != null && tags.size() >= 1) {
//                ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit, R.anim.fragment_slide_left_enter,
//                        R.anim.fragment_slide_left_exit);//动画
                ft.remove(fragment);
                ft.commit();
                int index = tags.indexOf(fragment.getClass().getName());
                tags.remove(index);
            }
        }
    }


    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        FragmentUtil.id = id;
    }
}
