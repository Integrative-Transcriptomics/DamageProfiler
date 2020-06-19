package org.damageprofiler.calculations;

import java.io.*;
import java.util.Random;

import org.damageprofiler.IO.DOMParser;
import org.apache.log4j.Logger;


/**
 * Created by neukamm on 25.07.16.
 */
public class SpecieHandler {

    private Logger LOG;
    private String gi;
    private String specie_name;

    public SpecieHandler(){
    }


    public void getSpecie(String rname){
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
            if(tax.equals("") && gi != null){
                LOG.info("\nRecord reference contains only gi ID. This is not supported by DamageProfiler.\n" +
                        "The species name will be set to NULL.\n" +
                        "Please make sure that the SAM/BAM file reference tag contains the tax ID");
            }
            if(!tax.equals("")){
               specie_name = getSpeciesByID(Integer.parseInt(tax));
               // specie_name = specie_name.replaceAll(" ", "_");
            }
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
    private String getSpeciesByID(int id) {
        String species="";
        Random rand = new Random();

        // Obtain a number between [0 - 1000].
        int n = rand.nextInt(1000);

        // run command line call to get XML file from
        // ncbi with information about the ncbiID of our species

        // try to call 'curl'. If not possible, do not set species name but only ID
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("curl https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=taxonomy&amp;id=" + id);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedWriter xmlOutput = new BufferedWriter(new FileWriter("ncbiID" + n + ".xml"));

            // read the output from the command and write it in xml file
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                xmlOutput.write(s);
            }
            xmlOutput.close();

            DOMParser domparser = new DOMParser(LOG);
            species = domparser.parse("ncbiID" + n + ".xml");
            File f = new File("ncbiID" + n + ".xml");
            f.delete();
        } catch (Exception e){
            System.out.println("'Curl' is not installed. If you would like to use this option, please install it.");
        }



        return species;
    }


    public String getSpecie_name() {
        return specie_name;
    }

    public void setLOG(Logger LOG) {
        this.LOG = LOG;
    }
}
