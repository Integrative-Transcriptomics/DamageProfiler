package IO;

import htsjdk.samtools.reference.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Created by peltzer on 25/06/15.
 */
public class FastACacher {
    private HashMap<String,byte[]> data = new HashMap<>();

    public FastACacher(File f) throws FileNotFoundException {
        ReferenceSequenceFile refSeq = ReferenceSequenceFileFactory.getReferenceSequenceFile(f);


        while(true){
            ReferenceSequence rs = refSeq.nextSequence();
            if(rs == null){
                break;
            } else {

                data.put(getKeyName(rs.getName()), rs.getBases());
            }
        }
    }

    public HashMap<String, byte[]> getData() {
        return data;
    }

    public String getKeyName(String reference_name){
        String name=reference_name;
        String[] reference_name_splitted = reference_name.split("ref");

        if(reference_name_splitted.length>1){
            name = reference_name_splitted[1].substring(1, reference_name_splitted[1].length()-1);

        }

        return name;


    }
}




