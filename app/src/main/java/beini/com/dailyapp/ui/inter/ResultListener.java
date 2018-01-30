package beini.com.dailyapp.ui.inter;

/**
 * Created by beini on 2018/1/30.
 */

public interface ResultListener<T> {
    void onSuccessd(T t);

    void onFailed();
}
