package org.damageprofiler.io.pdfoutput;

import java.awt.*;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LinePlot {

  private final double height;
  private double y_max = 0.0;
  private final ArrayList<XYSeries> all_data;
  private final int threshold;
  private final Color awtColor_DP_C_to_T;
  private final Color awtColor_DP_G_to_A;
  private final Color awtColor_DP_insertions;
  private final Color awtColor_DP_deletions;
  private final Color awtColor_DP_other;

  public LinePlot(
      final int threshold,
      final double height,
      final javafx.scene.paint.Color color_DP_C_to_T,
      final javafx.scene.paint.Color color_DP_G_to_A,
      final javafx.scene.paint.Color color_DP_insertions,
      final javafx.scene.paint.Color color_DP_deletions,
      final javafx.scene.paint.Color color_DP_other) {
    this.all_data = new ArrayList<>();
    this.threshold = threshold;
    this.height = height;

    awtColor_DP_C_to_T =
        new java.awt.Color(
            (float) color_DP_C_to_T.getRed(),
            (float) color_DP_C_to_T.getGreen(),
            (float) color_DP_C_to_T.getBlue(),
            (float) color_DP_C_to_T.getOpacity());
    awtColor_DP_G_to_A =
        new java.awt.Color(
            (float) color_DP_G_to_A.getRed(),
            (float) color_DP_G_to_A.getGreen(),
            (float) color_DP_G_to_A.getBlue(),
            (float) color_DP_G_to_A.getOpacity());
    awtColor_DP_insertions =
        new java.awt.Color(
            (float) color_DP_insertions.getRed(),
            (float) color_DP_insertions.getGreen(),
            (float) color_DP_insertions.getBlue(),
            (float) color_DP_insertions.getOpacity());
    awtColor_DP_deletions =
        new java.awt.Color(
            (float) color_DP_deletions.getRed(),
            (float) color_DP_deletions.getGreen(),
            (float) color_DP_deletions.getBlue(),
            (float) color_DP_deletions.getOpacity());
    awtColor_DP_other =
        new java.awt.Color(
            (float) color_DP_other.getRed(),
            (float) color_DP_other.getGreen(),
            (float) color_DP_other.getBlue(),
            (float) color_DP_other.getOpacity());
  }

  /**
   * add data as series, each series has a name, all series are collected
   *
   * @param data array to collect all data
   * @param name of the series
   */
  public void addData(final double[] data, final String name) {

    final XYSeries series = new XYSeries(name);
    for (int i = 0; i < threshold; i++) {
      series.add(i, data[i]);
    }
    all_data.add(series);
  }

  /**
   * to create a dataset, iterate over all series and add them. max and min value of y-axis is also
   * set
   *
   * @return dataset
   */
  public XYDataset createDataset() {

    final XYSeriesCollection dataset = new XYSeriesCollection();

    for (final XYSeries series : all_data) {
      dataset.addSeries(series);
      if (series.getMaxY() > y_max) {
        y_max = series.getMaxY();
      }
    }

    if (height != 0.0) {
      y_max = height;
    }

    return dataset;
  }

  public JFreeChart createChart(
      final String title,
      final XYDataset dataset,
      final double yMax,
      final int threshold,
      final boolean ssLibProtocolUsed) {

    // create the chart...
    final JFreeChart chart =
        ChartFactory.createXYLineChart(
            title, // chart title
            "", // x axis label
            "Frequency", // y axis label
            dataset, // data
            PlotOrientation.VERTICAL,
            true, // include legend
            true, // tooltips
            false // urls
            );

    chart.setBackgroundPaint(Color.white);

    final XYPlot plot = chart.getXYPlot();
    plot.setBackgroundPaint(Color.white);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);

    final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    for (int i = 0; i < all_data.size(); i++) {
      renderer.setSeriesLinesVisible(i, true);
      renderer.setSeriesShapesVisible(i, false);
    }

    final LegendItemCollection legendItemsOld = plot.getLegendItems();
    final LegendItemCollection legendItemsNew = new LegendItemCollection();

    legendItemsNew.add(legendItemsOld.get(0));
    legendItemsNew.add(legendItemsOld.get(1));
    legendItemsNew.add(legendItemsOld.get(2));
    legendItemsNew.add(legendItemsOld.get(3));
    legendItemsNew.add(legendItemsOld.get(4));

    legendItemsNew.get(0).setLinePaint(this.awtColor_DP_C_to_T);
    legendItemsNew.get(1).setLinePaint(this.awtColor_DP_G_to_A);
    legendItemsNew.get(2).setLinePaint(this.awtColor_DP_insertions);
    legendItemsNew.get(3).setLinePaint(this.awtColor_DP_deletions);
    legendItemsNew.get(4).setLinePaint(this.awtColor_DP_other);

    renderer.setSeriesStroke(0, new BasicStroke(3.0f));
    if (!ssLibProtocolUsed) renderer.setSeriesStroke(1, new BasicStroke(3.0f));
    renderer.setAutoPopulateSeriesStroke(false);

    plot.setFixedLegendItems(legendItemsNew);

    // set colour of line
    renderer.setSeriesPaint(0, this.awtColor_DP_C_to_T);
    renderer.setSeriesPaint(1, this.awtColor_DP_G_to_A);
    renderer.setSeriesPaint(2, this.awtColor_DP_insertions);
    renderer.setSeriesPaint(3, this.awtColor_DP_deletions);
    renderer.setSeriesPaint(4, this.awtColor_DP_other);
    for (int i = 5; i < all_data.size(); i++) {
      renderer.setSeriesPaint(i, this.awtColor_DP_other);
    }

    plot.setRenderer(renderer);
    plot.setDomainGridlinesVisible(true);
    plot.setRangeGridlinesVisible(true);
    plot.setRangeGridlinePaint(Color.gray);
    plot.setDomainGridlinePaint(Color.gray);

    // set range of axis
    final NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
    final NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();

    xAxis.setRange(0.00, threshold - 1);
    xAxis.setTickUnit(new NumberTickUnit(1.0));
    xAxis.setVerticalTickLabels(true);

    yAxis.setRange(0.0, yMax + 0.015);

    yAxis.setTickUnit(new NumberTickUnit(0.05));

    switch (title) {
      case ("3' end"):
        final String[] axis_three_prime = new String[threshold];
        int position_three_prime = 0;
        for (int i = threshold; i > 0; i--) {
          axis_three_prime[position_three_prime] = "-" + i;
          position_three_prime++;
        }

        final SymbolAxis rangeAxis_three_prime = new SymbolAxis("", axis_three_prime);
        plot.setDomainAxis(rangeAxis_three_prime);
        break;

      case ("5' end"):
        final String[] axis_five_prime = new String[threshold];
        int position_five_prime = 0;
        for (int i = 1; i < threshold + 1; i++) {
          axis_five_prime[position_five_prime] = String.valueOf(i);
          position_five_prime++;
        }

        final SymbolAxis rangeAxis_five_prime = new SymbolAxis("", axis_five_prime);
        plot.setDomainAxis(rangeAxis_five_prime);
        break;
    }

    return chart;
  }

  /*
     Getter
  */

  public double getY_max() {
    return y_max;
  }
}
