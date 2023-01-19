package org.damageprofiler.gui.dialogues;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import org.damageprofiler.io.Communicator;

public class RunInfoDialogue extends AbstractDialogue {

  private final Communicator communicator;
  private final Button btn_new_config;
  private ScrollPane scrollPaneRef;
  private ScrollPane scrollPaneInput;
  private ScrollPane scrollPaneOutput;

  public RunInfoDialogue(String message, Communicator communicator) {
    super(message);
    this.communicator = communicator;
    btn_new_config = new Button("New configuration");
    init();
  }

  public void init() {

    scrollPaneInput = new ScrollPane(new Label(communicator.getInput()));
    scrollPaneOutput = new ScrollPane(new Label(communicator.getOutfolder()));
    scrollPaneRef = new ScrollPane(new Label(communicator.getReference()));

    Label label_input = new Label("Input file: ");
    Label label_output = new Label("Output folder: ");
    Label label_ref = new Label("Reference file: ");

    gridPane.add(label_input, 0, ++row, 1, 1);
    gridPane.add(scrollPaneInput, 1, row, 2, 1);

    gridPane.add(label_output, 0, ++row, 1, 1);
    gridPane.add(scrollPaneOutput, 1, row, 2, 1);

    gridPane.add(label_ref, 0, ++row, 1, 1);
    gridPane.add(scrollPaneRef, 1, row, 2, 1);

    gridPane.add(new Separator(), 0, ++row, 1, 2);

    gridPane.add(btn_new_config, 0, ++row, 1, 1);
  }

  public void updateParameters() {
    scrollPaneInput.setContent(new Label(communicator.getInput()));
    scrollPaneOutput.setContent(new Label(communicator.getOutfolder()));
    scrollPaneRef.setContent(new Label(communicator.getReference()));
  }

  public Button getBtn_new_config() {
    return btn_new_config;
  }
}
