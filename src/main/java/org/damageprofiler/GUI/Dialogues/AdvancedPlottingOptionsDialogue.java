package org.damageprofiler.GUI.Dialogues;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


public class AdvancedPlottingOptionsDialogue {

    private TabPane tabPane;
    private Tab tab_DP;
    private TabAdvancedSettingsDamagePlot tabAdvancedSettingsDamagePlot;

    /**
     * Tab for the configuration of all colours of the damage patterns
     */
    public AdvancedPlottingOptionsDialogue(){

        fill();
    }

    /**
     * Fill the empty tab pane with all components
     */
    private void fill() {

        tabAdvancedSettingsDamagePlot = new TabAdvancedSettingsDamagePlot("Damage Profile");
        tab_DP = tabAdvancedSettingsDamagePlot.getTab();

        tabPane = new TabPane();
        tabPane.getTabs().addAll(tab_DP);

    }


    /*
            Getter
     */
    public TabPane getGridPane() {
        return tabPane;
    }
    public TabAdvancedSettingsDamagePlot getTabAdvancedSettingsDamagePlot() {
        return tabAdvancedSettingsDamagePlot;
    }

}
