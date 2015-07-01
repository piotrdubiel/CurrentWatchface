package io.watchface.current.common.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeView extends TextView {
    private Calendar calendar;
    private DateFormat timeFormat;

    public TimeView(Context context) {
        this(context, null);
    }

    public TimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        calendar = GregorianCalendar.getInstance();
        timeFormat = new SimpleDateFormat("HH mm");

        setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        setTextAlignment(TEXT_ALIGNMENT_CENTER);

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
        String timeText = timeFormat.format(calendar.getTime());
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(timeText);
//        stringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD) , 0, 2, 0);
        stringBuilder.setSpan(new AbsoluteSizeSpan(68, true) , 0, stringBuilder.length(), 0);
        //stringBuilder.setSpan(new AbsoluteSizeSpan(36, true), 3, stringBuilder.length(), 0);
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
