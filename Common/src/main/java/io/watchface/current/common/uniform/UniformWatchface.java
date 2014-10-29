package io.watchface.current.common.uniform;

import android.app.Activity;
import android.os.Bundle;

import io.watchface.current.common.R;

public class UniformWatchface extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchface_uniform);
    }
}
