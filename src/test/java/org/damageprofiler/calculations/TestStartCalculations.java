package org.damageprofiler.calculations;

import org.damageprofiler.io.Communicator;
import org.junit.jupiter.api.Test;

public class TestStartCalculations {

  @Test
  void damageProfiler() {
    final Communicator c = new Communicator();
    final StartCalculations starter = new StartCalculations();

    final String path_input = "src/test/resources/mapping_testfile.bam";
    final String path_output = "src/test/resources/test_out/";
    c.setInput(path_input);
    c.setOutfolder(path_output);
  }
}
