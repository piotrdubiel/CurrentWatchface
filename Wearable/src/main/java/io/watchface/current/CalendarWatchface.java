package io.watchface.current;

import android.app.Activity;
import android.os.Bundle;

import io.watchface.current.common.model.Event;
import io.watchface.current.common.view.EventsView;

public class CalendarWatchface extends Activity {
    private boolean isDimmed = false;
    private EventsView eventsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchface_calendar);
        eventsView = (EventsView) findViewById(R.id.events_view);
        eventsView.addEvent(new Event(3.5f,11.5f, 0xFF9A9CFF));
        eventsView.addEvent(new Event(1.0f, 2.5f, 0xFF16A765));
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
}
