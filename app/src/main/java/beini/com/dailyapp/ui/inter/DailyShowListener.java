package beini.com.dailyapp.ui.inter;

import java.util.List;

import beini.com.dailyapp.bean.DailyBean;

/**
 * Created by beini on 2017/12/7.
 */

public interface DailyShowListener {
    void onResult(boolean aBoolean, List<DailyBean> dailyBeans);
}
