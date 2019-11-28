package IO;

import org.apache.log4j.Logger;
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

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by neukamm on 6/26/15.
 */
public class LinePlot {

    private final double height;
    private final Logger LOG;
    private double y_max=0.0;
    private double y_min=0.0;
    private ArrayList<XYSeries> all_data;
    private String title;
    private int threshold;


    public LinePlot(String title, int threshold, double height, Logger LOG) {
        this.LOG = LOG;
        all_data = new ArrayList<>();
        this.title = title;
        this.threshold = threshold;
        this.height = height;

    }


    /**
     * add data as series, each serie has a name,
     * all series are collected
     * @param data array to collect all data
     * @param name of the series
     */
    public void addData(double[] data, String name){

        final XYSeries series = new XYSeries(name);
        for(int i = 0; i < threshold; i++){
            series.add((double)i, data[i]);
        }
        all_data.add(series);


    }


    /**
     * to create a dataset, iterate over all series and and add them.
     * max and min value of y axis is also set
     * @return dataset
     */
    public XYDataset createDataset() {

        final XYSeriesCollection dataset = new XYSeriesCollection();

        for(XYSeries series : all_data){
            dataset.addSeries(series);
            if(series.getMaxY() > y_max){y_max=series.getMaxY();}
            //if(series.getMinY() < y_min){y_min=series.getMinY();}
        }

        y_min = 0.0;
        if(height != 0.0){
            y_max = height;
        }

        return dataset;

    }


    /**
     * Creates a chart.
     *
     * @param dataset  the data for the chart.
     * @return a chart.
     */
    public JFreeChart createChart(final XYDataset dataset, double yMax, int threshold) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
                title ,                   // chart title
                "",                       // x axis label
                "Frequency",              // y axis label
                dataset,                  // data
                PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        for (int i = 0; i < all_data.size(); i++){
            renderer.setSeriesLinesVisible(i, true);
            renderer.setSeriesShapesVisible(i, false);
        }


        LegendItemCollection legendItemsOld = plot.getLegendItems();
        final LegendItemCollection legendItemsNew = new LegendItemCollection();

        legendItemsNew.add(legendItemsOld.get(0));
        legendItemsNew.add(legendItemsOld.get(1));
        legendItemsNew.add(legendItemsOld.get(2));
        legendItemsNew.add(legendItemsOld.get(3));
        legendItemsNew.add(legendItemsOld.get(4));

        legendItemsNew.get(0).setLinePaint(Color.RED);
        legendItemsNew.get(1).setLinePaint(Color.BLUE);
        legendItemsNew.get(2).setLinePaint(new Color(255, 0, 255));
        legendItemsNew.get(3).setLinePaint(Color.GREEN);
        legendItemsNew.get(4).setLinePaint(Color.GRAY);

        renderer.setSeriesStroke(0, new BasicStroke(5.0f));
        renderer.setSeriesStroke(1, new BasicStroke(5.0f));
        renderer.setAutoPopulateSeriesStroke(false);

        plot.setFixedLegendItems(legendItemsNew);

        // set colour of line
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesPaint(2, new Color(255, 0, 255));
        renderer.setSeriesPaint(3, Color.GREEN);
        renderer.setSeriesPaint(4, Color.GRAY);
        for(int i=5; i < all_data.size(); i++){
            renderer.setSeriesPaint(i, Color.GRAY);
        }

        plot.setRenderer(renderer);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setDomainGridlinePaint(Color.gray);

        // set range of axis
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();

        xAxis.setRange(0.00, threshold-1);
        xAxis.setTickUnit(new NumberTickUnit(1.0));
        xAxis.setVerticalTickLabels(true);


        yAxis.setRange(0.0, yMax+0.015);
        if(yMax+0.1 > 0.5){
            yAxis.setTickUnit(new NumberTickUnit(0.05));
        } else {
            yAxis.setTickUnit(new NumberTickUnit(0.05));
        }


        switch (this.title){
            case ("3' end"):

                String[] axis_three_prime =  new String[threshold];
                int position_three_prime=0;
                for(int i=threshold; i > 0; i--){
                    axis_three_prime[position_three_prime] = "-" + String.valueOf(i);
                    position_three_prime++;
                }

                SymbolAxis rangeAxis_three_prime = new SymbolAxis("", axis_three_prime);
                plot.setDomainAxis(rangeAxis_three_prime);
                break;

            case ("5' end"):

                String[] axis_five_prime =  new String[threshold];
                int position_five_prime=0;
                for(int i=1; i < threshold+1; i++){
                    axis_five_prime[position_five_prime] = String.valueOf(i);
                    position_five_prime++;
                }

                SymbolAxis rangeAxis_five_prime = new SymbolAxis("", axis_five_prime);
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
