package beini.com.dailyapp.ui.fragments;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.R;
import beini.com.dailyapp.adapter.DailyAdapter;
import beini.com.dailyapp.bean.DailyBean;
import beini.com.dailyapp.bean.DailyPageBean;
import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.Event;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.DailyPresenter;
import io.objectbox.Box;

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
        dailyPresenter.queryDailyBynum(returnDailyPageBean(), this);
    }


    @Override
    public void initView() {

    }

    @Event({R.id.btn_edit, R.id.btn_refresh})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_edit:
                baseActivity.replaceFragment(DailyEditFragment.class);
                break;
            case R.id.btn_refresh:
                dailyPresenter.queryDailyBynum(returnDailyPageBean(), this);
                break;
        }
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

    public void onFailed() {
        Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
    }

    public void onSuccess(List<DailyBean> dailyBeans) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_daily_list.setLayoutManager(linearLayoutManager);
        dailyAdapter = new DailyAdapter(getActivity(), dailyBeans);
        recycle_daily_list.setAdapter(dailyAdapter);
        dailyAdapter.setItemClick(new DailyAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
//                int num = 0;
//                for (int i = 0, size = dailyAdapter.getIsCheck().size(); i < size; i++) {
//                    int temp = dailyAdapter.getIsCheck().get(i);
//                    if (temp == View.VISIBLE) {
//                        num++;
//                    }
//                }
//                if (num < 5) {
//                    if (dailyAdapter.getIsCheck().size() > 0) {
//                        if (dailyAdapter.getIsCheck().get(pos) == View.VISIBLE) {
//                            dailyAdapter.getIsCheck().remove(pos);
//                            dailyAdapter.getIsCheck().add(pos, View.GONE);
//                        } else {
//                            dailyAdapter.getIsCheck().remove(pos);
//                            dailyAdapter.getIsCheck().add(pos, View.VISIBLE);
//                        }
//                    }
//                    dailyAdapter.notifyDataSetChanged();
//                } else {
//                    BLog.e("------------>");
//                }

            }
        });
    }

}
