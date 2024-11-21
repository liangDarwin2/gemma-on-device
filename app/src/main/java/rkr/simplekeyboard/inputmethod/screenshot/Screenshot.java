// package rkr.simplekeyboard.inputmethod.screenshot;
//
// import android.content.Context;
// import android.graphics.Bitmap;
// import android.graphics.PixelFormat;
// import android.hardware.display.DisplayManager;
// import android.hardware.display.VirtualDisplay;
// import android.media.Image;
// import android.media.ImageReader;
// import android.media.projection.MediaProjection;
// import android.os.Environment;
// import android.os.Handler;
// import android.os.Looper;
// import android.util.DisplayMetrics;
// import android.view.Display;
// import android.view.WindowManager;
//
// import java.io.File;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.nio.ByteBuffer;
//
// public class Screenshot {
//
//     private MediaProjection mediaProjection;
//     private ImageReader imageReader;
//     private VirtualDisplay virtualDisplay;
//     private Handler handler;
//     private Context context; // 添加上下文
//
//     public Screenshot(Context context, MediaProjection mediaProjection) {
//         this.context = context; // 保存上下文
//         this.mediaProjection = mediaProjection;
//         handler = new Handler(Looper.getMainLooper());
//     }
//
//     public void takeScreenshot() {
//         DisplayMetrics metrics = new DisplayMetrics();
//         // 使用传入的 context 获取 Display 对象
//         WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//         Display display = windowManager.getDefaultDisplay();
//         display.getRealMetrics(metrics);
//
//         int width = metrics.widthPixels;
//         int height = metrics.heightPixels;
//         int density = metrics.densityDpi;
//
//         imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2);
//         virtualDisplay = mediaProjection.createVirtualDisplay("Screenshot",
//                 width, height, density, DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY |
//                 DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, imageReader.getSurface(), null, handler);
//
//         imageReader.setOnImageAvailableListener(reader -> {
//             Image image = null;
//             try {
//                 image = reader.acquireLatestImage();
//                 if (image != null) {
//                     Image.Plane[] planes = image.getPlanes();
//                     ByteBuffer buffer = planes[0].getBuffer();
//                     int pixelStride = planes[0].getPixelStride();
//                     int rowStride = planes[0].getRowStride();
//                     int rowPadding = rowStride - pixelStride * width;
//
//                     Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
//                     bitmap.copyPixelsFromBuffer(buffer);
//
//                     // 裁剪 bitmap 到实际宽度
//                     Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
//
//                     saveBitmapToStorage(croppedBitmap);
//                 }
//             } catch (Exception e) {
//                 e.printStackTrace();
//             } finally {
//                 if (image != null) {
//                     image.close();
//                 }
//             }
//         }, handler);
//     }
//
//     private void saveBitmapToStorage(Bitmap bitmap) {
//         File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "screenshot.png");
//         try (FileOutputStream out = new FileOutputStream(file)) {
//             bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
//
//     public void stop() {
//         if (virtualDisplay != null) {
//             virtualDisplay.release();
//             virtualDisplay = null;
//         }
//         if (imageReader != null) {
//             imageReader.close();
//             imageReader = null;
//         }
//     }
// }
