package GUI;

import IO.Communicator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ReferenceFileChooserFX {

    private FileChooser fileChooser = new FileChooser();

    public ReferenceFileChooserFX(Communicator c){

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fasta", "*.fa","*.fasta", ".fas")
        );

        File f = fileChooser.showOpenDialog(new Stage());
        if(f != null){
            c.setReference(f.getAbsolutePath());
        }

    }

}
