package org.damageprofiler.calculations;

import org.damageprofiler.IO.FastACacher;
import htsjdk.samtools.*;
import htsjdk.samtools.util.SequenceUtil;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;


/**
 * Created by peltzer on 25/06/15.
 */
public class  DamageProfiler {

    private final FastACacher cache;
    private SamReader inputSam=null;
    private Functions useful_functions=null;
    private String species =null;
    private Logger LOG=null;
    private int numberOfUsedReads;
    private int numberOfRecords;
    private int threshold;
    private int length;
    private Frequencies frequencies;
    private File reference;
    LengthDistribution lengthDistribution;
    private ArrayList<Double> identity;
    private List<Double> editDistances;

    /**
     * constructor
     * @param cache
     */
    public DamageProfiler(FastACacher cache) {
        this.cache = cache;
    }

    /**
     *
     * @param input
     * @param reference
     * @param threshold
     * @param length
     * @param specie
     * @param LOG
     */
    public void init(File input, File reference, int threshold, int length, String specie, Logger LOG){
        // read bam/sam/cram file
        if (!input.exists()){
            System.err.println("SAM/BAM file not found. Please check your file path.\nInput: " +
                    input.getAbsolutePath());
            System.exit(0);
        } else {
            try{
                if(input.getAbsolutePath().endsWith(".sam") || input.getAbsolutePath().endsWith(".bam") ) {

                    inputSam = SamReaderFactory.make().enable(SamReaderFactory.Option.DONT_MEMORY_MAP_INDEX).
                            validationStringency(ValidationStringency.SILENT).open(input);

                } else if(input.getAbsolutePath().endsWith(".cram")){
                    if(!reference.isFile()){
                        System.err.println("Reference file is needed to reads CRAM files.");
                        System.exit(1);
                    } else {
                        inputSam = SamReaderFactory.make().enable(SamReaderFactory.Option.DONT_MEMORY_MAP_INDEX).
                                referenceSequence(reference).validationStringency(ValidationStringency.SILENT).open(input);
                    }
                }

                numberOfUsedReads = 0;
                numberOfRecords = 0;
                this.LOG = LOG;
                this.threshold = threshold;
                this.length = length;
                this.frequencies = new Frequencies(this.length, this.threshold, this.LOG);
                this.reference = reference;
                this.lengthDistribution = new LengthDistribution(this.LOG);
                this.lengthDistribution.init();
                this.identity = new ArrayList();
                this.editDistances = new ArrayList();
                this.species = specie;
                useful_functions = new Functions();

            } catch (Exception e){
                System.err.println("Invalid SAM/BAM file. Please check your file.");
                LOG.error("Invalid SAM/BAM file. Please check your file.");
                System.err.println(e.toString());
                System.exit(-1);
            }
        }
    }

    /**
     * get all sam records of input sam/bam file,
     * distinguish between mapped and mapped/merged and normalize values
     * after all calculations
     *
     *
     * @param use_only_merged_reads
     * @param use_all_reads
     * @throws Exception
     */
    public void extractSAMRecords(boolean use_only_merged_reads, boolean use_all_reads) {

        if(use_all_reads && use_only_merged_reads){
            LOG.info("-------------------");
            LOG.info("0 reads processed.\nRunning not possible. 'use_only_merged_reads' and 'use_all_reads' was set to 'true'");
            System.exit(1);

        } else {

            for(SAMRecord record : inputSam) {
                numberOfRecords++;

                if (this.species == null) {
                    handleRecord(use_only_merged_reads, use_all_reads, record);
                } else {
                    if (record.getReferenceName().equals(this.species)) {
                        handleRecord(use_only_merged_reads, use_all_reads, record);
                    }
                }
            }

            frequencies.normalizeValues();

            LOG.info("-------------------");
            LOG.info("# reads used for damage calculation: " + (numberOfUsedReads ));
        }
    }


