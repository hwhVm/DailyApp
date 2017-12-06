package beini.com.dailyapp.ui.model;

import java.util.List;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.bean.UserBean_;
import io.objectbox.Box;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by beini on 2017/11/23.
 */

public class StorageModel {

    public void saveUserBeanToDb(UserBean userBeanFromNet) {
        Flowable.just(userBeanFromNet).observeOn(Schedulers.io()).map((Function<UserBean, Object>) userBean -> {
            Box<UserBean> userBeanBox = GlobalApplication.getInstance().getBoxStore().boxFor(UserBean.class);
            userBeanBox.put(userBean);
            return true;
        }).subscribe();
    }

    public List<UserBean> getUserBeanFromDb() {
        Box<UserBean> userBeanBox = GlobalApplication.getInstance().getBoxStore().boxFor(UserBean.class);
        return userBeanBox.getAll();
    }

    public UserBean getUserBeanByEmail(String email) {
        Box<UserBean> userBeanBox = GlobalApplication.getInstance().getBoxStore().boxFor(UserBean.class);
        List<UserBean> userBeans = userBeanBox.find(UserBean_.email, email);
        if (userBeans != null && userBeans.size() > 0) {
            return userBeans.get(0);
        } else {
            return null;
        }
    }

}
