package com.ivanmagda.habito.barchart.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.ivanmagda.habito.R;
import com.ivanmagda.habito.barchart.formatters.HabitoBaseIAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Locale;

@SuppressLint("ViewConstructor")
public class XYMarkerView extends MarkerView {

    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());

    private TextView mContentTextView;
    private HabitoBaseIAxisValueFormatter mXAxisValueFormatter;

    public XYMarkerView(Context context, HabitoBaseIAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);
        this.mXAxisValueFormatter = xAxisValueFormatter;
        this.mContentTextView = (TextView) findViewById(R.id.tvContent);
    }

    // Callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        long date = mXAxisValueFormatter.getDateForValue(e.getX());
        mContentTextView.setText(FORMATTER.format(date) + ". " + String.valueOf((int) e.getY()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
