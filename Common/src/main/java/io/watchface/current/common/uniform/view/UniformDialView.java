package io.watchface.current.common.uniform.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import io.watchface.current.common.R;

public class UniformDialView extends View {
    private static final String TAG = "DialView";
    private final static int INIT_ANGLE = -90;

    private int hourMarkColor;
    private int minuteMarkColor;
    private int hourMarkLength;
    private int smallHourMarkLength;
    private int hourmarkWidth;
    private int minuteMarkLength;
    private int minuteMarkWidth;
    private int inset;
    private int backgroundColor;
    private PointF center = new PointF();
    private RectF bounds = new RectF();
    private float r;

    private final Paint hourMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint minuteMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public UniformDialView(Context context) {
        this(context, null);
    }

    public UniformDialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        inset = 10;
        hourMarkLength = 46;
        smallHourMarkLength = 38;
        hourmarkWidth = 6;
        hourMarkColor = 0xFF666666;
        minuteMarkLength = 8;
        minuteMarkWidth = 2;
        minuteMarkColor = 0xFFACACAC;
        backgroundColor = 0xFF2F2F2F;

        setBackgroundColor(backgroundColor);

        hourMarkPaint.setStrokeWidth(hourmarkWidth);
        hourMarkPaint.setStyle(Paint.Style.STROKE);
        hourMarkPaint.setStrokeCap(Paint.Cap.BUTT);
        hourMarkPaint.setColor(hourMarkColor);
        hourMarkPaint.setShadowLayer(4f, 1f, 1f, Color.BLACK);

        minuteMarkPaint.setStrokeWidth(minuteMarkWidth);
        minuteMarkPaint.setStyle(Paint.Style.STROKE);
        minuteMarkPaint.setStrokeCap(Paint.Cap.BUTT);
        minuteMarkPaint.setColor(minuteMarkColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bounds = new RectF(inset, inset, w - inset, h - inset);
        center = new PointF(bounds.centerX(), bounds.centerY());
        r = Math.min(bounds.width(), bounds.height()) / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(center.x, center.y);
        float bigHourR = r - minuteMarkLength - 4;
        for (int angle = INIT_ANGLE; angle < INIT_ANGLE + 360; angle += 90) {
            float start_x = (float) (bigHourR * Math.cos(Math.toRadians(angle)));
            float start_y = (float) (bigHourR * Math.sin(Math.toRadians(angle)));
            float end_x = (float) ((bigHourR - hourMarkLength) * Math.cos(Math.toRadians(angle)));
            float end_y = (float) ((bigHourR - hourMarkLength) * Math.sin(Math.toRadians(angle)));
            canvas.drawLine(start_x, start_y, end_x, end_y, hourMarkPaint);
        }
        float smallHourR = r - minuteMarkLength - 10;
        for (int angle = INIT_ANGLE; angle < INIT_ANGLE + 360; angle += 30) {
            if (angle % 90 == 0) continue;
            float start_x = (float) (smallHourR * Math.cos(Math.toRadians(angle)));
            float start_y = (float) (smallHourR * Math.sin(Math.toRadians(angle)));
            float end_x = (float) ((smallHourR - smallHourMarkLength) * Math.cos(Math.toRadians(angle)));
            float end_y = (float) ((smallHourR - smallHourMarkLength) * Math.sin(Math.toRadians(angle)));
            canvas.drawLine(start_x, start_y, end_x, end_y, hourMarkPaint);
        }
        for (int angle = INIT_ANGLE; angle < INIT_ANGLE + 360; angle += 6) {
            float start_x = (float) (r * Math.cos(Math.toRadians(angle)));
            float start_y = (float) (r * Math.sin(Math.toRadians(angle)));
            float end_x = (float) ((r - minuteMarkLength) * Math.cos(Math.toRadians(angle)));
            float end_y = (float) ((r - minuteMarkLength) * Math.sin(Math.toRadians(angle)));
            canvas.drawLine(start_x, start_y, end_x, end_y, minuteMarkPaint);
        }

        canvas.restore();
    }
}
