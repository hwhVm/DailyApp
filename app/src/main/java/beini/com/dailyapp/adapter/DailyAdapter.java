package beini.com.dailyapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import beini.com.dailyapp.R;
import beini.com.dailyapp.bean.DailyBean;

/**
 * Created by beini on 2017/11/16.
 */

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyHolder> {
    private List<DailyBean> dailyBeans;
    private Context context;
    private List<Integer> isCheck;
    private OnItemClickListener onItemClickListener;

    public DailyAdapter(Context context, List<DailyBean> dailyBeans) {
        this.context = context;
        this.dailyBeans = dailyBeans;
        isCheck = new ArrayList<>();
        for (int i = 0; i < dailyBeans.size(); i++) {
            isCheck.add(i, View.GONE);
        }
    }

    @Override
    public DailyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout, parent, false);
        DailyHolder dailyHolder = new DailyHolder(view);
        dailyHolder.setIsRecyclable(false);
        return dailyHolder;
    }

    @Override
    public void onBindViewHolder(final DailyHolder holder,final int position) {
        DailyBean dailyBean = dailyBeans.get(position);
        holder.textView.setText(dailyBean.getContent());
        holder.itemView.findViewById(R.id.iv_selected).setVisibility(isCheck.get(position));

        if (onItemClickListener != null) {
            holder.layout_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, position);
                }
            });
        }
    }


    class DailyHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView iv_selected;
        RelativeLayout layout_content;

        DailyHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_content);
            iv_selected = itemView.findViewById(R.id.iv_selected);
            layout_content = itemView.findViewById(R.id.layout_content);
        }
    }

    public interface OnItemClickListener {
        void onClick(View v, int pos);
    }

    public void setItemClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<Integer> getIsCheck() {
        return isCheck;
    }

    @Override
    public int getItemCount() {
        return dailyBeans.size();
    }

}
