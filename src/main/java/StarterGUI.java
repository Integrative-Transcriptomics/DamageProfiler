import org.damageprofiler.controller.DamageProfilerMainController;
import org.damageprofiler.gui.DamageProfilerMainGUI;
import org.damageprofiler.controller.PlottingSettingController;
import org.damageprofiler.controller.ProgressBarController;
import javafx.application.Application;
import javafx.stage.Stage;


public class StarterGUI extends Application {

    private static final String VERSION = "1.0";

    @Override
    public void start(Stage primaryStage) {

        ProgressBarController progressBarController = new ProgressBarController();
        progressBarController.create();

        DamageProfilerMainGUI damageProfilerMainGUI = new DamageProfilerMainGUI(VERSION, progressBarController);
        damageProfilerMainGUI.init(primaryStage);

        PlottingSettingController plottingSettingController = new PlottingSettingController();
        new DamageProfilerMainController(damageProfilerMainGUI, progressBarController, plottingSettingController);

    }


}
