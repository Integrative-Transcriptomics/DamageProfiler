package calculations;

import IO.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
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
                File file = new File(input);
                damageProfiler = new DamageProfiler(specieHandler);


                String ref = specie_input_string.split("\\|")[0].trim();
                String speciesname = damageProfiler.getSpeciesname(file, ref);

                String inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
                String output_folder = createOutputFolder(
                        outfolder,
                        inputfileNameWithOutExtension.split("/")[inputfileNameWithOutExtension.split("/").length - 1]
                                + File.separator + ref + "_" + speciesname);



                if (c.getTitle_plots() == null) {
                    inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
                } else {
                    inputfileNameWithOutExtension = c.getTitle_plots();
                }



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
                LOG.info("Analysis of file (-i):" + file + "\n"
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


                damageProfiler.init(file,
                        new File(reference),
                        threshold,
                        length,
                        specie_input_string,
                        LOG);

                damageProfiler.extractSAMRecords(use_only_merged_reads, use_all_reads);

                generateOutput(damageProfiler, output_folder, inputfileNameWithOutExtension, speciesname, ssLibProtocolUsed);
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
            String speciesname = damageProfiler.getSpeciesname(new File(input), species_ref_identifier);

            String inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));

            String output_folder = createOutputFolder(
                    outfolder,
                    inputfileNameWithOutExtension.split("/")[inputfileNameWithOutExtension.split("/").length - 1] +
                             File.separator + species_ref_identifier + "_" + speciesname);


            if (c.getTitle_plots() == null) {
                inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
            }
            else {
                inputfileNameWithOutExtension = c.getTitle_plots();
            }



            // init Logger
            initLogger(output_folder + "/DamageProfiler.log", "DamageProfiler v" + VERSION);

            // decompress input file if necessary
            if (input.endsWith(".gz")) {
                Unzip unzip = new Unzip(LOG);
                input = unzip.decompress(input);
            }

            // create new output folder
            File file = new File(input);
            // log settings
            LOG.info("Analysis of file (-i):" + file + "\n"
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




            damageProfiler.init(file,
                    new File(reference),
                    threshold,
                    length,
                    null,
                    LOG);

            damageProfiler.extractSAMRecords(use_only_merged_reads, use_all_reads);

            speciesListParser.setLOG(LOG);
            generateOutput(damageProfiler,output_folder, inputfileNameWithOutExtension, null, ssLibProtocolUsed);
        } else {

            /*
                    No species specified --> use all (mapping) reads
             */
            String inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));

            String output_folder = createOutputFolder(
                    outfolder,
                    inputfileNameWithOutExtension.split("/")[inputfileNameWithOutExtension.split("/").length - 1]);

            if (c.getTitle_plots() == null) {
                inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
            } else {
                inputfileNameWithOutExtension = c.getTitle_plots();
            }

            // init Logger
            initLogger(output_folder + "/DamageProfiler.log", "DamageProfiler v" + VERSION);

            // decompress input file if necessary
            if (input.endsWith(".gz")) {
                Unzip unzip = new Unzip(LOG);
                input = unzip.decompress(input);
            }

            // create new output folder
            File file = new File(input);
            // log settings
            LOG.info("Analysis of file (-i):" + file + "\n"
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

            damageProfiler.init(file,
                    new File(reference),
                    threshold,
                    length,
                    null,
                    LOG);
            damageProfiler.extractSAMRecords(use_only_merged_reads, use_all_reads);
            speciesListParser.setLOG(LOG);
            generateOutput(damageProfiler, output_folder, inputfileNameWithOutExtension, null, ssLibProtocolUsed);

        }


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

    private void initLogger(String outfolder, String log) {

        logClass = new LogClass();
        logClass.updateLog4jConfiguration(outfolder);
        logClass.setUp();

        LOG = logClass.getLogger(this.getClass());
        System.out.println("DamageProfiler v" + VERSION);
        LOG.info("DamageProfiler v" + VERSION);
        LOG.info(log);

    }

    private void generateOutput(
            DamageProfiler damageProfiler,
            String output_folder,
            String inputfileNameWithOutExtension,
            String spe,
            boolean ssLibProtocolUsed) throws IOException, DocumentException {

        if (damageProfiler.getNumberOfUsedReads() != 0) {

            outputGenerator = new OutputGenerator(
                    output_folder,
                    damageProfiler,
                    spe,
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
                    damageProfiler.getIdentity(),
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
    private static String createOutputFolder(String path, String inputfileNameWithOutExtension) {

        // use Pattern.quote(File.separator) to split file path
        File f = new File(path + File.separator + inputfileNameWithOutExtension);

        // create new output directory
        if (!f.isDirectory()) {
            f.mkdirs();
        }

        return f.getAbsolutePath();
    }


    public boolean isCalculationsDone() {
        return calculationsDone;
    }

    public void setCalculationsDone(boolean calculationsDone) {
        this.calculationsDone = calculationsDone;
    }

    public DamageProfiler getDamageProfiler(){
        return damageProfiler;
    }

    public OutputGenerator getOutputGenerator() {
        return outputGenerator;
    }
}
