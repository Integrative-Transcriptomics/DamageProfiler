package GUI;

import IO.Communicator;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;


public class BamFileChooser {
    private JFileChooser jfc = new JFileChooser();
    private FileFilter ff;


    public BamFileChooser(Communicator c){
        if(c.getInput() != null){
            jfc.setCurrentDirectory(new File(c.getInput()));
        }
        jfc.setMultiSelectionEnabled(true);
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        ff = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".bam") || file.getName().toLowerCase().endsWith(".sam") || file.getName().toLowerCase().endsWith(".gz");
            }

            @Override
            public String getDescription() {
                return "*.bam/*.sam or zipped *.sam Files supported";  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        jfc.setFileFilter(ff);
        int state = jfc.showOpenDialog(null);
        if (state == JFileChooser.APPROVE_OPTION){
            File[] f = jfc.getSelectedFiles();
            ArrayList<String> files = new ArrayList<String>();
            for (int i = 0; i < f.length; i++){
                files.add(f[i].getAbsolutePath());
            }

            c.setInput(files.get(0));

        }
    }

}