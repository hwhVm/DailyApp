package beini.com.dailyapp.bean;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Relation;

/**
 * Created by beini on 2017/10/19.
 */
@Entity
public class UserBean {
    @Id(assignable = true)
    private Long user_id;
    private String username;
    private String password;
    private String email;
    private int sex;
    @Relation(idProperty = "user_id")
    private List<DailyBean> stdudents;


    public List<DailyBean> getStdudents() {
        return stdudents;
    }

    public void setStdudents(List<DailyBean> stdudents) {
        this.stdudents = stdudents;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSex() {
        return sex;
    }


    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", sex=" + sex +
                '}';
    }
}
