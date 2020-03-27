package GUI.Dialogues;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ConfigurationDialogue {


    private GridPane config_gridpane;

    private Button btn_inputfile;
    private Button btn_reference;
    private Button btn_output;
    private Button btn_run;
    private Button btn_estimate_runtime;
    private Button btn_specieList;

    private TextField textfield_threshold;
    private TextField textfield_length;
    private TextField textfield_specie;
    private CheckBox checkbox_use_merged_reads;
    private CheckBox checkbox_ssLibs_protocol;
    private TextField textfield_title;
    //private CheckBox checkbox_dynamic_y_axis_height;
    private TextField textfield_y_axis_height;

    private ProgressBar progressBar;


    public ConfigurationDialogue(ProgressBar progressBar){

        config_gridpane = new GridPane();
        config_gridpane.setAlignment(Pos.CENTER);
        config_gridpane.setHgap(7);
        config_gridpane.setVgap(7);
        config_gridpane.setPadding(new Insets(10,10,10,10));

        this.progressBar = progressBar;
        addComponents();

    }

    private void addComponents() {

        btn_inputfile = new Button("Select input file");
        btn_reference = new Button("Select reference");
        btn_output = new Button("Select output");
        btn_specieList = new Button("Set list");
        btn_run = new Button("Run");
        btn_estimate_runtime = new Button("Estimate Runtime");

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

        textfield_threshold = new TextField();
        textfield_length = new TextField();
        textfield_specie = new TextField();
        textfield_title = new TextField();
        textfield_y_axis_height = new TextField();

        checkbox_use_merged_reads = new CheckBox("Only merged reads");
        checkbox_ssLibs_protocol = new CheckBox("Single-stranded library protocol");
        //checkbox_dynamic_y_axis_height = new CheckBox("Dynamic");

        btn_run.setDisable(true);
        btn_estimate_runtime.setDisable(true);
        textfield_length.setText("100");
        textfield_threshold.setText("25");
        textfield_y_axis_height.setText("0.4");

        TitledPane pane_advanced_plotting_options = new TitledPane();
        pane_advanced_plotting_options.setText("Advanced options (Plotting)");
        AdvancedPlottingOptionsDialogue advancedPlottingOptionsDialogue = new AdvancedPlottingOptionsDialogue();
        pane_advanced_plotting_options.setContent(advancedPlottingOptionsDialogue.getGridPane());
        pane_advanced_plotting_options.setExpanded(false);

        TitledPane pane_advanced_calculation_options = new TitledPane();
        pane_advanced_calculation_options.setText("Advanced options (Calculation)");
        HBox hbox_length_calc = new HBox();
        hbox_length_calc.setPadding(new Insets(10,10,10,10));
        hbox_length_calc.getChildren().addAll(label_length, textfield_length);
        pane_advanced_calculation_options.setContent(hbox_length_calc);
        pane_advanced_calculation_options.setExpanded(false);


        // add components to grid

        int row = 0;

        config_gridpane.add(label_title_config, 0, row, 1,1);
        config_gridpane.add(new Separator(), 0, ++row,3,1);


        config_gridpane.add(label_files, 0, ++row, 1,1);
        config_gridpane.add(btn_inputfile, 0, ++row,1,1);
        config_gridpane.add(btn_reference, 1, row,1,1);
        config_gridpane.add(btn_output, 2, row,1,1);
        config_gridpane.add(new Separator(), 0, ++row,3,1);

        //          PLOT

        config_gridpane.add(label_plot, 0, ++row, 1,1);
        config_gridpane.add(label_title, 0, ++row, 1,1);
        config_gridpane.add(textfield_title, 1, row, 2,1);
        config_gridpane.add(label_yaxis, 0, ++row, 1,1);
        //config_gridpane.add(checkbox_dynamic_y_axis_height, 1, row, 1,1);
        config_gridpane.add(textfield_y_axis_height, 1, row, 2,1);
        config_gridpane.add(label_threshold, 0, ++row, 1,1);
        config_gridpane.add(textfield_threshold, 1, row, 2,1);
        config_gridpane.add(pane_advanced_plotting_options, 0,++row, 3,1);
        config_gridpane.add(new Separator(), 0, ++row,3,1);

        //          CALCULATIONS
        config_gridpane.add(label_calculations, 0, ++row, 1,1);
        config_gridpane.add(checkbox_use_merged_reads, 0, ++row,1,1);
        config_gridpane.add(checkbox_ssLibs_protocol, 0, ++row, 1,1);
        config_gridpane.add(label_specie, 0, ++row, 1,1);
        config_gridpane.add(textfield_specie, 1, row, 2,1);
        config_gridpane.add(btn_specieList, 3, row, 1,1);
        config_gridpane.add(pane_advanced_calculation_options, 0,++row, 3,1);

        config_gridpane.add(new Separator(), 0, ++row,3,1);
        config_gridpane.add(btn_estimate_runtime, 0, ++row,1,1);
        config_gridpane.add(btn_run, 1, row,1,1);
        config_gridpane.add(progressBar, 2, row,1,1);

    }

    public GridPane getConfig_gridpane() {
        return config_gridpane;
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

    public CheckBox getCheckbox_ssLibs_protocol() {
        return checkbox_ssLibs_protocol;
    }

    public TextField getTextfield_title() {
        return textfield_title;
    }

    public TextField getTextfield_y_axis_height() {
        return textfield_y_axis_height;
    }

    public Button getBtn_estimate_runtime() {
        return btn_estimate_runtime;
    }
}
