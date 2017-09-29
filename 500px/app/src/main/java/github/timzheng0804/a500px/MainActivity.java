package github.timzheng0804.a500px;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import com.fivehundredpx.greedolayout.GreedoLayoutManager;
import com.fivehundredpx.greedolayout.GreedoSpacingItemDecoration;

public class MainActivity extends AppCompatActivity {

    RecyclerView photoRView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView
        photoRView = (RecyclerView) findViewById(R.id.photoRView);
        PhotoAdapter photoAdapter = new PhotoAdapter(this);
        GreedoLayoutManager layoutManager = new GreedoLayoutManager(photoAdapter);
        photoRView.setLayoutManager(layoutManager);
        photoRView.setAdapter(photoAdapter);
        layoutManager.setMaxRowHeight(860);

        // Set Image spacing
        int spacing = MyUtils.dpToPx(4, this);
        photoRView.addItemDecoration(new GreedoSpacingItemDecoration(spacing));

        // Set Pagination
        setScrollListenerForPagination();

        // Make Json Request to obtain photos
        VolleySingleton.getInstance(this).makeJsonRequest(this, photoAdapter);
    }

    private void setScrollListenerForPagination() {
        photoRView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    VolleySingleton.getInstance(getApplicationContext())
                            .makeJsonRequest(getApplicationContext(),
                                    (AdapterNotify) photoRView.getAdapter());
                }
            }
        });
    }
}
