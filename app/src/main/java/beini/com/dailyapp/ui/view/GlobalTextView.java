package beini.com.dailyapp.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * Created by beini on 2017/12/1.
 */

public class GlobalTextView extends android.support.v7.widget.AppCompatTextView {
    public GlobalTextView(Context context) {
        super(context);
    }

    public GlobalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setTextColor(Color.WHITE);
    }

    public GlobalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
