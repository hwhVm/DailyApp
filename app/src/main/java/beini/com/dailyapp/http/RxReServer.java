package beini.com.dailyapp.http;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by beini on 2017/4/14.
 */

public interface RxReServer {

    @POST("{url}")
    @NonNull
    Flowable<ResponseBody> sendRequestReturnResponseBody(@Path("url") String url, @Body Object baseRequestJson);

    @Multipart
    @POST("{url}")
        //单张
    Flowable<ResponseBody> uploadSingleFile(@Path("url") String url,
                                            @Part MultipartBody.Part file);

    //多张
    @Multipart
    @POST("{url}")
    Flowable<ResponseBody> uploadMultiFile(@Path("url") String url, @Part() List<MultipartBody.Part> parts);

    /**
     * 文件下载
     * content-length:文件大小
     * content- type：文件类型
     * RANGE：文件哪里下载
     * 对下载来源校验可以加入referer, 不是目标来源的可以不予下载权限。
     *
     * @param fileUrl
     * @return
     */
    @Streaming//下载大文件时候使用
    @GET
    Flowable<ResponseBody> downloadFile(@Url String fileUrl);
}
