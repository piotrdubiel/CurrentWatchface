package io.watchface.current.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.watchface.current.model.Event;

public class EventsView extends View {
    private static final String TAG = "EventsView";
    private static final float MARGIN = 28f;
    public static final float EVENT_WIDTH = 8f;
    private List<EventDrawer> events = new ArrayList<EventDrawer>();
    private RectF bounds = new RectF();

    public EventsView(Context context) {
        this(context, null);
    }

    public EventsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        if (isInEditMode()) {
            addEvent(new Event(3.5f,5.5f, Color.BLUE));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bounds = new RectF(MARGIN, MARGIN, w - MARGIN, h - MARGIN);
        for (EventDrawer event : events) {
            event.generatePath();
        }
        Log.d(TAG, "onSizeChanged");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (EventDrawer event : events) {
            event.draw(canvas);
        }
        Log.d(TAG, "onDraw");
    }

    public void addEvent(Event event) {
        EventDrawer eventDrawer = new EventDrawer(event);
        events.add(eventDrawer);

        ObjectAnimator.ofFloat(eventDrawer, "phase", 1.0f, 0.0f)
                .setDuration(1000)
                .start();

        requestLayout();
        invalidate();
        Log.d(TAG, "addEvent");
    }

    private class EventDrawer {
        private final static float INIT_ANGLE = 270f;
        private Event event;
        private Paint eventPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Path path;

        @SuppressWarnings("unused")
        private float phase;
        private float length;

        public EventDrawer(Event event) {
            this.event = event;
            eventPaint.setStrokeWidth(EVENT_WIDTH);
            eventPaint.setStyle(Paint.Style.STROKE);
            eventPaint.setStrokeCap(Paint.Cap.BUTT);
            eventPaint.setColor(event.getColor());
            generatePath();
        }

        public void generatePath() {
            path = new Path();
            float startAngle = INIT_ANGLE + (event.getBegin() / 12f) * 360f;
            float endAngle = INIT_ANGLE + (event.getEnd() / 12f) * 360f;
            path.arcTo(bounds, startAngle, endAngle - startAngle, false);

            PathMeasure measure = new PathMeasure(path, false);
            length = measure.getLength();
        }

        public void draw(Canvas canvas) {
            canvas.drawPath(path, eventPaint);
        }

        @SuppressWarnings("unused")
        public void setPhase(float phase) {
            this.phase = phase;
            eventPaint.setPathEffect(createPathEffect(length, phase, 0f));
            invalidate();
        }

    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[]{pathLength, pathLength},
                Math.max(phase * pathLength, offset));
    }
}
