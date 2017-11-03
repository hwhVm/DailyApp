package beini.com.dailyapp.util;

import android.util.Log;

/**
 * Created by beini on 2017/10/19.
 */

public class BLog {
    private static String tag = "com.beini";
    private static String retrofitLog = "com.retrofit";

    public static void e(String string) {
        Log.e(tag, string);
    }

    public static void h(String string) {
        Log.d(retrofitLog, string);
    }
}
