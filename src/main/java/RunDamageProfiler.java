import GUI.MainDP;
import IO.*;
import calculations.StartCalculations;

/**
 * Created by neukamm on 06.10.2016.
 */
public class RunDamageProfiler {

    private static final String VERSION = "0.2";
    private Communicator c;


    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {
        //System.setProperty("java.awt.headless", "true");
        System.out.println("DamageProfiler v" + VERSION);
        // init communicator
        Communicator c = new Communicator();

         /*

                  get input parameters
          */


        // if DamageProfiler is run with only one argument (damageprofiler),
        // start GUI
        // otherwise : parse command line arguments to communicator
        StartCalculations starter = new StartCalculations();
        if(args.length==0){
            MainDP damageProfilerGUI = new MainDP(c, starter);
        } else {
            UserOptionsParser userOptions = new UserOptionsParser(args, c);
            starter.start(c);
        }



    }



}
