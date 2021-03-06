package beini.com.dailyapp.net.progress;

/**
 * Created by beini on 2017/11/7.
 */

public interface ProgressListener {
    void onStart();
    /**
     * @param bytesRead 已经下载或上传字节数
     * @param contentLength    总字节数
     * @param done     是否完成
     */
    void update(long bytesRead, long contentLength, boolean done);

    void  onStop();

    void  onError(Exception e);
}
