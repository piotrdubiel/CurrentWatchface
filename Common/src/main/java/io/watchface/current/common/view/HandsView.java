package io.watchface.current.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.watchface.current.common.R;

public class HandsView extends View {
    private static final String TAG = "HandsView";
    private final static float INIT_ANGLE = -90f;

    private PointF center = new PointF();
    private float phase;
    private RectF hourHandBounds;
    private Rect hourHandDetailBounds;
    private RectF minuteHandBounds;
    private Rect minuteHandDetailBounds;
    private final Paint hourHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint hourTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint minuteHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint minuteTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint secondHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint secondTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint detailPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Calendar calendar;
    private Runnable ticker;
    private Handler handler;
    private boolean stopped;

    // attributes
    private int hourHandLength;
    private int hourHandWidth;
    private int hourHandDetailWidth;
    private int hourHandColor;

    private int minuteHandLength;
    private int minuteHandWidth;
    private int minuteHandDetailWidth;
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

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HandsView, defStyleAttr, 0);
        try {
            if (a != null) {
                hourHandLength = a.getDimensionPixelSize(R.styleable.HandsView_hour_hand_length, 110);
                hourHandWidth = a.getDimensionPixelSize(R.styleable.HandsView_hour_hand_width, 8);
                hourHandDetailWidth = hourHandWidth / 2;
                hourHandColor = a.getColor(R.styleable.HandsView_hour_hand_color, 0xFF424242);

                minuteHandLength = a.getDimensionPixelSize(R.styleable.HandsView_minute_hand_length, 120);
                minuteHandWidth = a.getDimensionPixelSize(R.styleable.HandsView_minute_hand_width, 6);
                minuteHandDetailWidth = minuteHandWidth  / 2;
                minuteHandColor = a.getColor(R.styleable.HandsView_minute_hand_color, 0xFF546E7A);

                secondHandLength = a.getDimensionPixelSize(R.styleable.HandsView_second_hand_length, 130);
                secondHandWidth = a.getDimensionPixelSize(R.styleable.HandsView_second_hand_width, 2);
                secondHandColor = a.getColor(R.styleable.HandsView_second_hand_color, 0xFFE51C23);

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
        detailPaint.setStyle(Paint.Style.FILL);
        detailPaint.setColor(Color.WHITE);

        hourHandPaint.setColor(hourHandColor);
        hourHandPaint.setStrokeWidth(1);
        hourHandPaint.setStrokeCap(Paint.Cap.BUTT);
        hourHandPaint.setStyle(Paint.Style.FILL);
        hourHandBounds = new RectF(0, -hourHandWidth / 2, hourHandLength, hourHandWidth / 2);
        hourHandDetailBounds = new Rect(hourHandLength - 10, -hourHandDetailWidth / 2, hourHandLength - 2, hourHandDetailWidth / 2);
        hourTextPaint.setColor(hourHandColor);
        hourTextPaint.setTextSize(30);
        hourTextPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));



        minuteHandPaint.setColor(minuteHandColor);
        minuteHandPaint.setStrokeWidth(1);
        minuteHandPaint.setStrokeCap(Paint.Cap.BUTT);
        minuteHandPaint.setStyle(Paint.Style.FILL);
        minuteHandBounds = new RectF(0, -minuteHandWidth / 2, minuteHandLength, minuteHandWidth / 2);
        minuteHandDetailBounds = new Rect(minuteHandLength - 10, -minuteHandDetailWidth / 2, minuteHandLength - 2, minuteHandDetailWidth / 2);
        minuteTextPaint.setColor(minuteHandColor);
        minuteTextPaint.setTextSize(26);
        minuteTextPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));


        secondHandPaint.setColor(secondHandColor);
        secondHandPaint.setStrokeWidth(secondHandWidth);
        secondHandPaint.setStrokeCap(Paint.Cap.BUTT);
        secondHandPaint.setStyle(Paint.Style.STROKE);
        secondTextPaint.setColor(secondHandColor);
        secondTextPaint.setTextSize(24);
        secondTextPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

        dotPaint.setColor(centerColor);
        dotPaint.setStyle(Paint.Style.FILL);

        setBackgroundColor(Color.TRANSPARENT);

        calendar = GregorianCalendar.getInstance();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(center.x, center.y);

        canvas.save();
        float seconds = calendar.get(Calendar.SECOND) / 60f;
        float secondAngle = INIT_ANGLE + seconds * 360f;
        canvas.rotate(secondAngle);
        canvas.drawLine(0, 0, secondHandLength, 0, secondHandPaint);
        //canvas.drawText(String.valueOf(time.second), 80f, -4, secondTextPaint);
        canvas.restore();

        canvas.save();
        float minutes = (calendar.get(Calendar.MINUTE) + seconds) / 60f;
        float minuteAngle = INIT_ANGLE + minutes * 360f;
        canvas.rotate(minuteAngle);
        canvas.drawRect(minuteHandBounds,  minuteHandPaint);
        canvas.drawRect(minuteHandDetailBounds, detailPaint);
        //canvas.drawLine(0, 0, minuteHandLength, 0, minuteHandPaint);
        //canvas.drawText(String.valueOf(time.minute), 80f, -4, minuteTextPaint);
        canvas.restore();

        canvas.save();
        float hours = (calendar.get(Calendar.HOUR) + minutes) / 12f;
        float hourAngle = INIT_ANGLE + hours * 360f;
        canvas.rotate(hourAngle);
        canvas.drawRect(hourHandBounds, hourHandPaint);
        canvas.drawRect(hourHandDetailBounds, detailPaint);
        //canvas.drawLine(0, 0, hourHandLength, 0, hourHandPaint);
        //canvas.drawText(String.valueOf(time.hour), 40f, -8, hourTextPaint);
        canvas.restore();

        canvas.drawCircle(0, 0, centerRadius, dotPaint);
        canvas.drawCircle(0, 0, centerRadius - 3, detailPaint);

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
                calendar.setTimeInMillis(System.currentTimeMillis());
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
