package beini.com.dailyapp.ui.module;

import beini.com.dailyapp.ui.model.DailyModel;
import beini.com.dailyapp.ui.presenter.DailyPresenter;
import beini.com.dailyapp.ui.presenter.UserPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Created by beini on 2017/10/19.
 */
@Module
public class DailyModule {

    @Provides
    public DailyPresenter returnDailyPresenter() {
        return new DailyPresenter();
    }

    @Provides
    public DailyModel returnDailyModel() {
        return new DailyModel();
    }

    @Provides
    public UserPresenter returnUserPresenter() {
        return new UserPresenter();
    }
}
