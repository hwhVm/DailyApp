package beini.com.dailyapp.ui.presenter;

import java.util.List;

import beini.com.dailyapp.bean.DailyBean;
import beini.com.dailyapp.bean.DailyPageBean;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.net.response.BaseResponseJson;
import beini.com.dailyapp.ui.inter.ResultListener;
import beini.com.dailyapp.ui.inter.UploadListener;
import beini.com.dailyapp.util.BLog;
import beini.com.dailyapp.util.GsonUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by beini on 2017/10/19.
 */

public class DailyPresenter extends BasePresenter {

    public void insertDaily(DailyBean dailyBean, final UploadListener listener) {
        requestModel.sendRequest(NetConstants.URL_ADD_DAILY, dailyBean, AndroidSchedulers.mainThread(),
                responseBody -> {
                    BaseResponseJson baseResponseJson = (BaseResponseJson) GsonUtil.getGsonUtil().fromJson(responseBody.string(), BaseResponseJson.class);
                    if (baseResponseJson.getReturnCode() == NetConstants.IS_SUCCESS) {
                        listener.onSuccessd(true);
                    } else {
                        listener.onSuccessd(false);
                    }
                },
                throwable -> listener.onSuccessd(false));
    }

    public void queryDailyBynum(DailyPageBean pageTableForm, final ResultListener<List<DailyBean>> listener) {
        requestModel.sendRequest(NetConstants.URL_QUERY_DAILY_BY_NUM, pageTableForm, AndroidSchedulers.mainThread(),
                responseBody -> {
                    String str = responseBody.string();
                    DailyPageBean dailyPageBean = (DailyPageBean) GsonUtil.getGsonUtil().fromJson(str, DailyPageBean.class);
                    if (dailyPageBean != null) {
                        listener.onSuccessd(dailyPageBean.getDailyBeans());
                    } else {
                        listener.onFailed();
                    }
                },
                throwable -> listener.onFailed());
    }

    public void queryDailyCount() {
        requestModel.sendRequest(NetConstants.URL_QUERY_DAILY_COUNT, "", AndroidSchedulers.mainThread(),
                responseBody -> BLog.e("       " + responseBody.string()),
                throwable -> {

                });
    }
}
