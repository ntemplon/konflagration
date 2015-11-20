package com.nfa.konflagration

import com.nfa.konflagration.chart.ChartBuilder
import com.nfa.konflagration.data.toSeries
import com.nfa.konflagration.gui.ChartDialog
import com.nfa.konflagration.io.FileLocations
import com.nfa.konflagration.io.readPointsFile
import com.nfa.konflagration.math.toBSpline
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeriesCollection
import java.awt.Color
import java.awt.Dimension
import javax.swing.JOptionPane
import javax.swing.UIManager

/**
 * Created by nathan on 8/28/15.
 */
fun main(args: Array<String>) {
    configureGui()

    //    val spline = listOf(Point(1.0, 1.0), Point(1.0, 2.0), Point(2.0, 2.0), Point(3.0, 4.0), Point(5.0, 2.0)).toBSpline(3)
    val spline = readPointsFile(FileLocations.AirfoilsFolder.resolve("NACA 2412.txt")).toBSpline(3)
    val dataset = XYSeriesCollection()
    dataset.addSeries(spline.toSeries(numPoints = 250, name = "NACA 2412"))
    //    val chart = ChartFactory.createXYLineChart("Test", "x", "y", dataset, PlotOrientation.VERTICAL, true, true, false)
    val chart = ChartBuilder.chart {
        title = "Test"
        xLabel = "x"
        yLabel = "y"
        data = dataset
        orientation = PlotOrientation.VERTICAL
        legend = true
        tooltips = true
        urls = false
    }
    chart.xyPlot.backgroundPaint = Color.WHITE
    chart.xyPlot.domainGridlinePaint = Color.GRAY
    chart.xyPlot.rangeGridlinePaint = Color.GRAY
    val dialog = ChartDialog(chart)
    dialog.size = Dimension(1200, 500)
    dialog.isVisible = true
}

private fun configureGui() {
    // Enable hardware acceleration
    System.setProperty("sun.java2d.opengl", "true")

    // Set look and feel
    try {
        UIManager.setLookAndFeel(Konflagration.LAF_NAME)
    } catch (ex: Exception) {

    }
}

private fun popup(message: String) = JOptionPane.showMessageDialog(null, message)

object Konflagration {
    private val NIMBUS: String = UIManager.getInstalledLookAndFeels().firstOrNull { info -> info.getName().equals("Nimbus") }?.className ?: ""
    private val METAL: String = UIManager.getInstalledLookAndFeels().firstOrNull { info -> info.getName().equals("Metal") }?.className ?: ""
    private val MOTIF: String = UIManager.getInstalledLookAndFeels().firstOrNull { info -> info.getName().equals("Motif") }?.className ?: ""
    private val NATIVE: String = UIManager.getSystemLookAndFeelClassName()
    public val LAF_NAME: String = METAL
}