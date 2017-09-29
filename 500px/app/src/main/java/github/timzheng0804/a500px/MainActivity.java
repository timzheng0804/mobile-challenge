package github.timzheng0804.a500px;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static com.google.android.flexbox.R.id.wrap;

public class MainActivity extends AppCompatActivity {

    RecyclerView photoRView;
    String url;
    int pageNumber;
    boolean loading;  // True when image being load, false otherwise
    ArrayList<Photograph> photographs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create List of Photos
        photographs = new ArrayList<>();

        // Initialize RecyclerView
        photoRView = (RecyclerView) findViewById(R.id.photoRView);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(wrap);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        photoRView.setLayoutManager(layoutManager);
        PhotoAdapter photoAdapter = new PhotoAdapter(this, photographs);
        photoRView.setAdapter(photoAdapter);

        // Set Cache Size 50 because Can't not resolve photo resizing when being load.
        photoRView.setItemViewCacheSize(50);
        photoRView.setDrawingCacheEnabled(true);
        photoRView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // Set Pagination
        setScrollListenerForPagination();

        loading = true;

        // Start with page number 1 in api
        pageNumber = 1;

        // Make Json Request to obtain photos
        makeJsonRequest();
    }

    private void setScrollListenerForPagination() {
        photoRView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !loading) {
                    loading = true;
                    makeJsonRequest();
                }
            }
        });
    }
    private void makeJsonRequest() {
        url = getString(R.string.RequestUrl)
                + getString(R.string.page)
                + pageNumber
                + getString(R.string.ConsumerKey)
                + getString(R.string.Photo_Size);

        VolleySingleton.getInstance(this).addToRequestQueue(
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
                                        Photograph photograph = new Photograph(numLikes, author,
                                                authorProfilePhoto, photographUrl, photoName);
                                        photographs.add(photograph);
                                    }
                                    photoRView.getAdapter().notifyDataSetChanged();
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
