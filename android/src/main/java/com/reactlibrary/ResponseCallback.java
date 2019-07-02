package com.reactlibrary;

/**
 * QRReaderr - com.reactlibrary
 * Created by Kerofrog on 6/28/19.
 */
public interface ResponseCallback {

    void onCancel();

    void onImagePicked(String uri);

    void onError(String message);

}
