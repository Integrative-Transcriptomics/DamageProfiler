package org.damageprofiler.IO.PDFoutput;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neukamm on 7/29/15.
 */
public class Histogram {


    private final Logger LOG;
    private List<double[]> data_collected;

    public Histogram(Logger LOG){

        this.LOG = LOG;
        data_collected = new ArrayList<>();
    }

    public void addData(List<Integer> data){
        double[] d = new double[data.size()];
        for(int i = 0; i < data.size(); i++){
            d[i] = data.get(i);
        }

        data_collected.add(d);
    }


    public HistogramDataset createDataset(String[] title, int max){

        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);

        int bin = max;

        for(int i = 0; i < data_collected.size(); i++){
            dataset.addSeries(title[i], data_collected.get(i), bin);
        }

        return dataset;

    }


    public JFreeChart createChart(HistogramDataset dataset, String title, String xlab, String ylab,
                                  double xmin, double xmax, boolean legend){

        JFreeChart chart = ChartFactory.createHistogram(
                title,//"Histogram read length",
                xlab, //"Read length",
                ylab, // "Occurrences",
                dataset,
                PlotOrientation.VERTICAL,
                legend,
                false,
                false
        );

//        chart.setBackgroundPaint(new Color(230,230,230));
        XYPlot xyplot = (XYPlot)chart.getPlot();
        xyplot.setForegroundAlpha(0.7F);
        xyplot.setBackgroundPaint(Color.WHITE);
        xyplot.setDomainGridlinePaint(new Color(150,150,150));
        xyplot.setRangeGridlinePaint(new Color(150,150,150));


        if(xmin > -1){
            ValueAxis axis = xyplot.getDomainAxis();
            axis.setLowerBound(xmin);
        }

        if(xmax > -1){
            ValueAxis axis = xyplot.getDomainAxis();
            axis.setUpperBound(xmax);
        }

        XYBarRenderer xybarrenderer = (XYBarRenderer)xyplot.getRenderer();
        xybarrenderer.setShadowVisible(false);
//        xybarrenderer.setBarPainter(new StandardXYBarPainter());
//        xybarrenderer.setMargin(0.2);

        return chart;

    }

}
