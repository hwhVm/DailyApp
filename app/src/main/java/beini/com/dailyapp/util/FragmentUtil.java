package beini.com.dailyapp.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.R;
import beini.com.dailyapp.ui.fragments.BaseFragment;
import beini.com.dailyapp.ui.fragments.DailyShowFragment;
import beini.com.dailyapp.ui.fragments.MineFragment;
import beini.com.dailyapp.ui.fragments.SquareFragment;

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

    public static Fragment addFragment(FragmentManager fragmentManager, Class fragment, String tag) {
        if (fragmentManager != null) {
            if (fm == null) {
                fm = fragmentManager;
            }
            hideAllFragment();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            //查找是否存在Fragment
            Fragment currentFragment = fragmentManager.findFragmentByTag(tag);

            if (currentFragment == null) {//创建
                currentFragment = (Fragment) ObjectUtil.createInstance(fragment);
                ft.add(id, currentFragment, tag);
                tags.add(tag);
            } else {
                ft.show(currentFragment);
            }
            ft.commit();

            return currentFragment;
        }
        return null;
    }


    private static long mLastKeyDown = 0;

    public static void removePreFragment() {
        if (tags != null && tags.size() > 1 && !isHome(fm)) {
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

    public static boolean isHome(FragmentManager fragmentManager) {
        if (fragmentManager.findFragmentById(R.id.frame_layout) instanceof DailyShowFragment || fragmentManager.findFragmentById(R.id.frame_layout) instanceof SquareFragment || fragmentManager.findFragmentById(R.id.frame_layout) instanceof MineFragment) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * remove between  fragmen
     */
    public static void removeBetweenFragment(Fragment fragment) {
        String tagNames = fragment.getClass().getName();
        if (tags.contains(tagNames)) {
            int index = tags.indexOf(tagNames);
            for (int i = index; i < tags.size(); i++) {
                Fragment fragmentDelete = fm.findFragmentByTag(tags.get(i));
                removeFragment(fragmentDelete);
            }
            addFragment(fm, fragment);
        } else {
            throw new RuntimeException("Fragment exist");
        }
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
            int index = tags.indexOf(fragment.getClass().getName());
            tags.remove(index);
            tags.add(fragment.getClass().getName());
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