    /**
     * get the record and filter according mapped_only setting
     *
     * @param use_only_merged_reads
     * @param use_all_reads
     * @param record
     */
    private void handleRecord(boolean use_only_merged_reads, boolean use_all_reads, SAMRecord record) {

        if(use_all_reads && !use_only_merged_reads){
            // process all reads
            processRecord(record);

        } else if (use_only_merged_reads && !use_all_reads){
            // process only mapped and merged reads
            if (!record.getReadUnmappedFlag() && record.getReadName().startsWith("M_")) {
                processRecord(record);
            }
        } else if(!use_only_merged_reads && !use_all_reads) {
            // process only mapped reads
            if (!record.getReadUnmappedFlag()) {
                // get all mapped reads
                processRecord(record);
            }
        }
    }



    /**
     * get reference sequence of the record based on the alignment
     * positions of the record.
     * If they differ in length, align according the CIGAR string.
     * In addition, the length distribution table is filled, the frequencies counted and
     * the damage computed.
     *
     * @param record
     * @throws IOException
     */

    private void processRecord(SAMRecord record) {
        numberOfUsedReads++;

        /*
            If MD value is set, use it to reconstruct reference
            Otherwise reconstruct it based on reference.

         */

        String reference_aligned="";
        String record_aligned="";


        // check if record has MD tag and no reference file is specified
        if(record.getStringAttribute(SAMTag.MD.name()) == null && (this.reference == null || !reference.isFile())){

            LOG.error("SAM/BAM file has no MD tag. Please specify reference file which is needed for MD tag calculations.");
            System.exit(1);

        } else {

            // MD tag needs to be calculated --> REF needs to be specified
            if (record.getStringAttribute(SAMTag.MD.name()) == null){
                SequenceUtil.calculateMdAndNmTags(record, cache.getData().get(record.getReferenceName()), true, true);
            }

            try{

                byte[] ref_seq = SequenceUtil.makeReferenceFromAlignment(record, false);
                reference_aligned = new String(ref_seq, "UTF-8");
                record_aligned = record.getReadString();
                proceed(record, record_aligned, reference_aligned);

            } catch (Exception e){

                System.err.println(record.getReadName() + "\nMD and NM value will be re-calculated.\nError:" + e);
                if(!reference.isFile()){
                    System.err.println("No MD tag defined. Please specify reference file which is needed for MD tag calculations.");
                    System.exit(1);
                }

                try{
                    SequenceUtil.calculateMdAndNmTags(record, cache.getData().get(record.getReferenceName()), true, true);
                    byte[] ref_seq = SequenceUtil.makeReferenceFromAlignment(record, false);
                    reference_aligned = new String(ref_seq, "UTF-8");
                    record_aligned = record.getReadString();
                    proceed(record, record_aligned, reference_aligned);
                    System.err.println("Re-calculation was successful!\n");

                } catch (Exception e1){
                    System.err.println("Re-calculation failed. Record " + record.getReadName() + " will be skipped.\n");
                }
            }
        }
    }


    private void proceed(SAMRecord record, String record_aligned, String reference_aligned) throws Exception {
        // report length distribution
        this.lengthDistribution.fillDistributionTable(record,record_aligned);

        // calculate distance between record and reference
        double hamming = useful_functions.getHammingDistance(record_aligned, reference_aligned);

        double id = record_aligned.length()-hamming / (double)record_aligned.length();
        this.identity.add(id);
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
    public HashMap<Double, Integer> getLength_distribution_map_forward() {return lengthDistribution.getLength_distribution_map_forward(); }
    public HashMap<Double, Integer> getLength_distribution_map_reverse() {return lengthDistribution.getLength_distribution_map_reverse(); }
    public List<Double> getLength_forward() {
        return lengthDistribution.getLength_forward();
    }
    public List<Double> getLength_all() {
        return lengthDistribution.getLength_all();
    }
    public List<Double> getLength_reverse() { return lengthDistribution.getLength_reverse(); }
    public int getNumberOfUsedReads() {
        return numberOfUsedReads;
    }

    public List<Double> getEditDistances() {
        return editDistances;
    }
    public int getNumberOfRecords(){
        return numberOfRecords;
    }
}
