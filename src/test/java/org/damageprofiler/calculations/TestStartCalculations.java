package org.damageprofiler.calculations;

import org.damageprofiler.io.Communicator;
import org.junit.jupiter.api.Test;

public class TestStartCalculations {

    @Test
    void damageProfiler(){
        Communicator c = new Communicator();
        StartCalculations starter = new StartCalculations("1.0 ");

        String path_input = "src/test/resources/mapping_testfile.bam";
        String path_output = "src/test/resources/test_out/";
        c.setInput(path_input);
        c.setOutfolder(path_output);

    }
}
