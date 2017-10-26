package beini.com.dailyapp.ui.presenter;

import javax.inject.Inject;

import beini.com.dailyapp.bean.DailyBean;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.fragments.DailyEditFragment;
import beini.com.dailyapp.ui.model.RequestModel;
import beini.com.dailyapp.ui.module.DailyModule;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by beini on 2017/10/19.
 */

public class DailyPresenter {

    @Inject
    RequestModel requestModel;

    @Inject
    public DailyPresenter() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }

    public void insertDaily(DailyBean dailyBean, final DailyEditFragment dailyEditFragment) {
        requestModel.sendRequest(NetConstants.URL_ADD_DAILY, dailyBean, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {

            }
        });

    }
}
