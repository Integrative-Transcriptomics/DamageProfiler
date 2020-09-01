package org.damageprofiler.calculations;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.DocumentException;
import htsjdk.samtools.reference.IndexedFastaSequenceFile;
import javafx.scene.paint.Color;
import org.apache.log4j.*;
import org.damageprofiler.io.*;
import org.jfree.chart.JFreeChart;


/**
 * Created by neukamm on 11.11.2016.
 */
public class StartCalculations {

    private String VERSION;
    private boolean calculationsDone = false;
    private Logger LOG;
    private String[] specieslist = null;
    private boolean use_only_merged_reads;
    private double height_damageplot;
    private int threshold;
    private int length;
    private String specieslist_filepath;
    private String reference;
    private String outfolder;
    private String input;
    private boolean ssLibProtocolUsed;
    private DamageProfiler damageProfiler;
    private OutputGenerator outputGenerator;
    private String inputfileNameWithOutExtension;
    private Communicator communicator;
    private IndexedFastaSequenceFile fastaSequenceFile;
    private FastACacher cache;
    private SpeciesListParser speciesListParser;
    private HashMap<String, List<JFreeChart>> species_output_summary;



    // plot settings
    private Color color_DP_C_to_T;
    private Color color_DP_G_to_A;
    private Color color_DP_insertions;
    private Color color_DP_deletions;
    private Color color_DP_other;
    private String output_folder;
    private String title;


    public StartCalculations(String version){
        VERSION = version;
    }


    /**
     * Start all calculations.
     * @param c Communicator containing all input parameters
     * @throws Exception
     */
    public void start(Communicator c) throws Exception {
        communicator = c;
        input = c.getInput();
        outfolder = c.getOutfolder();
        reference = c.getReference();
        String species_minus_s = c.getSpecies_ref_identifier();
        specieslist_filepath = c.getSpecieslist_filepath();
        length = c.getLength();
        threshold = c.getThreshold();
        height_damageplot = c.getyAxis_damageplot();
        use_only_merged_reads = c.isUse_merged_and_mapped_reads();
        ssLibProtocolUsed = c.isSsLibsProtocolUsed();

        color_DP_C_to_T = c.getColor_DP_C_to_T();
        color_DP_G_to_A = c.getColor_DP_G_to_A();
        color_DP_insertions = c.getColor_DP_insertions();
        color_DP_deletions = c.getColor_DP_deletions();
        color_DP_other = c.getColor_DP_other();


        if (c.getTitle_plots() == null) {
            inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));

