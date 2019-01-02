package com.xin.lv.yang.utils.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.xin.lv.yang.utils.activity.ImageGridActivity;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * 图片  屏幕  操作类
 */

public class BitmapUtil {
    private static final String TAG = "ScreenUtil";
    private static double RATIO = 0.85D;
    public static int screenWidth;
    public static int screenHeight;
    public static int screenMin;
    public static int screenMax;
    public static float density;
    public static float scaleDensity;
    public static float xdpi;
    public static float ydpi;
    public static int densityDpi;
    public static int dialogWidth;
    public static int statusbarheight;
    public static int navbarheight;

    // 图片期望尺寸
    private static final int IMAGE_REQUEST_SIZE = 500;

    public BitmapUtil() {
    }

    /**
     * dp转px
     *
     * @param dipValue dp数据
     */
    public static int dip2px(Context context, float dipValue) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        density = dm.density;
        return (int) (dipValue * density + 0.5F);
    }

    /**
     * px转dp
     *
     * @param pxValue px像素
     */

    public static int px2dip(Context context, float pxValue) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        density = dm.density;
        return (int) (pxValue / density + 0.5F);
    }


    /**
     * sp 转换成 px显示
     *
     * @param context 上下文
     * @param spValue 文本 sp值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static int getDialogWidth() {
        dialogWidth = (int) ((double) screenMin * RATIO);
        return dialogWidth;
    }


    /**
     * 初始化 Utils类
     */
    public static void init(Context context) {
        if (null != context) {
            DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
            screenMin = screenWidth > screenHeight ? screenHeight : screenWidth;
            density = dm.density;
            scaleDensity = dm.scaledDensity;
            xdpi = dm.xdpi;
            ydpi = dm.ydpi;
            densityDpi = dm.densityDpi;
        }
    }


    public static String saveImg(Bitmap bitmap, String name, Context context) {
        try {
            String path = getImagePath();
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            File mFile = new File(path, name);
            if (mFile.exists()) {

                return "";
            }

            FileOutputStream outputStream = new FileOutputStream(mFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Uri uri = Uri.fromFile(mFile);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

            return mFile.getPath();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void getInfo(Context context) {
        if (null != context) {
            DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
            screenMin = screenWidth > screenHeight ? screenHeight : screenWidth;
            screenMax = screenWidth < screenHeight ? screenHeight : screenWidth;
            density = dm.density;
            scaleDensity = dm.scaledDensity;
            xdpi = dm.xdpi;
            ydpi = dm.ydpi;
            densityDpi = dm.densityDpi;
            statusbarheight = getStatusBarHeight(context);
            navbarheight = getNavBarHeight(context);
            Log.d("ScreenUtil", "screenWidth=" + screenWidth + " screenHeight=" + screenHeight + " density=" + density);
        }
    }

    public static int getStatusBarHeight(Context context) {
        Class c = null;
        Object obj = null;
        Field field = null;
        boolean x = false;
        int sbar = 0;

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            int x1 = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x1);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return sbar;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getNavBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId > 0 ? resources.getDimensionPixelSize(resourceId) : 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public static Drawable getDrawable(Context context, String resource) {
        InputStream is = null;
        try {
            Resources e = context.getResources();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDensity = 240;
            options.inScreenDensity = e.getDisplayMetrics().densityDpi;
            options.inTargetDensity = e.getDisplayMetrics().densityDpi;
            is = context.getAssets().open(resource);
            Bitmap bitmap = BitmapFactory.decodeStream(is, new Rect(), options);
            BitmapDrawable var6 = new BitmapDrawable(context.getResources(), bitmap);
            return var6;
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }

        }

        return null;
    }

    public static View getHorizontalThinLine(Context context) {
        View line = new View(context);
        line.setBackgroundColor(context.getResources().getColor(android.R.color.black));
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(-1, 1);
        line.setLayoutParams(llp);
        return line;
    }

    public static View getVerticalThinLine(Context context) {
        View line = new View(context);
        line.setBackgroundColor(context.getResources().getColor(android.R.color.black));
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(1, -1);
        line.setLayoutParams(llp);
        return line;
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public static Bitmap getResizedBitmap(Context context, Uri uri, int widthLimit, int heightLimit) throws IOException {
        String path = null;
        Bitmap result = null;
        if (uri.getScheme().equals("file")) {
            path = uri.getPath();
        } else {
            if (!uri.getScheme().equals("content")) {
                return null;
            }

            Cursor exifInterface = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            exifInterface.moveToFirst();
            path = exifInterface.getString(0);
            exifInterface.close();
        }

        ExifInterface exifInterface1 = new ExifInterface(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int orientation = exifInterface1.getAttributeInt("Orientation", 0);
        int width;
        if (orientation == 6 || orientation == 8 || orientation == 5 || orientation == 7) {
            width = widthLimit;
            widthLimit = heightLimit;
            heightLimit = width;
        }

        width = options.outWidth;
        int height = options.outHeight;
        int sampleW = 1;

        int sampleH;
        for (sampleH = 1; width / 2 > widthLimit; sampleW <<= 1) {
            width /= 2;
        }

        while (height / 2 > heightLimit) {
            height /= 2;
            sampleH <<= 1;
        }

        boolean sampleSize = true;
        options = new BitmapFactory.Options();
        int sampleSize1;
        if (widthLimit != 2147483647 && heightLimit != 2147483647) {
            sampleSize1 = Math.max(sampleW, sampleH);
        } else {
            sampleSize1 = Math.max(sampleW, sampleH);
        }

        options.inSampleSize = sampleSize1;

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError var22) {
            var22.printStackTrace();
            options.inSampleSize <<= 1;
            bitmap = BitmapFactory.decodeFile(path, options);
        }

        Matrix matrix = new Matrix();
        if (bitmap == null) {
            return bitmap;
        } else {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            if (orientation == 6 || orientation == 8 || orientation == 5 || orientation == 7) {
                int xS = w;
                w = h;
                h = xS;
            }

            switch (orientation) {
                case 2:
                    matrix.preScale(-1.0F, 1.0F);
                    break;
                case 3:
                    matrix.setRotate(180.0F, (float) w / 2.0F, (float) h / 2.0F);
                    break;
                case 4:
                    matrix.preScale(1.0F, -1.0F);
                    break;
                case 5:
                    matrix.setRotate(90.0F, (float) w / 2.0F, (float) h / 2.0F);
                    matrix.preScale(1.0F, -1.0F);
                    break;
                case 6:
                    matrix.setRotate(90.0F, (float) w / 2.0F, (float) h / 2.0F);
                    break;
                case 7:
                    matrix.setRotate(270.0F, (float) w / 2.0F, (float) h / 2.0F);
                    matrix.preScale(1.0F, -1.0F);
                    break;
                case 8:
                    matrix.setRotate(270.0F, (float) w / 2.0F, (float) h / 2.0F);
            }

            float xS1 = (float) widthLimit / (float) bitmap.getWidth();
            float yS = (float) heightLimit / (float) bitmap.getHeight();
            matrix.postScale(Math.min(xS1, yS), Math.min(xS1, yS));

            try {
                result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                return result;
            } catch (OutOfMemoryError var21) {
                var21.printStackTrace();
                Log.e("ResourceCompressHandler", "OOMHeight:" + bitmap.getHeight() + "Width:" + bitmap.getHeight() + "matrix:" + xS1 + " " + yS);
                return null;
            }
        }
    }


    public static Bitmap compressBitmap(Context context, Uri uri, ImageView imageView) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        decodeBitmap(context, uri, options);
        options = new BitmapFactory.Options();
        options.inSampleSize = calculatInSampleSize(options, imageView);
        Bitmap bitmap = null;

        try {
            bitmap = decodeBitmap(context, uri, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    private static int calculatInSampleSize(BitmapFactory.Options options, ImageView imageView) {
        //获取位图的原宽高
        final int w = options.outWidth;
        final int h = options.outHeight;

        if (imageView != null) {
            //获取控件的宽高
            final int reqWidth = imageView.getWidth();
            final int reqHeight = imageView.getHeight();

            //默认为一(就是不压缩)
            int inSampleSize = 1;
            //如果原图的宽高比需要的图片宽高大
            if (w > reqWidth || h > reqHeight) {
                if (w > h) {
                    inSampleSize = Math.round((float) h / (float) reqHeight);
                } else {
                    inSampleSize = Math.round((float) w / (float) reqWidth);
                }
            }

            System.out.println("压缩比为:" + inSampleSize);

            return inSampleSize;

        } else {
            return 1;
        }
    }


    public static Bitmap decodeBitmap(Context context, Uri uri, BitmapFactory.Options options) {
        Bitmap bitmap = null;

        if (uri != null) {
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;
            try {
                /**
                 * 将图片的Uri地址转换成一个输入流
                 */
                inputStream = cr.openInputStream(uri);

                /**
                 * 将输入流转换成Bitmap
                 */
                bitmap = BitmapFactory.decodeStream(inputStream, null, options);

                assert inputStream != null;
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static String md5(Object object) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(toByteArray(object));
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException("Huh, MD5 should be supported?", var7);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        byte[] arr$ = hash;
        int len$ = hash.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            byte b = arr$[i$];
            if ((b & 255) < 16) {
                hex.append("0");
            }

            hex.append(Integer.toHexString(b & 255));
        }

        return hex.toString();
    }


    private static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutputStream ex = new ObjectOutputStream(bos);
            ex.writeObject(obj);
            ex.flush();
            bytes = bos.toByteArray();
            ex.close();
            bos.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return bytes;
    }

    private static Uri mTakePictureUri;

    /**
     * 获取uri数据
     *
     * @return uri
     */
    public static Uri getmTakePictureUri() {
        return mTakePictureUri;
    }


    private static File imageFile;

    public static File getImageFile() {
        return imageFile;
    }


    /**
     * 调用相机拍照
     */
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static Intent requestCamera(Context context) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!path.exists()) {
            path.mkdirs();
        }

        String name = System.currentTimeMillis() + ".png";
        File file = new File(path, name);

        BitmapUtil.imageFile = file;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        List resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        Uri uri;

        if (Build.VERSION.SDK_INT > 23) {
            try {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".FileProvider", file);
            } catch (Exception var10) {
                var10.printStackTrace();
                throw new RuntimeException("uri异常");
            }
        } else {
            uri = Uri.fromFile(file);
        }

        BitmapUtil.mTakePictureUri = uri;

        Iterator i$ = resInfoList.iterator();

        while (i$.hasNext()) {
            ResolveInfo resolveInfo = (ResolveInfo) i$.next();
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        return intent;

    }


    public void paiXu(List<Integer> list) {
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                if (integer.intValue() > t1.intValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

    }


    public static boolean isSdcardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    @SuppressLint("NewApi")
    public static void enableStrictMode() {
        if (hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            if (BitmapUtil.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                vmPolicyBuilder
                        .setClassInstanceLimit(ImageGridActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }


    }

    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;

    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public static List<Camera.Size> getResolutionList(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        return parameters.getSupportedPreviewSizes();
    }

    public static String getVideoPath() {
        if (isSdcardExist()) {
            String path = Environment.getExternalStorageDirectory().getPath();
            return path + "/video";
        }
        return "";
    }


    public static String getImagePath() {
        if (isSdcardExist()) {
            String path = Environment.getExternalStorageDirectory().getPath();
            return path + "/image";
        }
        return "";
    }


    public static Intent openPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return photoPickerIntent;

    }


    public static class ResolutionComparator implements Comparator<Camera.Size> {

        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.height != rhs.height)
                return lhs.height - rhs.height;
            else
                return lhs.width - rhs.width;
        }

    }

    public static String toTime(int var0) {
        var0 /= 1000;
        int var1 = var0 / 60;
        boolean var2 = false;
        if (var1 >= 60) {
            int var4 = var1 / 60;
            var1 %= 60;
        }

        int var3 = var0 % 60;
        return String.format(Locale.CHINA, "%02d:%02d", Integer.valueOf(var1), Integer.valueOf(var3));
    }


    /**
     * 获取视频第一帧 作为封面
     *
     * @param filePath 视频路径
     * @return 封面图片
     */

    public static Bitmap createVideoThumbnail(String filePath) {
        Class<?> clazz = null;
        Object instance = null;
        try {
            try {
                clazz = Class.forName("android.media.MediaMetadataRetriever");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                try {
                    instance = clazz.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }

            Method method = clazz.getMethod("setDataSource", String.class);
            method.invoke(instance, filePath);

            /// The method name changes between API Level 9 and 10.
            if (Build.VERSION.SDK_INT <= 9) {
                return (Bitmap) clazz.getMethod("captureFrame").invoke(instance);
            } else {
                byte[] data = (byte[]) clazz.getMethod("getEmbeddedPicture").invoke(instance);
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (bitmap != null) return bitmap;
                }
                return (Bitmap) clazz.getMethod("getFrameAtTime").invoke(instance);
            }
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            try {
                if (instance != null) {
                    clazz.getMethod("release").invoke(instance);
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }


    /**
     * 获取视频缩略图
     * @param url
     * @param width
     * @param height
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }



    /**
     * 裁剪
     */
    public static Intent startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            intent.putExtra("noFaceDetection", false);   //去除默认的人脸识别，否则和剪裁匡重叠
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        //返回数据 如果false 回调收不到
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG);

        return intent;

    }


    public static String getRealPathFromURI(Activity activity, Uri contentUri) {
        Cursor cursor = null;
        String locationPath = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            locationPath = cursor.getString(column_index);
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return locationPath;
    }

    /**
     * 根据bitmap 对象压缩
     *
     * @param image bitmap对象
     * @return
     */
    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) { //判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset(); //重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos); //这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;   //降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap); //压缩好比例大小后再进行质量压缩
    }


    /**
     * 图片质量压缩
     *
     * @param image
     * @return
     */
    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 图片比例大小压缩
     *
     * @param srcPath 本地图片路径
     * @return
     */
    public Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        // 此时返回bm为空
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;  //这里设置高度为800f
        float ww = 480f;  //这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;
        //be=1表示不缩放
        if (w > h && w > ww) {
            //如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            //如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }


}
