package beini.com.dailyapp.ui.fragments;


import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.R;
import beini.com.dailyapp.bean.DailyBean;
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
 * Create by beini 2017/10/19
 */
@ContentView(R.layout.fragment_daily_edit)
public class DailyEditFragment extends BaseFragment {
    @Inject
    DailyPresenter dailyPresenter;
    @ViewInject(R.id.daily_title)
    EditText daily_title;
    @ViewInject(R.id.daily_author)
    EditText daily_author;
    @ViewInject(R.id.daily_content)
    EditText daily_content;

    public void initData() {

    }

    @Override
    public void initView() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }

    @Event({R.id.btn_commit})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                dailyPresenter.insertDaily(returnDailyBean(), this);
                break;
        }
    }

    public DailyBean returnDailyBean() {
        DailyBean dailyBean = new DailyBean();
        dailyBean.setAuthor(daily_author.getText().toString());
        dailyBean.setDate(new Date().toString());
        dailyBean.setContent(daily_content.getText().toString());
        Box<UserBean> userBeanBox = GlobalApplication.getInstance().getBoxStore().boxFor(UserBean.class);
        List<UserBean> userBeans = userBeanBox.getAll();
        if (userBeans != null && userBeans.size() > 0) {
            dailyBean.setUser_id(userBeans.get(userBeans.size() - 1).getUser_id());
        }
        dailyBean.setTitle(daily_title.getText().toString());
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
