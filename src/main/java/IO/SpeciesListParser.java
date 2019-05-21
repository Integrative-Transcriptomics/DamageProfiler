package IO;

import calculations.SpecieHandler;
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
    private SpecieHandler specieHandler;

    public SpeciesListParser(String speciesListFile, Logger LOG) {
        this.speciesFile = speciesListFile;
        this.LOG = LOG;
        specieHandler = new SpecieHandler();
    }


    public String getSingleSpecie(String rname) {

        specieHandler.getSpecie(rname);
        return specieHandler.getSpecie_name();
    }

    public List<String> getList() {

        return readFile();
    }

    private List<String> readFile() {

        try {
            File file = new File(speciesFile);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            List<String> list = new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null) {
                list.add(line.trim());
            }

            fileReader.close();

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // will never reached (hopefully :) )
        return null;

    }

    public void setLOG(Logger LOG) {
        this.LOG = LOG;
        specieHandler.setLOG(LOG);
    }
}

