package IO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;


/**
 * Created by neukamm on 25.07.2016.
 */
public class Unzip {

    public Unzip(){

    }


    public String decompress(String infile) throws Exception {

        GZIPInputStream in = new GZIPInputStream(new FileInputStream(infile));
        String outfile = infile.substring(0, infile.lastIndexOf('.'));
        OutputStream out = new FileOutputStream(outfile);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

        return outfile;
    }
}
