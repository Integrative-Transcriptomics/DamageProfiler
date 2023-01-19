import javafx.application.Application;

/**
 * Created by neukamm on 06.10.2016.
 */
public class RunDamageProfiler {

    public static void main(final String[] args) throws Exception {

    /*
            get input parameters

              $ damageprofiler                    :   starts org.damageprofiler.GUI

              $ damageprofiler -i <> -o <> ....   :   parse command line arguments
    */

        if (args.length == 0) {
            new Thread(() -> Application.launch(StarterGUI.class)).start();

        } else {
            System.setProperty("java.awt.headless", "true");
            new StarterCLI(args);
        }
    }
}
