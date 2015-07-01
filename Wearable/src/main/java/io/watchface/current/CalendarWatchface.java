package io.watchface.current;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.wearable.watchface.CanvasWatchFaceService;

import io.watchface.current.common.model.Event;
import io.watchface.current.common.view.EventsView;


public class CalendarWatchface extends CanvasWatchFaceService {

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine {
        EventsView eventsView;

        public Engine() {
            eventsView = new EventsView(CalendarWatchface.this);
            eventsView.addEvent(new Event(3.5f, 11.5f, 0xFF9A9CFF));
            eventsView.addEvent(new Event(1.0f, 2.5f, 0xFF16A765));
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            super.onDraw(canvas, bounds);
            eventsView.draw(canvas);
        }
    }
}
