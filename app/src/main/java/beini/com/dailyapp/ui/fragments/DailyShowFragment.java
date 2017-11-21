package beini.com.dailyapp.ui.fragments;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import beini.com.dailyapp.R;
import beini.com.dailyapp.adapter.DailyAdapter;
import beini.com.dailyapp.bean.DailyBean;
import beini.com.dailyapp.bean.DailyPageBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.DailyPresenter;
import beini.com.dailyapp.util.BLog;

/**
 * Create by beini  2017/11/6
 */
@ContentView(R.layout.fragment_daily_show)
public class DailyShowFragment extends BaseFragment {
    @ViewInject(R.id.recycle_daily_list)
    RecyclerView recycle_daily_list;
    private DailyAdapter dailyAdapter;
    @Inject
    DailyPresenter dailyPresenter;

    @Override
    public void initData() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
//        dailyPresenter.queryDailyBynum();


        final List<DailyBean> dailyBeans = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_daily_list.setLayoutManager(linearLayoutManager);
        dailyAdapter = new DailyAdapter(getActivity(), dailyBeans);
        recycle_daily_list.setAdapter(dailyAdapter);
        dailyAdapter.setItemClick(new DailyAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                int num = 0;
                for (int i = 0, size = dailyAdapter.getIsCheck().size(); i < size; i++) {
                    int temp = dailyAdapter.getIsCheck().get(i);
                    if (temp == View.VISIBLE) {
                        num++;
                    }
                }
                if (num < 5) {
                    if (dailyAdapter.getIsCheck().size() > 0) {
                        if (dailyAdapter.getIsCheck().get(pos) == View.VISIBLE) {
                            dailyAdapter.getIsCheck().remove(pos);
                            dailyAdapter.getIsCheck().add(pos, View.GONE);
                        } else {
                            dailyAdapter.getIsCheck().remove(pos);
                            dailyAdapter.getIsCheck().add(pos, View.VISIBLE);
                        }
                    }
                    dailyAdapter.notifyDataSetChanged();
                } else {
                    BLog.e("------------>");
                }

            }
        });
    }

    @Override
    public void initView() {

    }

    public DailyPageBean returnDailyPageBean() {
        DailyPageBean dailyPageBean = new DailyPageBean();
//        dailyPageBean.set
        return dailyPageBean;
    }
}
