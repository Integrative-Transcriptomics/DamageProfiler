package org.damageprofiler.calculations;

import com.itextpdf.text.DocumentException;
import htsjdk.samtools.reference.IndexedFastaSequenceFile;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;
import org.damageprofiler.io.*;
import org.damageprofiler.services.Version;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Created by neukamm on 11.11.2016. */
public class StartCalculations {

  private boolean calculationsDone = false;
  private Logger logger;
  private String[] specieslist = null;
  private boolean use_only_merged_reads;
  private double height_damageplot;
  private int threshold;
  private int length;
  private String reference;
  private String outfolder;
  private String input;
  private boolean ssLibProtocolUsed;
  private DamageProfiler damageProfiler;
  private OutputGenerator outputGenerator;
  private FastACacher cache;
  private HashMap<String, List<JFreeChart>> species_output_summary;

  // plot settings
  private Color color_DP_C_to_T;
  private Color color_DP_G_to_A;
  private Color color_DP_insertions;
  private Color color_DP_deletions;
  private Color color_DP_other;
  private String output_folder;
  private String title;

  public StartCalculations() {}

  public void start(final Communicator c) throws Exception {
    input = c.getInput();
    outfolder = c.getOutfolder();
    reference = c.getReference();
    final String species_minus_s = c.getSpecies_ref_identifier();
    final String specieslist_filepath = c.getSpecieslist_filepath();
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
      final String inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
      final String[] splitted = inputfileNameWithOutExtension.split("/");
      title = splitted[splitted.length - 1];

    } else {
      title = c.getTitle_plots();
    }

    initLogger(outfolder + "/DamageProfiler.log");
    System.out.println("Welcome to DamageProfiler v" + Version.VERSION.getValue());
    System.out.println("\tInput file:" + input);
    System.out.println("\tOutput folder:" + outfolder);
    logger.info("Welcome to DamageProfiler v" + Version.VERSION.getValue() + "\n");

    // log settings
    logger.info("Parameters: \n");
    logger.info("\tAnalysis of file (-i):\t\t" + input);
    logger.info("\tOutput folder (-o):\t\t" + output_folder);
    logger.info("\tReference (-r, optional):\t" + reference);
    logger.info("\tSpecies (-s, optional):\t\t" + species_minus_s);
    logger.info("\tSpecies list (-sf, optional):\t" + specieslist_filepath);
    logger.info("\tLength (-l):\t\t\t" + length);
    logger.info("\tThreshold (-t):\t\t\t" + threshold);
    logger.info("\tHeight y-axis (-yaxis):\t\t" + height_damageplot);
    logger.info("\tColor C->T:\t\t\t" + color_DP_C_to_T);
    logger.info("\tColor G->A:\t\t\t" + color_DP_G_to_A);
    logger.info("\tColor insertions:\t\t" + color_DP_insertions);
    logger.info("\tColor deletions:\t\t" + color_DP_deletions);
    logger.info("\tColor other:\t\t\t" + color_DP_other);
    logger.info("\tTitle:\t\t\t\t" + title);
    logger.info("\tssLib protocol used:\t\t" + c.isSsLibsProtocolUsed());
    logger.info("\tUse only merged reads:\t\t" + c.isUse_merged_and_mapped_reads() + "\n");


    final SpeciesListParser speciesListParser = new SpeciesListParser(specieslist_filepath);

    if (!this.reference.equals("")) readReferenceInCache();

    /*
           If -s or -sf parameter is set, parse list and run DamageProfiler
           on each species
    */

