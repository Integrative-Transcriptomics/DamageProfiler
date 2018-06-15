package IO;

import calculations.SpecieHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpeciesListParser {


    private String speciesFile;

    public SpeciesListParser(String specie_name, String speciesListFile) {
        this.speciesFile = speciesListFile;
    }


    public String getSingleSpecie(String rname) throws InterruptedException, SAXException, ParserConfigurationException, IOException {

        String gi = "";
        SpecieHandler specieHandler = new SpecieHandler(gi, rname, null);
        specieHandler.getSpecie();

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
}
