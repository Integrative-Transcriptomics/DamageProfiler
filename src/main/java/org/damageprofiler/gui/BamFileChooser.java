package org.damageprofiler.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.damageprofiler.io.Communicator;

public class BamFileChooser {
  private final Communicator communicator;

  public BamFileChooser(final Communicator c) {
    this.communicator = c;
  }

  public void open() {

    final FileChooser fileChooser = new FileChooser();
    fileChooser
        .getExtensionFilters()
        .addAll(
            new FileChooser.ExtensionFilter("BAM", "*.bam"),
            new FileChooser.ExtensionFilter("SAM", "*.sam"));

    final List<File> f = fileChooser.showOpenMultipleDialog(new Stage());

    final ArrayList<String> files = new ArrayList<>();

    if (f.size() != 0) {
      for (final File file : f) {
        files.add(file.getAbsolutePath());
      }

      this.communicator.setInput(files.get(0));
    }
  }
}
