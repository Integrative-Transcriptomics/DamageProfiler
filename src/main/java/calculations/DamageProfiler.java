package calculations;


import IO.FastACacher;
import htsjdk.samtools.*;
import htsjdk.samtools.reference.IndexedFastaSequenceFile;
import htsjdk.samtools.util.SequenceUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by peltzer on 25/06/15.
 */
public class  DamageProfiler {

    private final SamReader inputSam;
    private IndexedFastaSequenceFile fastaSequenceFile;
    private int numberOfUsedReads;
    private int numberOfAllReads;
    private int threshold;
    private int length;
    private Frequencies frequencies;
    private File reference;
    private FastACacher cache;
    LengthDistribution lengthDistribution;
    private ArrayList<Double> identity;

    /**
     * constructor, set input and output filepaths
     * @param input
     * @param reference
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public DamageProfiler(File input, File reference, int threshold, int length) throws FileNotFoundException, UnsupportedEncodingException {
        // read bam/sam file
        inputSam = SamReaderFactory.make().enable(SamReaderFactory.Option.DONT_MEMORY_MAP_INDEX).
                validationStringency(ValidationStringency.LENIENT).open(input);
        numberOfUsedReads = 0;
        numberOfAllReads = 0;
        this.threshold = threshold;
        this.length = length;
        this.frequencies = new Frequencies(this.length, this.threshold);
        this.reference = reference;
        this.lengthDistribution = new LengthDistribution();
        this.lengthDistribution.init();
        this.identity = new ArrayList();
    }


    /**
     * get all sam records of input sam/bam file,
     * distinguish between mapped and mapped/merged and normalize values
     * after all calculations
     *
     *
     * @param use_only_merged_reads
     * @throws Exception
     */
    public String extractSAMRecords(boolean use_only_merged_reads) throws Exception{

        List<SAMSequenceRecord> seq_dict = inputSam.getFileHeader().getSequenceDictionary().getSequences();
        String seq_name=null;

        for(SAMRecord record : inputSam) {


            numberOfAllReads++;
            if (use_only_merged_reads) {
                // get only mapped and merged reads
                if (!record.getReadUnmappedFlag() && record.getReadName().startsWith("M_")) {
                    processRecord(record);
                    seq_name = seq_dict.get(0).getSequenceName();

                }
            } else if(!record.getReadUnmappedFlag()){
                // get all mapped reads
                processRecord(record);
                seq_name = seq_dict.get(0).getSequenceName();

            }
        }
        frequencies.normalizeValues();
        return seq_name;

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

            System.err.print("SAM/BAM file has no MD tag. Please specify reference file ");
            System.exit(-1);

        } else if (record.getStringAttribute(SAMTag.MD.name()) == null){

            readReferenceInCache();
            Aligner aligner = new Aligner();
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

        }
        else if(record.getStringAttribute(SAMTag.MD.name()) != null){
            // get reference corresponding to the record
            byte[] ref_seq = SequenceUtil.makeReferenceFromAlignment(record, false);
            reference_aligned = new String(ref_seq, "UTF-8");
            record_aligned = record.getReadString();


        }

        // report length distribution
        this.lengthDistribution.fillDistributionTable(record,record_aligned);
        int hamming = getHammingDistance(record_aligned, reference_aligned);
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
        cache = new FastACacher(reference);

    }

    /**
     * Get the hamming distance between two string sequences
     *
     * @param sequence1 - the first sequence
     * @param sequence2 - the second sequence
     * @return the hamming distance
     */
    public static int getHammingDistance(String sequence1, String sequence2){
        int distance =0;
        if(sequence1 == null || sequence2==null)
            System.exit(0);

        sequence1 = sequence1.toUpperCase();
        sequence2 = sequence2.toUpperCase();

        if(sequence1.length() != sequence2.length())
        {
            System.out.println("The string are not equal in length ,Please enter the strings wit equal lengths ");
        }

        for(int i=0;i < sequence1.length();i++)
        {
            if(sequence1.charAt(i)!=sequence2.charAt(i))
                distance++;
        }

        return distance;

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
    public int getNumberOfAllReads() {
        return numberOfAllReads;
    }
    public ArrayList<Double> getIdentity() { return identity; }
}
