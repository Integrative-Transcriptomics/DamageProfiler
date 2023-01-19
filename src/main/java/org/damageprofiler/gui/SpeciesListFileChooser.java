package org.damageprofiler.gui;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.damageprofiler.io.Communicator;

public class SpeciesListFileChooser {

  public SpeciesListFileChooser(Communicator c) {

    FileChooser fileChooser = new FileChooser();

    File f = fileChooser.showOpenDialog(new Stage());
    if (f != null) {
      c.setSpecieslist_filepath(f.getAbsolutePath());
    }
  }
}
