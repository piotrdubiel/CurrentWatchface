package io.watchface.current.view;

import android.animation.ObjectAnimator;
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
    private final static int INIT_ANGLE = -90;

    private int bottomInset;
    private int hourMarkColor;
    private int minuteMarkColor;
    private int backgroundColor;
    private int hourMarkLength;
    private int hourmarkWidth;
    private int minuteMarkLength;
    private int minuteMarkWidth;
    private int inset;
    private PointF center = new PointF();
    private RectF bounds = new RectF();
    private float r;
    private float phase;

    private final Paint hourMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint minuteMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

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
                inset = a.getDimensionPixelSize(R.styleable.DialView_inset, 8);
                hourMarkLength = a.getDimensionPixelSize(R.styleable.DialView_hour_mark_length, 18);
                hourmarkWidth = a.getDimensionPixelSize(R.styleable.DialView_hour_mark_width, 3);
                minuteMarkLength = a.getDimensionPixelSize(R.styleable.DialView_minute_mark_length, 8);
                minuteMarkWidth = a.getDimensionPixelSize(R.styleable.DialView_minute_mark_width, 2);
                backgroundColor = a.getColor(R.styleable.DialView_background_color, Color.WHITE);
                hourMarkColor = a.getColor(R.styleable.DialView_background_color, 0xFF333333);
                minuteMarkColor = a.getColor(R.styleable.DialView_minute_mark_color, 0xFF333333);
                bottomInset = a.getDimensionPixelSize(R.styleable.DialView_bottom_inset, 25);
            }
        } finally {
            if (a != null) a.recycle();
        }

        init();
    }

    private void init() {
        setBackgroundColor(backgroundColor);

        hourMarkPaint.setStrokeWidth(hourmarkWidth);
        hourMarkPaint.setStyle(Paint.Style.STROKE);
        hourMarkPaint.setStrokeCap(Paint.Cap.BUTT);
        hourMarkPaint.setColor(hourMarkColor);

        minuteMarkPaint.setStrokeWidth(minuteMarkWidth);
        minuteMarkPaint.setStyle(Paint.Style.STROKE);
        minuteMarkPaint.setStrokeCap(Paint.Cap.BUTT);
        minuteMarkPaint.setColor(minuteMarkColor);


        if (isInEditMode()) {
            phase = 360f;
        }
        else {
            ObjectAnimator.ofFloat(this, "phase", 0.0f, 360.0f)
                    .setDuration(2000)
                    .start();
        }
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
        float insetAngle = (float) Math.toDegrees(Math.asin((r + inset) / (bounds.bottom - bottomInset)));
        float insetAngleStart = 90f - insetAngle;
        canvas.translate(center.x, center.y);
        for (int angle = INIT_ANGLE; angle < INIT_ANGLE + 360; angle += 30) {
            if (angle > phase) continue;
            float start_x = (float) (r * Math.cos(Math.toRadians(angle)));
            float start_y = (float) (r * Math.sin(Math.toRadians(angle)));
            float end_x = (float) ((r - hourMarkLength) * Math.cos(Math.toRadians(angle)));
            float end_y = (float) ((r - hourMarkLength) * Math.sin(Math.toRadians(angle)));
            canvas.drawLine(start_x, start_y, end_x, end_y, hourMarkPaint);
        }
        for (int angle = INIT_ANGLE; angle < INIT_ANGLE + 360; angle += 6) {
            if (angle > phase) continue;
            if (angle % 30 == 0) continue;
            float start_x = (float) (r * Math.cos(Math.toRadians(angle)));
            float start_y = (float) (r * Math.sin(Math.toRadians(angle)));
            float end_x = (float) ((r - minuteMarkLength) * Math.cos(Math.toRadians(angle)));
            float end_y = (float) ((r - minuteMarkLength) * Math.sin(Math.toRadians(angle)));
            canvas.drawLine(start_x, start_y, end_x, end_y, minuteMarkPaint);
        }

        canvas.restore();
        Log.d(TAG,"onDraw");
    }


    public void setPhase(float phase) {
        this.phase = phase;
        invalidate();
    }
}
