package beini.com.dailyapp.util;


import com.google.gson.Gson;

/**
 * Created by beini on 2017/10/26.
 */

public class GsonUtil {
    private static Gson gson = null;
    private static GsonUtil gsonUtil = null;

    public static GsonUtil getGsonUtil() {
        if (gsonUtil == null) {
            gsonUtil = new GsonUtil();
            gson = new Gson();
        }
        return gsonUtil;
    }


    public Object fromJson(String str, Class temClass) {
        String decode = Base64Util.decode(str);
        return gson.fromJson(decode, temClass);

    }

    public String toJson(Object object) {
        return gson.toJson(object);
    }
}
