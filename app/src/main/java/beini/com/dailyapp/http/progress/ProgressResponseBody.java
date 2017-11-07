package beini.com.dailyapp.http.progress;

import android.support.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * Created by beini on 2017/11/7.
 */

public class ProgressResponseBody extends ResponseBody {

    @Nullable
    @Override
    public MediaType contentType() {
        return null;
    }

    @Override
    public long contentLength() {
        return 0;
    }

    @Override
    public BufferedSource source() {
        return null;
    }

//    private final ResponseBody responseBody;
//    private final ProgressListener progressListener;
//    private BufferedSource bufferedSource;

//    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
//        this.responseBody = responseBody;
//        this.progressListener = progressListener;
//    }

//    @Nullable
//    @Override
//    public MediaType contentType() {
//        BLog.e("           responseBody.contentType()= "+responseBody.contentType());
//        return responseBody.contentType();
//    }
//
//    @Override
//    public long contentLength() {
//        BLog.e("           responseBody.contentLength()= "+responseBody.contentLength());
//        return responseBody.contentLength();
//    }
//
//    @Override
//    public BufferedSource source() {
//        if (bufferedSource == null) {
//            bufferedSource = Okio.buffer(source(responseBody.source()));
//        }
//        BLog.e(" source ");
//        return bufferedSource;
//    }

//    private Source source(Source source) {
//        return new ForwardingSource(source) {
//            long totalBytesRead = 0L;
//
//            @Override
//            public long read(Buffer sink, long byteCount) throws IOException {
//                long bytesRead = super.read(sink, byteCount);
//                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
//                progressListener.onProgress(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
//                BLog.e(" bytesRead "+bytesRead);
//                return bytesRead;
//            }
//        };
//    }
}
