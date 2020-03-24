import IO.Communicator;
import IO.UserOptionsParser;
import calculations.RuntimeEstimator;
import calculations.StartCalculations;

public class StarterCLI {

    public StarterCLI(String version, String[] args) throws Exception {
        Communicator c = new Communicator();
        StartCalculations starter = new StartCalculations();
        starter.setVERSION(version);
        UserOptionsParser userOptions = new UserOptionsParser(args, c, version);
        RuntimeEstimator runtimeEstimator = new RuntimeEstimator(c.getInput());
        starter.start(c);
    }
}
