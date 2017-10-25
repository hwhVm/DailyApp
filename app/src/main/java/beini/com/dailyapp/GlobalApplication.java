package beini.com.dailyapp;

import android.app.Application;

/**
 * Created by beini on 2017/10/24.
 */

public class GlobalApplication extends Application {
    private static GlobalApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static GlobalApplication getInstance() {
        return INSTANCE;
    }
}