            String[] splitted = inputfileNameWithOutExtension.split("/");
            title = splitted[splitted.length-1];

        } else {
            inputfileNameWithOutExtension = communicator.getTitle_plots();
            title = communicator.getTitle_plots();
        }

        initLogger(outfolder + "/DamageProfiler.log");
        System.out.println("Welcome to DamageProfiler v" + VERSION + "\n");
        LOG.info(" ///////////////////////////////////////////////////////////////////");
        LOG.info(" //////////////\t\t\t\t\t/////////////");
        LOG.info("//////////////\tWelcome to DamageProfiler v" + VERSION + "\t/////////////");
        LOG.info("//////////////\t\t\t\t\t/////////////");
        LOG.info("///////////////////////////////////////////////////////////////////\n");


        // log settings
        LOG.info("Parameters: \n");
        LOG.info("\tAnalysis of file (-i):\t\t" + input);
        LOG.info("\tOutput folder (-o):\t\t" + output_folder);
        LOG.info("\tReference (-r, optional):\t" + reference);
        LOG.info("\tSpecies (-s, optional):\t\t" + species_minus_s);
        LOG.info("\tSpecies list (-sf, optional):\t" + specieslist_filepath);
        LOG.info("\tLength (-l):\t\t\t" + length);
        LOG.info("\tThreshold (-t):\t\t\t" + threshold);
        LOG.info("\tHeight y-axis (-yaxis):\t\t" + height_damageplot);
        LOG.info("\tColor C->T:\t\t\t" + color_DP_C_to_T);
        LOG.info("\tColor G->A:\t\t\t" + color_DP_G_to_A);
        LOG.info("\tColor insertions:\t\t" + color_DP_insertions);
        LOG.info("\tColor deletions:\t\t" + color_DP_deletions);
        LOG.info("\tColor other:\t\t\t" + color_DP_other);
        LOG.info("\tTitle:\t\t\t\t" + title);
        LOG.info("\tssLib protocol used:\t\t" + communicator.isSsLibsProtocolUsed());
        LOG.info("\tUse only merged reads:\t\t" + communicator.isUse_merged_and_mapped_reads() + "\n");

        this.inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));


        speciesListParser = new SpeciesListParser(
                specieslist_filepath,
                LOG
        );

        if(!this.reference.equals(""))
            readReferenceInCache();

        /*
                If -s or -sf parameter is set, parse list and run DamageProfiler
                on each species
         */

        if(specieslist_filepath != null){
            List<String> specieslist_tmp = new ArrayList<>(speciesListParser.getSpeciesList());
            specieslist = new String[specieslist_tmp.size()];
            specieslist_tmp.toArray(specieslist);

            parseSpeciesAndRun();
        } else if (species_minus_s != null) {
            //species_minus_s = species_minus_s.substring(1,species_minus_s.length()-1);
            this.specieslist = species_minus_s.split(",");
            parseSpeciesAndRun();
        } else {
            // Neither parameter -s not -sf are set. Use all mapped reads
            startDamageProfilerOnSingleSpecies(null);
        }
        calculationsDone=true;
    }




    /**
     * Start damage patterns calculations for a given species
     * @param species
     * @throws IOException
     * @throws DocumentException
     */
    private void startDamageProfilerOnSingleSpecies(String species) throws IOException, DocumentException {

        long startTime = System.currentTimeMillis();
        DecimalFormat df = new DecimalFormat("##,###.##");

        // create output folder based on file name
        if (outfolder != null){
            createOutputFolder(outfolder, species);
        }




        if (species != null){
            LOG.info("Starting analysis for species " + species + "\n");
        } else {
            LOG.info("Starting analysis\n");
        }

        damageProfiler = new DamageProfiler(cache);

        damageProfiler.init(new File(input),
                new File(reference),
                threshold,
                length,
                species,
                LOG);

        damageProfiler.extractSAMRecords(use_only_merged_reads);
        if(damageProfiler.getNumberOfUsedReads() != 0) {
            speciesListParser.setLOG(LOG);

            generateOutput(species, title);

            LOG.info("Statistics:");
            LOG.info("\t# of total reads in input file:\t\t" + df.format(damageProfiler.getNumberOfRecords()));
            LOG.info("\t# reads used for damage calculation:\t" + df.format(damageProfiler.getNumberOfUsedReads()) +
                    " (" + (damageProfiler.getNumberOfRecords()/(double)damageProfiler.getNumberOfUsedReads())*100 +
                    "%)\n");
            LOG.info("\tLength distribution:");
            LOG.info("\t\t- mean\t\t" + df.format(outputGenerator.getMeanLength()));
            LOG.info("\t\t- median\t" + df.format(outputGenerator.getMedian_length_dist()));
            LOG.info("\t\t- std\t\t" + df.format(outputGenerator.getStd_length_dist()) + "\n\n");

            LOG.info("FINISHED SUCCESSFULLY: Output files generated and saved: " + output_folder);

        } else {
            System.err.println("No reads found for " + specieslist[0]);
            LOG.error("No reads found for " + specieslist[0]);
        }


        long endTime = System.currentTimeMillis();
        long time_in_ms = (endTime - startTime);



        if(time_in_ms > 1000){
            double time_in_sec = time_in_ms/1000.0;
            if(time_in_sec > 60){
                double time_in_min = time_in_sec/60.0;
                if(species != null){
                    LOG.info("Runtime of DamageProfiler for species " + species + ":\t" + df.format(time_in_min) + " minutes\n");
                } else {
                    LOG.info("Runtime of DamageProfiler:\t" + df.format(time_in_min) + " minutes\n");
                }

            } else {
                if(species != null){
                    LOG.info("Runtime of DamageProfiler for species " + species + ":\t" +  df.format(time_in_sec) + " seconds\n");
                } else {
                    LOG.info("Runtime of DamageProfiler:\t" +  df.format(time_in_sec) + " seconds\n");
                }
            }
        } else {
            if(species != null){
                LOG.info("Runtime of DamageProfiler for species " + species + ":\t" +   df.format(time_in_ms) + " milliseconds\n");
            } else {
                LOG.info("Runtime of DamageProfiler:\t" +   df.format(time_in_ms) + " milliseconds\n");
            }
        }


    }


    /**
     * Iterate over species list and generate damage patterns for each species.
     *
     * @throws IOException
     * @throws DocumentException
     */
    private void parseSpeciesAndRun() throws IOException, DocumentException {
        if(specieslist.length > 0){

            // if only one species is in list -> run 'normal' DP
            if (specieslist.length == 1){

                // start DamageProfiler
                String speciesID = specieslist[0];

                Pattern p = Pattern.compile("[0-9A-Za-z!#$%&+./:;?@^_|~-]");
                Matcher m = p.matcher(speciesID);

                if (m.find()) {
                    startDamageProfilerOnSingleSpecies(speciesID);
                } else {
                    System.err.println("Reference ID " + speciesID + " doesn't match regex: " + p);
                    LOG.error("Reference ID " + speciesID + " doesn't match regex: " + p);
                }

            } else {

                species_output_summary = new HashMap<>();
                HashMap<String, Integer>  number_of_used_reads_summary = new HashMap<>();

                for (String specie_input_string : specieslist) {

                    // start DamageProfiler
                    startDamageProfilerOnSingleSpecies(specie_input_string);

                    // collect info for metagenomic summary output
                    String spec_no_space = specie_input_string.replace(" ", "_");
                    species_output_summary.put(spec_no_space,
                            List.of(outputGenerator.getChart_DP_5prime(),
                                    outputGenerator.getChart_DP_3prime(),
                                    outputGenerator.getEditDist_chart(),
                                    outputGenerator.getLength_chart_all(),
                                    outputGenerator.getLength_chart_sep())
                    );

                    number_of_used_reads_summary.put(
                            spec_no_space,
                            damageProfiler.getNumberOfUsedReads());
                }

                // generate metagenomic summary output
                MetagenomicOutput metagenomicOutput = new MetagenomicOutput(LOG);
                String[] splitted = input.split("/");
                String filename = splitted[splitted.length-1];
                metagenomicOutput.generate(
                        outfolder,
                        species_output_summary,
                        filename,
                        number_of_used_reads_summary,
                        specieslist
                );


            }

        } else {
            System.err.println("Species list is empty. Please check '-s' or '-sf' parameter.");
            LOG.error("Species list is empty. Please check '-s' or '-sf' parameter.");
        }
    }


    /**
     * Initialize log file
     *
     * @param outfolder
     */
    private void initLogger(String outfolder) {
        LogClass logClass = new LogClass();
        logClass.updateLog4jConfiguration(outfolder);
        logClass.setUp();

        LOG = logClass.getLogger(this.getClass());
    }

    /**
     * Generate output files.
     *
     * @throws IOException
     * @throws DocumentException
     */
    private void generateOutput(String speciesID, String title) throws IOException, DocumentException {

        if (damageProfiler.getNumberOfUsedReads() != 0) {

            outputGenerator = new OutputGenerator(
                    output_folder,
                    damageProfiler,
                    speciesID,
                    threshold,
                    length,
                    height_damageplot,
                    input,
                    LOG,
                    ssLibProtocolUsed,
                    color_DP_C_to_T,
                    color_DP_deletions,
                    color_DP_G_to_A,
                    color_DP_insertions,
                    color_DP_other,
                    damageProfiler.getNumberOfRecords(),
                    title
            );

            outputGenerator.writeLengthDistribution();
            outputGenerator.writeDamageFiles(
                    damageProfiler.getFrequencies().getCount_G_A_3_norm(),
                    damageProfiler.getFrequencies().getCount_C_T_5_norm()
            );
            outputGenerator.writeFrequenciesReference(damageProfiler.getFrequencies(), damageProfiler.getReferenceName());
            outputGenerator.writeDNAComp(damageProfiler.getFrequencies(), damageProfiler.getReferenceName());
            outputGenerator.writeDNAcomp_genome();
            outputGenerator.writeMisincorporations(
                    damageProfiler.getFrequencies(),
                    threshold
            );

            outputGenerator.computeSummaryMetrics();

            outputGenerator.writeJSON(VERSION);


            // create DamagePlots of 3' and 5' ends
            outputGenerator.plotMisincorporations();
            outputGenerator.plotLengthHistogram(
                    damageProfiler.getLength_all(),
                    damageProfiler.getLength_forward(),
                    damageProfiler.getLength_reverse()
            );
            outputGenerator.plotEditDistanceHistogram(
                    damageProfiler.getEditDistances(),
                    "Identity distribution"
            );
            outputGenerator.writeEditDistance(damageProfiler.getEditDistances());


        } else {
            LOG.warn("No reads processed. Can't create any output");
        }
    }



    /**
     * create output folder.
     * Save all files in subfolder, which has the same name as the input file
     * (without extension)
     *
     * @param path_ourDir path to result directory (user specified)
     * @param species species name and reference ID
     * @throws IOException
     */
    private void createOutputFolder(String path_ourDir, String species) {
        File f;
        if(species==null){
            f = new File(path_ourDir);
        } else {
            f = new File(path_ourDir + File.separator + species);
        }

        // create new output directory
        if (!f.isDirectory()) {
            f.mkdirs();
        }

        this.output_folder = f.getAbsolutePath();
    }

    /**
     * index reference file and put it in cache to get faster
     * access
     *
     * @throws FileNotFoundException
     */

    private void readReferenceInCache() throws FileNotFoundException{
        // read reference file as indexed reference
        fastaSequenceFile = new IndexedFastaSequenceFile(new File(this.reference));
        // store reference in cache to get faster access
        cache = new FastACacher(new File(reference), LOG);

    }

    /*
        Getter and Setter
     */
    public boolean isCalculationsDone() {
        return calculationsDone;
    }

    public void setCalculationsDone(boolean calculationsDone) {
        this.calculationsDone = calculationsDone;
    }

    public OutputGenerator getOutputGenerator() {
        return outputGenerator;
    }

    public String[] getSpecieslist() {
        return specieslist;
    }

    public HashMap<String, List<JFreeChart>> getSpecies_output_summary() {
        return species_output_summary;
    }
}
