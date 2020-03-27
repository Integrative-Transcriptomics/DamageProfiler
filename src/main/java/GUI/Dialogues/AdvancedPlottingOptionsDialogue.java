package GUI.Dialogues;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class AdvancedPlottingOptionsDialogue {
    private GridPane plotting_config_gridpane;

    public AdvancedPlottingOptionsDialogue(){
        plotting_config_gridpane = new GridPane();
        plotting_config_gridpane.setAlignment(Pos.CENTER);
        plotting_config_gridpane.setHgap(10);
        plotting_config_gridpane.setVgap(10);
        plotting_config_gridpane.setPadding(new Insets(15,15,15,15));

        fill();
    }

    private void fill() {

    }


    public Node getGridPane() {
        return plotting_config_gridpane;
    }
}
