package beini.com.dailyapp.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;

import beini.com.dailyapp.R;

/**
 * Created by beini on 2017/12/1.
 */

public class GlobalButton extends android.support.v7.widget.AppCompatButton {

    public GlobalButton(Context context) {
        super(context);
    }

    public GlobalButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(context.getResources().getColor(R.color.color_widget_backgound));
        setPadding(0, 20, 0, 20);
        setGravity(Gravity.CENTER);
        setTextColor(Color.WHITE);
    }

    public GlobalButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
