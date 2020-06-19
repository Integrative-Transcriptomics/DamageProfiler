package org.damageprofiler;

import org.damageprofiler.calculations.StartCalculations;
import org.damageprofiler.IO.Communicator;
import org.damageprofiler.IO.UserOptionsParser;

public class StarterCLI {

    public StarterCLI(String version, String[] args) throws Exception {
        Communicator c = new Communicator();
        StartCalculations starter = new StartCalculations();
        starter.setVERSION(version);
        UserOptionsParser userOptions = new UserOptionsParser(args, c, version);
        starter.start(c);
    }
}
