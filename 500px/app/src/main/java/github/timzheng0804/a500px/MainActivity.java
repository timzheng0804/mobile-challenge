package github.timzheng0804.a500px;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import com.fivehundredpx.greedolayout.GreedoLayoutManager;
import com.fivehundredpx.greedolayout.GreedoSpacingItemDecoration;

import github.timzheng0804.a500px.Modle.AdapterNotify;
import github.timzheng0804.a500px.Modle.VolleySingleton;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    static final int SLIDE_REQUEST = 1;  // Slide request when user quit full screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.photoRView);
        PhotoAdapter photoAdapter = new PhotoAdapter(this);
        GreedoLayoutManager layoutManager = new GreedoLayoutManager(photoAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photoAdapter);
        layoutManager.setMaxRowHeight(860);

        // Set Image spacing
        int spacing = MyUtils.dpToPx(4, this);
        recyclerView.addItemDecoration(new GreedoSpacingItemDecoration(spacing));

        // Set Pagination
        setScrollListenerForPagination();

        // Make Json Request to obtain photos
        VolleySingleton.getInstance(this).makeJsonRequest(this, photoAdapter);
    }

    /**
     * Set Scroll Listener for recyclerview, when scrolled to the end, request for more photos
     */
    private void setScrollListenerForPagination() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = ((GreedoLayoutManager)recyclerView.getLayoutManager()).getChildCount();
                int totalItemCount = ((GreedoLayoutManager)recyclerView.getLayoutManager()).getItemCount();
                int visiblePosition = ((GreedoLayoutManager)recyclerView.getLayoutManager())
                        .findFirstVisibleItemPosition();
                if ((visibleItemCount + visiblePosition) >= totalItemCount - 10) {
                    VolleySingleton.getInstance(getApplicationContext())
                            .makeJsonRequest(getApplicationContext(),
                                    (AdapterNotify) recyclerView.getAdapter());
                }
            }
        });
    }

    /**
     * Slide to the last image being saw in fullscreen
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SLIDE_REQUEST && resultCode == RESULT_OK) {
            recyclerView.getAdapter().notifyDataSetChanged();
            int index = data.getIntExtra(this.getString(R.string.index), -1);
            if (index == -1) return;
            recyclerView.getLayoutManager().scrollToPosition(index);
        }
    }
}
