package org.damageprofiler.gui;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.damageprofiler.io.Communicator;

public class ReferenceFileChooser {

  private final Communicator communicator;

  public ReferenceFileChooser(final Communicator c) {
    this.communicator = c;
  }

  public void open() {
    final FileChooser fileChooser = new FileChooser();

    fileChooser
        .getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("Fasta", "*.fa", "*.fasta", ".fas"));

    final File f = fileChooser.showOpenDialog(new Stage());
    if (f != null) {
      communicator.setReference(f.getAbsolutePath());
    }
  }
}
