package github.timzheng0804.a500px;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Tim on 27/09/2017.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> photos;

    public PhotoAdapter(Context context, ArrayList<String> photos) {
        this.mContext = context;
        this.photos = photos;
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
        String url = photos.get(position);
        holder.setPhotoUrl(url);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView photoView;
        VolleySingleton volleySingleton;

        public ViewHolder(View itemView) {
            super(itemView);
            photoView = (NetworkImageView) itemView.findViewById(R.id.photoView);
            volleySingleton = VolleySingleton.getInstance(mContext);
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
