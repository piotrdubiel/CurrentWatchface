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
import android.os.BatteryManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class BatteryView extends View {
    private RectF bounds = new RectF();
    private PointF center = new PointF();
    private float batterLevel = 0.5f;
    private ImageView imageView;
    private final Paint batteryOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint batteryInsidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint batteryTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        imageView = new ImageView(getContext());
        batteryOutlinePaint.setStyle(Paint.Style.STROKE);

        batteryInsidePaint.setStyle(Paint.Style.FILL);
        //batteryInsidePaint.setColor(0xFF42BD41);
        batteryInsidePaint.setColor(0xFFBDBDBD);

        batteryTextPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        batteryTextPaint.setTextSize(20);
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
        canvas.translate(center.x + 12, center.y + 8);
        String batterStatus = String.valueOf((int)(100 * batterLevel)) + "%";
        canvas.drawText(batterStatus, 0, 0, batteryTextPaint);
        canvas.restore();

        canvas.save();
        float batteryLength = bounds.width() / 2 - 120;
        float batteryHeight = 20;
        canvas.translate(center.x + 70, center.y - batteryHeight / 2);
        canvas.drawRect(0, 0, batteryLength * batterLevel, batteryHeight, batteryInsidePaint);
        canvas.drawRect(0, 0, batteryLength, batteryHeight, batteryOutlinePaint);
        canvas.drawRect(batteryLength, 6, batteryLength + 4, 14, batteryOutlinePaint);
        canvas.restore();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            batterLevel = (float) intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 1) / intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
            imageView.setImageResource(intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0));
            update();
        }
    };

    private void update() {

        invalidate();
    }

    private void registerReceivers() {
        Context context = getContext();
        context.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private void unregisterReceivers() {
        Context context = getContext();
        context.unregisterReceiver(broadcastReceiver);
    }
}
