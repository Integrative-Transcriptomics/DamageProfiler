package org.damageprofiler.calculations;

import htsjdk.samtools.*;
import htsjdk.samtools.util.SequenceUtil;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.log4j.Logger;
import org.damageprofiler.io.FastACacher;

/** Created by peltzer on 25/06/15. */
public class DamageProfiler {

  private final FastACacher cache;
  private SamReader inputSam = null;
  private Functions useful_functions = null;
  private String species = null;
  private Logger LOG = null;
  private int numberOfUsedReads;
  private int numberOfRecords;
  private Frequencies frequencies;

  private File reference;
  LengthDistribution lengthDistribution;
  private List<Integer> editDistances;
  private Set<String> ref_name_list;

  /**
   * constructor
   *
   * @param cache Cache fasta reference for faster access
   */
  public DamageProfiler(final FastACacher cache) {
    this.cache = cache;
  }

  /**
   * @param input path to input SAM/BAM file
   * @param reference path to reference file
   * @param threshold threshold (int) for calculations
   * @param length length (int) for calculations
   * @param specie List of one or more species, for which damage patterns has to be calculated
   * @param LOG Log file
   */
  public void init(
      final File input,
      final File reference,
      final int threshold,
      final int length,
      final String specie,
      final Logger LOG) {
    // read bam/sam/cram file
    if (!input.exists()) {
      LOG.error(
          "SAM/BAM file not found. Please check your file path.\nInput: "
              + input.getAbsolutePath());
      System.err.println(
          "SAM/BAM file not found. Please check your file path.\nInput: "
              + input.getAbsolutePath());
      System.exit(0);
    } else {
      try {
        if (input.getAbsolutePath().endsWith(".sam") || input.getAbsolutePath().endsWith(".bam")) {

          inputSam =
              SamReaderFactory.make()
                  .enable(SamReaderFactory.Option.DONT_MEMORY_MAP_INDEX)
                  .validationStringency(ValidationStringency.SILENT)
                  .open(input);

          LOG.info("\tReading file " + input);

        } else if (input.getAbsolutePath().endsWith(".cram")) {
          if (!reference.isFile()) {
            System.err.println("Reference file is needed to reads CRAM files.");
            LOG.error("Reference file is needed to reads CRAM files.");
            System.exit(1);
          } else {
            inputSam =
                SamReaderFactory.make()
                    .enable(SamReaderFactory.Option.DONT_MEMORY_MAP_INDEX)
                    .referenceSequence(reference)
                    .validationStringency(ValidationStringency.SILENT)
                    .open(input);
          }
        }

        numberOfUsedReads = 0;
        numberOfRecords = 0;
        this.LOG = LOG;
        this.frequencies = new Frequencies(length, threshold, this.LOG);
        this.reference = reference;
        this.lengthDistribution = new LengthDistribution(this.LOG);
        this.lengthDistribution.init();
        this.editDistances = new ArrayList<>();
        this.species = specie;
        this.useful_functions = new Functions();
        this.ref_name_list = new HashSet<>();

      } catch (final Exception e) {
        System.err.println("Invalid SAM/BAM file. Please check your file.");
        LOG.error("Invalid SAM/BAM file. Please check your file.");
        System.exit(-1);
      }
    }
  }

  /**
   * get all sam records of input sam/bam file, distinguish between mapped and mapped/merged and
   * normalize values after all calculations
   *
   * @param use_only_merged_reads boolean: only merged reads will be used for damage calculation
   */
  public void extractSAMRecords(final boolean use_only_merged_reads) {

    LOG.info("\tStart processing each mapped record in input file");

    for (final SAMRecord record : inputSam) {
      numberOfRecords++;

      if (this.species == null) {
        handleRecord(use_only_merged_reads, record);
      } else {
        if (record.getReferenceName().equals(this.species)) {
          handleRecord(use_only_merged_reads, record);
        }
      }
    }

    frequencies.normalizeValues();
  }

  /**
   * get the record and filter according mapped_only setting
   *
   * @param use_only_merged_reads boolean: use only merged reads
   * @param record SAM record to process
   */
  private void handleRecord(final boolean use_only_merged_reads, final SAMRecord record) {

    if (use_only_merged_reads) {
      // process only mapped and merged reads
      if (!record.getReadUnmappedFlag() && record.getReadName().startsWith("M_")) {
        processRecord(record);
      }
    } else {
      // process only mapped reads
      if (!record.getReadUnmappedFlag()) {
        // get all mapped reads
        processRecord(record);
      }
    }
  }

