package calculations;


import IO.FastACacher;
import htsjdk.samtools.*;
import htsjdk.samtools.reference.IndexedFastaSequenceFile;
import htsjdk.samtools.util.SequenceUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


/**
 * Created by peltzer on 25/06/15.
 */
public class  DamageProfiler {

    private SamReader inputSam=null;
    private Functions useful_functions=null;
    private String specie=null;
    private Logger LOG=null;
    private IndexedFastaSequenceFile fastaSequenceFile;
    private int numberOfUsedReads;
    private int threshold;
    private int length;
    private Frequencies frequencies;
    private File reference;
    private FastACacher cache;
    LengthDistribution lengthDistribution;
    private ArrayList<Double> identity;
    private SpecieHandler specieHandler;
    private double timePer100000RecordsInSeconds = 1;
    private long actualRuntime=0;
    private SamReader inputSamMate;
    private long numberOfRecords;

    /**
     * constructor
     * @param specieHandler
     */
    public DamageProfiler(SpecieHandler specieHandler) {
        this.specieHandler = specieHandler;

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
        // read bam/sam file
        if (!input.exists()){
            System.err.println("SAM/BAM file not found. Please check your file path.\nInput: " +
                    input.getAbsolutePath());
            System.exit(0);
        } else {
            try{

                inputSam = SamReaderFactory.make().enable(SamReaderFactory.Option.DONT_MEMORY_MAP_INDEX).
                        validationStringency(ValidationStringency.LENIENT).open(input);

                inputSamMate = SamReaderFactory.make().enable(SamReaderFactory.Option.DONT_MEMORY_MAP_INDEX).
                        validationStringency(ValidationStringency.LENIENT).open(input);

                numberOfUsedReads = 0;
                this.LOG = LOG;
                this.threshold = threshold;
                this.length = length;
                this.frequencies = new Frequencies(this.length, this.threshold, this.LOG);
                this.reference = reference;
                this.lengthDistribution = new LengthDistribution(this.LOG);
                this.lengthDistribution.init();
                this.identity = new ArrayList();
                this.specie = specie;
                useful_functions = new Functions(this.LOG);


            } catch (Exception e){
                System.err.println("Invalid SAM/BAM file. Please check your file.");
                LOG.error("Invalid SAM/BAM file. Please check your file.");
                System.out.println(e.toString());
                System.exit(0);
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
    public void extractSAMRecords(boolean use_only_merged_reads, boolean use_all_reads) throws Exception{

        if(use_all_reads && use_only_merged_reads){
            LOG.info("-------------------");
            LOG.info("0 reads processed.\nRunning not possible. 'use_only_merged_reads' and 'use_all_reads' was set to 'true'");
            System.exit(0);

        } else {

            // estimate runtime:
            numberOfRecords = getNumberOfrecords();
            System.out.println("Number of records to process: " + numberOfRecords);
            long estimatedRuntimeInSeconds = (long) (numberOfRecords/100000 * timePer100000RecordsInSeconds);

            if(estimatedRuntimeInSeconds > 60) {
                long minutes = estimatedRuntimeInSeconds / 60;
                long seconds = estimatedRuntimeInSeconds % 60;
                System.out.println("Estimated Runtime: " + minutes + " minutes, and " + seconds + " seconds.");
            } else {
                System.out.println("Estimated Runtime: " + estimatedRuntimeInSeconds + " seconds.");
            }

            // measure runtime per 100,000 records to estimate total runtime
            long startTime = System.currentTimeMillis();

            for(SAMRecord record : inputSam) {

                if (this.specie == null) {

                    handleRecord(use_only_merged_reads, use_all_reads, record);

                    // print number of processed reads
                    if (numberOfUsedReads % 100 == 0) {
                        LOG.info(numberOfUsedReads + " Reads processed.");
                    }

                } else {

                    if (record.getReferenceName().contains(this.specie)) {

                        handleRecord(use_only_merged_reads, use_all_reads, record);

                        // print number of processed reads
                        if (numberOfUsedReads % 100 == 0) {
                            LOG.info(numberOfUsedReads + " Reads processed.");
                        }
                    }
                }

                if((numberOfUsedReads % 100000) == 0){

                    long currtime_post_execution = System.currentTimeMillis();
                    long diff = currtime_post_execution - startTime;

                    long runtime_s = diff / 1000;
                    actualRuntime += runtime_s;
                    startTime = System.currentTimeMillis();
                }
            }




            frequencies.normalizeValues();

            LOG.info("-------------------");
            LOG.info("# reads used for damage calculation: " + (numberOfUsedReads ));
        }

        if(actualRuntime > 60) {
            long minutes = actualRuntime / 60;
            long seconds = actualRuntime % 60;
            System.out.println("Runtime per record: " + minutes + " minutes, and " + seconds + " seconds.");
        } else {
            System.out.println("Runtime per record: " + actualRuntime + " seconds.");
        }

    }

    private long getNumberOfrecords() {
        long count = 0;
        for(SAMRecord record : inputSamMate){
            count++;
        }

        inputSamMate=null;
        return count;
    }


    private void handleRecord(boolean use_only_merged_reads, boolean use_all_reads, SAMRecord record) throws Exception {



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

    private void processRecord(SAMRecord record) throws Exception{
        numberOfUsedReads++;



        /*
            If MD value is set, use it to reconstruct reference
            Otherwise reconstruct reference it based on the CIGAR string

         */

        String reference_aligned="";
        String record_aligned="";


        // check if record has MD tag and no reference file is specified
        if(record.getStringAttribute(SAMTag.MD.name()) == null && this.reference == null){

            LOG.error("SAM/BAM file has no MD tag. Please specify reference file ");
            System.exit(0);

        } else if (record.getStringAttribute(SAMTag.MD.name()) == null){

            readReferenceInCache();
            Aligner aligner = new Aligner(LOG);
            // SAMRecord has 1-based coordinate system -> closed interval [..,..]
            // normal Array 0-based coordinate system -> interval half-cloded-half-open [...,...)
            int start = record.getAlignmentStart() - 1;
            int stop = record.getAlignmentEnd();

            // get reference sequence
            byte[] refSeq = Arrays.copyOfRange(cache.getData().get(cache.getKeyName(record.getReferenceName())), start, stop);
            String reference = new String(refSeq, "UTF-8");
            // align record and reference

            if (record.getReadLength() != reference.length()) {
                String[] record_reference = aligner.align(record.getReadString(), reference, record);
                reference_aligned = record_reference[1];
                record_aligned = record_reference[0];

            } else {
                reference_aligned = record.getReadString();
                record_aligned = record.getReadString();
            }

        } else if(record.getStringAttribute(SAMTag.MD.name()) != null){
            // get reference corresponding to the record
            if(record.getCigar().getReadLength() != 0 && record.getCigar().getReadLength() == record.getReadLength()){
//                if(record.getCigarString().contains("S")){
//                    System.out.println("Cigar contains soft clipping");
//                } else if(record.getCigarString().contains("D")){
//                    System.out.println("Cigar contains deletion");
//                } else {
                    byte[] ref_seq = SequenceUtil.makeReferenceFromAlignment(record, false);
                    reference_aligned = new String(ref_seq, "UTF-8");
                    record_aligned = record.getReadString();
               // }



            } else {
                LOG.info("Skipped record (length does not match): " + record.getReadName());
            }

        }


        // report length distribution
        this.lengthDistribution.fillDistributionTable(record,record_aligned);
        int hamming = useful_functions.getHammingDistance(record_aligned, reference_aligned);
        double id = (double)(record_aligned.length()-hamming) / (double)record_aligned.length();
        this.identity.add(id);

        // calculate frequencies
        frequencies.count(record, record_aligned, reference_aligned);
        frequencies.calculateMisincorporations(record, record_aligned, reference_aligned);

    }


    /**
     * index reference file and put it in cache to get faster
     * access
     *
     * @throws FileNotFoundException
     */

    private void readReferenceInCache() throws FileNotFoundException{
        // read reference file as indexed reference
        fastaSequenceFile = new IndexedFastaSequenceFile(reference);
        // store reference in cache to get faster access
        cache = new FastACacher(reference, LOG);

    }



    /*
     * Getter
     */


    public Frequencies getFrequencies() {
        return frequencies;
    }
    public HashMap<Integer, Integer> getLength_distribution_map_forward() {return lengthDistribution.getLength_distribution_map_forward(); }
    public HashMap<Integer, Integer> getLength_distribution_map_reverse() {return lengthDistribution.getLength_distribution_map_reverse(); }
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

    public long getNumberOfAllReads() {
        return numberOfRecords;
    }
    public ArrayList<Double> getIdentity() { return identity; }

    public String getSpeciesname(File file, String ref) {

        SamReader input = SamReaderFactory.make().enable(SamReaderFactory.Option.DONT_MEMORY_MAP_INDEX).
                validationStringency(ValidationStringency.LENIENT).open(file);

        for(SAMRecord record : input) {
            if(record.getReferenceName().contains(ref)){
                specieHandler.getSpecie(record.getReferenceName());
                String spe = specieHandler.getSpecie_name();
                return spe.replace(" ", "_").trim();
            }
        }
        return null;
    }


}
