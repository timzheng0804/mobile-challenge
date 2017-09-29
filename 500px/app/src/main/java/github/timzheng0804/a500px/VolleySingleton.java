package github.timzheng0804.a500px;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.widget.Adapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tim on 27/09/2017.
 */

class VolleySingleton {
    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private boolean loading;
    private int pageNumber;


    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    private VolleySingleton(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(50);
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

    public void makeJsonRequest(Context context, final AdapterNotify adapter) {
        if (loading) return;
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
        ++pageNumber;
        loading = false;
    }
}
