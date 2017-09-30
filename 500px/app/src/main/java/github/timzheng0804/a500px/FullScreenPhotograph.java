package github.timzheng0804.a500px;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Tim on 29/09/2017.
 */

public class FullScreenPhotograph extends AppCompatActivity {

    ViewPager viewPager;

    int curIndex;

    /**
     * Best practice for starting a activity
     * @param context context of previous activity
     * @param index the index of the item being clicked
     */

    public static Intent startActivity(Context context, int index) {
        Intent intent = new Intent(context, FullScreenPhotograph.class);
        intent.putExtra(context.getString(R.string.index), index);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_activity);

        // Initiate View Pager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(this));

        // Get index of the item being clicked
        curIndex = getIntent().getIntExtra(getString(R.string.index), -1);

        // Null Checking
        if (curIndex != -1) {
            viewPager.setCurrentItem(curIndex);
        }

        setViewPagerOnPageChangeListener();

    }

    void setViewPagerOnPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Photograph photograph = Photograph.getItemFromList(position);
                android.support.v7.app.ActionBar actionBar = getSupportActionBar();
                if (photograph == null || actionBar == null) return;
                actionBar.setTitle(photograph.getPhotographName());
            }

            @Override
            public void onPageSelected(int position) {
                if (position == Photograph.getListSize() - 1) {
                    VolleySingleton.getInstance(getApplicationContext())
                            .makeJsonRequest(getApplicationContext(), (AdapterNotify) viewPager.getAdapter());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
