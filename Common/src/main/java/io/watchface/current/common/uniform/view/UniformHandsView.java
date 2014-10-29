package io.watchface.current.common.uniform.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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

public class UniformHandsView extends View {
    private static final String TAG = "HandsView";
    private final static float INIT_ANGLE = -90f;

    private PointF center = new PointF();
    private float phase;
    private RectF hourHandBounds;
    private final Paint hourHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint hourDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint hourTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint minuteHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint minuteDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint minuteTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint secondHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint secondDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint secondTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Calendar calendar;
    private Runnable ticker;
    private Handler handler;
    private boolean stopped;

    // attributes
    private int hourHandLength;
    private int hourHandWidth;
    private int hourDotRadius;
    private int hourHandColor;

    private int minuteHandLength;
    private int minuteHandWidth;
    private int minuteDotRadius;
    private int minuteHandColor;

    private int secondHandLength;
    private int secondHandWidth;
    private int secondHandColor;
    private int secondDotRadius;

    private Path minuteHand;


    public UniformHandsView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        hourHandLength = 90;
        hourHandWidth = 8;
        hourHandColor = 0xFF666666;
        hourDotRadius = 8;

        minuteHandLength = 135;
        minuteHandWidth = 6;
        minuteDotRadius = 5;
        minuteHandColor = 0xFF8C8C8C;

        secondHandLength = 140;
        secondHandWidth = 2;
        secondDotRadius = 3;
        secondHandColor = Color.WHITE;

        hourHandPaint.setColor(hourHandColor);
        hourHandPaint.setStrokeWidth(1);
        hourHandPaint.setStrokeCap(Paint.Cap.BUTT);
        hourHandPaint.setStyle(Paint.Style.FILL);
        hourHandPaint.setShadowLayer(20f, 20f, 20f, Color.BLACK);

        hourDotPaint.setColor(hourHandColor);
        hourDotPaint.setStyle(Paint.Style.FILL);

        hourHandBounds = new RectF(0, -hourHandWidth / 2, hourHandLength, hourHandWidth / 2);
        hourTextPaint.setColor(hourHandColor);
        hourTextPaint.setTextSize(30);
        hourTextPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));


        createMinuteHand();
        minuteHandPaint.setColor(minuteHandColor);
        minuteHandPaint.setStrokeWidth(1);
        minuteHandPaint.setStrokeCap(Paint.Cap.BUTT);
        minuteHandPaint.setStyle(Paint.Style.FILL);
        minuteHandPaint.setShadowLayer(5f, 1f, 1f, Color.BLACK);

        minuteDotPaint.setColor(minuteHandColor);
        minuteDotPaint.setStyle(Paint.Style.FILL);

        minuteTextPaint.setColor(minuteHandColor);
        minuteTextPaint.setTextSize(26);
        minuteTextPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));


        secondHandPaint.setColor(secondHandColor);
        secondHandPaint.setStrokeWidth(secondHandWidth);
        secondHandPaint.setStrokeCap(Paint.Cap.BUTT);
        secondHandPaint.setStyle(Paint.Style.STROKE);
        secondHandPaint.setShadowLayer(5f, 1f, 1f, Color.BLACK);

        secondDotPaint.setColor(secondHandColor);
        secondDotPaint.setStyle(Paint.Style.FILL);

        secondTextPaint.setColor(secondHandColor);
        secondTextPaint.setTextSize(24);
        secondTextPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

        setBackgroundColor(Color.TRANSPARENT);

        calendar = GregorianCalendar.getInstance();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(center.x, center.y);

        float seconds = calendar.get(Calendar.SECOND) / 60f;
        float secondAngle = INIT_ANGLE + seconds * 360f;
        float minutes = (calendar.get(Calendar.MINUTE) + seconds) / 60f;
        float minuteAngle = INIT_ANGLE + minutes * 360f;
        float hours = (calendar.get(Calendar.HOUR) + minutes) / 12f;
        float hourAngle = INIT_ANGLE + hours * 360f;

        canvas.save();
        canvas.rotate(hourAngle);
        canvas.drawRect(hourHandBounds, hourHandPaint);
        canvas.drawCircle(0, 0, hourDotRadius, hourDotPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(minuteAngle);
        canvas.drawPath(minuteHand, minuteHandPaint);
        canvas.drawCircle(0, 0, minuteDotRadius, minuteDotPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(secondAngle);
        canvas.drawLine(-20, 0, secondHandLength, 0, secondHandPaint);
        canvas.drawCircle(0, 0, secondDotRadius, secondDotPaint);
        canvas.restore();

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

    private void createMinuteHand() {
        minuteHand = new Path();
        int starty = -minuteHandWidth / 2;
        int endy = minuteHandWidth / 2;
        minuteHand.moveTo(0, starty);
        minuteHand.lineTo(minuteHandLength, starty);
        minuteHand.lineTo(minuteHandLength + 5, 0);
        minuteHand.lineTo(minuteHandLength, endy);
        minuteHand.lineTo(0, endy);
        minuteHand.lineTo(0, starty);
        minuteHand.close();
    }
}
