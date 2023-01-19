package org.damageprofiler.gui;

import java.io.File;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.damageprofiler.io.Communicator;

public class OutputDirChooser {

  public OutputDirChooser(Communicator c) {
    DirectoryChooser dirChooser = new DirectoryChooser();
    File f = dirChooser.showDialog(new Stage());
    if (f != null) {
      c.setOutfolder(f.getAbsolutePath());
    }
  }
}
