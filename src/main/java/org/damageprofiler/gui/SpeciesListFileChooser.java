package org.damageprofiler.gui;

import org.damageprofiler.io.Communicator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SpeciesListFileChooser {


    public SpeciesListFileChooser(Communicator c){

        FileChooser fileChooser = new FileChooser();

        File f = fileChooser.showOpenDialog(new Stage());
        if(f != null){
            c.setSpecieslist_filepath(f.getAbsolutePath());
        }
    }
}
