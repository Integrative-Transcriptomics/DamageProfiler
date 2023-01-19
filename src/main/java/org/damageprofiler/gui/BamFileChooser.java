package org.damageprofiler.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.damageprofiler.io.Communicator;

public class BamFileChooser {

  public BamFileChooser(Communicator c) {

    FileChooser fileChooser = new FileChooser();
    fileChooser
        .getExtensionFilters()
        .addAll(
            new FileChooser.ExtensionFilter("BAM", "*.bam"),
            new FileChooser.ExtensionFilter("SAM", "*.sam"));

    List<File> f = fileChooser.showOpenMultipleDialog(new Stage());

    ArrayList<String> files = new ArrayList<>();

    if (f.size() != 0) {
      for (File file : f) {
        files.add(file.getAbsolutePath());
      }

      c.setInput(files.get(0));
    }
  }
}
