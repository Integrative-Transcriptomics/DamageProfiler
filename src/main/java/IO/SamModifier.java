package IO;

import java.io.*;

/**
 * Created by neukamm on 19.05.16.
 *
 * This class extracts one specie from SAM/BAM file with multiple species.
 *
 */
public class SamModifier {

    private File input;
    private String outfolder;
    private String outfile;


    public SamModifier(File sam_input, String outfolder){
        this.input = sam_input;
        this.outfolder = outfolder;
    }


    /**
     * Starts process builder to process bash file
     *
     * @param specie
     * @throws IOException
     * @throws InterruptedException
     */
    public void extractSpecieSequences(String specie) throws IOException, InterruptedException {


        File tempScript = createTempScript(specie);

        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
        } finally {
            tempScript.delete();
        }


    }

    /**
     * create temporal bash script to filter and merge new sam/bam file. The file only contains reads from the
     * specified species, the header contains only the corresponding reference
     *
     * @param specie
     * @return
     * @throws IOException
     */
    public File createTempScript(String specie) throws IOException {
        this.outfile = this.outfolder + "/" + specie + ".sam";
        File tempScript = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash");

        // get header of sam file
        printWriter.println("samtools view -H  " + this.input + " > " +  this.outfolder + "/" + specie + ".header.sam");

        // delete all entries starting with "@SQ"
        printWriter.println("sed -i '/^@SQ/d' " + this.outfolder + "/" + specie + ".header.sam");

        // get all records corresponding to selected species
        printWriter.println("grep '" + specie + "' " + this.input + " > " +  this.outfolder + "/" + specie + ".records.sam");

        // concatenate files to one sam file
        printWriter.println("cat " +  this.outfolder + "/" + specie + ".header.sam " + this.outfolder + "/" + specie + ".records.sam > " + this.outfolder + "/" + specie + ".sam");

        // clean up temporary files
        printWriter.println("rm " + this.outfolder + "/" + specie + ".header.sam");
        printWriter.println("rm " + this.outfolder + "/" + specie + ".records.sam");

        printWriter.close();

        return tempScript;
    }

    public String getOutfile() {
        return outfile;
    }
}
