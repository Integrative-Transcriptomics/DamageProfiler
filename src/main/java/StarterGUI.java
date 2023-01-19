import javafx.application.Application;
import javafx.stage.Stage;
import org.damageprofiler.controller.DamageProfilerMainController;
import org.damageprofiler.controller.PlottingSettingController;
import org.damageprofiler.controller.ProgressBarController;
import org.damageprofiler.gui.DamageProfilerMainGUI;

public class StarterGUI extends Application {


    @Override
    public void start(final Stage primaryStage) {

        final ProgressBarController progressBarController = new ProgressBarController();
        progressBarController.create();

        final DamageProfilerMainGUI damageProfilerMainGUI =
                new DamageProfilerMainGUI(progressBarController);
        damageProfilerMainGUI.init(primaryStage);

        final PlottingSettingController plottingSettingController = new PlottingSettingController();
        new DamageProfilerMainController(
                damageProfilerMainGUI, progressBarController, plottingSettingController);
    }
}
