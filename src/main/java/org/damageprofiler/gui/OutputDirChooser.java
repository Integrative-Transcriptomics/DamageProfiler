package org.damageprofiler.gui;

import java.io.File;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.damageprofiler.io.Communicator;

public class OutputDirChooser {
  private final Communicator communicator;

  public OutputDirChooser(final Communicator c) {
    this.communicator = c;
  }

  public void open() {
    final DirectoryChooser dirChooser = new DirectoryChooser();
    final File f = dirChooser.showDialog(new Stage());
    if (f != null) {
      communicator.setOutfolder(f.getAbsolutePath());
    }
  }
}
