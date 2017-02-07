package com.ivanmagda.habito.barchart.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.ivanmagda.habito.R;

@SuppressLint("ViewConstructor")
public class XYMarkerView extends MarkerView {

    private TextView mContentTextView;
    private IAxisValueFormatter mXAxisValueFormatter;

    public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);

        this.mXAxisValueFormatter = xAxisValueFormatter;
        this.mContentTextView = (TextView) findViewById(R.id.tvContent);
    }

    // Callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String xAxisString = mXAxisValueFormatter.getFormattedValue(e.getX(), null);
        String xAxisStringCap = xAxisString.substring(0, 1).toUpperCase() + xAxisString.substring(1);
        mContentTextView.setText(xAxisStringCap + ", " + String.valueOf((int) e.getY()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
