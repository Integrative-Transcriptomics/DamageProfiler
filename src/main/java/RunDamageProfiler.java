import GUI.MainDP;
import IO.*;
import calculations.StartCalculations;

/**
 * Created by neukamm on 06.10.2016.
 */
public class RunDamageProfiler {

    private static final String VERSION = "0.3.8";


    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {
        System.out.println("DamageProfiler v" + VERSION);
        Communicator c = new Communicator();

         /*

                  get input parameters

                    $ damageprofiler                    :   starts GUI

                    $ damageprofiler -i <> -o <> ....   :   parse command line arguments

          */

        StartCalculations starter = new StartCalculations();
        if(args.length==0){
            MainDP damageProfilerGUI = new MainDP(c, starter, VERSION);
        } else {
            UserOptionsParser userOptions = new UserOptionsParser(args, c);
            starter.start(c);
        }

    }

}
