package calculations;

import IO.Communicator;
import IO.OutputGenerator;
import IO.SamModifier;
import IO.Unzip;
import calculations.DamageProfiler;
import calculations.SpecieHandler;

import java.io.File;
import java.io.IOException;

/**
 * Created by neukamm on 11.11.2016.
 */
public class StartCalculations {

    private long currtime_prior_execution;  // start time to get overall runtime

    public StartCalculations(){ }

    public void start(Communicator c) throws Exception {

        currtime_prior_execution = System.currentTimeMillis();

        String input = c.getInput();
        String outfolder = c.getOutfolder();
        String reference = c.getReference();
        String rname = c.getRname();
        String specie_name = "";
        int length = c.getLength();
        int threshold = c.getThreshold();
        boolean use_only_merged_reads = !c.isUse_merged_and_mapped_reads();

        // decompress input file if necessary
        if(input.endsWith(".gz")){
            Unzip unzip = new Unzip();
            input = unzip.decompress(input);
        }

        // create new output folder
        File file = new File(input);
        String inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
        String output_folder = createOutputFolder(outfolder);

        // create variables for tax id and gi ID if specie is set
        if(rname != null){
            String gi = "";

            SpecieHandler specieHandler = new SpecieHandler();
            specieHandler.init(gi, rname);
            specieHandler.getSpecie();
            specie_name = specieHandler.getSpecie_name();
            output_folder = createOutputFolder(output_folder);
            gi = specieHandler.getGi();

            // write out all SAM records corresponding to the specie
            SamModifier samModifier = new SamModifier(file, output_folder);
            samModifier.extractSpecieSequences(gi);
            file = new File(samModifier.getOutfile());

        }

        // start DamageProfiler
        DamageProfiler damageProfiler = new DamageProfiler(file, new File(reference), threshold, length);
        damageProfiler.extractSAMRecords(use_only_merged_reads);

        // generate output files
        OutputGenerator outputGenerator = new OutputGenerator(output_folder, damageProfiler, specie_name, threshold, length);
        outputGenerator.writeLengthDistribution(input);
        outputGenerator.writeDamageFiles(damageProfiler.getFrequencies().getCount_G_A_3_norm(),
                damageProfiler.getFrequencies().getCount_C_T_5_norm());
        outputGenerator.writeFrequenciesReference(damageProfiler.getFrequencies());
        outputGenerator.writeDNAComp(damageProfiler.getFrequencies());
        outputGenerator.writeDNAcomp_genome(damageProfiler.getFrequencies());
        outputGenerator.writeMisincorporations(damageProfiler.getFrequencies(), threshold);


        // create DamagePlots of 3' and 5' ends
        outputGenerator.plotMisincorporations(inputfileNameWithOutExtension);
        outputGenerator.plotLengthHistogram(damageProfiler.getLength_all(), damageProfiler.getLength_forward(),
                damageProfiler.getLength_reverse(), inputfileNameWithOutExtension);
        outputGenerator.plotIdentitiyHistogram(damageProfiler.getIdentity(), "Identitiy distribution", inputfileNameWithOutExtension);


        // print runtime
        long currtime_post_execution = System.currentTimeMillis();
        long diff = currtime_post_execution - currtime_prior_execution;
        long runtime_s = diff / 1000;
        if(runtime_s > 60) {
            long minutes = runtime_s / 60;
            long seconds = runtime_s % 60;
            System.out.println("Runtime of Module was: " + minutes + " minutes, and " + seconds + " seconds.");
        } else {
            System.out.println("Runtime of Module was: " + runtime_s + " seconds.");
        }

    }


    /**
     * create output folder.
     * Save all files in subfolder, which has the same name as the input file
     * (without extension)
     *
     * @param path
     * @throws IOException
     */
    private static String createOutputFolder(String path) throws IOException {

        // use Pattern.quote(File.separator) to split file path
        File f = new File(path);

        // create new output directory
        if (!f.isDirectory()) {
            f.mkdirs();
        }

        return path;
    }
}
