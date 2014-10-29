package io.watchface.current.common.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateView extends TextView {
    private Calendar calendar;
    private DateFormat dayFormat;
    private DateFormat dateFormat;

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

        dateFormat = new SimpleDateFormat("dd MMM");

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
        String dateText = dateFormat.format(calendar.getTime());
        String dayText = dayFormat.format(calendar.getTime());
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(dateText);
        stringBuilder.setSpan(new AbsoluteSizeSpan(20, true), 0, stringBuilder.length(), 0);
        stringBuilder.setSpan(new ForegroundColorSpan(0xFF212121), 0, 2, 0);
        stringBuilder.setSpan(new ForegroundColorSpan(0xFF212121), 3, stringBuilder.length(), 0);
        stringBuilder.append("\n");
        stringBuilder.append(dayText);
        stringBuilder.setSpan(new AbsoluteSizeSpan(12, true), dateText.length() + 1, stringBuilder.length(), 0);
        stringBuilder.setSpan(new ForegroundColorSpan(0xFF212121), dateText.length() + 1, stringBuilder.length(), 0);
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
