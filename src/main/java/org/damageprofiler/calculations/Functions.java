package org.damageprofiler.calculations;

import org.apache.commons.text.similarity.HammingDistance;

/**
 * Created by neukamm on 13.03.17.
 */
public class Functions {

    /**
     * Get the hamming distance between two string sequences
     *
     * @param sequence1 - the first sequence
     * @param sequence2 - the second sequence
     * @return the hamming distance
     */
    public int getHammingDistance(String sequence1, String sequence2){
        HammingDistance hammingDistance = new HammingDistance();
        return hammingDistance.apply(sequence1, sequence2);
    }

}
