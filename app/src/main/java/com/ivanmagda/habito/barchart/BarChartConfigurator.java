package com.ivanmagda.habito.barchart;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.utils.HabitoDateUtils;

import java.util.ArrayList;
import java.util.Calendar;

public final class BarChartConfigurator {

    private static final int MAX_VISIBLE_VALUE_COUNT = 60;

    private BarChart mBarChart;

    public BarChartConfigurator(BarChart barChart) {
        this.mBarChart = barChart;
    }

    public void setup(@NonNull final Habit habit, BarChartRange.DateRange dateRange) {
        configureBarChart();
        setData(habit, dateRange);
    }

    private void configureBarChart() {
        mBarChart.setDrawBarShadow(true);
        mBarChart.setDrawValueAboveBar(true);

        mBarChart.getDescription().setEnabled(false);

        // If more than 60 entries are displayed in the chart, no values will be drawn.
        mBarChart.setMaxVisibleValueCount(MAX_VISIBLE_VALUE_COUNT);

        // Scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false);

        mBarChart.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new WeekDayAxisValueFormatter();

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(WeekDayAxisValueFormatter.LABEL_COUNT);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = mBarChart.getAxisLeft();
        //leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = mBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(mBarChart.getContext(), xAxisFormatter);
        mv.setChartView(mBarChart);
        mBarChart.setMarker(mv);
    }

    public void setData(@NonNull final Habit habit, BarChartRange.DateRange dateRange) {
        ArrayList<BarEntry> yValues = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        int max = 0;
        switch (dateRange) {
            case WEEK:
                long thisWeek = HabitoDateUtils.getStartOfThisWeek();
                for (int i = 0; i < 7; i++) {
                    calendar.setTimeInMillis(thisWeek);
                    calendar.add(Calendar.DATE, i);
                    long targetDate = calendar.getTimeInMillis();

                    int count = 0;
                    for (long checkmarkDate : habit.getRecord().getCheckmarks()) {
                        if (HabitoDateUtils.sameDay(targetDate, checkmarkDate)) count++;
                    }
                    yValues.add(new BarEntry(i, count));

                    if (count > max) max = count;
                }
                break;
            case MONTH:
                break;
            case YEAR:
                break;
            default:
                throw new IllegalArgumentException("Receive illegal date range");
        }

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount((int) Math.floor(max / 2) + 1, false);

        BarDataSet barDataSet;
        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            barDataSet = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            barDataSet.setValues(yValues);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
            mBarChart.invalidate();
        } else {
            barDataSet = new BarDataSet(yValues, "This week");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.7f);

            mBarChart.setData(data);
        }
    }

}
