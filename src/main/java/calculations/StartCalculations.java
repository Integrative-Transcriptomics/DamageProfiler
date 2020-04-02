package calculations;

import IO.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import javafx.scene.paint.Color;
import org.apache.log4j.*;


/**
 * Created by neukamm on 11.11.2016.
 */
public class StartCalculations {

    private String VERSION;
    private LogClass logClass;
    private long currtime_prior_execution;  // start time to get overall runtime
    private boolean calculationsDone = false;
    private Logger LOG;
    private List<String> specieslist = null;
    private List<String> species_name_list;
    private SpeciesListParser speciesListParser;
    private boolean use_only_merged_reads;
    private double height_damageplot; //= 0.4; // set yaxis height to 40% as default
    private int threshold;
    private int length;
    private String specieslist_filepath;
    private String species_ref_identifier;
    private String reference;
    private String outfolder;
    private String input;
    private SpecieHandler specieHandler;
    private boolean use_all_reads;
    private double xaxis_min_id_histogram;
    private double xaxis_max_id_histogram;
    private double xaxis_min_length_histogram;
    private double xaxis_max_length_histogram;
    private boolean ssLibProtocolUsed;
    private DamageProfiler damageProfiler;
    private OutputGenerator outputGenerator;
    private String inputfileNameWithOutExtension;
    private Communicator communicator;


    // plot settings
    private Color color_DP_C_to_T;
    private Color color_DP_G_to_A;
    private Color color_DP_insertions;
    private Color color_DP_deletions;
    private Color color_DP_other;
    private String output_folder;
    private String speciesname = null;


    public StartCalculations(){

    }

    public void setVERSION(String version){
        VERSION = version;
    }
    public void start(Communicator c) throws Exception {

        currtime_prior_execution = System.currentTimeMillis();
        input = c.getInput();
        outfolder = c.getOutfolder();
        reference = c.getReference();
        species_ref_identifier = c.getSpecies_ref_identifier();
        specieslist_filepath = c.getSpecieslist_filepath();
        length = c.getLength();
        threshold = c.getThreshold();
        height_damageplot = c.getyAxis_damageplot();
        xaxis_min_id_histogram = c.getXaxis_histo_id_min();
        xaxis_max_id_histogram = c.getXaxis_histo_id_max();
        xaxis_min_length_histogram = c.getXaxis_histo_length_min();
        xaxis_max_length_histogram = c.getXaxis_histo_length_max();
        use_only_merged_reads = c.isUse_merged_and_mapped_reads();
        use_all_reads = c.isUse_all_reads();
        ssLibProtocolUsed = c.isSsLibsProtocolUsed();
        speciesListParser=null;
        species_name_list=null;

        color_DP_C_to_T = c.getColor_DP_C_to_T();
        color_DP_G_to_A = c.getColor_DP_G_to_A();
        color_DP_insertions = c.getColor_DP_insertions();
        color_DP_deletions = c.getColor_DP_deletions();
        color_DP_other = c.getColor_DP_other();


        this.inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
        this.communicator = c;

        SpeciesListParser speciesListParser = new SpeciesListParser(
                specieslist_filepath,
                LOG
        );


        if(specieslist_filepath != null){

            /*
                parse species references (-sf) and run DP for each reference in the file
             */
            specieslist = new ArrayList<>();
            specieslist.addAll(speciesListParser.getList());


            for (int i = 0; i < specieslist.size(); i++) {
                String specie_input_string = specieslist.get(i);
                //String specie_name = species_real_name_list.get(i);



                // start DamageProfiler
                damageProfiler = new DamageProfiler(specieHandler);


                String ref = specie_input_string.split("\\|")[0].trim();
                speciesname = damageProfiler.getSpeciesname(new File(input), ref);


                createOutputFolder(
                        outfolder,
                        inputfileNameWithOutExtension.split("/")[inputfileNameWithOutExtension.split("/").length - 1]
                                + File.separator + ref + "_" + speciesname);

                initPlot();

                // init Logger
                initLogger(output_folder + "/DamageProfiler_" + ref + "_" + speciesname +".log",
                        "Calculate damage profile for species " + ref + " (" + speciesname + ")");

                // decompress input file if necessary
                if (input.endsWith(".gz")) {
                    Unzip unzip = new Unzip(LOG);
                    input = unzip.decompress(input);
                }

                // create new output folder
                // log settings
                LOG.info("\nAnalysis of file (-i):" + input + "\n"
                        + "Output folder (-o):" + output_folder + "\n"
                        + "Reference (-r, optional) :" + reference + "\n"
                        + "Specie (-s, optional):" + specie_input_string + "\n"
                        + "Species list (-sf, optional):" + c.getSpecieslist_filepath() + "\n"
                        + "Length (-l): " + length + "\n"
                        + "Threshold (-t): " + threshold + "\n"
                        + "Height yaxis (-yaxis): " + height_damageplot + "\n"
                        + "x-axis min ID histogram (-xaxis_histo_id_min): " + xaxis_min_id_histogram + "\n"
                        + "x-axis max ID histogram (-xaxis_histo_id_max): " + xaxis_max_id_histogram + "\n"
                        + "x-axis min length histogram (-xaxis_histo_length_min): " + xaxis_min_length_histogram + "\n"
                        + "x-axis max length histogram (-xaxis_histo_length_max): " + xaxis_max_length_histogram + "\n");


                damageProfiler.init(new File(input),
                        new File(reference),
                        threshold,
                        length,
                        specie_input_string,
                        LOG);

                damageProfiler.extractSAMRecords(use_only_merged_reads, use_all_reads);

            }


        } else if(species_ref_identifier != null){

            // start DamageProfiler
            damageProfiler = new DamageProfiler(

                    specieHandler);

            /*
                parse species reference (-s) and run DP
             */

            this.specieslist = new ArrayList<>();
            specieslist.add(species_ref_identifier);
            speciesname = damageProfiler.getSpeciesname(new File(input), species_ref_identifier);

            String inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));

