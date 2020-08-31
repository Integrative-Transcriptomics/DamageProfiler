package org.damageprofiler.gui;

import org.damageprofiler.io.Communicator;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class OutputDirChooser {


    public OutputDirChooser(Communicator c) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File f = dirChooser.showDialog(new Stage());
        if (f != null){
            c.setOutfolder(f.getAbsolutePath());
            System.out.println("Output folder: " + f.getAbsolutePath());
        }

    }
}
