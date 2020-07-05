import org.damageprofiler.controller.DamageProfilerMainController;
import org.damageprofiler.GUI.DamageProfilerMainGUI;
import org.damageprofiler.controller.ProgressBarController;
import javafx.application.Application;
import javafx.stage.Stage;


public class StarterGUI extends Application {

    private static final String VERSION = "0.5.0";

    @Override
    public void start(Stage primaryStage) {

        ProgressBarController progressBarController = new ProgressBarController();
        progressBarController.create();

        DamageProfilerMainGUI damageProfilerMainGUI = new DamageProfilerMainGUI(VERSION, progressBarController);
        damageProfilerMainGUI.init(primaryStage);

        DamageProfilerMainController damageProfilerMainController = new DamageProfilerMainController(damageProfilerMainGUI, progressBarController);

    }


}
