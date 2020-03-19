import GUI.MainGui;
import IO.*;
import calculations.RuntimeEstimator;
import calculations.StartCalculations;
import javafx.application.Application;

/**
 * Created by neukamm on 06.10.2016.
 */
public class RunDamageProfiler {

    private static final String VERSION = "0.4.9";


    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {

         /*

                  get input parameters

                    $ damageprofiler                    :   starts GUI

                    $ damageprofiler -i <> -o <> ....   :   parse command line arguments

          */


        System.setProperty("java.awt.headless", "true");

        if(args.length==0){
            //MainDP damageProfilerGUI = new MainDP(c, starter, VERSION);
            System.out.println(VERSION);
            new Thread(() -> Application.launch(MainGui.class)).start();

        } else {
            Communicator c = new Communicator();
            StartCalculations starter = new StartCalculations(VERSION);
            UserOptionsParser userOptions = new UserOptionsParser(args, c, VERSION);
            RuntimeEstimator runtimeEstimator = new RuntimeEstimator(c.getInput());
            starter.start(c, runtimeEstimator);

        }


    }

}
