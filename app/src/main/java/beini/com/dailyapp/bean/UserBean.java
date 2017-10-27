package beini.com.dailyapp.bean;

/**
 * Created by beini on 2017/10/19.
 */
public class UserBean {
    private long id;
    private String username;
    private String password;
    private String email;
    private int sex;

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", sex=" + sex +
                '}';
    }

    public long getId() {
        return id;
    }

    public UserBean setId(long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserBean setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserBean setEmail(String email) {
        this.email = email;
        return this;
    }

    public int getSex() {
        return sex;
    }

    public UserBean setSex(int sex) {
        this.sex = sex;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserBean setPassword(String password) {
        this.password = password;
        return this;
    }
}
