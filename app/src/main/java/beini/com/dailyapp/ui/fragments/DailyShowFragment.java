package beini.com.dailyapp.ui.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.R;
import beini.com.dailyapp.adapter.BaseBean;
import beini.com.dailyapp.adapter.DailyAdapter;
import beini.com.dailyapp.bean.DailyBean;
import beini.com.dailyapp.bean.DailyPageBean;
import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.constant.Constants;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.inter.DailyShowListener;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.DailyPresenter;
import beini.com.dailyapp.ui.route.RouteService;
import io.objectbox.Box;

/**
 * Create by beini  2017/11/6
 */
@ContentView(R.layout.fragment_daily_show)
public class DailyShowFragment extends BaseFragment implements DailyShowListener {
    @ViewInject(R.id.recycle_daily_list)
    RecyclerView recycle_daily_list;
    @Inject
    DailyPresenter dailyPresenter;

    @Override
    public void init() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
        dailyPresenter.queryDailyBynum(returnDailyPageBean(), this);
    }

    @Override
    protected void hiddenChanged(boolean hidden) {
        baseActivity.setToolbarTitle(getString(R.string.daily_list));
        baseActivity.setAddVisibility(View.VISIBLE);
        baseActivity.setAddImageDrawable(getResources().getDrawable(R.mipmap.icon_addx));
        baseActivity.setAddOnClickListener(v -> RouteService.getInstance().setArgs(null).jumpToDailyEdit(baseActivity));
    }

    public DailyPageBean returnDailyPageBean() {
        DailyPageBean dailyPageBean = new DailyPageBean();
        dailyPageBean.setCurrentPage(0);
        dailyPageBean.setPageSize(100);
        Box<UserBean> userBeanBox = GlobalApplication.getInstance().getBoxStore().boxFor(UserBean.class);
        List<UserBean> userBeans = userBeanBox.getAll();
        if (userBeans != null && userBeans.size() > 0) {
            dailyPageBean.setUser_id(userBeans.get(userBeans.size() - 1).getUser_id());
        }
        return dailyPageBean;
    }

    @Override
    public void onResult(boolean aBoolean, List<DailyBean> dailyBeans) {
        if (aBoolean) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            final DailyAdapter dailyAdapter = new DailyAdapter(new BaseBean<>(R.layout.item_daily, dailyBeans, true));
            recycle_daily_list.setLayoutManager(linearLayoutManager);
            recycle_daily_list.setAdapter(dailyAdapter);
            dailyAdapter.setItemClick((view, position) -> {
                DailyBean dailyBean = dailyBeans.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.DAILY_EDIT_DATA, dailyBean);
                RouteService.getInstance().setArgs(bundle).jumpToDailyEdit(baseActivity);
            });
        } else {
            showToast(getString(R.string.load_faild));
        }
    }
}
