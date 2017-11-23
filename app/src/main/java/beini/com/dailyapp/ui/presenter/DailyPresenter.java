package beini.com.dailyapp.ui.presenter;

import javax.inject.Inject;

import beini.com.dailyapp.bean.DailyBean;
import beini.com.dailyapp.bean.DailyPageBean;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.net.response.BaseResponseJson;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.fragments.DailyEditFragment;
import beini.com.dailyapp.ui.fragments.DailyShowFragment;
import beini.com.dailyapp.ui.model.RequestModel;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.util.BLog;
import beini.com.dailyapp.util.GsonUtil;
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
                BaseResponseJson baseResponseJson = (BaseResponseJson) GsonUtil.getGsonUtil().fromJson(responseBody.string(), BaseResponseJson.class);
                if (baseResponseJson.getReturnCode() == NetConstants.IS_SUCCESS) {
                    dailyEditFragment.onUIShow(true);
                } else {
                    dailyEditFragment.onUIShow(false);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                BLog.e("     throwable= " + throwable.getLocalizedMessage());
                dailyEditFragment.onUIShow(false);
            }
        });
    }

    public void queryDailyBynum(DailyPageBean pageTableForm, final DailyShowFragment dailyShowFragment) {
        requestModel.sendRequest(NetConstants.URL_QUERY_DAILY_BY_NUM, pageTableForm, AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                DailyPageBean dailyPageBean = (DailyPageBean) GsonUtil.getGsonUtil().fromJson(responseBody.string(), DailyPageBean.class);
                if (dailyPageBean != null) {
                    dailyShowFragment.onSuccess(dailyPageBean.getDailyBeans());
                } else {
                    dailyShowFragment.onFailed();
                }

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                BLog.e("  throwable " + throwable.getLocalizedMessage());
                dailyShowFragment.onFailed();
            }
        });
    }

    public void queryDailyCount() {
        requestModel.sendRequest(NetConstants.URL_QUERY_DAILY_COUNT, "", AndroidSchedulers.mainThread(), new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                BLog.e("       " + responseBody.string());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }
}
