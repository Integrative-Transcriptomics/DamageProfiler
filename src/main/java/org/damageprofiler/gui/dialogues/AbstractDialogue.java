package org.damageprofiler.gui.dialogues;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AbstractDialogue {

  protected final GridPane gridPane;
  protected int row;

  public AbstractDialogue(String message) {
    gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(15, 15, 15, 15));

    fillGrid(message);
  }

  private void fillGrid(String message) {

    Label label_message = new Label(message);
    label_message.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

    row = 0;
    gridPane.add(label_message, 0, row, 2, 1);
    gridPane.add(new Separator(), 0, ++row, 2, 1);
  }

  public GridPane getGridPane() {
    return gridPane;
  }
}
