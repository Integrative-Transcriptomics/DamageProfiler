import org.damageprofiler.calculations.StartCalculations;
import org.damageprofiler.io.Communicator;
import org.damageprofiler.io.UserOptionsParser;

public class StarterCLI {

  public StarterCLI(final String[] args) throws Exception {
    final Communicator c = new Communicator();
    final StartCalculations starter = new StartCalculations();
    new UserOptionsParser(args, c);
    starter.start(c);
    System.exit(0);
  }
}
