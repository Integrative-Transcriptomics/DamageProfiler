package GUI;

import IO.Communicator;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by neukamm on 02.09.2016.
 */
public class OutputDirChooser {
    private JFileChooser jfc = new JFileChooser();

    public OutputDirChooser(Communicator c) {
        if (c.getOutfolder() != null) {
            jfc.setCurrentDirectory(new File(c.getOutfolder()));
        }
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int state = jfc.showOpenDialog(null);
        if (state == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            c.setOutfolder(f.getAbsolutePath());
        }
    }

}




