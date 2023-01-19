package org.damageprofiler.gui;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.damageprofiler.io.Communicator;

public class SpeciesListFileChooser {

  private final Communicator communicator;

  public SpeciesListFileChooser(final Communicator c) {
    this.communicator = c;
  }

  public void open() {

    final FileChooser fileChooser = new FileChooser();

    final File f = fileChooser.showOpenDialog(new Stage());
    if (f != null) {
      communicator.setSpecieslist_filepath(f.getAbsolutePath());
    }
  }
}
