package beini.com.dailyapp.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import beini.com.dailyapp.bind.ViewInjectorImpl;
import beini.com.dailyapp.ui.BaseActivity;
import beini.com.dailyapp.ui.view.LoadingDailog;
import beini.com.dailyapp.util.BLog;

/**
 * Created by beini on 2017/10/19.
 */

public abstract class BaseFragment extends Fragment {
    public BaseActivity baseActivity;

    @Override
    public void onAttach(Activity activity) {
        baseActivity = (BaseActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = ViewInjectorImpl.registerInstance(this, inflater, container);
        baseActivity.recoverToolbar();
        init();
        hiddenChanged(false);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            hiddenChanged(true);
        }
        super.onHiddenChanged(hidden);
    }

    public abstract void init();

    protected abstract void hiddenChanged(boolean hidden);

    public void showToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    private LoadingDailog loadingDailog;

    void showLoading() {
        if (loadingDailog == null) {
            loadingDailog = new LoadingDailog();
            loadingDailog.show(getFragmentManager(), "");
        }
    }

    void cancelLoading() {
        loadingDailog.dismiss();
    }
}
