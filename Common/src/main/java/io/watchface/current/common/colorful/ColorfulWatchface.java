package io.watchface.current.common.colorful;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import io.watchface.current.common.R;

import io.watchface.current.common.tools.ColorTools;
import io.watchface.current.common.view.DateView;
import io.watchface.current.common.view.TimeView;

public class ColorfulWatchface extends Activity {
    private boolean isDimmed = false;
    private RelativeLayout rootView;
    private TimeView timeView;
    private DateView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchface_colorful);
        rootView = (RelativeLayout) findViewById(R.id.clock_layout);
        timeView = (TimeView) findViewById(R.id.time_view);
        dateView = (DateView) findViewById(R.id.date_view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isDimmed = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isDimmed = false;
    }

    void setBackgroundColor(int color) {
        rootView.setBackgroundColor(color);
        timeView.setTextColor(ColorTools.textColorForBackground(color));
        dateView.setTextColor(ColorTools.textColorForBackground(color));
    }

}

