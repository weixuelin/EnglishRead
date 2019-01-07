package com.xin.lv.yang.utils.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.xin.lv.yang.utils.R;
import com.xin.lv.yang.utils.zxing.camera.CameraManager;
import com.xin.lv.yang.utils.zxing.decode.DecodeFormatManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * 图片加载相关操作
 */

public class ImageUtil {

    private static ImageUtil imageUtil;
    private static final int TIME = 100;

    public static synchronized ImageUtil getInstance() {
        if (imageUtil == null) {
            imageUtil = new ImageUtil();
        }
        return imageUtil;

    }


    public interface OnGetBitmap {
        void getBitmap(Bitmap bitmap);
    }

    /**
     * 转换成bitmap对象
     *
     * @param context 上下文
     * @param call    回调
     */
    public void loadByBitmap(Context context, String filePath, int w, int h, final OnGetBitmap call) {

        RequestOptions options = RequestOptions.priorityOf(Priority.HIGH).centerCrop().override(w, h);

        Glide.with(context).asBitmap().load(filePath).apply(options).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                Log.i("result", "www---" + resource.getWidth() + "---hhh---" + resource.getHeight());

                call.getBitmap(resource);

            }
        });

    }


    public void loadCircleCropImage(Context context, ImageView imageView, String url, int resId) {
        RequestOptions options = RequestOptions
                .circleCropTransform()
                .priority(Priority.HIGH)
                .error(resId)
                .placeholder(resId)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(context).load(url).apply(options).into(imageView);

    }

    /**
     * 加载网络图片，使用glide框架,自定义图片样式
     *
     * @param context   上下文对象 context
     * @param imageView ImageView
     * @param url       图片的地址
     */
    public void loadImageByTransformation(Context context, ImageView imageView, String url, int resId, Transformation<Bitmap> transformation) {
        if (context != null) {
            RequestOptions options = RequestOptions
                    .bitmapTransform(transformation)
                    .priority(Priority.HIGH)
                    .error(resId)
                    .placeholder(resId)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(context).load(url).apply(options).into(imageView);
        }
    }

    public void loadImageByTransformation(Activity activity, final ImageView imageView, String url, int resId, Transformation<Bitmap> transformation) {
        if (!activity.isFinishing()) {
            RequestOptions options = RequestOptions
                    .bitmapTransform(transformation)
                    .priority(Priority.HIGH)
                    .error(resId)
                    .placeholder(resId)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(activity).load(url).apply(options).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imageView.setImageDrawable(resource);
                }
            });
        }
    }


    public void loadImageByTransformation(Fragment fragment, final ImageView imageView, String url, int resId, Transformation<Bitmap> transformation) {
        if (fragment != null) {
            RequestOptions options = RequestOptions
                    .bitmapTransform(transformation)
                    .priority(Priority.HIGH)
                    .error(resId)
                    .placeholder(resId)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(fragment).load(url).apply(options).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imageView.setImageDrawable(resource);
                }
            });
        }
    }


    public void loadImageNoTransformationByOverride(Context context, final ImageView imageView, String url, int resId, int w, int h) {
        if (context != null) {
            RequestOptions options = RequestOptions
                    .placeholderOf(resId)
                    .centerCrop()
                    .error(resId)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            RequestBuilder<Drawable> builder = Glide.with(context).load(url).transition(new DrawableTransitionOptions().crossFade(TIME)).apply(options);
            builder.preload(w, h);
            builder.into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imageView.setImageDrawable(resource);
                }
            });

        }
    }

    public void loadImageNoTransformationByOverride(Activity activity, final ImageView imageView, String url, int resId, int w, int h) {
        if (!activity.isFinishing()) {
            RequestOptions options = RequestOptions
                    .placeholderOf(resId)
                    .centerCrop()
                    .error(resId)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            RequestBuilder<Drawable> builder = Glide.with(activity).load(url).transition(new DrawableTransitionOptions().crossFade(TIME)).apply(options);
            builder.preload(w, h);
            builder.into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imageView.setImageDrawable(resource);
                }
            });

        }
    }

    public void loadImageNoTransformationByOverride(Fragment fragment, final ImageView imageView, String uri, int resId, int w, int h) {
        if (fragment != null) {

            RequestOptions options = RequestOptions
                    .placeholderOf(resId)
                    .centerCrop()
                    .error(resId)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            RequestBuilder<Drawable> builder = Glide.with(fragment).load(uri).transition(new DrawableTransitionOptions().crossFade(TIME)).apply(options);
            builder.preload(w, h);
            builder.into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imageView.setImageDrawable(resource);
                }
            });

        }
    }


    /**
     * 加载圆形图片
     */
    public void loadCircleImage(Context context, final ImageView imageView, String url, int resId) {

        Log.i("result", "图片地址----" + url);

        RequestOptions options = RequestOptions
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(resId)
                .error(resId)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context).load(url).apply(options)
                .transition(new DrawableTransitionOptions().crossFade(TIME))
                .into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                imageView.setImageDrawable(resource);
            }
        });
    }


    /**
     * 加载圆角图片
     */
    public void loadRoundCircleImage(Context context, ImageView imageView, String url, int resId, int r) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new RoundedCornersTransformation(context, r, 1, RoundedCornersTransformation.CornerType.ALL))
                .placeholder(resId)
                .error(resId)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(context).load(url).apply(options).into(imageView);

    }


    /**
     * 加载圆角图片-指定任意部分圆角（图片上、下、左、右四个角度任意定义）
     */
    public void loadCustRoundCircleImage(Context context, ImageView imageView, String url, int resId, RoundedCornersTransformation.CornerType type) {
        int r = (int) context.getResources().getDimension(R.dimen.dp_20);

        RequestOptions options = RequestOptions
                .bitmapTransform(new RoundedCornersTransformation(context, r, 1, type))
                .placeholder(resId)
                .error(resId)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context).load(url).apply(options).into(imageView);
    }


    /**
     * 加载模糊图片（自定义透明度）
     */
    public void loadBlurImage(Context context, final ImageView imageView, String url, int resId, int blur) {
        RequestOptions options = new RequestOptions()
                .placeholder(resId)
                .error(resId)
                .priority(Priority.HIGH)
                .bitmapTransform(new BlurTransformation(context, blur))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                imageView.setImageDrawable(resource);
            }
        });
    }

    /**
     * 加载灰度(黑白)图片（自定义透明度）
     */
    public void loadBlackImage(Context context, final ImageView imageView, String url, int resId) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(resId)
                .errorOf(resId)
                .priority(Priority.HIGH)
                .bitmapTransform(new GrayscaleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                imageView.setImageDrawable(resource);
            }
        });
    }


    public void loadImageNoTransformation(Context context, final ImageView imageView, int resId, String url) {
        Log.i("result","url--------"+url);

        if (context != null && !((Activity) context).isFinishing()) {

            RequestOptions options = RequestOptions
                    .placeholderOf(resId)
                    .errorOf(resId)          //加载失败显示图片
                    .priorityOf(Priority.HIGH)   //优先级
                    .diskCacheStrategyOf(DiskCacheStrategy.NONE); //缓存策略

            Glide.with(context).load(url).apply(options).transition(new DrawableTransitionOptions().crossFade(TIME))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setImageDrawable(resource);
                        }
                    });

        }
    }

    public void loadImageNoTransformation(Activity activity, final ImageView imageView, int resId, String url) {

        if (!activity.isFinishing()) {
            RequestOptions options = RequestOptions
                    .placeholderOf(resId)
                    .errorOf(resId)          //加载失败显示图片
                    .priorityOf(Priority.HIGH)   //优先级
                    .diskCacheStrategyOf(DiskCacheStrategy.NONE); //缓存策略

            Log.i("result", "url======" + url);

            Glide.with(activity).load(url).apply(options)
                    .transition(new DrawableTransitionOptions()
                            .crossFade(TIME)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imageView.setImageDrawable(resource);
                }
            });

        }
    }

    public void loadImageNoTransformation(Fragment fragment, final ImageView imageView, int resId, String url) {
        if (fragment != null) {
            RequestOptions options = RequestOptions
                    .placeholderOf(resId)
                    .error(resId)          //加载失败显示图片
                    .centerCrop()
                    .priority(Priority.HIGH)   //优先级
                    .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略

            Glide.with(fragment).load(url).apply(options).transition(new DrawableTransitionOptions().crossFade(TIME)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imageView.setImageDrawable(resource);
                }
            });
        }
    }


    /**
     * 加载图片 实现宽度满屏，高度自适应
     *
     * @param context   上下文对象
     * @param imageView ImageView
     * @param resId     加载失败显示图片
     * @param url       加载图片地址
     */
    public void loadImageNoTransformationWithW(Context context, final ImageView imageView, int resId, String url) {

        Log.i("result", "===url======" + url);

        if (context != null) {

            RequestOptions options = RequestOptions
                    .placeholderOf(resId)
                    .error(resId)          //加载失败显示图片
                    .centerCrop()
                    .priority(Priority.HIGH)   //优先级
                    .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略

            Glide.with(context).load(url)
                    .transition(new DrawableTransitionOptions().crossFade(TIME))
                    .apply(options)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (imageView == null) {
                                return false;
                            }
                            if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            }

                            ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                            float scale = (float) vw / (float) resource.getIntrinsicWidth();
                            int vh = Math.round(resource.getIntrinsicHeight() * scale);
                            params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                            imageView.setLayoutParams(params);

                            return false;

                        }
                    }).into(imageView);
        }
    }


    /**
     * 剪裁bitmap 图片
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        if (w <= 0 || h <= 0) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbmp;
    }


    /**
     * Drawable into Bitmap
     */

    public Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * Get round Bitmap
     *
     * @param bitmap
     * @param roundPx
     * @return
     */
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    /**
     * Get reflection Bitmap
     */

    public Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight()
                + reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }


    public Drawable createAdaptiveDrawableByDrawableId(Activity activity, int drawableId) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        Bitmap bitmap = ((BitmapDrawable) activity.getResources().getDrawable(drawableId)).getBitmap();
        bitmap = zoomBitmap(bitmap, width, height);
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    // Create adaptive drawable by bitmap
    public Drawable createAdaptiveDrawableByBitmap(Activity activity, Bitmap bitmap) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        bitmap = zoomBitmap(bitmap, width, height);
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }


    /**
     * Returns the bitmap position inside an imageView.
     *
     * @param imageView source ImageView
     * @return 0: left, 1: top, 2: width, 3: height
     */
    public int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (imgViewH - actH) / 2;
        int left = (imgViewW - actW) / 2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }


    public boolean createQRImage(String content, int widthPix, int heightPix, Bitmap logoBm, String filePath) {
        try {
            if (content == null || "".equals(content)) {
                return false;
            }

            // 配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
//            hints.put(EncodeHintType.MARGIN, 2); //default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 在二维码中间添加Logo图案
     */
    private Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }


    /**
     * 识别图片 信息
     *
     * @param bitmap   图片信息
     * @param callback 回调
     */
    public void analyticBitmap(Bitmap bitmap, AnalyticCallback callback) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<BarcodeFormat>();

            // 这里设置可扫描的类型
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);//条形码
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);//二维码

        }

        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult = null;
        try {

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            rawResult = multiFormatReader.decode(new BinaryBitmap(new HybridBinarizer(source)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rawResult != null) {
            if (callback != null) {
                callback.onAnalyzeSuccess(bitmap, rawResult.getText());
            }
        } else {
            if (callback != null) {
                callback.onAnalyzeFailed();
            }
        }


    }


    /**
     * 初始化扫描框
     */
    public Rect initCrop(Context context, CameraManager cameraManager, RelativeLayout captureCropLayout, RelativeLayout captureContainter) {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        captureCropLayout.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight(context);

        int cropWidth = captureCropLayout.getWidth();
        int cropHeight = captureCropLayout.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = captureContainter.getWidth();
        int containerHeight = captureContainter.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        return new Rect(x, y, width + x, height + y);

    }


    private int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public interface AnalyticCallback {
        void onAnalyzeSuccess(Bitmap bitmap, String string);

        void onAnalyzeFailed();
    }

}
