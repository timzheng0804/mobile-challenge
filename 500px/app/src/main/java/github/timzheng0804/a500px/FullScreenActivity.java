package github.timzheng0804.a500px;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import github.timzheng0804.a500px.Modle.AdapterNotify;
import github.timzheng0804.a500px.Modle.Photograph;
import github.timzheng0804.a500px.Modle.VolleySingleton;

/**
 * Created by Tim on 29/09/2017.
 */

public class FullScreenActivity extends AppCompatActivity {

    ViewPager viewPager;

    /**
     * Best practice for starting a activity
     * @param context context of previous activity
     * @param index the index of the item being clicked
     */

    public static void startActivity(Context context, int index) {
        Intent intent = new Intent(context, FullScreenActivity.class);
        intent.putExtra(context.getString(R.string.index), index);
        ((Activity)context).startActivityForResult(intent, MainActivity.SLIDE_REQUEST);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initiate View Pager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(this));

        // Get index of the item being clicked
        int curIndex = getIntent().getIntExtra(getString(R.string.index), -1);

        // Null Checking
        if (curIndex != -1) {
            viewPager.setCurrentItem(curIndex);
        }


        // Set View Page Change Listener
        setViewPagerOnPageChangeListener();
    }

    /**
     * Set View Pager On Page Change Listener for
     * changing action bar text and requesting for more photos
     */
    void setViewPagerOnPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Set the action bar text to the photograph name
                Photograph photograph = Photograph.getItemFromList(position);
                android.support.v7.app.ActionBar actionBar = getSupportActionBar();
                if (photograph == null || actionBar == null) return;
                actionBar.setTitle(photograph.getPhotographName());
            }

            @Override
            public void onPageSelected(int position) {
                 // when scrolled to the end, request for more photographs
                if (position == Photograph.getListSize() - 1) {
                    Log.d("Current Position", position + "");
                    VolleySingleton.getInstance(getApplicationContext())
                            .makeJsonRequest(getApplicationContext(), (AdapterNotify) viewPager.getAdapter());
                    viewPager.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     *  Send Back the position of the photograph to main activty so it can scroll to that position
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(this.getString(R.string.index), viewPager.getCurrentItem());
        setResult(Activity.RESULT_OK , intent);
        finish();
        super.onBackPressed();
    }
}
