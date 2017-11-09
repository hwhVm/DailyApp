package beini.com.dailyapp.http.progress;

import java.io.IOException;

import beini.com.dailyapp.util.BLog;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by beini on 2017/11/9.
 */

public class CusNetworkInterceptor implements Interceptor {
    private ProgressListener progressListener;

    public CusNetworkInterceptor(ProgressListener progressListener) {
        BLog.e("    CusNetworkInterceptor    ");
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        BLog.e("    intercept    ");
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                .build();
    }
}
