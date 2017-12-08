package beini.com.dailyapp.util;

import java.util.List;

/**
 * Created by beini on 2017/12/8.
 */

public interface PermissionCallbacks {

    void onPermissionsGranted(int requestCode, List<String> perms);

    void onPermissionsDenied(int requestCode, List<String> perms);

    void onPermissionsAllGranted();
}
