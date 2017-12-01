package beini.com.dailyapp.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.EditText;

import beini.com.dailyapp.R;

/**
 * Created by beini on 2017/12/1.
 */

public class GlobalEditText extends EditText {

    public GlobalEditText(Context context) {
        super(context);
    }

    public GlobalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setBackground(context.getResources().getDrawable(R.drawable.edit_text_style));
        setPadding(30, 20, 0, 20);
        setTextColor(Color.GRAY);
    }

    public GlobalEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
