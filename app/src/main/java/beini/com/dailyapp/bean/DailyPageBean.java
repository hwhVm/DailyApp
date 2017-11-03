package beini.com.dailyapp.bean;


import java.util.List;

/**
 * Created by beini on 2017/4/12.
 */
public class DailyPageBean {
    private int currentPage;//当前页
    private int pageSize;//每页记录数
    private int beginIndex;//开始位置
    private int pageCount;//共多少页
    private int dailyCount;//共多少条记录
    private List<DailyBean> dailyBeans;
    private int user_id;


    public int getDailyCount() {
        return dailyCount;
    }

    public void setDailyCount(int dailyCount) {
        this.dailyCount = dailyCount;
    }

    public List<DailyBean> getDailyBeans() {
        return dailyBeans;
    }

    public void setDailyBeans(List<DailyBean> dailyBeans) {
        this.dailyBeans = dailyBeans;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

}
