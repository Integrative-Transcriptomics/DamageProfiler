package org.damageprofiler.calculations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

public class TestSpeciesListParser {

  @Test
  void testParser() {

    final String file = "src/test/resources/species_list.txt";
    final SpeciesListParser speciesListParser = new SpeciesListParser(file);
    final List<String> expectedResult =
        List.of("NC_031253.1|tax|1805457|", "NC_031266.1|tax|1821541|");
    assertEquals(expectedResult, speciesListParser.getSpeciesList());
  }
}
