package com.nfa.konflagration.chart

import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYDataset
import org.jfree.data.xy.XYSeriesCollection

/**
 * Created by nathan on 11/18/15.
 */
public class ChartBuilder {

    // Properties
    public var title: String = "Title"
    public var xLabel: String = "X Axis"
    public var yLabel: String = "Y Axis"
    public var data: XYDataset = XYSeriesCollection()
    public var orientation: PlotOrientation = PlotOrientation.VERTICAL
    public var legend: Boolean = true
    public var tooltips: Boolean = true
    public var urls: Boolean = false


    public fun toChart(): JFreeChart = ChartFactory.createXYLineChart(title, xLabel, yLabel, data, orientation, legend, tooltips, urls)


    // Static Methods
    companion object {
        fun chart(init: ChartBuilder.() -> Unit): JFreeChart {
            val chart = ChartBuilder()
            chart.init()
            return chart.toChart()
        }
    }

}