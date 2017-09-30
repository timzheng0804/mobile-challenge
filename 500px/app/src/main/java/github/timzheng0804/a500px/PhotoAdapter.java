package github.timzheng0804.a500px;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.fivehundredpx.greedolayout.GreedoLayoutSizeCalculator;

/**
 * Created by Tim on 27/09/2017.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>
        implements GreedoLayoutSizeCalculator.SizeCalculatorDelegate, AdapterNotify {

    private Context mContext;

    public PhotoAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.single_photo, parent, false);
        return new ViewHolder(v);
    }

    /**
     * When View being bind, load photo into networkimageview.
     * @param holder current recyclerview holder
     * @param position position of the view being showed to user
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photograph photograph = Photograph.getItemFromList(position);
        if (photograph == null) return;
        String url = photograph.getPhotographUrl();
        holder.setPhotoUrl(url);
        holder.setPhotoViewOnClick(position);
    }

    @Override
    public int getItemCount() {
        return Photograph.getListSize();
    }

    /**
     * Set Aspect Ratio of Image
     * @param i index of the image
     * @return
     */
    @Override
    public double aspectRatioForIndex(int i) {
        Photograph photograph = Photograph.getItemFromList(i);
        if(photograph == null) return 0;
        return (double) photograph.getWidth() / (double) photograph.getHeight();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView photoView;
        VolleySingleton volleySingleton;

        public ViewHolder(View itemView) {
            super(itemView);
            photoView = (NetworkImageView) itemView.findViewById(R.id.photoView);
            volleySingleton = VolleySingleton.getInstance(mContext);
        }

        private void setPhotoViewOnClick(final int index) {
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FullScreenPhotograph.startActivity(mContext, index);
                }
            });
        }

        /**Load url into NetworkImage View
         *
         * @param url url of the photo
         */
        public void setPhotoUrl(String url) {
            photoView.setImageUrl(url, VolleySingleton.getInstance(mContext).getImageLoader());

        }
    }


}
