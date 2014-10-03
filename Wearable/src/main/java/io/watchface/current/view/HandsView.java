package io.watchface.current.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import io.watchface.current.R;

public class HandsView extends View {
    private static final String TAG = "HandsView";
    private final static float INIT_ANGLE = -90f;

    private PointF center = new PointF();
    private float phase;
    private final Paint hourHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint minuteHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint secondHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Time time;
    private Runnable ticker;
    private Handler handler;
    private boolean stopped;

    // attributes
    private int hourHandLength;
    private int hourHandWidth;
    private int hourHandColor;

    private int minuteHandLength;
    private int minuteHandWidth;
    private int minuteHandColor;

    private int secondHandLength;
    private int secondHandWidth;
    private int secondHandColor;

    private int centerRadius;
    private int centerColor;


    public HandsView(Context context) {
        this(context, null);
    }

    public HandsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HandsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DialView, defStyleAttr, 0);
        try {
            if (a != null) {
                hourHandLength = a.getDimensionPixelSize(R.styleable.HandsView_hour_hand_length, 90);
                hourHandWidth = a.getDimensionPixelSize(R.styleable.HandsView_hour_hand_width, 6);
                hourHandColor = a.getColor(R.styleable.HandsView_hour_hand_color, 0xFF666666);

                minuteHandLength = a.getDimensionPixelSize(R.styleable.HandsView_minute_hand_length, 125);
                minuteHandWidth = a.getDimensionPixelSize(R.styleable.HandsView_minute_hand_width, 3);
                minuteHandColor = a.getColor(R.styleable.HandsView_minute_hand_color, 0xFF888888);

                secondHandLength = a.getDimensionPixelSize(R.styleable.HandsView_second_hand_length, 120);
                secondHandWidth = a.getDimensionPixelSize(R.styleable.HandsView_second_hand_width, 2);
                secondHandColor = a.getColor(R.styleable.HandsView_second_hand_color, 0xFF4DA882);

//                centerRadius = a.getDimensionPixelSize(R.styleable.HandsView_center_radius, 6);
//                centerColor = a.getColor(R.styleable.HandsView_center_color, 0xFF333333);
                centerRadius = 6;
                centerColor = 0xFF333333;
            }
        } finally {
            if (a != null) a.recycle();
        }

        init();
    }

    private void init() {
        hourHandPaint.setColor(hourHandColor);
        hourHandPaint.setStrokeWidth(hourHandWidth);
        hourHandPaint.setStrokeCap(Paint.Cap.ROUND);
        hourHandPaint.setStyle(Paint.Style.STROKE);

        minuteHandPaint.setColor(minuteHandColor);
        minuteHandPaint.setStrokeWidth(minuteHandWidth);
        minuteHandPaint.setStrokeCap(Paint.Cap.ROUND);
        minuteHandPaint.setStyle(Paint.Style.STROKE);

        secondHandPaint.setColor(secondHandColor);
        secondHandPaint.setStrokeWidth(secondHandWidth);
        secondHandPaint.setStrokeCap(Paint.Cap.ROUND);
        secondHandPaint.setStyle(Paint.Style.STROKE);

        dotPaint.setColor(centerColor);
        dotPaint.setStyle(Paint.Style.FILL);

        time = new Time();

        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(center.x, center.y);

        canvas.save();
        float seconds = time.second / 60f;
        float secondAngle = INIT_ANGLE + seconds * 360f;
        canvas.rotate(secondAngle);
        canvas.drawLine(0, 0, secondHandLength, 0, secondHandPaint);
        canvas.restore();

        canvas.save();
        float minutes = (time.minute + seconds) / 60f;
        float minuteAngle = INIT_ANGLE + minutes * 360f;
        canvas.rotate(minuteAngle);
        canvas.drawLine(0, 0, minuteHandLength, 0, minuteHandPaint);
        canvas.restore();

        canvas.save();
        float hours = (time.hour + minutes) / 12f;
        float hourAngle = INIT_ANGLE + hours * 360f;
        canvas.rotate(hourAngle);
        canvas.drawLine(0, 0, hourHandLength, 0, hourHandPaint);
        canvas.restore();

        canvas.drawCircle(0, 0, centerRadius, dotPaint);

        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        RectF bounds = new RectF(0, 0, w, h);
        center = new PointF(bounds.centerX(), bounds.centerY());
    }

    @Override
    protected void onAttachedToWindow() {
        stopped = false;
        super.onAttachedToWindow();
        handler = new Handler();

        ticker = new Runnable() {
            public void run() {
                if (stopped) return;
                time.set(System.currentTimeMillis());
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                handler.postAtTime(ticker, next);
            }
        };
        ticker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopped = true;
    }
}
