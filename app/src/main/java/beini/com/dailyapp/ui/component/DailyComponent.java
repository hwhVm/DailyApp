package beini.com.dailyapp.ui.component;

import beini.com.dailyapp.ui.fragments.DailyEditFragment;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.DailyPresenter;
import dagger.Component;

/**
 * Created by beini on 2017/10/19.
 */
@Component(modules = DailyModule.class)
public interface DailyComponent {
    void inject(DailyEditFragment dailyEditFragment);

    void inject(DailyPresenter dailyPresenter);
}
