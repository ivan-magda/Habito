package com.ivanmagda.habito.barchart;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ivanmagda.habito.barchart.formatters.HabitoBarChartValueFormatter;
import com.ivanmagda.habito.barchart.formatters.HabitoBaseIAxisValueFormatter;
import com.ivanmagda.habito.barchart.formatters.MonthAxisValueFormatter;
import com.ivanmagda.habito.barchart.formatters.WeekDayAxisValueFormatter;
import com.ivanmagda.habito.barchart.formatters.YearAxisValueFormatter;
import com.ivanmagda.habito.barchart.view.XYMarkerView;
import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.view.model.HabitoBarChartViewModel;

import java.util.ArrayList;
import java.util.List;

public final class HabitoBarChartConfigurator {

    private static final int MAX_VISIBLE_VALUE_COUNT = 60;

    private BarChart mBarChart;
    private HabitoBaseIAxisValueFormatter mXAxisFormatter;

    private Habit mHabit;
    private HabitoBarChartRange.DateRange mDateRange;

    private HabitoBarChartDataSource mDataSource;
    private HabitoBarChartViewModel mViewModel;

    public HabitoBarChartConfigurator(BarChart barChart) {
        this.mBarChart = barChart;
    }

    public void setup(@NonNull final Habit habit, HabitoBarChartRange.DateRange dateRange) {
        this.mHabit = habit;
        this.mDateRange = dateRange;
        this.mViewModel = new HabitoBarChartViewModel(habit, dateRange);
        this.mDataSource = new HabitoBarChartDataSource(habit, dateRange, new HabitoBarChartDataSource.Delegate() {
            @Override
            public int numberOfEntries() {
                return mViewModel.getXAxisLabelCount();
            }
        });
        configureBarChart();
        setData();
    }

    private void configureBarChart() {
        mBarChart.setDrawBarShadow(true);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setDrawGridBackground(false);

        // If more than 60 entries are displayed in the chart, no values will be drawn.
        mBarChart.setMaxVisibleValueCount(MAX_VISIBLE_VALUE_COUNT);
        // Scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false);

        configureXAxis();
        configureLeftAxis();
        configureRightAxis();
        configureLegend();
        configureMarkerView();
    }

    private void configureXAxis() {
        switch (mDateRange) {
            case WEEK:
                mXAxisFormatter = new WeekDayAxisValueFormatter();
                break;
            case MONTH:
                mXAxisFormatter = new MonthAxisValueFormatter();
                break;
            case YEAR:
                mXAxisFormatter = new YearAxisValueFormatter();
                break;
        }

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(mViewModel.getXAxisLabelCount());
        xAxis.setValueFormatter(mXAxisFormatter);
    }

    private void configureLeftAxis() {
        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
    }

    private void configureRightAxis() {
        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void configureLegend() {
        Legend legend = mBarChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(9f);
        legend.setTextSize(11f);
        legend.setXEntrySpace(4f);
    }

    private void configureMarkerView() {
        XYMarkerView markerView = new XYMarkerView(mBarChart.getContext(), mXAxisFormatter);
        markerView.setChartView(mBarChart);
        mBarChart.setMarker(markerView);
    }

    private void setData() {
        List<BarEntry> yValues = mDataSource.buildData();
        final int labelCount = ((int) Math.floor(mDataSource.getMaxValue() / 2)) + 1;

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(labelCount, false);

        final String setName = mViewModel.getBarDataSetName(mBarChart.getContext());
        BarDataSet barDataSet;

        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            barDataSet = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            barDataSet.setValues(yValues);
            barDataSet.setLabel(setName);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
            mBarChart.invalidate();
        } else {
            barDataSet = new BarDataSet(yValues, setName);
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueFormatter(new HabitoBarChartValueFormatter());

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.7f);

            mBarChart.setData(data);
        }
    }

}
