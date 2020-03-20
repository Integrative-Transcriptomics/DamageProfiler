package GUI.Plots;


import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

public class DamagePlot {

    public DamagePlot() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("No of employees");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Revenue per employee");

        LineChart lineChart = new LineChart(xAxis, yAxis);



    }
}
