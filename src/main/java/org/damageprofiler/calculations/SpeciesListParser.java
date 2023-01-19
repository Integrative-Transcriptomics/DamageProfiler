package org.damageprofiler.calculations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpeciesListParser {

  private final String speciesFile;

  public SpeciesListParser(String speciesListFile) {
    this.speciesFile = speciesListFile;
  }

  /**
   * Read file with species listed by the user.
   *
   * @return
   */
  private List<String> readFile() {

    try {
      File file = new File(speciesFile);
      FileReader fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line;
      List<String> list_species = new ArrayList<>();

      while ((line = bufferedReader.readLine()) != null) {
        list_species.add(line.trim());
      }

      fileReader.close();

      return list_species;
    } catch (IOException e) {
      e.printStackTrace();
    }

    // will never reached (hopefully :) )
    return null;
  }

  /*
     Getter and Setter
  */

  public List<String> getSpeciesList() {

    return readFile();
  }
}