    if (specieslist_filepath != null) {
      final List<String> specieslist_tmp = new ArrayList<>(speciesListParser.getSpeciesList());
      specieslist = new String[specieslist_tmp.size()];
      specieslist_tmp.toArray(specieslist);

      parseSpeciesAndRun();
    } else if (species_minus_s != null) {
      // species_minus_s = species_minus_s.substring(1,species_minus_s.length()-1);
      this.specieslist = species_minus_s.split(",");
      parseSpeciesAndRun();
    } else {
      // Neither parameter -s not -sf are set. Use all mapped reads
      startDamageProfilerOnSingleSpecies(null);
    }
    calculationsDone = true;
  }

  private void startDamageProfilerOnSingleSpecies(final String species)
      throws IOException, DocumentException {

    final long startTime = System.currentTimeMillis();
    final DecimalFormat df = new DecimalFormat("##,###.##");

    // create output folder based on file name
    if (outfolder != null) {
      createOutputFolder(outfolder, species);
    }

    if (species != null) {
      logger.info("Starting analysis for species " + species + "\n");
    } else {
      logger.info("Starting analysis\n");
    }

    damageProfiler = new DamageProfiler(cache);

    damageProfiler.init(new File(input), new File(reference), threshold, length, species, logger);

    damageProfiler.extractSAMRecords(use_only_merged_reads);
    if (damageProfiler.getNumberOfUsedReads() != 0) {
      generateOutput(species, title);

      logger.info("Statistics:");
      logger.info(
          "\t# of total reads in input file:\t\t" + df.format(damageProfiler.getNumberOfRecords()));
      logger.info(
          "\t# reads used for damage calculation:\t"
              + df.format(damageProfiler.getNumberOfUsedReads())
              + " ("
              + (damageProfiler.getNumberOfRecords()
                      / (double) damageProfiler.getNumberOfUsedReads())
                  * 100
              + "%)\n");
      logger.info("\tLength distribution:");
      logger.info("\t\t- mean\t\t" + df.format(outputGenerator.getMeanLength()));
      logger.info("\t\t- median\t" + df.format(outputGenerator.getMedian_length_dist()));
      logger.info("\t\t- std\t\t" + df.format(outputGenerator.getStd_length_dist()) + "\n\n");

      logger.info("FINISHED SUCCESSFULLY: Output files generated and saved: " + output_folder);

    } else {
      System.err.println("No reads found for " + specieslist[0]);
      logger.error("No reads found for " + specieslist[0]);
    }

    final long endTime = System.currentTimeMillis();
    final long time_in_ms = (endTime - startTime);

    if (time_in_ms > 1000) {
      final double time_in_sec = time_in_ms / 1000.0;
      if (time_in_sec > 60) {
        final double time_in_min = time_in_sec / 60.0;
        if (species != null) {
          logger.info(
              "Runtime of DamageProfiler for species "
                  + species
                  + ":\t"
                  + df.format(time_in_min)
                  + " minutes\n");
        } else {
          logger.info("Runtime of DamageProfiler:\t" + df.format(time_in_min) + " minutes\n");
        }

      } else {
        if (species != null) {
          logger.info(
              "Runtime of DamageProfiler for species "
                  + species
                  + ":\t"
                  + df.format(time_in_sec)
                  + " seconds\n");
        } else {
          logger.info("Runtime of DamageProfiler:\t" + df.format(time_in_sec) + " seconds\n");
        }
      }
    } else {
      if (species != null) {
        logger.info(
            "Runtime of DamageProfiler for species "
                + species
                + ":\t"
                + df.format(time_in_ms)
                + " milliseconds\n");
      } else {
        logger.info("Runtime of DamageProfiler:\t" + df.format(time_in_ms) + " milliseconds\n");
      }
    }
  }

  private void parseSpeciesAndRun() throws IOException, DocumentException {
    if (specieslist.length > 0) {

      // if only one species is in list -> run 'normal' DP
      if (specieslist.length == 1) {

        // start DamageProfiler
        final String speciesID = specieslist[0];

        final Pattern p = Pattern.compile("[0-9A-Za-z!#$%&+./:;?@^_|~-]");
        final Matcher m = p.matcher(speciesID);

        if (m.find()) {
          startDamageProfilerOnSingleSpecies(speciesID);
        } else {
          System.err.println("Reference ID " + speciesID + " doesn't match regex: " + p);
          logger.error("Reference ID " + speciesID + " doesn't match regex: " + p);
        }

      } else {

        species_output_summary = new HashMap<>();
        final HashMap<String, Integer> number_of_used_reads_summary = new HashMap<>();

        for (final String specie_input_string : specieslist) {

          // start DamageProfiler
          startDamageProfilerOnSingleSpecies(specie_input_string);

          // collect info for metagenomic summary output
          final String spec_no_space = specie_input_string.replace(" ", "_");
          species_output_summary.put(
              spec_no_space,
              Arrays.asList(
                  outputGenerator.getChart_DP_5prime(),
                  outputGenerator.getChart_DP_3prime(),
                  outputGenerator.getEditDist_chart(),
                  outputGenerator.getLength_chart_all(),
                  outputGenerator.getLength_chart_sep()));

          number_of_used_reads_summary.put(spec_no_space, damageProfiler.getNumberOfUsedReads());
        }

        // generate metagenomic summary output
        final MetagenomicOutput metagenomicOutput = new MetagenomicOutput(logger);
        final String[] splitted = input.split("/");
        final String filename = splitted[splitted.length - 1];
        metagenomicOutput.generate(
            outfolder, species_output_summary, filename, number_of_used_reads_summary, specieslist);
      }

    } else {
      System.err.println("Species list is empty. Please check '-s' or '-sf' parameter.");
      logger.error("Species list is empty. Please check '-s' or '-sf' parameter.");
    }
  }

  private void initLogger(final String outfolder) {
    final LogClass logClass = new LogClass();
    logClass.updateLog4jConfiguration(outfolder);
    logClass.setUp();

    logger = logClass.getLogger(this.getClass());
  }

  private void generateOutput(final String speciesID, final String title)
      throws IOException, DocumentException {

    if (damageProfiler.getNumberOfUsedReads() != 0) {

      outputGenerator =
          new OutputGenerator(
              output_folder,
              damageProfiler,
              speciesID,
              threshold,
              length,
              height_damageplot,
              input,
                  logger,
              ssLibProtocolUsed,
              color_DP_C_to_T,
              color_DP_deletions,
              color_DP_G_to_A,
              color_DP_insertions,
              color_DP_other,
              damageProfiler.getNumberOfRecords(),
              title);

      outputGenerator.writeLengthDistribution();
      outputGenerator.writeDamageFiles(
          damageProfiler.getFrequencies().getCount_G_A_3_norm(),
          damageProfiler.getFrequencies().getCount_C_T_5_norm());
      outputGenerator.writeFrequenciesReference(
          damageProfiler.getFrequencies(), damageProfiler.getReferenceName());
      outputGenerator.writeDNAComp(
          damageProfiler.getFrequencies(), damageProfiler.getReferenceName());
      outputGenerator.writeDNAcomp_genome();
      outputGenerator.writeMisincorporations(damageProfiler.getFrequencies(), threshold);

      outputGenerator.computeSummaryMetrics();

      outputGenerator.writeJSON(Version.VERSION.getValue());

      // create DamagePlots of 3' and 5' ends
      outputGenerator.plotMisincorporations();
      outputGenerator.plotLengthHistogram(
          damageProfiler.getLength_all(),
          damageProfiler.getLength_forward(),
          damageProfiler.getLength_reverse());
      outputGenerator.plotEditDistanceHistogram(
          damageProfiler.getEditDistances(), "Identity distribution");
      outputGenerator.writeEditDistance(damageProfiler.getEditDistances());

    } else {
      logger.warn("No reads processed. Can't create any output");
    }
  }

  private void createOutputFolder(final String path_ourDir, final String species) {
    final File f;
    if (species == null) {
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

  private void readReferenceInCache() throws FileNotFoundException {
    // read reference file as indexed reference
    final IndexedFastaSequenceFile fastaSequenceFile = new IndexedFastaSequenceFile(new File(this.reference));
    // store reference in cache to get faster access
    cache = new FastACacher(new File(reference));
  }

  public boolean isCalculationsDone() {
    return calculationsDone;
  }

  public void setCalculationsDone(final boolean calculationsDone) {
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
