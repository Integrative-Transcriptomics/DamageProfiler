package GUI;

import IO.Communicator;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class OutputDirChooser {

    private DirectoryChooser dirChooser = new DirectoryChooser();

    public OutputDirChooser(Communicator c) {
        File f = dirChooser.showDialog(new Stage());
        if (f != null){
            c.setOutfolder(f.getAbsolutePath());
            System.out.println("Output folder: " + f.getAbsolutePath());
        }

    }
}
