package org.damageprofiler.calculations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpeciesListParser {

  private final String speciesFile;

  public SpeciesListParser(final String speciesListFile) {
    this.speciesFile = speciesListFile;
  }

  private List<String> readFile() {

    try {
      final File file = new File(speciesFile);
      final FileReader fileReader = new FileReader(file);
      final BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line;
      final List<String> list_species = new ArrayList<>();

      while ((line = bufferedReader.readLine()) != null) {
        list_species.add(line.trim());
      }

      fileReader.close();

      return list_species;
    } catch (final IOException e) {
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
