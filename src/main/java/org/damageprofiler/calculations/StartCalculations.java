package org.damageprofiler.calculations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private double height_damageplot; // set y-axis height to 40% as default
    private int threshold;
    private int length;
    private String specieslist_filepath;
    private String reference;
    private String outfolder;
    private String input;
    private boolean use_all_reads;
    private boolean ssLibProtocolUsed;
    private DamageProfiler damageProfiler;
    private OutputGenerator outputGenerator;
    private String inputfileNameWithOutExtension;
    private Communicator communicator;
    private IndexedFastaSequenceFile fastaSequenceFile;
    private FastACacher cache;



    // plot settings
    private Color color_DP_C_to_T;
    private Color color_DP_G_to_A;
    private Color color_DP_insertions;
    private Color color_DP_deletions;
    private Color color_DP_other;
    private String output_folder;
    private SpeciesListParser speciesListParser;


    public StartCalculations(){

    }

    public void setVERSION(String version){
        VERSION = version;
    }

    /**
     * Start all calculations.
     * @param c
     * @throws Exception
     */
    public void start(Communicator c) throws Exception {

        input = c.getInput();
        outfolder = c.getOutfolder();
        reference = c.getReference();
        String species_minus_s = c.getSpecies_ref_identifier();
        specieslist_filepath = c.getSpecieslist_filepath();
        length = c.getLength();
        threshold = c.getThreshold();
        height_damageplot = c.getyAxis_damageplot();
        use_only_merged_reads = c.isUse_merged_and_mapped_reads();
        use_all_reads = c.isUse_all_reads();
        ssLibProtocolUsed = c.isSsLibsProtocolUsed();

        color_DP_C_to_T = c.getColor_DP_C_to_T();
        color_DP_G_to_A = c.getColor_DP_G_to_A();
        color_DP_insertions = c.getColor_DP_insertions();
        color_DP_deletions = c.getColor_DP_deletions();
        color_DP_other = c.getColor_DP_other();

        this.inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
        this.communicator = c;

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

        // create output folder based on file name
        if (outfolder != null){
            createOutputFolder(outfolder, species);
        }

        if (communicator.getTitle_plots() == null) {
            inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
        } else {
            inputfileNameWithOutExtension = communicator.getTitle_plots();
        }

        // init Logger
        initLogger(output_folder + "/DamageProfiler.log", "DamageProfiler v" + VERSION);

        // log settings
        LOG.info("Analysis of file (-i):" + input + "\n"
                + "Output folder (-o):" + output_folder + "\n"
                + "Reference (-r, optional) :" + reference + "\n"
                + "Species (-s, optional): " + species + "\n"
                + "Species list (-sf, optional):" + specieslist_filepath + "\n"
                + "Length (-l): " + length + "\n"
                + "Threshold (-t): " + threshold + "\n"
                + "Height y-axis (-yaxis): " + height_damageplot);

        damageProfiler = new DamageProfiler(cache);

        damageProfiler.init(new File(input),
                new File(reference),
                threshold,
                length,
                species,
                LOG);
        damageProfiler.extractSAMRecords(use_only_merged_reads, use_all_reads);
        if(damageProfiler.getNumberOfUsedReads() != 0) {
            speciesListParser.setLOG(LOG);

            generateOutput(species);
        } else {
            System.err.println("No reads found for " + specieslist[0]);
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
                }

            } else {

                HashMap<String, List<JFreeChart>> species_output_summary = new HashMap<>();
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
                                    outputGenerator.getLength_chart_all())
                    );

                    number_of_used_reads_summary.put(
                            spec_no_space,
                            damageProfiler.getNumberOfUsedReads());
                }

                // generate metagenomic summary output
                MetagenomicOutput metagenomicOutput = new MetagenomicOutput();
                String[] splitted = input.split("/");
                String filename = splitted[splitted.length-1];
                metagenomicOutput.generate(
                        outfolder,
                        species_output_summary,
                        filename,
                        number_of_used_reads_summary
                );

            }

        } else {
            System.err.println("Species list is empty. Please check '-s' or '-sf' parameter.");
        }
    }


    /**
     * Initialize log file
     *
     * @param outfolder
     * @param log
     */
    private void initLogger(String outfolder, String log) {

        LogClass logClass = new LogClass();
        logClass.updateLog4jConfiguration(outfolder);
        logClass.setUp();

        LOG = logClass.getLogger(this.getClass());
        System.out.println("DamageProfiler v" + VERSION);
        LOG.info(log);

    }

    /**
     * Generate output files.
     *
     * @throws IOException
     * @throws DocumentException
     */
    private void generateOutput(String speciesID) throws IOException, DocumentException {

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
                    damageProfiler.getNumberOfRecords()
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
            outputGenerator.plotMisincorporations(inputfileNameWithOutExtension);
            outputGenerator.plotLengthHistogram(
                    damageProfiler.getLength_all(),
                    damageProfiler.getLength_forward(),
                    damageProfiler.getLength_reverse(),
                    inputfileNameWithOutExtension
            );
            outputGenerator.plotEditDistanceHistogram(
                    damageProfiler.getEditDistances(),
                    "Identity distribution",
                    inputfileNameWithOutExtension
            );
            outputGenerator.writeEditDistance(damageProfiler.getEditDistances());

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
}
