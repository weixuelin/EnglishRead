package com.xin.lv.yang.utils.utils;

/**
 * 识别
 */
public class DiscernUtil {

//    /**
//     * 识别脸部
//     * @param bmp  图片
//     * @param context  上下文对象
//     * @return   脸部图片
//     */
//    @Nullable
//    private Bitmap getFaceBitmap(Bitmap bmp, Context context) {
//
//        FaceDetector faceDetector = new FaceDetector.Builder(context).setTrackingEnabled(false).build();
//        if (!faceDetector.isOperational()) {
//            System.out.println("Face detector not working");
//            return null;
//        }
//
//        Bitmap faceBitmap = null;
//
//        Frame frame = new Frame.Builder().setBitmap(bmp).build();
//
//        SparseArray<Face> faces = faceDetector.detect(frame);
//        Log.e("RoungImage", "00100bmp.getWidth" + bmp.getWidth());
//        Log.e("RoungImage", "00100bmp.getHeight" + bmp.getHeight());
//        Log.e("RoungImage", "006faces:" + faces.size());
//        for (int i = 0; i < faces.size(); i++) {
//            Face thisFace = faces.valueAt(i);
//
//            int faceWidth = (int) thisFace.getWidth();
//            Log.e("RoungImage", "007faceWidth:" + faceWidth);
//            int faceHeight = (int) thisFace.getHeight();
//            Log.e("RoungImage", "008faceHeight:" + faceHeight);
//            int x1 = (int) thisFace.getPosition().x;
//            Log.e("RoungImage", "009x1:" + x1);
//            int y1 = (int) thisFace.getPosition().y;
//            Log.e("RoungImage", "0010y1" + y1);
//            faceBitmap = Bitmap.createBitmap(bmp,
//                    x1 > (faceWidth / 2) ? (x1 - faceWidth / 2) : 0,
//                    y1 > (faceHeight / 2) ? (y1 - faceHeight / 2) : 0,
//                    1.5 * faceWidth < bmp.getWidth() ? (int) (1.5 * faceWidth) : bmp.getWidth(),
//                    1.5 * faceHeight < bmp.getWidth() ? (int) (1.5 * faceHeight) : bmp.getHeight());
//
//        }
//        if (faceBitmap != null) return faceBitmap;
//        return bmp;
//    }

}
