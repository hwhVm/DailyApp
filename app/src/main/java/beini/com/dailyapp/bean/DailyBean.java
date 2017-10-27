package beini.com.dailyapp.bean;

/**
 * Created by beini on 2017/7/8.
 */
public class DailyBean {

    private int id;
    private String title;
    private String date;
    private String content;
    private String author;
    private String picUrl;


    public String getContent() {
        return content;
    }

    public DailyBean setContent(String content) {
        this.content = content;
        return this;
    }

    public int getId() {
        return id;
    }

    public DailyBean setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DailyBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public DailyBean setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public DailyBean setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getDate() {
        return date;
    }

    public DailyBean setDate(String date) {
        this.date = date;
        return this;
    }

}
