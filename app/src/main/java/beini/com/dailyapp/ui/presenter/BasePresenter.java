package beini.com.dailyapp.ui.presenter;

import javax.inject.Inject;

import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.model.RequestModel;
import beini.com.dailyapp.ui.model.StorageModel;
import beini.com.dailyapp.ui.module.DailyModule;

/**
 * Created by beini on 2017/12/7.
 */

public class BasePresenter {
    @Inject
    RequestModel requestModel;
    @Inject
    StorageModel storageModel;
    
    @Inject
    public BasePresenter() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
    }
}
