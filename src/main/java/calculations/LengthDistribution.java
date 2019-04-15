package calculations;

import htsjdk.samtools.SAMRecord;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 31.05.16.
 */
public class LengthDistribution {

    private final Logger LOG;
    private HashMap<Integer, Integer> length_distribution_map_forward;
    private HashMap<Integer, Integer> length_distribution_map_reverse;
    private HashMap<Double, Double> length_distribution_map;
    private List<Double> length_forward;
    private List<Double> length_reverse;
    private List<Double> length_all;


    public LengthDistribution(Logger LOG){
        this.LOG = LOG;
    }

    public void init(){

        length_distribution_map_forward = new HashMap<>();
        length_distribution_map_reverse = new HashMap<>();
        length_distribution_map = new HashMap<>();
        length_forward = new ArrayList<>();
        length_reverse = new ArrayList<>();
        length_all = new ArrayList<>();

    }



    /**
     * determine length distribution,
     * distinguish between forward and reverse strand
     *
     * @param record
     */
    public void fillDistributionTable(SAMRecord record, String record_aligned){

        // int record_length = record.getReadLength();
        int record_length = record_aligned.length();
        length_all.add((double)record_length);

        // check if record is on forward or reverse strand
        if(record.getReadNegativeStrandFlag()){
            if(!length_distribution_map_reverse.containsKey(record_length)){
                length_distribution_map_reverse.put(record_length, 1);
            } else {
                int count = length_distribution_map_reverse.get(record_length);
                length_distribution_map_reverse.put(record_length, count + 1);
            }
            length_reverse.add((double)record_length);



        } else {
            if(!length_distribution_map_forward.containsKey(record_length)){
                length_distribution_map_forward.put(record_length, 1);
            } else {
                int count = length_distribution_map_forward.get(record_length);
                length_distribution_map_forward.put(record_length, count + 1);
            }
            length_forward.add((double)record_length);


        }

        if(!length_distribution_map.containsKey(record_length)){
            length_distribution_map.put((double)record_length, 1.0);
        } else {
            double count = length_distribution_map.get(record_length);
            length_distribution_map.put((double)record_length, count + 1);
        }
    }


    /*
     *  Getter
     *
     */


    public HashMap<Integer, Integer> getSeqLen(LengthDistribution lengthDistribution) {

        List<Double> length_all = lengthDistribution.getLength_all();
        HashMap<Integer, Integer> map_length_occurrences_all = new HashMap<>();

        for(double d : length_all){
            if(!map_length_occurrences_all.containsKey(d)){
                map_length_occurrences_all.put((int)d, 1);
            } else {
                int count = map_length_occurrences_all.get(d);
                map_length_occurrences_all.put((int)d, count + 1);
            }
        }
        return map_length_occurrences_all;
    }


    public HashMap<Integer, Integer> getLength_distribution_map_forward() {
        return length_distribution_map_forward;
    }

    public HashMap<Integer, Integer> getLength_distribution_map_reverse() {
        return length_distribution_map_reverse;
    }

    public HashMap<Double, Double> getLength_distribution_map() {
        return length_distribution_map;
    }

    public List<Double> getLength_forward() {
        return length_forward;
    }

    public List<Double> getLength_reverse() {
        return length_reverse;
    }

    public List<Double> getLength_all() {
        return length_all;
    }

}
