import javafx.application.Application;
import org.apache.log4j.Logger;

public class RunDamageProfiler {

  private static final Logger logger = Logger.getLogger(RunDamageProfiler.class);

  public static void main(final String[] args) throws Exception {

    if (args.length == 0) {
      logger.info("Start DamageProfiler with User Interface");
      new Thread(() -> Application.launch(StarterGUI.class)).start();

    } else {
      logger.info("Start DamageProfiler with Command Line Interface");
      System.setProperty("java.awt.headless", "true");
      new StarterCLI(args);
    }
  }
}
