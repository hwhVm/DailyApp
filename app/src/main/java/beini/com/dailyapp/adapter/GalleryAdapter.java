package beini.com.dailyapp.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import beini.com.dailyapp.R;

/**
 * Created by beini on 2017/12/5.
 */

public class GalleryAdapter extends BaseAdapter {

    private List<Bitmap> mSelected;

    public GalleryAdapter(@NonNull BaseBean<Bitmap> baseBean) {
        super(baseBean);
        this.mSelected = baseBean.getBaseList();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        Bitmap uri = mSelected.get(position);
        getImageView((ViewHolder) holder, R.id.image_add).setImageBitmap(mSelected.get(position));
    }
}
