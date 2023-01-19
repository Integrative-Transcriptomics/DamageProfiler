package org.damageprofiler.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.damageprofiler.controller.ProgressBarController;
import org.damageprofiler.gui.dialogues.ConfigurationDialogue;
import org.damageprofiler.io.Communicator;
import org.damageprofiler.services.ImageUtils;
import org.damageprofiler.services.Version;

public class DamageProfilerMainGUI {

  private final ProgressBarController progressBarController;
  private final Communicator communicator = new Communicator();
  private BorderPane root;
  private ConfigurationDialogue config_dialogue;
  private Button btn_leftpane_info;
  private Button btn_help;

  public DamageProfilerMainGUI(final ProgressBarController progressBarController) {
    this.progressBarController = progressBarController;
  }

  public void init(final Stage primaryStage) {

    primaryStage.setTitle("DamageProfiler v" + Version.VERSION.getValue());

    root = new BorderPane();

    config_dialogue = new ConfigurationDialogue(progressBarController.getProgressBar());

    final ScrollPane scrollPane_adv_config = new ScrollPane();
    scrollPane_adv_config.setPadding(new Insets(10, 10, 10, 10));
    scrollPane_adv_config.setContent(config_dialogue.getConfig_gridpane());
    root.setCenter(scrollPane_adv_config);
    root.setLeft(generateLeftPane());

    primaryStage.setScene(new Scene(root, 1100, 700));
    primaryStage.setResizable(true);
    primaryStage.show();
  }

  private VBox generateLeftPane() {

    // Image Source
    final ImageView imageView = ImageUtils.loadLogo();

    final HBox hbxImg = new HBox();
    hbxImg.setPadding(new Insets(10, 0, 10, 0));
    hbxImg.setAlignment(Pos.CENTER);
    hbxImg.getChildren().add(imageView);

    final VBox leftPanel = new VBox();
    btn_leftpane_info = new Button("Configuration");
    btn_help = new Button("Show help");

    // style buttons
    btn_leftpane_info.setPrefHeight(30);
    btn_leftpane_info.setPrefWidth(200);

    btn_help.setPrefHeight(30);
    btn_help.setPrefWidth(200);

    leftPanel.getChildren().addAll(hbxImg, btn_leftpane_info, btn_help);
    leftPanel.setPadding(new Insets(10, 10, 10, 10));

    return leftPanel;
  }

  public Communicator getCommunicator() {
    return communicator;
  }

  public BorderPane getRoot() {
    return root;
  }

  public Button getBtn_leftpane_info() {
    return btn_leftpane_info;
  }

  public Button getBtn_help() {
    return btn_help;
  }

  public ConfigurationDialogue getConfig_dialogue() {
    return config_dialogue;
  }
}
