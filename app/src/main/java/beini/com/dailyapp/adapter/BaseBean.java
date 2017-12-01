package beini.com.dailyapp.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by beini on 2017/2/18.
 */

public class BaseBean<T> {
    private int id;
    private List<T> baseList;
    private boolean openCheck;

    public BaseBean(@LayoutRes int id, @NonNull List<T> baseList) {
        this.id = id;
        this.baseList = baseList;
    }

    public BaseBean(@LayoutRes int id, @NonNull List<T> baseList, boolean openCheck) {
        this.id = id;
        this.baseList = baseList;
        this.openCheck = openCheck;
    }

    public int getId() {
        return id;
    }

    public void setId(@LayoutRes int id) {
        this.id = id;
    }

    public List<T> getBaseList() {
        return baseList;
    }

    public void setBaseList(@NonNull List<T> baseList) {
        this.baseList = baseList;
    }

    public boolean isOpenCheck() {
        return openCheck;
    }

    public void setOpenCheck(boolean openCheck) {
        this.openCheck = openCheck;
    }

}
