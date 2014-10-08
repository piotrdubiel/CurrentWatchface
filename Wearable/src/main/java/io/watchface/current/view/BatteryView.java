package io.watchface.current.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.BatteryManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class BatteryView extends View {
    private RectF bounds = new RectF();
    private PointF center = new PointF();
    private float batterLevel = 1.0f;
    private ImageView imageView;
    private final Paint batteryOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

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
        canvas.translate(center.x + 20, center.y - 15);
        canvas.drawRect(0, 0, bounds.width() / 2 - 60, 30, batteryOutlinePaint);

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
