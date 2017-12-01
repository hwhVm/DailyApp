package beini.com.dailyapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by beini on 2017/7/8.
 */
@Entity
public class DailyBean implements Parcelable {
    @Id(assignable = true)
    private long daily_id;
    private String title;
    private String date;
    private String content;
    private String author;
    private String picUrl;
    private long user_id;


    public long getDaily_id() {
        return daily_id;
    }

    public void setDaily_id(long daily_id) {
        this.daily_id = daily_id;
    }


    @Override
    public String toString() {
        return "DailyBean{" +
                "id=" + daily_id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }


    public void setDaily_id(int daily_id) {
        this.daily_id = daily_id;
    }

    public String getContent() {
        return content;
    }

    public DailyBean setContent(String content) {
        this.content = content;
        return this;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
