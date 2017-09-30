package github.timzheng0804.a500px.Modle;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import github.timzheng0804.a500px.R;

/**
 * Created by Tim on 27/09/2017.
 */

public class VolleySingleton {
    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private boolean loading;
    private int pageNumber;

    /**
     * Singleton design pattern
     * @param context context of Activity
     * @return
     */

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    private VolleySingleton(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(100);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
        pageNumber = 1;
        loading = false;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


    /**
     * Make Json Requst to 500px Api for 1 page of photos
     * @param context the activity context
     * @param adapter the adapter being used to process photos
     */

    public void makeJsonRequest(Context context, final AdapterNotify adapter) {
        if (loading) return;  // if still loading, do not load next page
        loading = true;

        String url = context.getString(R.string.RequestUrl)
                    + context.getString(R.string.page)
                    + pageNumber
                    + context.getString(R.string.ConsumerKey)
                    + context.getString(R.string.Photo_Size);

        addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray arr = response.getJSONArray("photos");
                                    for (int i = 0; i < arr.length(); ++i) {
                                        // Create Single Photo Object
                                        JSONObject obj = (JSONObject) arr.get(i);
                                        String photographUrl = obj.getJSONArray("images")
                                                .getJSONObject(0).getString("url");
                                        String photoName = obj.getString("name");
                                        String numLikes = obj.getString("votes_count");
                                        String author = obj.getJSONObject("user")
                                                .getString("firstname")
                                                + " "
                                                + obj.getJSONObject("user").getString("lastname");
                                        String authorProfilePhoto = obj.getJSONObject("user")
                                                .getString("userpic_url");
                                        int width = obj.getInt("width");
                                        int height = obj.getInt("height");
                                        Photograph photograph = new Photograph(width, height,numLikes,
                                                author, authorProfilePhoto, photographUrl, photoName);
                                        Photograph.addToPhotoList(photograph);
                                    }
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle Error
                    }
                }));
        loading = false;
        ++pageNumber;
    }
}
