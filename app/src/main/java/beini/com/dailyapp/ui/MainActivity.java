package beini.com.dailyapp.ui;

import beini.com.dailyapp.ui.route.RouteService;

public class MainActivity extends BaseActivity {

    @Override
    public void init() {
        RouteService.getInstance().setBaseActivity(this);
        RouteService.getInstance().jumpToAnyWhere(RouteService.FRAGMENT_LOGIN);
    }
}
