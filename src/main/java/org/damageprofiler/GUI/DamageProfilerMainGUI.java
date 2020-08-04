package org.damageprofiler.GUI;

import org.damageprofiler.GUI.Dialogues.ConfigurationDialogue;
import org.damageprofiler.IO.Communicator;
import org.damageprofiler.controller.ProgressBarController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;

public class DamageProfilerMainGUI {


    private final String version;
    private final ProgressBarController progressBarController;
    private Stage primaryStage;
    private Communicator communicator = new Communicator();
    private BorderPane root;
    private ConfigurationDialogue config_dialogue;
    private Button btn_leftpane_identityDist;
    private Button btn_leftpane_info;
    private Button btn_leftpane_damageProfile;
    private Button btn_leftpane_lengthDist;
    private Button btn_help;


    public DamageProfilerMainGUI(String version, ProgressBarController progressBarController) {
        this.version = version;
        this.progressBarController = progressBarController;
    }

    public void init(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("DamageProfiler v" + version);

        root = new BorderPane();

        config_dialogue = new ConfigurationDialogue(progressBarController.getProgressBar());

        ScrollPane scrollPane_adv_config = new ScrollPane();
        scrollPane_adv_config.setPadding(new Insets(10,10,10,10));
        scrollPane_adv_config.setContent(config_dialogue.getConfig_gridpane());
        root.setCenter(scrollPane_adv_config);
        root.setLeft(generateLeftPane());

        this.primaryStage.setScene(new Scene(root, 950, 700));
        this.primaryStage.setResizable(true);
        this.primaryStage.show();

    }

    private VBox generateLeftPane() {

        // Image Source
        InputStream input= getClass().getClassLoader().getResourceAsStream("logo.png");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);

        // Create a Label with label and Icon
        Label label = new Label("", imageView);



        VBox leftPanel = new VBox();
        btn_leftpane_damageProfile = new Button("Damage Plot");
        btn_leftpane_info = new Button("Run Configuration");
        btn_help = new Button("Show help");
        btn_leftpane_lengthDist = new Button("Length Distribution");
        btn_leftpane_identityDist = new Button("Edit distance");

        // style buttons
        btn_leftpane_info.setPrefHeight(30);
        btn_leftpane_info.setPrefWidth(200);

        btn_help.setPrefHeight(30);
        btn_help.setPrefWidth(200);

        btn_leftpane_damageProfile.setPrefHeight(30);
        btn_leftpane_damageProfile.setPrefWidth(200);
        btn_leftpane_damageProfile.setDisable(true);

        btn_leftpane_lengthDist.setPrefHeight(30);
        btn_leftpane_lengthDist.setPrefWidth(200);
        btn_leftpane_lengthDist.setDisable(true);

        btn_leftpane_identityDist.setPrefHeight(30);
        btn_leftpane_identityDist.setPrefWidth(200);
        btn_leftpane_identityDist.setDisable(true);

        leftPanel.getChildren().addAll(label, btn_leftpane_info, btn_leftpane_damageProfile, btn_leftpane_lengthDist, btn_leftpane_identityDist, btn_help);
        leftPanel.setPadding(new Insets(10,10,10,10));

        return leftPanel;
    }



    public Communicator getCommunicator() {
        return communicator;
    }

    public BorderPane getRoot() {
        return root;
    }

    public Button getBtn_leftpane_identityDist() {
        return btn_leftpane_identityDist;
    }

    public Button getBtn_leftpane_info() { return btn_leftpane_info; }

    public Button getBtn_leftpane_damageProfile() {
        return btn_leftpane_damageProfile;
    }

    public Button getBtn_leftpane_lengthDist() {
        return btn_leftpane_lengthDist;
    }

    public Button getBtn_help() { return btn_help; }

    public ConfigurationDialogue getConfig_dialogue() { return config_dialogue; }

    public String getVersion() { return version; }
}
