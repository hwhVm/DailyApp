package beini.com.dailyapp.ui.module;

import beini.com.dailyapp.ui.model.RequestModel;
import beini.com.dailyapp.ui.presenter.DailyPresenter;
import beini.com.dailyapp.ui.presenter.FilePresenter;
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
    public RequestModel returnDailyModel() {
        return new RequestModel();
    }

    @Provides
    public UserPresenter returnUserPresenter() {
        return new UserPresenter();
    }

    @Provides
    public FilePresenter returnFilePresenter() {
        return new FilePresenter();
    }
}
