package beini.com.dailyapp;

import android.app.Application;
import beini.com.dailyapp.bean.MyObjectBox;
import io.objectbox.BoxStore;

/**
 * Created by beini on 2017/10/24.
 */

public class GlobalApplication extends Application {
    private static GlobalApplication INSTANCE;
    private BoxStore boxStore;

    public BoxStore getBoxStore() {
        return boxStore;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        boxStore = MyObjectBox.builder().androidContext(this).build();

    }

    public static GlobalApplication getInstance() {
        return INSTANCE;
    }

}