  /**
   * get reference sequence of the record based on the alignment positions of the record. If they
   * differ in length, align according the CIGAR string. In addition, the length distribution table
   * is filled, the frequencies counted and the damage computed.
   *
   * @param record SAM record to process
   */
  private void processRecord(final SAMRecord record) {
    numberOfUsedReads++;
    ref_name_list.add(record.getReferenceName());

    /*
       If MD value is set, use it to reconstruct reference
       Otherwise reconstruct it based on reference.

    */

    String reference_aligned;
    String record_aligned;

    // check if record has MD tag and no reference file is specified
    if (record.getStringAttribute(SAMTag.MD.name()) == null
        && (this.reference == null || !reference.isFile())) {

      LOG.error(
          "SAM/BAM file has no MD tag. Please specify reference file which is needed for MD tag calculations.");
      System.exit(1);

    } else {

      // MD tag needs to be calculated --> REF needs to be specified
      if (record.getStringAttribute(SAMTag.MD.name()) == null) {
        SequenceUtil.calculateMdAndNmTags(
            record, cache.getData().get(record.getReferenceName()), true, true);
      }

      try {

        final byte[] ref_seq = SequenceUtil.makeReferenceFromAlignment(record, false);
        reference_aligned = new String(ref_seq, StandardCharsets.UTF_8);
        record_aligned = record.getReadString();
        proceed(record, record_aligned, reference_aligned);

      } catch (final Exception e) {

        System.err.println(
            record.getReadName() + "\nMD and NM value will be re-calculated.\nError:" + e);
        LOG.error(record.getReadName() + "\nMD and NM value will be re-calculated.\nError:" + e);
        if (!reference.isFile()) {
          System.err.println(
              "No MD tag defined. Please specify reference file which is needed for MD tag calculations.");
          LOG.error(
              "No MD tag defined. Please specify reference file which is needed for MD tag calculations.");
          System.exit(1);
        }

        try {
          SequenceUtil.calculateMdAndNmTags(
              record, cache.getData().get(record.getReferenceName()), true, true);
          final byte[] ref_seq = SequenceUtil.makeReferenceFromAlignment(record, false);
          reference_aligned = new String(ref_seq, StandardCharsets.UTF_8);
          record_aligned = record.getReadString();
          proceed(record, record_aligned, reference_aligned);
          System.err.println("Re-calculation was successful!\n");
          LOG.info("Re-calculation was successful!\n");

        } catch (final Exception e1) {
          System.err.println(
              "Re-calculation failed. Record " + record.getReadName() + " will be skipped.\n");
          LOG.warn("Re-calculation failed. Record " + record.getReadName() + " will be skipped.\n");
        }
      }
    }
  }

  private void proceed(
      final SAMRecord record, final String record_aligned, final String reference_aligned) {
    // report length distribution
    this.lengthDistribution.fillDistributionTable(record, record_aligned);

    // calculate distance between record and reference
    final int hamming = useful_functions.getHammingDistance(record_aligned, reference_aligned);
    this.editDistances.add(hamming);

    // calculate frequencies
    frequencies.count(record, record_aligned, reference_aligned);

    // calculate base misincorporations
    frequencies.calculateMisincorporations(record, record_aligned, reference_aligned);
  }

  /*
   * Getter
   */

  public Frequencies getFrequencies() {
    return frequencies;
  }

  public HashMap<Integer, Integer> getLength_distribution_map_forward() {
    return lengthDistribution.getLength_distribution_map_forward();
  }

  public HashMap<Integer, Integer> getLength_distribution_map_reverse() {
    return lengthDistribution.getLength_distribution_map_reverse();
  }

  public List<Integer> getLength_forward() {
    return lengthDistribution.getLength_forward();
  }

  public List<Integer> getLength_all() {
    return lengthDistribution.getLength_all();
  }

  public List<Integer> getLength_reverse() {
    return lengthDistribution.getLength_reverse();
  }

  public int getNumberOfUsedReads() {
    return numberOfUsedReads;
  }

  public List<Integer> getEditDistances() {
    return editDistances;
  }

  public int getNumberOfRecords() {
    return numberOfRecords;
  }

  public List<String> getReferenceName() {
    return new ArrayList<>(ref_name_list);
  }
}
