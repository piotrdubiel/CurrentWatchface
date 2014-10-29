package io.watchface.current.common.uniform.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


public class UniformLayout extends RelativeLayout {
    private UniformDialView dialView;
    private UniformHandsView handsView;

    public UniformLayout(Context context) {
        this(context, null);
    }

    public UniformLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UniformLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        dialView = new UniformDialView(getContext());
        handsView = new UniformHandsView(getContext());

        addView(dialView);
        addView(handsView);
    }

}
