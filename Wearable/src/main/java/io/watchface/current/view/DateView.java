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
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateView extends TextView {
    private Calendar calendar;
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

        setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

        if (isInEditMode()) {
            update();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerReceivers();
        update();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterReceivers();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            update();
        }
    };

    private void update() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        String dateText = dateFormat.format(calendar.getTime());
        String dayText = dayFormat.format(calendar.getTime());
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(dateText);
        stringBuilder.setSpan(new AbsoluteSizeSpan(25, true), 0, stringBuilder.length(), 0);
        stringBuilder.setSpan(new ForegroundColorSpan(0xFF212121), 0, 2, 0);
        stringBuilder.setSpan(new ForegroundColorSpan(0xFF3E2723), 3, stringBuilder.length(), 0);
        stringBuilder.append("\n");
        stringBuilder.append(dayText);
        stringBuilder.setSpan(new AbsoluteSizeSpan(16, true), dateText.length() + 1, stringBuilder.length(), 0);
        stringBuilder.setSpan(new ForegroundColorSpan(0xFFB0120A), dateText.length() + 1, stringBuilder.length(), 0);
        setText(stringBuilder);
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
