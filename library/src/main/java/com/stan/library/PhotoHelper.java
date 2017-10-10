package com.stan.library;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;

/**
 * Created by uu on 2017/9/28.
 */

public enum PhotoHelper {
    INSTANCE;
    public static final String TAG = "PhotoHelper";

    private static final int TAKE_BIG_PICTURE = 0x11;
    private static final int CROP_BIG_PICTURE = 0x12;
    private static final int CODE_GALLERY_REQUEST = 0x13;
    private Activity activity;
    private int width,height;
    private BitmapBackListener backListener;
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file
    private Uri imageUri;//The Uri to store the big bitmap

    public void prepare(Activity activity, int width, int height, BitmapBackListener backListener){
        this.activity = activity;
        this.width = width;
        this.height = height;
        this.backListener = backListener;
    }

    public void takePhoto() {
        imageUri = Uri.parse(IMAGE_FILE_LOCATION);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, TAKE_BIG_PICTURE);//or TAKE_SMALL_PICTURE
    }

    public void openGallery() {
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    public void handleResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_BIG_PICTURE:
                cropImageUri(imageUri, width, height, CROP_BIG_PICTURE);
                break;
            case CODE_GALLERY_REQUEST:
                if (data!=null) {
                    imageUri = data.getData();
                    cropImageUri(imageUri, width, height, CROP_BIG_PICTURE);
                }
                break;
            case CROP_BIG_PICTURE://from crop_big_picture
                if(imageUri != null){
                    Bitmap bitmap = decodeUriAsBitmap(activity,imageUri);
                    if (backListener!=null)
                        backListener.onBitmapResult(imageUri,bitmap);
                }
                break;
        }
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, requestCode);
    }

    public Bitmap decodeUriAsBitmap(Activity activity,Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
