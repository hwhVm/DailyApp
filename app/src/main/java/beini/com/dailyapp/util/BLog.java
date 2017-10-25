package beini.com.dailyapp.util;

import android.util.Log;

/**
 * Created by beini on 2017/10/19.
 */

public class BLog {
    private static String tag = "com.beini";

    public static void e(String string) {
        Log.e(tag, string);
    }
}
