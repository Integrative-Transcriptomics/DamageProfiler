import org.damageprofiler.calculations.StartCalculations;
import org.damageprofiler.io.Communicator;
import org.damageprofiler.io.UserOptionsParser;

public class StarterCLI {

  public StarterCLI(final String version, final String[] args) throws Exception {
    final Communicator c = new Communicator();
    final StartCalculations starter = new StartCalculations(version);
    new UserOptionsParser(args, c, version);
    starter.start(c);
    System.exit(0);
  }
}
