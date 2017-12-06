package beini.com.dailyapp.ui;

import beini.com.dailyapp.ui.route.RouteService;

public class MainActivity extends BaseActivity {

    //new Thread(new Runnable() {
//        @Override
//        public void run() {
//            System.out.println("Before Java8, too much code for too little to do");
//        }
    @Override
    public void initView() {
        RouteService.getInstance().jumpToLogin(this);
    }
}
