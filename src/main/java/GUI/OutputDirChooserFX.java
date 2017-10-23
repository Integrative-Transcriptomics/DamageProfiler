package GUI;

import IO.Communicator;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class OutputDirChooserFX {

    private DirectoryChooser dirChooser = new DirectoryChooser();

    public OutputDirChooserFX(Communicator c) {
        File f = dirChooser.showDialog(new Stage());
        c.setOutfolder(f.getAbsolutePath());
        System.out.println(f.getAbsolutePath());

    }
}
