package org.damageprofiler.calculations;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSpeciesListParser {


    @Test
    void testParser(){

        String file="src/test/resources/species_list.txt";
        SpeciesListParser speciesListParser = new SpeciesListParser(file);
        List<String> expectedResult = List.of("NC_031253.1|tax|1805457|", "NC_031266.1|tax|1821541|");
        assertEquals(expectedResult, speciesListParser.getSpeciesList());


    }


}
