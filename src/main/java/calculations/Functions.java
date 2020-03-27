package calculations;

import org.apache.commons.text.similarity.HammingDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.log4j.Logger;

/**
 * Created by neukamm on 13.03.17.
 */
public class Functions {

    private final Logger LOG;

    public Functions(Logger LOG){
        this.LOG = LOG;
    }


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
//        int distance =0;
//        if(sequence1 == null || sequence2==null)
//            System.exit(1);
//
//        sequence1 = sequence1.toUpperCase();
//        sequence2 = sequence2.toUpperCase();
//
//        if(sequence1.length() != sequence2.length())
//        {
//            System.out.println("Functions:getHammingDistance(): Different length, please enter the strings with equal length.");
//        }
//
//        for(int i=0;i < sequence1.length();i++)
//        {
//            if(sequence1.charAt(i)!=sequence2.charAt(i))
//                distance++;
//        }
//
//        //System.out.println("Hamming Distance: " + distance);
//        return distance;

    }

    public int getLevenshteinDistance(String sequence1, String sequence2){
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        return levenshteinDistance.apply(sequence1, sequence2);
    }
}
