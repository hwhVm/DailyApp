package beini.com.dailyapp.net;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by beini on 2017/4/14.
 */

public interface RxReServer {
    /**
     * get
     */
    //缓存
    @GET("{url}")
    Flowable<ResponseBody> requestStandardGet(@Path("url") String url, @Header("Cache-Control") String cacheControl, @Header("max-age") String maxAge);

    /**
     * post
     */

    //标准模式
    @FormUrlEncoded
    @POST("{url}")
    Flowable<ResponseBody> requestStandardPost(@Path("url") String url, @Field("userId") String userId, @Field("password") String password);

    //多参数 @QueryMap
    @POST("{url}")
    Call<String> queryMap(@Path("url") String url, @QueryMap Map<String, String> maps);

    //json模式
    @POST("{url}")
    @NonNull
    Flowable<ResponseBody> sendRequestReturnResponseBody(@Path("url") String url, @Body Object baseRequestJson);

    //json模式 缓存
    @POST("{url}")
    @NonNull
    Flowable<ResponseBody> sendRequestReturnResponseBodyCache(@Path("url") String url, @Header("Cache-Control") String cacheControl, @Header("max-age") String maxAge, @Body Object baseRequestJson);

    /**
     * upload file
     */
    @Multipart
    @POST("{url}")
    //单张
    Flowable<ResponseBody> uploadSingleFile(@Path("url") String url, @Part MultipartBody.Part file);

    //多张
    @Multipart
    @POST("{url}")
    Flowable<ResponseBody> uploadMultiFile(@Path("url") String url, @Part() List<MultipartBody.Part> parts);

    @Multipart
    @POST("{url}")
    Flowable<ResponseBody> uploadBreakpoint(@Header("RANGE") String range, @Path("url") String url, @Part MultipartBody.Part file);
    /**
     * download file
     */
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
    @Streaming//下载大文件时候使用,小文件可以不写
    @GET
    Flowable<ResponseBody> downloadFile(@Url String fileUrl);

    /**
     * 断点下载
     * 请求文件总大小
     * 根据机型高低，分配多个线程下载
     * 记录下载进度，大小，类型等到数据库
     * 同时更新UI和通知栏，提示用户
     * 下载结束后更新数据库下载数据，追加组合文件
     * 判断文件大小，检验文件大小
     *
     * @param range
     * @param lastModify 文件是否修改  @Header("If-Range") String lastModify,
     * @param url
     * @return
     */
    @GET
    @Streaming
    Flowable<ResponseBody> downloadBreakpoint(@Header("RANGE") String range, @Url String url);


}

