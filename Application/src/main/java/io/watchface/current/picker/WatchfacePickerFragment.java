package io.watchface.current.picker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import io.watchface.current.R;

public class WatchfacePickerFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private View watchfacePreview;

    public static WatchfacePickerFragment newInstance(View watchfacePreview) {
        WatchfacePickerFragment fragment = new WatchfacePickerFragment();
        fragment.watchfacePreview = watchfacePreview;
        return fragment;
    }

    public WatchfacePickerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_watchface_picker, container, false);

        LinearLayout contentView = (LinearLayout) rootView.findViewById(R.id.content);
        contentView.addView(watchfacePreview);

        return rootView;
    }
}
