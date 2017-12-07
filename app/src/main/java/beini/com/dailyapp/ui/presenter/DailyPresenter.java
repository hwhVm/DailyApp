package beini.com.dailyapp.ui.presenter;

import javax.inject.Inject;

import beini.com.dailyapp.bean.DailyBean;
import beini.com.dailyapp.bean.DailyPageBean;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.net.response.BaseResponseJson;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.inter.DailyShowListener;
import beini.com.dailyapp.ui.inter.GlobalApplicationListener;
import beini.com.dailyapp.ui.model.RequestModel;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.util.BLog;
import beini.com.dailyapp.util.Base64Util;
import beini.com.dailyapp.util.GsonUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

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

    public void insertDaily(DailyBean dailyBean, final GlobalApplicationListener globalApplicationListener) {
        requestModel.sendRequest(NetConstants.URL_ADD_DAILY, dailyBean, AndroidSchedulers.mainThread(), responseBody -> {
            BaseResponseJson baseResponseJson = (BaseResponseJson) GsonUtil.getGsonUtil().fromJson(responseBody.string(), BaseResponseJson.class);
            if (baseResponseJson.getReturnCode() == NetConstants.IS_SUCCESS) {
                globalApplicationListener.onResult(true);
            } else {
                globalApplicationListener.onResult(false);
            }
        }, throwable -> {
            globalApplicationListener.onResult(false);
        });
    }

    public void queryDailyBynum(DailyPageBean pageTableForm, final DailyShowListener dailyShowListener) {
        requestModel.sendRequest(NetConstants.URL_QUERY_DAILY_BY_NUM, pageTableForm, AndroidSchedulers.mainThread(), responseBody -> {
            String str = responseBody.string();
            String decode = Base64Util.decode(str);
            DailyPageBean dailyPageBean = (DailyPageBean) GsonUtil.getGsonUtil().fromJson(decode, DailyPageBean.class);
            if (dailyPageBean != null) {
                dailyShowListener.onResult(true, dailyPageBean.getDailyBeans());
            } else {
                dailyShowListener.onResult(false, null);
            }
        }, throwable -> {
            dailyShowListener.onResult(false, null);
        });
    }

    public void queryDailyCount() {
        requestModel.sendRequest(NetConstants.URL_QUERY_DAILY_COUNT, "", AndroidSchedulers.mainThread(), responseBody -> BLog.e("       " + responseBody.string()), new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }
}
