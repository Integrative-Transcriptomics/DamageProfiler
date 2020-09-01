package org.damageprofiler.gui.dialogues;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Objects;

public class AbstractApplication {

    private final Stage stage;
    protected final GridPane gridPane;
    protected int row;
    private StackPane layout;

    public AbstractApplication(String header, String message){

        stage = new Stage();
        stage.setTitle(header);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(new Stage());

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10,10,10,10));
        layout = new StackPane();
        Scene dialogScene = new Scene(layout, 600, 250);
        stage.setScene(dialogScene);

        fillGrid(message);

    }

    private void fillGrid(String message) {

        Label mssg = new Label();
        mssg.setWrapText(true);
        mssg.setText(message);
        row = 0;
        gridPane.add(mssg, 0,row,2,1);
        gridPane.add(new Separator(), 0,++row,2,1);

        InputStream input = getClass().getClassLoader().getResourceAsStream("logo.png");
        assert input != null;
        Image image = new Image(Objects.requireNonNull(input));
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(0.3);
        layout.getChildren().setAll(imageView, gridPane);

    }

    public void show(){
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
