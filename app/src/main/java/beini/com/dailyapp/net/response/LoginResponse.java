package beini.com.dailyapp.net.response;


import beini.com.dailyapp.bean.UserBean;

/**
 * Created by beini on 2017/10/25.
 */
public class LoginResponse extends BaseResponseJson {
    private String token;
    private String ip;
    private UserBean userBean;

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
