package com.ivanmagda.habito.barchart

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.ivanmagda.habito.barchart.formatters.HabitoBarChartValueFormatter
import com.ivanmagda.habito.barchart.formatters.HabitoBaseIAxisValueFormatter
import com.ivanmagda.habito.barchart.view.XYMarkerView
import com.ivanmagda.habito.view.model.HabitoBarChartViewModel
import java.util.*

class HabitoBarChartConfigurator(private val barChart: BarChart) {

    private lateinit var xAxisFormatter: HabitoBaseIAxisValueFormatter
    private lateinit var dataSource: HabitoBarChartDataSource
    private lateinit var viewModel: HabitoBarChartViewModel

    fun setup(dataSource: HabitoBarChartDataSource, viewModel: HabitoBarChartViewModel) {
        this.dataSource = dataSource
        this.viewModel = viewModel

        configureBarChart()
        setData()
    }

    private fun configureBarChart() {
        barChart.setDrawBarShadow(true)
        barChart.setDrawValueAboveBar(true)
        barChart.description.isEnabled = false
        barChart.setDrawGridBackground(false)

        // If more than 60 entries are displayed in the chart, no values will be drawn.
        barChart.setMaxVisibleValueCount(MAX_VISIBLE_VALUE_COUNT)
        // Scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false)

        configureXAxis()
        configureLeftAxis()
        configureRightAxis()
        configureLegend()
        configureMarkerView()
    }

    private fun configureXAxis() {
        xAxisFormatter = viewModel.xAxisFormatter

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelCount = dataSource.numberOfEntries
        xAxis.valueFormatter = xAxisFormatter
    }

    private fun configureLeftAxis() {
        val leftAxis = barChart.axisLeft
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f
    }

    private fun configureRightAxis() {
        barChart.axisRight.isEnabled = false
    }

    private fun configureLegend() {
        val legend = barChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.form = Legend.LegendForm.SQUARE
        legend.formSize = 9f
        legend.textSize = 11f
        legend.xEntrySpace = 4f
    }

    private fun configureMarkerView() {
        val markerView = XYMarkerView(barChart.context, xAxisFormatter)
        markerView.chartView = barChart
        barChart.marker = markerView
    }

    private fun setData() {
        val yValues = dataSource.data
        val labelCount = Math.floor((dataSource.maxValue / 2).toDouble()).toInt() + 1

        val leftAxis = barChart.axisLeft
        leftAxis.setLabelCount(labelCount, false)

        val setName = viewModel.getBarDataSetName(barChart.context)
        val barDataSet: BarDataSet

        if (barChart.data != null && barChart.data.dataSetCount > 0) {
            barDataSet = barChart.data.getDataSetByIndex(0) as BarDataSet
            barDataSet.values = yValues
            barDataSet.label = setName
            barChart.data.notifyDataChanged()
            barChart.notifyDataSetChanged()
            barChart.invalidate()
        } else {
            barDataSet = BarDataSet(yValues, setName)
            barDataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
            barDataSet.valueFormatter = HabitoBarChartValueFormatter()

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(barDataSet)

            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.barWidth = 0.7f

            barChart.data = data
        }
    }

    companion object {
        private val MAX_VISIBLE_VALUE_COUNT = 60
    }
}
