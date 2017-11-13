package beini.com.dailyapp.bean;

/**
 * Created by beini on 2017/11/10.
 */

public class FileRequestBean {
    private int id;
    private String range;
    private long lastModified;
    private String fileName;
    private String fileMd5;

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    private String filePath;
    private long fileSize;

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public FileRequestBean setId(int id) {
        this.id = id;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileRequestBean setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public long getLastModified() {
        return lastModified;
    }

    public FileRequestBean setLastModified(long lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getRange() {
        return range;
    }

    public FileRequestBean setRange(String range) {
        this.range = range;
        return this;
    }

    @Override
    public String toString() {
        return "FileRequestBean{" +
                "id=" + id +
                ", range='" + range + '\'' +
                ", lastModified=" + lastModified +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }

}
