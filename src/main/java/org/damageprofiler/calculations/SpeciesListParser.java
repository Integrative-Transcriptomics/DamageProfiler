package org.damageprofiler.calculations;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpeciesListParser {


    private Logger LOG;
    private String speciesFile;
    private SpeciesHandler speciesHandler;

    public SpeciesListParser(String speciesListFile, Logger LOG) {
        this.speciesFile = speciesListFile;
        this.LOG = LOG;
        speciesHandler = new SpeciesHandler();
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
    public void setLOG(Logger LOG) {
        this.LOG = LOG;
        speciesHandler.setLOG(LOG);
    }


    public List<String> getSpeciesList() {

        return readFile();
    }
}

