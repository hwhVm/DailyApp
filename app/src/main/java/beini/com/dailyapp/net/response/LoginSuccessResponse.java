package beini.com.dailyapp.net.response;

/**
 * Created by beini on 2017/10/25.
 */
public class LoginSuccessResponse extends BaseResponseJson {
    private String token;
    private long userId;
    private String ip;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
