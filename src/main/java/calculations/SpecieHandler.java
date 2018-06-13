package calculations;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import IO.DOMParser;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;


/**
 * Created by neukamm on 25.07.16.
 */
public class SpecieHandler {

    private final Logger LOG;
    private String gi;
    private String rname;
    private String specie_name;

    public SpecieHandler(String gi, String rname, Logger LOG){
        this.LOG = LOG;
        this.gi = gi;
        this.rname = rname;
    }


    public void getSpecie() throws IOException, SAXException, ParserConfigurationException, InterruptedException {
        if (rname != null) {
            String tax = "";


            // get tax id from RNAME string
            String[] rname_split = rname.split("\\|");
            for (int i = 0; i < rname_split.length; i++) {
                switch (rname_split[i]) {
                    case "gi":
                        gi = rname_split[i + 1];
                        break;
                    case "tax":
                        tax = rname_split[i + 1];
                        break;
                }
            }
            // map tax ID to name
            specie_name = getSpeciesByID(Integer.parseInt(tax));
            specie_name = specie_name.replaceAll(" ", "_");



        }
    }




    /**
     * This method runs the command line program "curl" to get
     * the information of the species from ncbi DB.
     * Then, it writes the output into a xml file and
     * parses this to get the species name of the ncbi ID
     *
     * @param id
     * @return species name as string
     * @throws IOException
     */
    private String getSpeciesByID(int id) throws IOException, SAXException, ParserConfigurationException {
        String species;

        // run command line call to get XML file from
        // ncbi with information about the ncbiID of our species
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec("curl https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=taxonomy&amp;id=" + id);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedWriter xmlOutput = new BufferedWriter(new FileWriter("ncbiID.xml"));

        // read the output from the command and write it in xml file
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            xmlOutput.write(s);
        }
        xmlOutput.close();

        DOMParser domparser = new DOMParser(LOG);
        species = domparser.parse("ncbiID.xml");


        return species;
    }


    public String getSpecie_name() {
        return specie_name;
    }

    public String getGi() {
        return gi;
    }
}
