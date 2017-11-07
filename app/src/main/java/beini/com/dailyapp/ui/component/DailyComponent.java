package beini.com.dailyapp.ui.component;

import beini.com.dailyapp.ui.fragments.DailyEditFragment;
import beini.com.dailyapp.ui.fragments.LoginFragment;
import beini.com.dailyapp.ui.fragments.RegisterFragment;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.DailyPresenter;
import beini.com.dailyapp.ui.presenter.UserPresenter;
import dagger.Component;

/**
 * Created by beini on 2017/10/19.
 */
@Component(modules = DailyModule.class)
public interface DailyComponent {
    void inject(DailyEditFragment dailyEditFragment);

    void inject(DailyPresenter dailyPresenter);

    void inject(UserPresenter userPresenter);

    void inject(RegisterFragment registerFragment);

    void inject(LoginFragment loginFragment);
}
