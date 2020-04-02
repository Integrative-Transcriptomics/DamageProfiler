package GUI.Dialogues;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class TabAdvancedSettingsDamagePlot {

    private final Tab tab;
    private ColorPicker colorPicker_C_to_T;
    private ColorPicker colorPicker_G_to_A;
    private ColorPicker colorPicker_insertions;
    private ColorPicker colorPicker_deletions;
    private ColorPicker colorPicker_others;

    public TabAdvancedSettingsDamagePlot(String title){

        this.tab = new Tab(title);
        fill();

    }

    private void fill() {
        GridPane gridpane = new GridPane();
        gridpane.setAlignment(Pos.BOTTOM_LEFT);
        gridpane.setHgap(7);
        gridpane.setVgap(7);
        gridpane.setPadding(new Insets(10,10,10,10));

        Button btn_reset = new Button("Reset");
        colorPicker_C_to_T = generateColorPicker(Color.RED);
        colorPicker_G_to_A = generateColorPicker(Color.BLUE);
        colorPicker_insertions = generateColorPicker(Color.valueOf("FF00FF"));
        colorPicker_deletions = generateColorPicker(Color.GREEN);
        colorPicker_others = generateColorPicker(Color.GREY);

        gridpane.add(new Label("C->T"), 0,0,1,1);
        gridpane.add(colorPicker_C_to_T, 0,1,1,1);

        gridpane.add(new Label("G->A"), 1,0,1,1);
        gridpane.add(colorPicker_G_to_A, 1,1,1,1);

        gridpane.add(new Label("Insertions"), 0,2,1,1);
        gridpane.add(colorPicker_insertions, 0,3,1,1);

        gridpane.add(new Label("Deletions"), 1,2,1,1);
        gridpane.add(colorPicker_deletions, 1,3,1,1);

        gridpane.add(new Label("Others"), 0,4,1,1);
        gridpane.add(colorPicker_others, 0,5,1,1);

        gridpane.add(btn_reset, 2,5,1,1);

        tab.setContent(gridpane);
    }


    private ColorPicker generateColorPicker(Color color) {
        ColorPickerPane colorPickerPane = new ColorPickerPane(color);
        return colorPickerPane.getPicker();

    }

    public Tab getTab() {
        return tab;
    }

    public ColorPicker getColorPicker_C_to_T() {
        return colorPicker_C_to_T;
    }

    public ColorPicker getColorPicker_G_to_A() {
        return colorPicker_G_to_A;
    }

    public ColorPicker getColorPicker_insertions() {
        return colorPicker_insertions;
    }

    public ColorPicker getColorPicker_deletions() {
        return colorPicker_deletions;
    }

    public ColorPicker getColorPicker_others() {
        return colorPicker_others;
    }
}
