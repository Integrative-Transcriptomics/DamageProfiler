import org.damageprofiler.calculations.StartCalculations;
import org.damageprofiler.io.Communicator;
import org.damageprofiler.io.UserOptionsParser;

public class StarterCLI {

    public StarterCLI(String version, String[] args) throws Exception {
        Communicator c = new Communicator();
        StartCalculations starter = new StartCalculations();
        starter.setVERSION(version);
        new UserOptionsParser(args, c, version);
        starter.start(c);
        System.exit(0);
    }
}
