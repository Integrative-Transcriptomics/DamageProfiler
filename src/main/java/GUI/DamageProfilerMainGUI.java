package GUI;

import GUI.Dialogues.ConfigurationDialogue;
import IO.Communicator;
import controller.ProgressBarController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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


    public DamageProfilerMainGUI(String version, ProgressBarController progressBarController) {
        this.version = version;
        this.progressBarController = progressBarController;
    }

    public void init(Stage primaryStage){

        //JMetro jMetro = new JMetro(Style.LIGHT);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("DamageProfiler v" + version);

        root = new BorderPane();

        config_dialogue = new ConfigurationDialogue(progressBarController.getProgressBar());

        root.setCenter(config_dialogue.getConfig_gridpane());
        root.setLeft(generateLeftPane());

        //jMetro.setScene(new Scene(root, 750, 500));
        //this.primaryStage.setScene(jMetro.getScene());
        this.primaryStage.setScene(new Scene(root, 900, 600));
        this.primaryStage.setResizable(true);
        this.primaryStage.show();

    }

    private VBox generateLeftPane() {
        VBox leftPanel = new VBox();
        btn_leftpane_damageProfile = new Button("Damage Plot");
        btn_leftpane_info = new Button("Run Configuration");
        btn_leftpane_lengthDist = new Button("Length Distribution");
        btn_leftpane_identityDist = new Button("Identity Distribution");

        // style buttons
        btn_leftpane_info.setPrefHeight(30);
        btn_leftpane_info.setPrefWidth(200);

        btn_leftpane_damageProfile.setPrefHeight(30);
        btn_leftpane_damageProfile.setPrefWidth(200);
        btn_leftpane_damageProfile.setDisable(true);

        btn_leftpane_lengthDist.setPrefHeight(30);
        btn_leftpane_lengthDist.setPrefWidth(200);
        btn_leftpane_lengthDist.setDisable(true);

        btn_leftpane_identityDist.setPrefHeight(30);
        btn_leftpane_identityDist.setPrefWidth(200);
        btn_leftpane_identityDist.setDisable(true);

        leftPanel.getChildren().addAll(btn_leftpane_info, btn_leftpane_damageProfile, btn_leftpane_lengthDist, btn_leftpane_identityDist);
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

    public Button getBtn_leftpane_info() {
        return btn_leftpane_info;
    }

    public Button getBtn_leftpane_damageProfile() {
        return btn_leftpane_damageProfile;
    }

    public Button getBtn_leftpane_lengthDist() {
        return btn_leftpane_lengthDist;
    }

    public ConfigurationDialogue getConfig_dialogue() {
        return config_dialogue;
    }
}
