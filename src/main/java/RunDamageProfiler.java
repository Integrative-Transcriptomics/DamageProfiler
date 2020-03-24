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
            new Thread(() -> Application.launch(StarterGUI.class)).start();

        } else {
            StarterCLI starterCLI = new StarterCLI(VERSION, args);
        }


    }

}
