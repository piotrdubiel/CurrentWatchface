package io.watchface.current.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import io.watchface.current.R;

public class DialView extends View {
    private static final String TAG = "DialView";
    private int hour_mark_length;
    private int hour_mark_width;
    private int minute_mark_length;
    private int minute_mark_width;
    private int inset;
    private PointF center;
    private RectF bounds;
    private float r;

    private Paint hourMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint minuteMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DialView(Context context) {
        this(context, null);
    }

    public DialView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DialView, defStyleAttr, 0);
        try {
            if (a != null) {
                inset = a.getDimensionPixelSize(R.styleable.DialView_inset, 10);
                hour_mark_length = a.getDimensionPixelSize(R.styleable.DialView_hour_mark_length, 18);
                hour_mark_width = a.getDimensionPixelSize(R.styleable.DialView_hour_mark_width, 3);
                minute_mark_length = a.getDimensionPixelSize(R.styleable.DialView_minute_mark_length, 6);
                minute_mark_width = a.getDimensionPixelSize(R.styleable.DialView_minute_mark_width, 2);
            }
        } finally {
            if (a != null) a.recycle();
        }

        init();
    }

    private void init() {
        hourMarkPaint.setStrokeWidth(hour_mark_width);
        hourMarkPaint.setStyle(Paint.Style.STROKE);
        hourMarkPaint.setStrokeCap(Paint.Cap.BUTT);
        hourMarkPaint.setColor(0xFF333333);

        minuteMarkPaint.setStrokeWidth(minute_mark_width);
        minuteMarkPaint.setStyle(Paint.Style.STROKE);
        minuteMarkPaint.setStrokeCap(Paint.Cap.BUTT);
        minuteMarkPaint.setColor(0xFF333333);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bounds = new RectF(inset, inset, w - inset, h - inset);
        center = new PointF(bounds.centerX(), bounds.centerY());
        r = Math.min(bounds.width(), bounds.height()) / 2;
        Log.d(TAG, "onSizeChanged");
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(center.x, center.y);
        for (int angle = 0; angle < 360; angle+=30) {
            float start_x = (float) (r * Math.cos(Math.toRadians(angle)));
            float start_y = (float) (r * Math.sin(Math.toRadians(angle)));
            float end_x = (float) ((r - hour_mark_length) * Math.cos(Math.toRadians(angle)));
            float end_y = (float) ((r - hour_mark_length) * Math.sin(Math.toRadians(angle)));
            canvas.drawLine(start_x, start_y, end_x, end_y, hourMarkPaint);
        }
        for (int angle = 0; angle < 360; angle+=6) {
            if (angle % 30 == 0) continue;
            float start_x = (float) (r * Math.cos(Math.toRadians(angle)));
            float start_y = (float) (r * Math.sin(Math.toRadians(angle)));
            float end_x = (float) ((r - minute_mark_length) * Math.cos(Math.toRadians(angle)));
            float end_y = (float) ((r - minute_mark_length) * Math.sin(Math.toRadians(angle)));
            canvas.drawLine(start_x, start_y, end_x, end_y, minuteMarkPaint);
        }
        canvas.restore();
        Log.d(TAG, "onDraw");
    }
}
