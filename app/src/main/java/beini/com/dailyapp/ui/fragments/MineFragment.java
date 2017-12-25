package beini.com.dailyapp.ui.fragments;


import android.app.Fragment;
import android.view.View;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.Event;
import beini.com.dailyapp.util.FragmentUtil;
import beini.com.dailyapp.util.ObjectUtil;

/**
 * Create by beini  2017/12/8
 */
@ContentView(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {


    @Override
    protected void lazyLoad() {

    }

    @Override
    public void init() {

    }

    @Override
    protected void hiddenChanged(boolean hidden) {
        baseActivity.setNavigationVisibility(View.VISIBLE);
        baseActivity.setBackVisibility(View.GONE);
    }

    @Event(R.id.btn_demo)
    private void mEvent(View view) {
        FragmentUtil.removeBetweenFragment((Fragment) ObjectUtil.createInstance(DailyShowFragment.class));
    }

}
