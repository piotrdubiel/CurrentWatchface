package io.watchface.current;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

import io.watchface.current.model.Event;
import io.watchface.current.view.ClockLayout;

public class ClockActivity extends Activity implements View.OnClickListener {
    private ClockLayout clockLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        clockLayout = (ClockLayout) findViewById(R.id.clock_view);
        clockLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clockLayout.addEvent(new Event(3.5f,6.0f, Color.BLUE));
    }
}
