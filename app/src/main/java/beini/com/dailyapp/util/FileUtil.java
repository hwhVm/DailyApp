package beini.com.dailyapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.SequenceInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import okio.BufferedSink;
import okio.Okio;

/**
 * Created by beini on 2017/10/27.
 */
public class FileUtil {

    public static void writeInputStreamToSD(String fileDir, InputStream inputStream) {
        File file = new File(fileDir);
        try {
            BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
            bufferedSink.writeAll(Okio.source(inputStream));
            bufferedSink.flush();
            bufferedSink.close();
        } catch (IOException e) {
            BLog.e("  writeInputStreamToSD e.getLocalizedMessage()= " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static void writeBytesToSD(String fileDir, byte[] data) {
        try {
            RandomAccessFile fos = new RandomAccessFile(fileDir, "rw");
            FileChannel fileChannel = fos.getChannel();
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, data.length);
            buffer.put(data);
            fileChannel.close();
            fos.close();
        } catch (IOException e) {
            BLog.e(" e.getLocalizedMessage()= " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static void copyFile(String strPath, String destPath) throws IOException {
        FileInputStream fileInputStream;
        FileOutputStream filefOutputStream;
        File file = new File(strPath);
        if (!file.exists()) {
            file.createNewFile();
        }
        fileInputStream = new FileInputStream(strPath);
        filefOutputStream = new FileOutputStream(destPath);
        byte[] bufferByte = new byte[100 * 1024];//缓冲区的大小如何设置 ？
        int len, count = 0;
        try {
            while ((len = fileInputStream.read(bufferByte)) != -1) {
                filefOutputStream = new FileOutputStream(destPath);
                filefOutputStream.write(bufferByte, 0, len);
            }
            filefOutputStream.flush();
            filefOutputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 拼接
     *
     * @param cellsPath
     * @param newFilePath
     */
    public static void merge(List<String> cellsPath, String newFilePath) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(newFilePath);
            ArrayList<FileInputStream> cellsInputString = new ArrayList<>();

            for (int i = 0; i < cellsPath.size(); i++) {
                cellsInputString.add(new FileInputStream(cellsPath.get(i)));
            }
            final Iterator<FileInputStream> iterator = cellsInputString.iterator();
            Enumeration<FileInputStream> enumeration = new Enumeration<FileInputStream>() {
                @Override
                public boolean hasMoreElements() {
                    return iterator.hasNext();
                }

                @Override
                public FileInputStream nextElement() {
                    return iterator.next();
                }
            };

            SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
            int count;

            byte[] buf = new byte[100 * 1024];
            while ((count = sequenceInputStream.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, count);
            }
            sequenceInputStream.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void appendFile(String str, String dest) {
        RandomAccessFile randomAccessStr;
        RandomAccessFile randomAccessDest;
        try {
            randomAccessStr = new RandomAccessFile(str, "rw");
            randomAccessDest = new RandomAccessFile(dest, "rw");
            long strLength = randomAccessStr.length();
            long destLength = randomAccessDest.length();
            randomAccessStr.setLength(strLength + destLength);
            FileChannel fileChannelStr = randomAccessStr.getChannel();
            FileChannel fileChannel1Dest = randomAccessDest.getChannel();
            fileChannelStr.transferFrom(fileChannel1Dest, strLength, fileChannel1Dest.size());
            randomAccessStr.close();
            randomAccessDest.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
