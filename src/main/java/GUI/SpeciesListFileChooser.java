package GUI;

import IO.Communicator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SpeciesListFileChooser {

    private FileChooser fileChooser = new FileChooser();

    public SpeciesListFileChooser(Communicator c){

        File f = fileChooser.showOpenDialog(new Stage());
        if(f != null){
            c.setSpecieslist_filepath(f.getAbsolutePath());
        }

    }
}
