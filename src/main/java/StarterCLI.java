import org.damageprofiler.calculations.StartCalculations;
import org.damageprofiler.io.Communicator;
import org.damageprofiler.io.UserOptionsParser;

public class StarterCLI {

  public StarterCLI(final String[] commandLineArguments) throws Exception {
    final Communicator communicator = new Communicator();
    final StartCalculations starter = new StartCalculations();
    new UserOptionsParser(commandLineArguments, communicator);
    starter.start(communicator);
    System.exit(0);
  }
}
