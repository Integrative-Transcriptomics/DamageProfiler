package GUI;

import IO.Communicator;
import calculations.StartCalculations;
import javafx.application.Application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;


public class MainGuiFX extends Application {

    private Button btn_inputfile;
    private Button btn_reference;
    private Button btn_output;
    private Button btn_plotting_options;
    private Button btn_run;
    private TextField textfield_threshold;
    private TextField textfield_length;
    private TextField textfield_specie;
    private CheckBox checkbox_use_mapped_reads;
    private TextField textfield_title;
    private CheckBox checkbox_dynamic_y_axis_height;
    private TextField textfield_y_axis_height;
    private Communicator communicator = new Communicator();
    private StartCalculations starter = new StartCalculations(null);

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("DamageProfiler configuration");

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(7);
        root.setVgap(7);

        addComponents(root);
        addListener();

        primaryStage.setScene(new Scene(root, 550, 400));
        primaryStage.setResizable(true);
        primaryStage.show();


    }


    private void addListener() {

        checkbox_dynamic_y_axis_height.selectedProperty().addListener((ov, old_val, new_val) -> {
            if(new_val){
                textfield_y_axis_height.setDisable(true);
            } else if(!new_val){
                textfield_y_axis_height.setDisable(false);
            }
        });



        btn_inputfile.setOnAction(e -> {

            BamFileChooserFX fqfc = new BamFileChooserFX(communicator);
            //Check if some input was truly selected

            if (checkIfInputWasSelected()) {
                btn_run.setDisable(false);
            } else {
                btn_run.setDisable(true);
            }

        });

        btn_reference.setOnAction(e -> {
                ReferenceFileChooserFX rfc = new ReferenceFileChooserFX(communicator);
                //Check if some input was truly selected

            if (checkIfInputWasSelected()) {
                btn_run.setDisable(false);
            } else {
                btn_run.setDisable(true);
            }

        });

        btn_output.setOnAction(e -> {

            OutputDirChooserFX rfc = new OutputDirChooserFX(communicator);

            if (checkIfInputWasSelected()) {
                btn_run.setDisable(false);
            } else {
                btn_run.setDisable(true);
            }

        });


        btn_run.setOnAction(e -> {

            // set all user options
            communicator.setLength(Integer.parseInt(textfield_length.getText()));
            communicator.setThreshold(Integer.parseInt(textfield_threshold.getText()));
            communicator.setSpecie_name(textfield_specie.getText());
            if(!checkbox_dynamic_y_axis_height.isSelected()){
                try {
                    communicator.setyAxis(Double.parseDouble(textfield_y_axis_height.getText()));
                } catch (Exception ex){
                    LOGGER.log(Level.INFO, "Height value not valid.");

                }
            }


            if(!textfield_title.getText().equals("")){
                communicator.setTitle_plots(textfield_title.getText());
            }


            try {
                starter.start(communicator);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });


    }

    private void addComponents(GridPane root) {

        btn_inputfile = new Button("Select input file");
        btn_reference = new Button("Select reference");
        btn_output = new Button("Select output");
        btn_plotting_options = new Button("Plotting options");
        btn_run = new Button("Run");

        Label label_threshold = new Label("Number of bases (x-axis)");
        Label label_yaxis = new Label("Height y-axis");
        Label label_length = new Label("Set number of bases (calculations)");
        Label label_specie = new Label("Filter specie");
        Label label_title = new Label("Set title");
        Label label_plot = new Label("Plot");
        label_plot.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Label label_calculations = new Label("Calculations");
        label_calculations.setFont(Font.font("Verdana", FontWeight.BOLD, 14));


        textfield_threshold = new TextField();
        textfield_length = new TextField();
        textfield_specie = new TextField();
        textfield_title = new TextField();
        textfield_y_axis_height = new TextField("Enter height");

        checkbox_use_mapped_reads = new CheckBox("Use only mapped reads");
        checkbox_dynamic_y_axis_height = new CheckBox("Dynamic");

        btn_run.setDisable(true);
        textfield_length.setText("100");
        textfield_threshold.setText("25");
        checkbox_dynamic_y_axis_height.setSelected(true);
        textfield_y_axis_height.setDisable(true);


        // add components to grid

        int row = 0;

        root.add(btn_inputfile, 0, row,1,1);
        root.add(btn_reference, 1, row,1,1);
        root.add(btn_output, 2, row,1,1);

        root.add(new Separator(), 0, ++row,3,1);

        //          PLOT

        root.add(label_plot, 0, ++row, 1,1);

        root.add(label_title, 0, ++row, 1,1);
        root.add(textfield_title, 1, row, 2,1);

        root.add(label_yaxis, 0, ++row, 1,1);
        root.add(checkbox_dynamic_y_axis_height, 1, row, 1,1);
        root.add(textfield_y_axis_height, 2, row, 1,1);

        root.add(label_threshold, 0, ++row, 1,1);
        root.add(textfield_threshold, 1, row, 2,1);

        root.add(label_specie, 0, ++row, 1,1);
        root.add(textfield_specie, 1, row, 2,1);

        root.add(checkbox_use_mapped_reads, 0, ++row,1,1);

        root.add(new Separator(), 0, ++row,3,1);

        //          CALCULATIONS

        root.add(label_calculations, 0, ++row, 1,1);
        root.add(label_length, 0, ++row, 1,1);
        root.add(textfield_length, 1, row, 2,1);

        root.add(new Separator(), 0, ++row,3,1);

        root.add(btn_run, 0, ++row,3,1);

    }

    private boolean checkIfInputWasSelected() {
        boolean tmp = false;
        if (communicator.getInput() != null && communicator.getReference() != null && communicator.getOutfolder() != null) {
            if (communicator.getInput().length() != 0) {
                tmp = true;
            }
        }
        return tmp;
    }



}
