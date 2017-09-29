package github.timzheng0804.a500px;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by Tim on 29/09/2017.
 */

public class PagerAdapter extends android.support.v4.view.PagerAdapter implements AdapterNotify {

    Context mContext;

    public PagerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.fullscreen_photo, container, false);

        Photograph photograph = Photograph.getItemFromList(position);
        if (photograph == null) return v;

        Log.i("Url", photograph.getPhotographUrl());
        NetworkImageView imageView = (NetworkImageView) v.findViewById(R.id.fullScreenImageView);
        imageView.setImageUrl(photograph.getPhotographUrl(),
                VolleySingleton.getInstance(mContext).getImageLoader());

        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Photograph.getListSize();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
