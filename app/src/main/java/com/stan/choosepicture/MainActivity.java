package com.stan.choosepicture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.stan.library.BitmapBackListener;
import com.stan.library.PhotoHelper;

public class MainActivity extends AppCompatActivity implements BitmapBackListener {
    public static final String TAG = "MainActivity";

    ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        icon = (ImageView) findViewById(R.id.icon);
        /* step 1 init
         *  params (activity,width,height,bitmapListener)
         */
        PhotoHelper.INSTANCE.prepare(this,200,200,this);
    }

    /* step 4 start photo pick event */
    public void setIcon(View view) {
        switch (view.getId()) {
            case R.id.take_photo:
                PhotoHelper.INSTANCE.takePhoto();
                break;

            case R.id.choose_picture:
                PhotoHelper.INSTANCE.openGallery();
                break;
        }
    }

    /* step 2 let PhotoHelper handle back result */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PhotoHelper.INSTANCE.handleResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* step 3 handle bitmap in bitmapListener */
    @Override
    public void onBitmapResult(Uri uri,Bitmap bitmap) {
        icon.setImageBitmap(bitmap);
    }
}
