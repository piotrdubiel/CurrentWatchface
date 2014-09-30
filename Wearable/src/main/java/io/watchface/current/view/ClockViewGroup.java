package io.watchface.current.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class ClockViewGroup extends ViewGroup {
    private DialView dialView;


    public ClockViewGroup(Context context) {
        this(context, null);
    }

    public ClockViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBackgroundColor(Color.WHITE);

        dialView = new DialView(getContext());
        addView(dialView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
