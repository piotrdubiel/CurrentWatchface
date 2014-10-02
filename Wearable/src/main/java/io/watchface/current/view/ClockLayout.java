package io.watchface.current.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import io.watchface.current.model.Event;

public class ClockLayout extends ViewGroup {
    private final static String TAG = "ClockLayout";
    private final EventsView eventsView;
    private final DialView dialView;

    public ClockLayout(Context context) {
        this(context, null);
    }

    public ClockLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBackgroundColor(Color.WHITE);

        dialView = new DialView(getContext());
        addView(dialView);

        eventsView = new EventsView(getContext());
        addView(eventsView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        update(r - l, b - t);
        Log.d(TAG, "onLayout");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        update(w, h);
        Log.d(TAG, "onSizeChanged");
    }

    private void update(int w, int h) {
        dialView.layout(0, 0, w, h);
        eventsView.layout(0, 0, w, h);
    }

    public void addEvent(Event event) {
        eventsView.addEvent(event);
        Log.d(TAG, "addEvent");
    }
}
