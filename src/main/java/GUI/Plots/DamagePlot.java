package GUI.Plots;


import IO.OutputGenerator;
import calculations.StartCalculations;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;

public class DamagePlot {


    private final StartCalculations starter;
    private OutputGenerator outputGenerator;
    private LineChart<String, Number> lineChart3prime;
    private LineChart<String, Number> lineChart5prime;

    public DamagePlot(OutputGenerator outputGenerator, StartCalculations starter) {

        this.outputGenerator = outputGenerator;
        this.starter = starter;

        CategoryAxis xAxis_5 = new CategoryAxis();
        NumberAxis yAxis_5 = new NumberAxis(0.0, outputGenerator.getMaxYdamapePlot(), 0.05);

        CategoryAxis xAxis_3 = new CategoryAxis();
        NumberAxis yAxis_3 = new NumberAxis(0.0, outputGenerator.getMaxYdamapePlot(), 0.05);

        lineChart5prime = new LineChart<String,Number>(xAxis_5,yAxis_5);
        lineChart5prime.setCreateSymbols(false);

        lineChart3prime = new LineChart<String,Number>(xAxis_3,yAxis_3);
        lineChart3prime.setCreateSymbols(false);

        createFivePrimePlot();
        createThreePrimePlot();

    }


    private void createFivePrimePlot() {

        lineChart5prime.setTitle("5' end");

        // C -> T misincorporations 5'
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("C -> T");

        double[] data_C_T_5 = starter.getDamageProfiler().getFrequencies().getCount_C_T_5_norm();
        for(int i = 0; i < outputGenerator.getThreshold(); i++){
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), data_C_T_5[i]));
        }

        // G -> A misincorporations 5'
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("G -> A");

        double[] data_G_A_5 = starter.getDamageProfiler().getFrequencies().getCount_G_A_5_norm();
        for(int i = 0; i < outputGenerator.getThreshold(); i++){
            series2.getData().add(new XYChart.Data(String.valueOf(i+1), data_G_A_5[i]));
        }

        lineChart5prime.getData().addAll(series1, series2);

    }

    private void createThreePrimePlot() {

        lineChart3prime.setTitle("3' end");

        // C -> T misincorporations 3'

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("C -> T");
        double[] data_C_T_3 = this.outputGenerator.getThree_C_to_T_reverse();

        for(int i = 0; i < outputGenerator.getThreshold(); i++){
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), data_C_T_3[i]));
        }



        // G -> A misincorporations 3'

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("G -> A");
        double[] data_G_A_3 = this.outputGenerator.getThree_G_to_A_reverse();

        for(int i = 0; i < outputGenerator.getThreshold(); i++){
            series2.getData().add(new XYChart.Data(String.valueOf(i+1), data_G_A_3[i]));
        }

        lineChart3prime.getData().addAll(series1, series2);

    }

    public HBox getDamageProfile() {
        HBox plots_combined = new HBox();

        lineChart5prime.prefHeightProperty().bind(plots_combined.heightProperty());
        lineChart5prime.prefWidthProperty().bind(plots_combined.widthProperty());

        lineChart3prime.prefHeightProperty().bind(plots_combined.heightProperty());
        lineChart3prime.prefWidthProperty().bind(plots_combined.widthProperty());

        plots_combined.getChildren().addAll(lineChart5prime, lineChart3prime);
        return plots_combined;
    }
}
