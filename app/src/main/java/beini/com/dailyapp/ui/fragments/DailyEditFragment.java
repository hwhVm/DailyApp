package beini.com.dailyapp.ui.fragments;


import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bean.DailyBean;
import beini.com.dailyapp.bean.DailyPageBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.Event;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.DailyPresenter;
import beini.com.dailyapp.util.CertManager;


/**
 * Create by beini 2017/10/19
 */
@ContentView(R.layout.fragment_daily_edit)
public class DailyEditFragment extends BaseFragment {
    @Inject
    DailyPresenter dailyPresenter;


    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }

    @Event({R.id.btn_test, R.id.btn_get_cer_info, R.id.btn_get_info,R.id.btn_get_dailyCount})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_test:
                dailyPresenter.insertDaily(returnDailyBean(), this);
                break;
            case R.id.btn_get_cer_info:
                try {
                    CertManager.testReadX509CerFile(getActivity().getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_get_info:
                DailyPageBean dailyPageBean = new DailyPageBean();
                dailyPageBean.setBeginIndex(0);
                dailyPageBean.setPageSize(3);
                dailyPresenter.queryDailyBynum(dailyPageBean);
                break;
            case R.id.btn_get_dailyCount:
                dailyPresenter.queryDailyCount();
                break;
        }
    }

    public DailyBean returnDailyBean() {
        DailyBean dailyBean = new DailyBean();
        dailyBean.setAuthor("by beini");
        dailyBean.setContent("test content");
        dailyBean.setDate("2017.10.19");
        return dailyBean;
    }

    public void onUIShow(boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.daily_add_successed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.daily_add_failed), Toast.LENGTH_SHORT).show();
        }
    }
}
