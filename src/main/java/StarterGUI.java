import controller.DamageProfilerMainController;
import GUI.DamageProfilerMainGUI;
import controller.ProgressBarController;
import controller.TabPaneAdvPlottingController;
import javafx.application.Application;

import javafx.stage.Stage;


public class StarterGUI extends Application {

    private static final String VERSION = "0.4.9";


    @Override
    public void start(Stage primaryStage) {

        ProgressBarController progressBarController = new ProgressBarController();
        progressBarController.create();

        DamageProfilerMainGUI damageProfilerMainGUI = new DamageProfilerMainGUI(VERSION, progressBarController);
        damageProfilerMainGUI.init(primaryStage);

        DamageProfilerMainController damageProfilerMainController = new DamageProfilerMainController(damageProfilerMainGUI, progressBarController);

    }


}
