package com.xin.lv.yang.utils.utils;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


/**
 * 文件操作类
 */

public class FileUtils {
    private static String TAG = "FileUtils";

    public FileUtils() { }


    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
            return res;
        } else {
            return "";
        }
    }


    public static InputStream getFileInputStream(String path) {
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException var3) {
            var3.printStackTrace();
        }

        return fileInputStream;
    }

    public static byte[] getByteFromUri(Uri uri) {
        InputStream input = getFileInputStream(uri.getPath());

        Object bytes;
        try {
            int e = 0;

            while (true) {
                if (e == 0) {
                    e = input.available();
                    if (e != 0) {
                        continue;
                    }
                }

                byte[] bytes1 = new byte[e];
                input.read(bytes1);
                byte[] e1 = bytes1;
                return e1;
            }
        } catch (Exception var14) {
            bytes = null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException var13) {
                }
            }

        }

        return (byte[]) bytes;
    }

    public static void writeByte(Uri uri, byte[] data) {
        File fileFolder = new File(uri.getPath().substring(0, uri.getPath().lastIndexOf("/")));
        fileFolder.mkdirs();
        File file = new File(uri.getPath());

        try {
            BufferedOutputStream e = new BufferedOutputStream(new FileOutputStream(file));
            e.write(data);
            e.close();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public static File convertBitmap2File(Bitmap bm, String dir, String name) {
        File file = new File(dir);
        if (!file.exists()) {
            Log.e(TAG, "convertBitmap2File: dir does not exist! -" + file.getAbsolutePath());
            file.mkdirs();
        }

        file = new File(file.getPath() + File.separator + name);

        try {
            BufferedOutputStream e = new BufferedOutputStream(new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG, 100, e);
            e.flush();
            e.close();
        } catch (IOException var5) {
            var5.printStackTrace();
            Log.e(TAG, "convertBitmap2File: Exception!");
        }

        return file;
    }

    public static File copyFile(File src, String path, String name) {
        File dest = null;
        if (!src.exists()) {
            Log.e(TAG, "copyFile: src file does not exist! -" + src.getAbsolutePath());
            return dest;
        } else {
            dest = new File(path);
            if (!dest.exists()) {
                Log.d(TAG, "copyFile: dir does not exist!");
                dest.mkdirs();
            }

            dest = new File(path + name);

            try {
                FileInputStream e = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];

                int length;
                while ((length = e.read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                }

                fos.flush();
                fos.close();
                e.close();
                return dest;
            } catch (IOException var8) {
                var8.printStackTrace();
                Log.e(TAG, "copyFile: Exception!");
                return dest;
            }
        }
    }

    public static byte[] file2byte(File file) {
        if (!file.exists()) {
            Log.e(TAG, "file2byte: src file does not exist! -" + file.getAbsolutePath());
            return null;
        } else {
            byte[] buffer = null;

            try {
                FileInputStream e1 = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];

                int n;
                while ((n = e1.read(b)) != -1) {
                    bos.write(b, 0, n);
                }

                e1.close();
                bos.close();
                buffer = bos.toByteArray();
            } catch (Exception var6) {
                var6.printStackTrace();
                Log.e(TAG, "file2byte: Exception!");
            }

            return buffer;
        }
    }

    public static File byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;

        try {
            File e = new File(filePath);
            if (!e.exists()) {
                Log.i("result", "byte2File: dir does not exist!");
                e.mkdirs();
            }

            file = new File(e.getPath() + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception var19) {
            var19.printStackTrace();
            Log.e(TAG, "byte2File: Exception!");
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException var18) {
                    var18.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var17) {
                    var17.printStackTrace();
                }
            }

        }

        return file;
    }

    public static String getCachePath(Context context) {
        return getCachePath(context, "");
    }



    public static String getCachePath(Context context, @NonNull String dir) {
        boolean sdCardExist = Environment.getExternalStorageState().equals("mounted");
        File cacheDir = context.getExternalCacheDir();
        if (!sdCardExist || cacheDir == null || !cacheDir.exists() || !cacheDir.mkdirs()) {
            cacheDir = context.getCacheDir();
        }

        File tarDir = new File(cacheDir.getPath() + File.separator + dir);
        if (!tarDir.exists()) {
            boolean result = tarDir.mkdir();
            Log.w(TAG, "getCachePath = " + tarDir.getPath() + ", result = " + result);
            if (!result) {
                tarDir = new File("/sdcard/cache/" + dir);
                if (!tarDir.exists()) {
                    result = tarDir.mkdirs();
                }

                Log.e(TAG, "change path = " + tarDir.getPath() + ", result = " + result);
            }
        }

        return tarDir.getPath();
    }

    public static String getMediaDownloadDir(Context context) {
        boolean sdCardExist = Environment.getExternalStorageState().equals("mounted");
        String path = "/sdcard";
        if (sdCardExist) {
            File e = Environment.getExternalStorageDirectory();
            path = e.getPath();
        }

        try {
            Resources e1 = context.getResources();
            String filePath = e1.getString(e1.getIdentifier("rc_media_message_default_save_path", "string", context.getPackageName()));
            Log.i(TAG, "getMediaDownloadDir: filePath=" + filePath);
            path = path + filePath;
            File file = new File(path);
            if (!file.exists() && !file.mkdirs()) {
                path = "/sdcard";
            }

        } catch (Resources.NotFoundException var6) {
            var6.printStackTrace();
            path = "/sdcard";
        }

        return path;
    }

    /**
     * 解压缩功能.
     * 将zipFile文件解压到folderPath目录下.
     *
     * @throws Exception
     */
    public int upZipFile(File zipFile, String folderPath) throws IOException {
        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {

                String dirstr = folderPath + ze.getName();
                ///  dirstr.trim();
                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");

                File f = new File(dirstr);
                f.mkdir();
                continue;
            }

            File f = getRealFileName(folderPath, ze.getName());
            OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
        return 0;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                try {
                    // substr.trim();
                    substr = new String(substr.getBytes("8859_1"), "GB2312");

                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ret = new File(ret, substr);

            }
            Log.d("upZipFile", "1ret = " + ret);
            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length - 1];
            try {
                //substr.trim();
                substr = new String(substr.getBytes("8859_1"), "GB2312");
                Log.d("upZipFile", "substr = " + substr);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ret = new File(ret, substr);
            Log.d("upZipFile", "2ret = " + ret);
            return ret;
        }
        return ret;
    }


    /**
     * 无需解压直接读取Zip文件和文件内容
     *
     * @param file
     * @throws Exception
     */
    public static void readZipFile(String file) throws Exception {
        ZipFile zf = new ZipFile(file);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (ze.isDirectory()) {

            } else {
                if (ze.getName().equals("sbl1.mbn")) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.contains("OK")) {

                        }
                    }
                    br.close();
                }

            }
        }
        zin.closeEntry();
    }


    /**
     * 保存bitmap 到文件
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = getCachePath(context);
        } else {
            savePath = context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
        try {
            filePic = new File(savePath, generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }

    /**
     * 产生随机文件名
     *
     * @return
     */
    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }


    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }



}
