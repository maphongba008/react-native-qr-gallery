package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;

import java.io.File;

public class ImagePicker {

    private static final int IMAGE_PICKER_REQUEST = 467082;

    private ResponseCallback callback;

    private final ActivityEventListener activityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (requestCode == IMAGE_PICKER_REQUEST) {
                if (callback != null) {
                    if (resultCode == Activity.RESULT_CANCELED) {
                        callback.onCancel();
                    } else if (resultCode == Activity.RESULT_OK) {
                        Uri uri = data.getData();
                        if (uri == null) {
                            callback.onImagePicked(null);
                            return;
                        }
                        String realPath = RealPathUtil.getRealPathFromURI(activity, uri);
                        try {
                            File file = RealPathUtil.createFileFromURI(activity, uri);
                            realPath = file.getAbsolutePath();
                        } catch (Exception e) {
                            callback.onImagePicked(null);
                        }
                        callback.onImagePicked(realPath);
                    }

                }
            }
        }

    };

    public ActivityEventListener getActivityEventListener() {
        return activityEventListener;
    }

    void openGallery(Activity activity, ResponseCallback callback) {
        this.callback = callback;
        try {
            final Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Pick an image");
            activity.startActivityForResult(chooserIntent, IMAGE_PICKER_REQUEST);
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

}