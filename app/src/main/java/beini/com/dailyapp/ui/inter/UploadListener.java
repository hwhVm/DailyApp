package beini.com.dailyapp.ui.inter;

import beini.com.dailyapp.net.response.FileResponse;

/**
 * Created by beini on 2017/12/7.
 */

public interface UploadListener extends ResultListener<Boolean> {
    void onResult(FileResponse fileResponse);
}
