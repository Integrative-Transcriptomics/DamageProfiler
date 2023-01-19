import javafx.application.Application;
import javafx.stage.Stage;
import org.damageprofiler.controller.DamageProfilerMainController;
import org.damageprofiler.controller.PlottingSettingController;
import org.damageprofiler.controller.ProgressBarController;
import org.damageprofiler.gui.DamageProfilerMainGUI;

public class StarterGUI extends Application {

  private static final String VERSION = "1.0";

  @Override
  public void start(final Stage primaryStage) {

    final ProgressBarController progressBarController = new ProgressBarController();
    progressBarController.create();

    final DamageProfilerMainGUI damageProfilerMainGUI =
        new DamageProfilerMainGUI(VERSION, progressBarController);
    damageProfilerMainGUI.init(primaryStage);

    final PlottingSettingController plottingSettingController = new PlottingSettingController();
    new DamageProfilerMainController(
        damageProfilerMainGUI, progressBarController, plottingSettingController);
  }
}
