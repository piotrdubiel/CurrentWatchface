package io.watchface.current;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

import io.watchface.current.model.Event;
import io.watchface.current.view.ClockLayout;
import io.watchface.current.view.EventsView;

public class ClockActivity extends Activity implements View.OnClickListener {
    private ClockLayout clockLayout;
    private EventsView eventsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock);
//        clockLayout = (ClockLayout) findViewById(R.id.clock_view);
//        clockLayout.setOnClickListener(this);
        eventsView = (EventsView) findViewById(R.id.events_view);
        eventsView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        eventsView.addEvent(new Event(3.5f,12.5f, 0xFF9A9CFF));
        eventsView.addEvent(new Event(1.0f,2.5f, 0xFF16A765));
    }
}
