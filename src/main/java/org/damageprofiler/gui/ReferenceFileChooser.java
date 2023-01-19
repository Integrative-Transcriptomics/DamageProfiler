package org.damageprofiler.gui;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.damageprofiler.io.Communicator;

public class ReferenceFileChooser {

  public ReferenceFileChooser(Communicator c) {

    FileChooser fileChooser = new FileChooser();

    fileChooser
        .getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("Fasta", "*.fa", "*.fasta", ".fas"));

    File f = fileChooser.showOpenDialog(new Stage());
    if (f != null) {
      c.setReference(f.getAbsolutePath());
    }
  }
}
