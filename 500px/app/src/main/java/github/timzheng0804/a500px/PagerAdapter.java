package github.timzheng0804.a500px;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import github.timzheng0804.a500px.Modle.AdapterNotify;
import github.timzheng0804.a500px.Modle.Photograph;
import github.timzheng0804.a500px.Modle.VolleySingleton;


/**
 * Created by Tim on 29/09/2017.
 */

public class PagerAdapter extends android.support.v4.view.PagerAdapter implements AdapterNotify {

    private Context mContext;

    public PagerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.fullscreen_photo, container, false);

        Photograph photograph = Photograph.getItemFromList(position);
        if (photograph == null) return v;

        // Set Photograph
        NetworkImageView imageView = (NetworkImageView) v.findViewById(R.id.fullScreenImageView);
        imageView.setImageUrl(photograph.getPhotographUrl(),
                VolleySingleton.getInstance(mContext).getImageLoader());

        // Set Author Info
        CircleImageView authorProfilePic = (CircleImageView) v.findViewById(R.id.profilePic);
        TextView authorName = (TextView) v.findViewById(R.id.authorName);
        TextView numLikes = (TextView) v.findViewById(R.id.numLikes);
        String numPeopleLiked = photograph.getNumLikes() + " " + mContext.getString(R.string.people_liked);

        Picasso.with(mContext).load(photograph.getAuthorProfilePhoto()).resize(200,200)
                .centerCrop().into(authorProfilePic);
        authorName.setText(photograph.getAuthor());
        numLikes.setText(numPeopleLiked);

        // Add view to Viewpager
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
