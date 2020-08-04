package org.damageprofiler.GUI;

import org.damageprofiler.IO.Communicator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ReferenceFileChooser {

    private FileChooser fileChooser = new FileChooser();

    public ReferenceFileChooser(Communicator c){

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fasta", "*.fa","*.fasta", ".fas")
        );

        File f = fileChooser.showOpenDialog(new Stage());
        if(f != null){
            c.setReference(f.getAbsolutePath());
        }

    }

}
