package beini.com.dailyapp.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import beini.com.dailyapp.constant.Constants;

/**
 * Created by beini on 2017/11/28.
 */

public class BitmapUtil {

    public static Bitmap getBitmapFromResource(Context context, int resId, int n) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = n;  //将原图缩小n倍
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    public static Bitmap getBitmapFromLocal(String pathName, int n) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = n;
        return BitmapFactory.decodeFile(pathName, options);
    }

    /**
     * 可以用于加载图片前获取图片类型尺寸等
     *
     * @param pathName
     */
    public static void getInfoFromImage(String pathName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//bitmap为null但可查询bitmap相关信息
        BitmapFactory.decodeFile(pathName, options);
        int imageheight = options.outHeight;//inJustDecodeBounds为true时:代表原始高度，为false时:包括了缩放之后的高度，下同；
        int imageWidth = options.outWidth;
        int desity = options.inDensity;//密度  通过Bitmap.setDensity(int)设置默认为0；
        boolean inScaled = options.inScaled;
        String mimeType = options.outMimeType;//类型
        BLog.e("------>imageheight=" + imageheight + "   imageWidth=" + imageWidth + "   desity=" + desity + "  inScaled=" + inScaled + "  mimeType=" + mimeType);
    }

    public Bitmap getBitmapFromResourceByStream(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;//不透明位图，不需要保真度高的色彩
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);//减少对文件的操作次数，从而达到提高性能的目的,坏处是要额外的内存来做缓冲区;
        return BitmapFactory.decodeStream(is, null, opt);
    }

    //file:/storage/emulated/0/a.jpg
    public static Bitmap getBitmapByUri(Context context, Uri uri) {
        if (uri == null) return null;
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap convertBase64ToBitmap(String str) {
        Bitmap bitmap;
        byte[] bitmapArray;
        bitmapArray = Base64.decode(str, Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                bitmapArray.length);
        return bitmap;
    }


    public static String getBase64ByFilePath(String filePath) {
        File file = new File(filePath);
        FileInputStream is;
        try {
            is = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                out.write(i);
                i = is.read();
            }
            return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getLoacalBitmapByPath(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveBitmapToLocal(Context context, Bitmap bitmap) {
        String tempFileName = String.valueOf(System.currentTimeMillis());
        File file = new File(Constants.EXTEND_STORAGE_PATH, tempFileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), tempFileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + "/sdcard/namecard/")));
        file.delete();
    }

    public byte[] convertBitempToByteArray(Bitmap bitmap) {
        if ((bitmap != null && bitmap.isRecycled())) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] result = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
