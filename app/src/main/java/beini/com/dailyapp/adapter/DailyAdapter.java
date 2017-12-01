package beini.com.dailyapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bean.DailyBean;

/**
 * Created by beini on 2017/12/1.
 */

public class DailyAdapter extends BaseAdapter {
    private List<DailyBean> dailyBeans;


    public DailyAdapter(@NonNull BaseBean<DailyBean> baseBean) {
        super(baseBean);
        this.dailyBeans = baseBean.getBaseList();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        DailyBean dailyBean = dailyBeans.get(position);
        getTextView((ViewHolder) holder, R.id.text_content).setText(dailyBean.getTitle());
        if (isOpenCheck()) {
            getImageView((ViewHolder) holder, R.id.iv_selected).setVisibility((Integer) getIsCheck().get(position));
        }
    }
}
