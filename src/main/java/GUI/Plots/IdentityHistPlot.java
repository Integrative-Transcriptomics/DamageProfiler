package GUI.Plots;

import javafx.scene.chart.*;
import java.util.*;


public class IdentityHistPlot {

    private BarChart<String, Number> barChart;

    private Double[] data;
    private HashMap<Double, Integer> idents_count;


    public IdentityHistPlot(ArrayList<Double> identity_data) {

        prepareData(identity_data);
        groupData();

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis,yAxis);

        barChart.setCategoryGap(0);
        barChart.setBarGap(1);
        barChart.setTitle("Read identity");

        yAxis.setLabel("Number of reads");
        xAxis.setLabel("Read identity");

        XYChart.Series series1 = new XYChart.Series();

        List<Double> targetList = new ArrayList<>(idents_count.keySet());
        Collections.sort(targetList);

        for(double d : targetList){
            if (idents_count.containsKey(d)){
                series1.getData().add(new XYChart.Data(String.valueOf(d), idents_count.get(d)));
            } else {
                series1.getData().add(new XYChart.Data(String.valueOf(d), 0));
            }

        }

        barChart.getData().addAll(series1);

    }



    //prepare data
    private void prepareData(ArrayList<Double> identity_data){
        data = new Double[identity_data.size()];
        for(int i=0; i < identity_data.size(); i++){
            data[i] = Math.round(identity_data.get(i) * 1000.0) / 1000.0;
        }
    }

    //count data identities in groups
    private void groupData(){

        idents_count = new HashMap<Double, Integer>();

        for (double ident : data) {
            if (!idents_count.containsKey(ident)) {
                idents_count.put(ident, 1);
            } else {
                int count_tmp = idents_count.get(ident);
                count_tmp=count_tmp+1;
                idents_count.put(ident, count_tmp);
            }
        }

    }

    public BarChart<String, Number> getBarChart() {
        return barChart;
    }
}
