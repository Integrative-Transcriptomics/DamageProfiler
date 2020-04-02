package calculations;


import IO.FastACacher;
import htsjdk.samtools.*;
import htsjdk.samtools.reference.IndexedFastaSequenceFile;
import htsjdk.samtools.util.SequenceUtil;
import org.apache.log4j.Logger;

import java.io.*;
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
    private int numberOfRecords;
    private int threshold;
    private int length;
    private Frequencies frequencies;
    private File reference;
    private FastACacher cache;
    LengthDistribution lengthDistribution;
    private ArrayList<Double> identity;
    private SpecieHandler specieHandler;
    private long actualRuntime=0;
    private long runtime_ms;
    private List<Integer> editDistances;

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
        // read bam/sam/cram file
        if (!input.exists()){
            System.err.println("SAM/BAM file not found. Please check your file path.\nInput: " +
                    input.getAbsolutePath());
            System.exit(0);
        } else {
            try{

                if(input.getAbsolutePath().endsWith(".sam") || input.getAbsolutePath().endsWith(".bam") ) {

                    inputSam = SamReaderFactory.make().enable(SamReaderFactory.Option.DONT_MEMORY_MAP_INDEX).
                            validationStringency(ValidationStringency.LENIENT).open(input);

                } else if(input.getAbsolutePath().endsWith(".cram")){
                    System.out.println(reference.getName());
                    if(!reference.isFile()){
                        System.err.println("Reference file is needed to reads CRAM files.");
                        System.exit(1);
                    } else {
                        inputSam = SamReaderFactory.make().enable(SamReaderFactory.Option.DONT_MEMORY_MAP_INDEX).
                                referenceSequence(reference).validationStringency(ValidationStringency.LENIENT).open(input);

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
                this.specie = specie;
                useful_functions = new Functions(this.LOG);


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
    public void extractSAMRecords(boolean use_only_merged_reads, boolean use_all_reads) throws Exception{

        if(use_all_reads && use_only_merged_reads){
            LOG.info("-------------------");
            LOG.info("0 reads processed.\nRunning not possible. 'use_only_merged_reads' and 'use_all_reads' was set to 'true'");
            System.exit(1);

        } else {

            // measure runtime
            long startTime = System.currentTimeMillis();

            for(SAMRecord record : inputSam) {

                numberOfRecords++;

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

            }

            frequencies.normalizeValues();

            LOG.info("-------------------");
            LOG.info("# reads used for damage calculation: " + (numberOfUsedReads ));
        }

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
            Otherwise reconstruct it based on reference.

         */

        String reference_aligned="";
        String record_aligned="";


        // check if record has MD tag and no reference file is specified
        if(record.getStringAttribute(SAMTag.MD.name()) == null && this.reference == null){

            LOG.error("SAM/BAM file has no MD tag. Please specify reference file which is needed for MD tag calculations.");
            System.exit(1);

        } else {

            // MD tag needs to be calculated --> REF needs to be specified
            if (record.getStringAttribute(SAMTag.MD.name()) == null){
                if(!reference.isFile()){
                    System.err.println("No MD tag defined. Please specify reference file which is needed for MD tag calculations.");
                    System.exit(1);
                }
                readReferenceInCache();
                SequenceUtil.calculateMdAndNmTags(record, cache.getData().get(record.getReferenceName()), true, true);
            }

            byte[] ref_seq = SequenceUtil.makeReferenceFromAlignment(record, false);
            reference_aligned = new String(ref_seq, "UTF-8");
            record_aligned = record.getReadString();

        }

        // report length distribution
        this.lengthDistribution.fillDistributionTable(record,record_aligned);

        // calculate distance between record and reference
        int hamming = useful_functions.getHammingDistance(record_aligned, reference_aligned);

        // todo: so far unused, could be added as user-choice
        int levenshtein = useful_functions.getLevenshteinDistance(record_aligned, reference_aligned);

        double id = (double)(record_aligned.length()-hamming) / (double)record_aligned.length();
        this.identity.add(id);
        this.editDistances.add(hamming);

        // calculate frequencies
        frequencies.count(record, record_aligned, reference_aligned);

        // calculate base misincorporations
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
    public List<Integer> getLength_forward() {
        return lengthDistribution.getLength_forward();
    }
    public List<Integer> getLength_all() {
        return lengthDistribution.getLength_all();
    }
    public List<Integer> getLength_reverse() { return lengthDistribution.getLength_reverse(); }
    public int getNumberOfUsedReads() {
        return numberOfUsedReads;
    }

    public ArrayList<Double> getIdentity() {
        return identity; }

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


    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public List<Integer> getEditDistances() {
        return editDistances;
    }
}