            createOutputFolder(
                    outfolder,
                    inputfileNameWithOutExtension.split("/")[inputfileNameWithOutExtension.split("/").length - 1] +
                             File.separator + species_ref_identifier + "_" + speciesname);

            initPlot();

            // init Logger
            initLogger(output_folder + "/DamageProfiler.log", "DamageProfiler v" + VERSION);

            // decompress input file if necessary
            if (input.endsWith(".gz")) {
                Unzip unzip = new Unzip(LOG);
                input = unzip.decompress(input);
            }

            // log settings
            LOG.info("Analysis of file (-i):" + input + "\n"
                    + "Output folder (-o):" + output_folder + "\n"
                    + "Reference (-r, optional) :" + reference + "\n"
                    + "Specie (-s, optional):" + specieslist + "\n"
                    + "Species list (-sf, optional):" + specieslist_filepath + "\n"
                    + "Length (-l): " + length + "\n"
                    + "Threshold (-t): " + threshold + "\n"
                    + "Height yaxis (-yaxis): " + height_damageplot  + "\n"
                    + "x-axis min ID histogram (-xaxis_histo_min): " + xaxis_min_id_histogram + "\n"
                    + "x-axis max ID histogram (-xaxis_histo_max): " + xaxis_max_id_histogram + "\n"
                    + "x-axis min length histogram (-xaxis_histo_length_min): " + xaxis_min_length_histogram + "\n"
                    + "x-axis max length histogram (-xaxis_histo_length_max): " + xaxis_max_length_histogram + "\n");




            damageProfiler.init(new File(input),
                    new File(reference),
                    threshold,
                    length,
                    null,
                    LOG);

