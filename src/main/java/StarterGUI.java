import GUI.DamageProfilerMainController;
import GUI.DamageProfilerMainGUI;
import javafx.application.Application;

import javafx.stage.Stage;



public class StarterGUI extends Application {

    private static final String VERSION = "0.4.9";


    @Override
    public void start(Stage primaryStage) {

        DamageProfilerMainGUI damageProfilerMainGUI = new DamageProfilerMainGUI(VERSION);
        damageProfilerMainGUI.init(primaryStage);

        DamageProfilerMainController damageProfilerMainController = new DamageProfilerMainController(damageProfilerMainGUI);



    }


}
