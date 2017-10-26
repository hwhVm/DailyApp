package beini.com.dailyapp.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import beini.com.dailyapp.bind.ViewInjectorImpl;
import beini.com.dailyapp.ui.BaseActivity;

/**
 * Created by beini on 2017/10/19.
 */

public abstract class BaseFragment extends Fragment {
    public BaseActivity baseActivity;

    @Override
    public void onAttach(Context context) {
        baseActivity = (BaseActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = ViewInjectorImpl.registerInstance(this, inflater, container);
        initData();
        initView();
        return view;
    }

    public abstract void initData();

    public abstract void initView();

}
