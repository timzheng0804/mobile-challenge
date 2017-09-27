package github.timzheng0804.a500px;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Tim on 27/09/2017.
 */

public class PhotoVolley {
    private static PhotoVolley mInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static PhotoVolley getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PhotoVolley(context);
        }
        return mInstance;
    }

    private PhotoVolley(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                return;
            }
        });
    }

    public RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }
}
