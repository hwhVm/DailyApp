package beini.com.dailyapp.net.response;

/**
 * Created by beini on 2017/10/26.
 */
public class FileResponse extends BaseResponseJson {
    private String fileId;

    public String getFileId() {
        return fileId;
    }

    public FileResponse setFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }
}
