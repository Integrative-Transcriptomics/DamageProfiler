package org.damageprofiler.gui.dialogues;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.damageprofiler.services.ImageUtils;

public class AbstractApplication {

  private final Stage stage;
  protected final GridPane gridPane;
  protected int row;
  private final StackPane layout;

  public AbstractApplication(final String header, final String message) {

    stage = new Stage();
    stage.setTitle(header);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(new Stage());

    gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    layout = new StackPane();
    final Scene dialogScene = new Scene(layout, 600, 250);
    stage.setScene(dialogScene);

    fillGrid(message);
  }

  private void fillGrid(final String message) {

    final Label mssg = new Label();
    mssg.setWrapText(true);
    mssg.setText(message);
    row = 0;
    gridPane.add(mssg, 0, row, 2, 1);
    gridPane.add(new Separator(), 0, ++row, 2, 1);

    final ImageView imageView = ImageUtils.loadLogo();
    imageView.setOpacity(0.3);
    layout.getChildren().setAll(imageView, gridPane);
  }

  public void show() {
    stage.show();
  }

  public void close() {
    stage.close();
  }
}
