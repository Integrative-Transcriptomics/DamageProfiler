import GUI.MainGuiFX;
import IO.*;
import calculations.StartCalculations;
import javafx.application.Application;

/**
 * Created by neukamm on 06.10.2016.
 */
public class RunDamageProfiler {

    private static final String VERSION = "0.3.12";


    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {

         /*

                  get input parameters

                    $ damageprofiler                    :   starts GUI

                    $ damageprofiler -i <> -o <> ....   :   parse command line arguments

          */


        if(args.length==0){
            //MainDP damageProfilerGUI = new MainDP(c, starter, VERSION);
            new Thread(() -> Application.launch(MainGuiFX.class)).start();

        } else {
            Communicator c = new Communicator();
            StartCalculations starter = new StartCalculations(VERSION);
            UserOptionsParser userOptions = new UserOptionsParser(args, c);
            starter.start(c);

        }


    }

}
