package com.stan.library;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by uu on 2017/9/28.
 */

public interface BitmapBackListener {
    void onBitmapResult(Uri uri,Bitmap bitmap);
}
