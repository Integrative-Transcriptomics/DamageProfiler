package GUI;

import IO.Communicator;
import calculations.StartCalculations;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class DamageProfilerMainGUI {


    private final String version;
    private Stage primaryStage;
    private Button btn_inputfile;
    private Button btn_reference;
    private Button btn_output;
    private Button btn_plotting_options;
    private Button btn_run;
    private Button btn_specieList;
    private TextField textfield_threshold;
    private TextField textfield_length;
    private TextField textfield_specie;
    private CheckBox checkbox_use_merged_reads;
    private CheckBox checkbos_ssLibs_protocol;
    private TextField textfield_title;
    //private CheckBox checkbox_dynamic_y_axis_height;
    private TextField textfield_y_axis_height;
    private Communicator communicator = new Communicator();
    private StartCalculations starter = new StartCalculations(null);
    private ProgressBar progressBar;
    private BorderPane root;
    private GridPane config_gridpane;
    private Button btn_leftpane_identityDist;
    private Button btn_leftpane_info;
    private Button btn_leftpane_damageProfile;
    private Button btn_leftpane_lengthDist;


    public DamageProfilerMainGUI(String version) {
        this.version = version;
    }

    public void init(Stage primaryStage){

        //JMetro jMetro = new JMetro(Style.LIGHT);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("DamageProfiler v" + version);

        root = new BorderPane();

        config_gridpane = new GridPane();
        config_gridpane.setAlignment(Pos.CENTER);
        config_gridpane.setHgap(7);
        config_gridpane.setVgap(7);
        config_gridpane.setPadding(new Insets(10,10,10,10));

        addComponents(config_gridpane);
        addLeftPane();

        root.setCenter(config_gridpane);
        //jMetro.setScene(new Scene(root, 750, 500));
        //this.primaryStage.setScene(jMetro.getScene());
        this.primaryStage.setScene(new Scene(root, 900, 600));
        this.primaryStage.setResizable(true);
        this.primaryStage.show();

    }

    private void addLeftPane() {
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
        root.setLeft(leftPanel);
    }

    private void addComponents(GridPane root) {

        btn_inputfile = new Button("Select input file");
        btn_reference = new Button("Select reference");
        btn_output = new Button("Select output");
        btn_plotting_options = new Button("Plotting options");
        btn_specieList = new Button("Set list");
        btn_run = new Button("Run");

        Label label_threshold = new Label("Number of bases (x-axis)");
        Label label_yaxis = new Label("Height y-axis");
        Label label_length = new Label("Set number of bases (calculations)");
        Label label_specie = new Label("Filter for specie");
        Label label_title = new Label("Set title");
        Label label_plot = new Label("Plot");
        label_plot.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Label label_files = new Label("Files");
        label_files.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        Label label_calculations = new Label("Calculations");
        label_calculations.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        Label label_title_config = new Label("Configuration");
        label_title_config.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        progressBar = new ProgressBar(0);

        textfield_threshold = new TextField();
        textfield_length = new TextField();
        textfield_specie = new TextField();
        textfield_title = new TextField();
        textfield_y_axis_height = new TextField();

        checkbox_use_merged_reads = new CheckBox("Only merged reads");
        checkbos_ssLibs_protocol = new CheckBox("Single-stranded library protocol");
        //checkbox_dynamic_y_axis_height = new CheckBox("Dynamic");

        btn_run.setDisable(true);
        textfield_length.setText("100");
        textfield_threshold.setText("25");
        textfield_y_axis_height.setText("0.4");
        //checkbox_dynamic_y_axis_height.setSelected(true);
        //textfield_y_axis_height.setDisable(true);


        // add components to grid

        int row = 0;

        root.add(label_title_config, 0, row, 1,1);
        root.add(new Separator(), 0, ++row,3,1);


        root.add(label_files, 0, ++row, 1,1);
        root.add(btn_inputfile, 0, ++row,1,1);
        root.add(btn_reference, 1, row,1,1);
        root.add(btn_output, 2, row,1,1);
        root.add(new Separator(), 0, ++row,3,1);

        //          PLOT

        root.add(label_plot, 0, ++row, 1,1);
        root.add(label_title, 0, ++row, 1,1);
        root.add(textfield_title, 1, row, 2,1);
        root.add(label_yaxis, 0, ++row, 1,1);
        //root.add(checkbox_dynamic_y_axis_height, 1, row, 1,1);
        root.add(textfield_y_axis_height, 1, row, 2,1);
        root.add(label_threshold, 0, ++row, 1,1);
        root.add(textfield_threshold, 1, row, 2,1);
        root.add(label_specie, 0, ++row, 1,1);
        root.add(textfield_specie, 1, row, 2,1);
        root.add(btn_specieList, 3, row, 1,1);
        root.add(new Separator(), 0, ++row,3,1);

        //          CALCULATIONS
        root.add(label_calculations, 0, ++row, 1,1);
        root.add(checkbox_use_merged_reads, 0, ++row,1,1);
        root.add(checkbos_ssLibs_protocol, 0, ++row, 1,1);
        root.add(label_length, 0, ++row, 1,1);
        root.add(textfield_length, 1, row, 2,1);
        root.add(new Separator(), 0, ++row,3,1);
        root.add(btn_run, 0, ++row,1,1);
        root.add(progressBar, 1, row,1,1);


    }


    public Button getBtn_inputfile() {
        return btn_inputfile;
    }

    public Button getBtn_reference() {
        return btn_reference;
    }

    public Button getBtn_output() {
        return btn_output;
    }

    public Button getBtn_plotting_options() {
        return btn_plotting_options;
    }

    public Button getBtn_run() {
        return btn_run;
    }

    public Button getBtn_specieList() {
        return btn_specieList;
    }

    public TextField getTextfield_threshold() {
        return textfield_threshold;
    }

    public TextField getTextfield_length() {
        return textfield_length;
    }

    public TextField getTextfield_specie() {
        return textfield_specie;
    }

    public CheckBox getCheckbox_use_merged_reads() {
        return checkbox_use_merged_reads;
    }

    public CheckBox getCheckbos_ssLibs_protocol() {
        return checkbos_ssLibs_protocol;
    }

    public TextField getTextfield_title() {
        return textfield_title;
    }

    public TextField getTextfield_y_axis_height() {
        return textfield_y_axis_height;
    }

    public Communicator getCommunicator() {
        return communicator;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public BorderPane getRoot() {
        return root;
    }

    public GridPane getConfig_gridpane() {
        return config_gridpane;
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
}
