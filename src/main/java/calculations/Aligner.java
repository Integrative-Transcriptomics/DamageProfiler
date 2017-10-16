package calculations;

import htsjdk.samtools.CigarElement;
import htsjdk.samtools.SAMRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by neukamm on 06.10.2016.
 */
public class Aligner {

    private final Logger LOG;

    public Aligner(Logger LOG){
        this.LOG = LOG;

    }

    /*
    # from Martin Kircher, description of CIGAR operations
    # BAM Description
    #M 0 alignment match (can be a sequence match or mismatch)
    #I 1 insertion to the reference
    #D 2 deletion from the reference
    #N 3 skipped region from the reference
    #S 4 soft clipping (clipped sequences present in SEQ)
    #H 5 hard clipping (clipped sequences NOT present in SEQ)
    #P 6 padding (silent deletion from padded reference)
    #= 7 sequence match
    #X 8 sequence mismatch
     */

    /**
     * align reference and read according cigar string
     *
     * @param seq
     * @param ref
     * @param record
     * @return
     */
    public String[] align(String seq, String ref, SAMRecord record){

        List<CigarElement> cigar_elements = record.getCigar().getCigarElements();

        String ref_aligned = ref;
        List<double[]> coord = parseCigar(cigar_elements,1);
        for(int i = 0; i < coord.size(); i++){
            double[] entry = coord.get(i);
            ref_aligned = new StringBuilder(ref_aligned).insert((int)entry[1], StringUtils.repeat("-", (int)entry[0])).toString();
        }
        String read_aligned = seq;
        coord.clear();
        coord = parseCigar(cigar_elements,2);
        for(int i = 0; i < coord.size(); i++){
            double[] entry = coord.get(i);
            read_aligned = new StringBuilder(read_aligned).insert((int)entry[1]-1, StringUtils.repeat("-", (int)entry[0])).toString();
        }

        return new String[]{read_aligned, ref_aligned};

    }


    /**
     * parse CIGAR string, get length of each operation and position of operation in read
     *
     * @param cigar
     * @param ope
     * @return double[] coordinates = position in record sequence and number of occurrences
     */
    private List<double[]> parseCigar(List<CigarElement> cigar, int ope){
        List<double[]> coordinates = new ArrayList<>();
        int tlength = 0;

        //count matches, indels and mismatches
        List<Integer> oplist = Arrays.asList(0, 1, 2, 7, 8);

        for(int i = 0; i < cigar.size(); i++){
            int length = cigar.get(i).getLength();
            int op = cigar.get(i).getOperator().ordinal();
            if(op == ope){
                coordinates.add(new double[]{length, tlength});

            }
            if(oplist.contains(op)){
                tlength+= length;
            }
        }

        return coordinates;
    }
}
