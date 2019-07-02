
package com.reactlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;

public class RNQrGalleryModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    ImagePicker picker;

    public RNQrGalleryModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.picker = new ImagePicker();
        reactContext.addActivityEventListener(picker.getActivityEventListener());
    }


    @Override
    public String getName() {
        return "RNQrGallery";
    }


    @ReactMethod
    public void readQRCode(final Promise response) {
        // open gallery to pick image
        picker.openGallery(getCurrentActivity(), new ResponseCallback() {
            @Override
            public void onCancel() {
                response.resolve("CANCEL");
            }

            @Override
            public void onImagePicked(String uri) {
                if (uri == null) {
                    response.resolve(null);
                    return;
                }
                try {
                    String result = QRHelper.readQRCode(reactContext, uri);
                    response.resolve(result);
                } catch (RuntimeException e) {
                    response.reject(e);
                }
            }

            @Override
            public void onError(String message) {
                response.reject(new RuntimeException(message));
            }
        });
    }

    static class QRHelper {

        @Nullable
        static String readQRCode(Context context, String path) throws RuntimeException {
            File f = new File(path);

            if (!f.exists() || f.isDirectory()) {
                throw new RuntimeException("Invalid image");
            }
            BarcodeDetector detector = new BarcodeDetector.Builder(context)
                    .setBarcodeFormats(Barcode.QR_CODE).build();

            Bitmap myBitmap = decodeSampledBitmapFromFile(f.getAbsolutePath(), 300, 300);

            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
            SparseArray<Barcode> barcodes = detector.detect(frame);
            if (barcodes.size() > 0) {
                return barcodes.valueAt(0).rawValue;
            }
            return null;
        }

        static int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        static Bitmap decodeSampledBitmapFromFile(String filePath,
                                                  int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(filePath, options);
        }
    }
}