            damageProfiler.extractSAMRecords(use_only_merged_reads, use_all_reads);
            speciesListParser.setLOG(LOG);

        } else {

            /*
                    No species specified --> use all (mapping) reads
             */
            String inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));

            createOutputFolder(
                    outfolder,
                    inputfileNameWithOutExtension.split("/")[inputfileNameWithOutExtension.split("/").length - 1]);

            initPlot();

            // init Logger
            initLogger(output_folder + "/DamageProfiler.log", "DamageProfiler v" + VERSION);

            // decompress input file if necessary
            if (input.endsWith(".gz")) {
                Unzip unzip = new Unzip(LOG);
                input = unzip.decompress(input);
            }

            // create new output folder

            // log settings
            LOG.info("Analysis of file (-i):" + input + "\n"
                    + "Output folder (-o):" + output_folder + "\n"
                    + "Reference (-r, optional) :" + reference + "\n"
                    + "Specie (-s, optional):" + specieslist + "\n"
                    + "Species list (-sf, optional):" + c.getSpecieslist_filepath() + "\n"
                    + "Length (-l): " + length + "\n"
                    + "Threshold (-t): " + threshold + "\n"
                    + "Height yaxis (-yaxis): " + height_damageplot + "\n"
                    + "x-axis min ID histogram (-xaxis_histo_min): " + xaxis_min_id_histogram + "\n"
                    + "x-axis max ID histogram (-xaxis_histo_max): " + xaxis_max_id_histogram + "\n"
                    + "x-axis min length histogram (-xaxis_histo_length_min): " + xaxis_min_length_histogram + "\n"
                    + "x-axis max length histogram (-xaxis_histo_length_max): " + xaxis_max_length_histogram + "\n");



            // start DamageProfiler
            damageProfiler = new DamageProfiler(specieHandler);

            damageProfiler.init(new File(input),
                    new File(reference),
                    threshold,
                    length,
                    null,
                    LOG);
            damageProfiler.extractSAMRecords(use_only_merged_reads, use_all_reads);
            speciesListParser.setLOG(LOG);



        }

        generateOutput();


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

        calculationsDone=true;

    }

    private void initPlot() {
        if (communicator.getTitle_plots() == null) {
            inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
        } else {
            inputfileNameWithOutExtension = communicator.getTitle_plots();
        }

    }

    private void initLogger(String outfolder, String log) {

        logClass = new LogClass();
        logClass.updateLog4jConfiguration(outfolder);
        logClass.setUp();

        LOG = logClass.getLogger(this.getClass());
        System.out.println("DamageProfiler v" + VERSION);
        LOG.info(log);

    }

    private void generateOutput() throws IOException, DocumentException {

        if (damageProfiler.getNumberOfUsedReads() != 0) {

            outputGenerator = new OutputGenerator(
                    output_folder,
                    damageProfiler,
                    speciesname,
                    threshold,
                    length,
                    height_damageplot,
                    xaxis_min_id_histogram,
                    xaxis_max_id_histogram,
                    xaxis_min_length_histogram,
                    xaxis_max_length_histogram,
                    input,
                    LOG,
                    damageProfiler.getNumberOfRecords(),
                    ssLibProtocolUsed
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

            outputGenerator.computeSummaryMetrics();

            outputGenerator.writeJSON(VERSION);


            // create DamagePlots of 3' and 5' ends
            outputGenerator.plotMisincorporations(inputfileNameWithOutExtension);
            outputGenerator.plotLengthHistogram(
                    damageProfiler.getLength_all(),
                    damageProfiler.getLength_forward(),
                    damageProfiler.getLength_reverse(),
                    inputfileNameWithOutExtension
            );
            outputGenerator.plotIdentitiyHistogram(
                    damageProfiler.getEditDistances(), // damageProfiler.getIdentity(),
                    "Identity distribution",
                    inputfileNameWithOutExtension
            );

            LOG.info("Output files generated");

        } else {
            LOG.warn("No reads processed. Can't create any output");
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
    private void createOutputFolder(String path, String inputfileNameWithOutExtension) {

        // use Pattern.quote(File.separator) to split file path
        File f = new File(path + File.separator + inputfileNameWithOutExtension);

        // create new output directory
        if (!f.isDirectory()) {
            f.mkdirs();
        }

        this.output_folder = f.getAbsolutePath();
    }


    public boolean isCalculationsDone() {
        return calculationsDone;
    }

    public void setCalculationsDone(boolean calculationsDone) {
        this.calculationsDone = calculationsDone;
    }

    public OutputGenerator getOutputGenerator() {
        return outputGenerator;
    }
}
