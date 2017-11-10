package beini.com.dailyapp.http.progress;

import android.support.annotation.Nullable;

import java.io.IOException;

import beini.com.dailyapp.util.BLog;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by beini on 2017/11/7.
 */

public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private final ProgressListener progressListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
        BLog.e("   ProgressResponseBody   ");
        this.responseBody = responseBody;
        this.progressListener = progressListener;
        this.progressListener.onStart();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        BLog.e("       source     ");
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) {
                long bytesRead = 0;
                try {
                    bytesRead = super.read(sink, byteCount);
                } catch (IOException e) {
                    progressListener.onError(e);
                    e.printStackTrace();
                }
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                if (bytesRead == -1) {
                    progressListener.onStop();
                }
                return bytesRead;
            }
        };
    }


}
