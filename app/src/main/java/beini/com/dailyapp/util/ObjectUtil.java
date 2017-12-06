package beini.com.dailyapp.util;

/**
 * Created by beini on 2017/10/19.
 */

public class ObjectUtil {

    public static Object createInstance(Class<?> className) {
        Class<?> c = null;
        try {
            c = Class.forName(className.getName());
            return c.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return c;
    }
}
