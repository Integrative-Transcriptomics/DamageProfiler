package org.damageprofiler.io.pdfoutput;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

public class Histogram {

  private final List<double[]> data_collected;

  public Histogram() {
    data_collected = new ArrayList<>();
  }

  public void addData(final List<Integer> data) {
    final double[] d = new double[data.size()];
    for (int i = 0; i < data.size(); i++) {
      d[i] = data.get(i);
    }
    data_collected.add(d);
  }

  public HistogramDataset createDataset(final String[] title, final double max) {

    final HistogramDataset dataset = new HistogramDataset();
    dataset.setType(HistogramType.FREQUENCY);

    final int bin = (int) max;

    for (int i = 0; i < data_collected.size(); i++) {
      dataset.addSeries(title[i], data_collected.get(i), bin);
    }

    return dataset;
  }

  public JFreeChart createChart(
      final HistogramDataset dataset,
      final String title,
      final String xlab,
      final String ylab,
      final boolean legend) {

    final JFreeChart chart =
        ChartFactory.createHistogram(
            title, // "Histogram edit distance",
            xlab, // "Read length",
            ylab, // "Occurrences",
            dataset,
            PlotOrientation.VERTICAL,
            legend,
            false,
            false);

    final XYPlot xyplot = (XYPlot) chart.getPlot();
    xyplot.setForegroundAlpha(0.7F);
    xyplot.setBackgroundPaint(Color.WHITE);
    xyplot.setDomainGridlinePaint(new Color(150, 150, 150));
    xyplot.setRangeGridlinePaint(new Color(150, 150, 150));

    final XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
    xybarrenderer.setShadowVisible(false);

    return chart;
  }
}
