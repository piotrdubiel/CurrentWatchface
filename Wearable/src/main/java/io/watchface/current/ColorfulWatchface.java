package io.watchface.current;

import android.app.Activity;
import android.os.Bundle;

public class ColorfulWatchface extends Activity {
    private boolean isDimmed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchface_colorful);
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

