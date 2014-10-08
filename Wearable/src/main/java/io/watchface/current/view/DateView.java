package io.watchface.current.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateView extends View {
    private Calendar calendar;
    private RectF bounds = new RectF();
    private PointF center = new PointF();
    private DateFormat dayFormat;
    private DateFormat dateFormat;
    private final Paint dayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint datePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DateView(Context context) {
        this(context, null);
    }

    public DateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        calendar = GregorianCalendar.getInstance();
        dayFormat = new SimpleDateFormat("EEEE");
        dayPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        dayPaint.setTextSize(20);

        dateFormat = new SimpleDateFormat("dd MMM");
        datePaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        datePaint.setTextSize(36);

        if (isInEditMode()) {
            update();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerReceivers();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterReceivers();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bounds = new RectF(0, 0, w, h);
        center = new PointF(bounds.centerX(), bounds.centerY());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        String day = dayFormat.format(calendar.getTime());
        String date = dateFormat.format(calendar.getTime());
        canvas.drawText(date, 40, center.y - 10, datePaint);
        canvas.drawLine(40, center.y, center.x - 20, center.y, datePaint);
        canvas.drawText(day, 40, center.y + 20, dayPaint);
        canvas.restore();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            update();
        }
    };

    private void update() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        invalidate();
    }

    private void registerReceivers() {
        Context context = getContext();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        context.registerReceiver(broadcastReceiver, filter);
    }

    private void unregisterReceivers() {
        Context context = getContext();
        context.unregisterReceiver(broadcastReceiver);
    }
}
