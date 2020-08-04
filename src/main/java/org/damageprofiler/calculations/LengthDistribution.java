package org.damageprofiler.calculations;

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
    private HashMap<Double, Integer> length_distribution_map_forward;
    private HashMap<Double, Integer> length_distribution_map_reverse;
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
        double record_length = record_aligned.length();
        length_all.add(record_length);

        // check if record is on forward or reverse strand
        if(record.getReadNegativeStrandFlag()){
            if(!length_distribution_map_reverse.containsKey(record_length)){
                length_distribution_map_reverse.put(record_length, 1);
            } else {
                int count = length_distribution_map_reverse.get(record_length);
                length_distribution_map_reverse.put(record_length, count + 1);
            }
            length_reverse.add(record_length);



        } else {
            if(!length_distribution_map_forward.containsKey(record_length)){
                length_distribution_map_forward.put(record_length, 1);
            } else {
                int count = length_distribution_map_forward.get(record_length);
                length_distribution_map_forward.put(record_length, count + 1);
            }
            length_forward.add(record_length);


        }

        if(!length_distribution_map.containsKey(record_length)){
            length_distribution_map.put((double)record_length, 1.0);
        } else {
            double count = length_distribution_map.get(record_length);
            length_distribution_map.put((double)record_length, count + 1);
        }
    }

    /*
            Getter
     */

    public HashMap<Double, Integer> getLength_distribution_map_forward() {
        return length_distribution_map_forward;
    }

    public HashMap<Double, Integer> getLength_distribution_map_reverse() {
        return length_distribution_map_reverse;
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
