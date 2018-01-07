package com.ivanmagda.habito.barchart.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.ivanmagda.habito.R
import com.ivanmagda.habito.barchart.formatters.HabitoBaseIAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ViewConstructor")
class XYMarkerView(context: Context,
                   private val xAxisValueFormatter: HabitoBaseIAxisValueFormatter)
    : MarkerView(context, R.layout.custom_marker_view) {

    private val contentTextView: TextView = findViewById(R.id.tvContent)

    // Callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @SuppressLint("SetTextI18n")
    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        if (entry != null) {
            val date = xAxisValueFormatter.getDateForValue(entry.x)
            contentTextView.text = FORMATTER.format(date) + ". " + entry.y.toInt().toString()
        }

        super.refreshContent(entry, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    companion object {
        private val FORMATTER = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    }
}
