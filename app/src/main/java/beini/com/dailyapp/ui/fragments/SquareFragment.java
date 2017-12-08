package beini.com.dailyapp.ui.fragments;


import android.view.View;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bind.ContentView;

/**
 * Create by beini 2017/12/8
 */
@ContentView(R.layout.fragment_square)
public class SquareFragment extends BaseFragment {


    @Override
    public void init() {

    }

    @Override
    protected void hiddenChanged(boolean hidden) {
        baseActivity.setNavigationVisibility(View.VISIBLE);
        baseActivity.setBackVisibility(View.GONE);
    }
}
