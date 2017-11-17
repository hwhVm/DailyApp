package beini.com.dailyapp.ui.fragments;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import beini.com.dailyapp.R;
import beini.com.dailyapp.adapter.DailyAdapter;
import beini.com.dailyapp.bean.DailyBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.util.BLog;

/**
 * Create by beini  2017/11/6
 */
@ContentView(R.layout.fragment_daily_show)
public class DailyShowFragment extends BaseFragment {
    @ViewInject(R.id.recycle_daily_list)
    RecyclerView recycle_daily_list;
    private DailyAdapter dailyAdapter;

    @Override
    public void initData() {
        final List<DailyBean> dailyBeans = new ArrayList<>();
        final DailyBean dailyBean1 = new DailyBean().setContent("11111111111");
        dailyBeans.add(dailyBean1);
        DailyBean dailyBean2 = new DailyBean().setContent("22222222222");
        dailyBeans.add(dailyBean2);
        DailyBean dailyBean3 = new DailyBean().setContent("333333333");
        dailyBeans.add(dailyBean3);
        DailyBean dailyBean4 = new DailyBean().setContent("444444444");
        dailyBeans.add(dailyBean4);
        DailyBean dailyBean5 = new DailyBean().setContent("555555555");
        dailyBeans.add(dailyBean5);
        DailyBean dailyBean6 = new DailyBean().setContent("66666666");
        dailyBeans.add(dailyBean6);
        DailyBean dailyBean7 = new DailyBean().setContent("777777777");
        dailyBeans.add(dailyBean7);
        DailyBean dailyBean8 = new DailyBean().setContent("888888888");
        dailyBeans.add(dailyBean8);
        DailyBean dailyBean9 = new DailyBean().setContent("999999999");
        dailyBeans.add(dailyBean9);
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
}
