package beini.com.dailyapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by beini on 2017/12/23.
 */

public class SlidingView extends View {
    private int lastX;
    private int lastY;

    public SlidingView(Context context) {
        super(context);
    }

    public SlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                //调用layout方法来重新放置它的位置
                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                break;
        }
        return true;
    }
}
