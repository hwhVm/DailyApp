package beini.com.dailyapp.ui;

import beini.com.dailyapp.ui.route.RouteService;

public class MainActivity extends BaseActivity {

    @Override
    public void initView() {
        RouteService.getInstance().jumpToLogin(this);
    }
}
