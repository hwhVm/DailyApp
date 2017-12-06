package beini.com.dailyapp.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by beini on 2017/2/18.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<T> baseLit;
    private int layoutId;
    private List<Integer> isCheck;
    private boolean openCheck = false;//是否支持多选
    private int checkNum = 9;
    private CheckListener checkListener;

    public BaseAdapter(@NonNull BaseBean<T> baseBean) {
        this.baseLit = baseBean.getBaseList();
        this.layoutId = baseBean.getId();
        this.openCheck=baseBean.isOpenCheck();
        if (openCheck) {
            isCheck = new ArrayList<>();
            for (int i = 0; i < baseLit.size(); i++) {
                isCheck.add(i, View.GONE);
            }
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public int getItemCount() {
        if (baseLit == null || baseLit.size() == 0) {
            return 0;
        }
        return baseLit.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        ViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }

    protected TextView getTextView(@NonNull ViewHolder viewHolder, @IdRes int viewId) {
        return (TextView) viewHolder.view.findViewById(viewId);
    }

    protected ImageView getImageView(@NonNull ViewHolder viewHolder, @IdRes int viewId) {
        return (ImageView) viewHolder.view.findViewById(viewId);
    }

    protected Button getButton(@NonNull ViewHolder viewHolder, @IdRes int viewId) {
        return (Button) viewHolder.view.findViewById(viewId);
    }

    //item  click 事件
    private OnItemClickListener itemClickListener = null;

    public BaseAdapter<T> setItemClick(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            int position = (int) view.getTag();
            if (openCheck) {
                int num = returnNum();
                if (num < checkNum) {
                    if (getIsCheck().size() > 0) {
                        if (getIsCheck().get(position) == View.VISIBLE) {
                            getIsCheck().remove(position);
                            getIsCheck().add(position, View.GONE);
                        } else {
                            getIsCheck().remove(position);
                            getIsCheck().add(position, View.VISIBLE);
                        }
                    }
                    if(checkListener!=null)
                    checkListener.checked();
                } else {
                    if(checkListener!=null)
                    checkListener.moreNum();
                }
            }
            itemClickListener.onItemClick(view, position);
        }
    }


    public int returnNum() {
        int num = 0;
        for (int i = 0, size = getIsCheck().size(); i < size; i++) {
            int temp = getIsCheck().get(i);
            if (temp == View.VISIBLE) {
                num++;
            }
        }
        return num;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemClickCheckListener {
        void onItemClick(View view, int position, CheckListener checkListener);
    }

    // item onlongClick 事件
    private onItemLongClickListener itemLongClickListener = null;

    public void setOnItemLongClickListener(onItemLongClickListener onItemLongClickListener) {
        this.itemLongClickListener = onItemLongClickListener;
    }

    @Override
    public boolean onLongClick(View v) {
        if (itemLongClickListener != null)
            itemLongClickListener.onItemLongClick(v, (int) v.getTag());
        return true;
    }


    public interface onItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public boolean isOpenCheck() {
        return openCheck;
    }

    public BaseAdapter<T> setOpenCheck(boolean openCheck, CheckListener checkListener) {
        this.openCheck = openCheck;
        this.checkListener = checkListener;
        return this;
    }

    public BaseAdapter<T> setOpenCheckListener(CheckListener checkListener) {
        this.checkListener = checkListener;
        return this;
    }

    public List<Integer> getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(List<Integer> isCheck) {
        this.isCheck = isCheck;
    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }
}
