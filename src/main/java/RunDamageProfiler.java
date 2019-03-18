import GUI.MainGuiFX;
import IO.*;
import calculations.StartCalculations;
import javafx.application.Application;

/**
 * Created by neukamm on 06.10.2016.
 */
public class RunDamageProfiler {

    private static final String VERSION = "0.4.5";


    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {

         /*

                  get input parameters

                    $ damageprofiler                    :   starts GUI

                    $ damageprofiler -i <> -o <> ....   :   parse command line arguments

          */


        if(args.length==0){
            new Thread(() -> Application.launch(MainGuiFX.class)).start();

        } else {
            Communicator c = new Communicator();
            StartCalculations starter = new StartCalculations(VERSION);
            UserOptionsParser userOptions = new UserOptionsParser(args, c);

            starter.start(c);

        }


    }

}
import GUI.MainGuiFX;
2
import IO.*;
3
import calculations.StartCalculations;
4
import javafx.application.Application;
5
import org.apache.log4j.Logger;
6
​
7
/**
8
 * Created by neukamm on 06.10.2016.
9
 */
10
public class RunDamageProfiler {
11
​
12
    private static final String VERSION = "0.4.4";
13
​
14
​
15
    @SuppressWarnings("static-access")
16
    public static void main(String[] args) throws Exception {
17
​
18
         /*
19
​
20
                  get input parameters
21
​
22
                    $ damageprofiler                    :   starts GUI
23
​
24
                    $ damageprofiler -i <> -o <> ....   :   parse command line arguments
25
​
26
          */
27
​
28
​
29
        if(args.length==0){
30
            new Thread(() -> Application.launch(MainGuiFX.class)).start();
31
​
32
        } else {
33
            Communicator c = new Communicator();
34
            StartCalculations starter = new StartCalculations(VERSION);
35
            UserOptionsParser userOptions = new UserOptionsParser(args, c);
36
​
37
            starter.start(c);
38
​
39
        }
40
​
41
​
42
    }
43
​
44
}
45
