package GUI.Plots;

import calculations.DamageProfiler;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LengthDistPlot {

    private BarChart<String, Number> bc_data_all;
    private BarChart<String, Number> bc_data_split;

    public LengthDistPlot(DamageProfiler damageProfiler){

        generate_alldata(damageProfiler.getLength_all());
        generate_split_data(damageProfiler.getLength_distribution_map_forward(),
                damageProfiler.getLength_distribution_map_reverse());


    }

    private void generate_split_data(HashMap<Integer, Integer> length_distribution_map_forward, HashMap<Integer, Integer> length_distribution_map_reverse) {

        //Double[] data_forward = prepareData((List<Double>) length_distribution_map_forward);
        //Double[] data_reverse = prepareData((List<Double>) length_distribution_map_reverse);

        HashMap<Integer, Integer> length_map_forward = length_distribution_map_forward;
        HashMap<Integer, Integer> length_map_reverse = length_distribution_map_reverse;

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        bc_data_split = new BarChart<>(xAxis,yAxis);

        bc_data_split.setCategoryGap(0);
        bc_data_split.setBarGap(1);
        bc_data_split.setTitle("Read length distribution");

        yAxis.setLabel("Number of reads");
        xAxis.setLabel("Read length");

        XYChart.Series series_forward = new XYChart.Series();

        List<Integer> targetList_forward = new ArrayList<>(length_map_forward.keySet());
        Collections.sort(targetList_forward);

        for(int d : targetList_forward){
            if (length_map_forward.containsKey(d)){
                series_forward.getData().add(new XYChart.Data(String.valueOf(d), length_map_forward.get(d)));
            } else {
                series_forward.getData().add(new XYChart.Data(String.valueOf(d), 0));
            }

        }


        XYChart.Series series_reverse = new XYChart.Series();

        List<Integer> targetList_reverse = new ArrayList<>(length_map_reverse.keySet());
        Collections.sort(targetList_reverse);

        for(double d : targetList_reverse){
            if (length_map_reverse.containsKey(d)){
                series_reverse.getData().add(new XYChart.Data(String.valueOf(d), length_map_reverse.get(d)));
            } else {
                series_reverse.getData().add(new XYChart.Data(String.valueOf(d), 0));
            }

        }
        bc_data_split.getData().addAll(series_forward, series_reverse);


    }

    private void generate_alldata(List<Integer> length_all) {

        Double[] data_all = prepareData(length_all);
        HashMap<Double, Integer> length_map_all = groupData(data_all);


        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        bc_data_all = new BarChart<>(xAxis,yAxis);

        bc_data_all.setCategoryGap(0);
        bc_data_all.setBarGap(1);
        bc_data_all.setTitle("Read length distribution");

        yAxis.setLabel("Number of reads");
        xAxis.setLabel("Read length");

        XYChart.Series series1 = new XYChart.Series();

        List<Double> targetList = new ArrayList<>(length_map_all.keySet());
        Collections.sort(targetList);

        for(double d : targetList){
            if (length_map_all.containsKey(d)){
                series1.getData().add(new XYChart.Data(String.valueOf(d), length_map_all.get(d)));
            } else {
                series1.getData().add(new XYChart.Data(String.valueOf(d), 0));
            }

        }

        bc_data_all.getData().addAll(series1);

    }

    private HashMap<Double, Integer> groupData(Double[] data_all) {

        HashMap<Double, Integer> idents_count = new HashMap<Double, Integer>();

        for (double ident : data_all) {
            if (!idents_count.containsKey(ident)) {
                idents_count.put(ident, 1);
            } else {
                int count_tmp = idents_count.get(ident);
                count_tmp=count_tmp+1;
                idents_count.put(ident, count_tmp);
            }
        }

        return idents_count;
    }

    private Double[] prepareData(List<Integer> length_data_all) {


        Double[] data_all = new Double[length_data_all.size()];
        for(int i=0; i < length_data_all.size(); i++){
            data_all[i] = Math.round(length_data_all.get(i) * 1000.0) / 1000.0;
        }

        return data_all;
    }

    public HBox getBc() {
        HBox plots_combined = new HBox();

        bc_data_all.prefHeightProperty().bind(plots_combined.heightProperty());
        bc_data_all.prefWidthProperty().bind(plots_combined.widthProperty());

        bc_data_split.prefHeightProperty().bind(plots_combined.heightProperty());
        bc_data_split.prefWidthProperty().bind(plots_combined.widthProperty());

        plots_combined.getChildren().addAll(bc_data_all, bc_data_split);
        return plots_combined;
    }
}
