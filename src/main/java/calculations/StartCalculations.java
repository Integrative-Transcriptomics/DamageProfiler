package calculations;

import IO.Communicator;
import IO.LogClass;
import IO.OutputGenerator;
import IO.Unzip;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;

import javafx.stage.Stage;
import org.apache.log4j.*;


/**
 * Created by neukamm on 11.11.2016.
 */
public class StartCalculations {

    private final String VERSION;
    private LogClass logClass;
    private long currtime_prior_execution;  // start time to get overall runtime
    private boolean calculationsDone=true;
    private Logger LOG;

    public StartCalculations(String version){
        VERSION = version;
    }

    public void start(Communicator c) throws Exception {

        currtime_prior_execution = System.currentTimeMillis();

        String input = c.getInput();
        String outfolder = c.getOutfolder();
        String reference = c.getReference();
        String rname = c.getRname();
        String specie_name = "";
        int length = c.getLength();
        int threshold = c.getThreshold();
        double height = c.getyAxis();
        boolean use_only_merged_reads = !c.isUse_merged_and_mapped_reads();

        // init Logger
        logClass = new LogClass();
        logClass.updateLog4jConfiguration(outfolder + "/DamageProfiler.log");
        logClass.setUp();
        LOG = logClass.getLogger(this.getClass());
        System.out.println("DamageProfiler v" + VERSION);
        LOG.info("DamageProfiler v" + VERSION);


        // decompress input file if necessary
        if(input.endsWith(".gz")){
            Unzip unzip = new Unzip(LOG);
            input = unzip.decompress(input);
        }

        // create new output folder
        File file = new File(input);
        // log settings
        LOG.info("Analysis of file :" + file + "\n"
                + "Output folder :" + outfolder + "\n"
                + "Reference (optional) :" + reference + "\n"
                + "Reference (optional) :" + reference + "\n"
                + "Specie (optional):" + rname + "\n"
                + "Length: " + length + "\n"
                + "Threshold: " + threshold + "\n"
                + "Height yaxis: " + height);

        String inputfileNameWithOutExtension;
        if(c.getTitle_plots() == null ){
            inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
        } else {
            inputfileNameWithOutExtension = c.getTitle_plots();
        }
        String output_folder = createOutputFolder(outfolder);

        // create variables for tax id and gi ID if specie is set
        if(rname != null){
            String gi = "";
            SpecieHandler specieHandler = new SpecieHandler(gi, rname, LOG);
            specieHandler.getSpecie();
            specie_name = specieHandler.getSpecie_name();
            output_folder = createOutputFolder(output_folder);
        }

        // start DamageProfiler
        DamageProfiler damageProfiler = new DamageProfiler(
                file,
                new File(reference),
                threshold,
                length,
                rname,
                LOG
        );
        damageProfiler.extractSAMRecords(use_only_merged_reads);

        if(damageProfiler.getNumberOfUsedReads()!=0){
            // generate output files if more that 0 reads were processed
            OutputGenerator outputGenerator = new OutputGenerator(
                    output_folder,
                    damageProfiler,
                    specie_name,
                    threshold,
                    length,
                    height,
                    input,
                    LOG
            );
            outputGenerator.writeLengthDistribution();
            outputGenerator.writeDamageFiles(
                    damageProfiler.getFrequencies().getCount_G_A_3_norm(),
                    damageProfiler.getFrequencies().getCount_C_T_5_norm()
            );
            outputGenerator.writeFrequenciesReference(damageProfiler.getFrequencies());
            outputGenerator.writeDNAComp(damageProfiler.getFrequencies());
            outputGenerator.writeDNAcomp_genome(damageProfiler.getFrequencies());
            outputGenerator.writeMisincorporations(
                    damageProfiler.getFrequencies(),
                    threshold
            );


            // create DamagePlots of 3' and 5' ends
            outputGenerator.plotMisincorporations(inputfileNameWithOutExtension);
            outputGenerator.plotLengthHistogram(
                    damageProfiler.getLength_all(),
                    damageProfiler.getLength_forward(),
                    damageProfiler.getLength_reverse(),
                    inputfileNameWithOutExtension
            );
            outputGenerator.plotIdentitiyHistogram(
                    damageProfiler.getIdentity(),
                    "Identitiy distribution",
                    inputfileNameWithOutExtension
            );

            LOG.info("Output files generated");


            // print runtime
            long currtime_post_execution = System.currentTimeMillis();
            long diff = currtime_post_execution - currtime_prior_execution;
            long runtime_s = diff / 1000;
            if(runtime_s > 60) {
                long minutes = runtime_s / 60;
                long seconds = runtime_s % 60;
                LOG.info("Runtime of Module was: " + minutes + " minutes, and " + seconds + " seconds.");
            } else {
                LOG.info("Runtime of Module was: " + runtime_s + " seconds.");
            }

        } else {
            LOG.warn("No reads processed. Can't create any output");
        }

        calculationsDone=true;




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

    public boolean isCalculationsDone() {
        return calculationsDone;
    }
}
