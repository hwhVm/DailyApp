package beini.com.dailyapp.util;

/**
 * Created by beini on 2017/12/5.
 */
public class StringUtil {
    public static String returnStr(String str) {
        int i = str.indexOf(".");
        String result = str.substring(i, str.length());
        return result;
    }
}
