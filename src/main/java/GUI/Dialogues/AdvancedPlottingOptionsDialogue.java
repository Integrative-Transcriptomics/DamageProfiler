package GUI.Dialogues;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


public class AdvancedPlottingOptionsDialogue {

    private TabPane tabPane;
    private Tab tab_Length_Dist;
    private Tab tab_Edit_Dist;
    private Tab tab_DP;
    private TabAdvancedSettingsLengthDistribution tabAdvancedSettingsLengthDistribution;
    private TabAdvancedSettingsDamagePlot tabAdvancedSettingsDamagePlot;
    private TabAdvancedSettingsEditdistance tabAdvancedSettingsEditdistance;

    public AdvancedPlottingOptionsDialogue(){

        fill();
    }

    private void fill() {

        tabAdvancedSettingsDamagePlot = new TabAdvancedSettingsDamagePlot("Damage Profile");
        tab_DP = tabAdvancedSettingsDamagePlot.getTab();

        tabAdvancedSettingsEditdistance = new TabAdvancedSettingsEditdistance("Edit Distance");
        tab_Edit_Dist = tabAdvancedSettingsEditdistance.getTab();

        tabAdvancedSettingsLengthDistribution = new TabAdvancedSettingsLengthDistribution("Length Distribution");
        tab_Length_Dist = tabAdvancedSettingsLengthDistribution.getTab();

        tabPane = new TabPane();
        tabPane.getTabs().addAll(tab_DP,tab_Edit_Dist, tab_Length_Dist);

    }




    public TabPane getGridPane() {
        return tabPane;
    }

    public Tab getTab_Length_Dist() {
        return tab_Length_Dist;
    }

    public Tab getTab_Edit_Dist() {
        return tab_Edit_Dist;
    }

    public Tab getTab_DP() {
        return tab_DP;
    }

    public TabAdvancedSettingsDamagePlot getTabAdvancedSettingsDamagePlot() {
        return tabAdvancedSettingsDamagePlot;
    }

    public TabAdvancedSettingsLengthDistribution getTabAdvancedSettingsLengthDistribution() {
        return tabAdvancedSettingsLengthDistribution;
    }

    public TabAdvancedSettingsEditdistance getTabAdvancedSettingsEditdistance() {
        return tabAdvancedSettingsEditdistance;
    }
}
