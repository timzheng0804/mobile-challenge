package github.timzheng0804.a500px;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView photoRView;
    PhotoVolley volley;
    String url;
    ArrayList<String> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoRView = (RecyclerView) findViewById(R.id.photoRView);
        url = "";

        volley = PhotoVolley.getInstance(this);

        makeJsonRequest();
    }

    private void makeJsonRequest() {
        volley.getmRequestQueue().add(new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Change Listener
            }
        }, null));
    }
}
