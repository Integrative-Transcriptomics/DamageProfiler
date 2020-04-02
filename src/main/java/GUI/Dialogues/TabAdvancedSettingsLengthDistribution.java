package GUI.Dialogues;

import javafx.scene.control.Tab;

public class TabAdvancedSettingsLengthDistribution {

    private final Tab tab;

    public TabAdvancedSettingsLengthDistribution(String title){
        this.tab = new Tab(title);
        fill();
    }

    private void fill() {

    }

    public Tab getTab() {

        return tab;
    }
}
