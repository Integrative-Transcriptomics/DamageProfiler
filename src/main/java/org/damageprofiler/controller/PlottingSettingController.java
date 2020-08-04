package org.damageprofiler.controller;

import org.damageprofiler.GUI.Dialogues.AdvancedPlottingOptionsDialogue;
import org.damageprofiler.GUI.Dialogues.TabAdvancedSettingsDamagePlot;

public class PlottingSettingController {

    public PlottingSettingController(){

    }

    /**
     * Adding all listener to advancedPlottingOptionsDialogue
     *
     * @param advancedPlottingOptionsDialogue
     */
    public void addListener(AdvancedPlottingOptionsDialogue advancedPlottingOptionsDialogue) {
        TabAdvancedSettingsDamagePlot dp_settings_pane = advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot();
        dp_settings_pane.getBtn_reset().setOnAction(e ->{
            dp_settings_pane.reset();

        });

    }
}
