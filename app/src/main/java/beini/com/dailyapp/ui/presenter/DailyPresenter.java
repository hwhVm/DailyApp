package beini.com.dailyapp.ui.presenter;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import javax.inject.Inject;

import beini.com.dailyapp.bean.DailyBean;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.http.response.BaseResponseJson;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.fragments.DailyEditFragment;
import beini.com.dailyapp.ui.model.DailyModel;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.util.BLog;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by beini on 2017/10/19.
 */

public class DailyPresenter {

    @Inject
    DailyModel dailyModel;

    @Inject
    public DailyPresenter() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }

    public void insertDaily(DailyBean dailyBean, final DailyEditFragment dailyEditFragment) {
        dailyModel.insertDaily(NetConstants.URL_ADD_DAILY, dailyBean, new Subscriber<BaseResponseJson>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(BaseResponseJson baseResponseJson) {
                dailyEditFragment.onUIShow((baseResponseJson.getReturnCode() == 0));

            }

            @Override
            public void onError(Throwable t) {
                BLog.e("   t=="+t);
                dailyEditFragment.onUIShow(false);
            }

            @Override
            public void onComplete() {
            }
        }, AndroidSchedulers.mainThread());
    }
}
