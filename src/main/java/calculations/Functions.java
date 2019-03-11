package calculations;

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
}
