package org.damageprofiler.calculations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TestFunctions {

  @ParameterizedTest(name = "Hamming distance of {0} and {1} should equal {2}")
  @CsvSource({"AAA, BBB, 3", "AAA, AAA, 0", "AAA, B-B, 3"})
  void getHammingDistance(String first, String second, int expectedResult) {
    Functions functions = new Functions();
    assertEquals(
        expectedResult,
        functions.getHammingDistance(first, second),
        () ->
            "Hamming distance of " + first + " and " + second + " should equal " + expectedResult);
  }
}
