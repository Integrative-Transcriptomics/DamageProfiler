package GUI;

import GUI.Dialogues.AbstractDialogue;
import GUI.Plots.DamagePlot;
import GUI.Plots.IdentityHistPlot;
import GUI.Plots.LengthDistPlot;
import IO.Communicator;
import calculations.RuntimeEstimator;
import calculations.StartCalculations;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.lang.reflect.Field;

public class DamageProfilerMainController {

    private final Button btn_leftpane_info;
    private final Button btn_leftpane_identityDist;
    private final Button btn_leftpane_damageProfile;
    private final Button btn_leftpane_lengthDist;
    private Communicator communicator;
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
    private StartCalculations starter = new StartCalculations(null);
    private ProgressBar progressBar;
    private Task startCalculuations;
    private DamageProfilerMainGUI mainGUI;


    public DamageProfilerMainController(DamageProfilerMainGUI damageProfilerMainGUI){
        this.mainGUI = damageProfilerMainGUI;
        this.communicator = mainGUI.getCommunicator();
        this.btn_inputfile = mainGUI.getBtn_inputfile();
        this.btn_reference = mainGUI.getBtn_reference();
        this.btn_output = mainGUI.getBtn_output();
        this.btn_plotting_options = mainGUI.getBtn_plotting_options();
        this.btn_run = mainGUI.getBtn_run();
        this.btn_specieList = mainGUI.getBtn_specieList();
        this.textfield_threshold = mainGUI.getTextfield_threshold();
        this.textfield_length = mainGUI.getTextfield_length();
        this.textfield_specie = mainGUI.getTextfield_specie();
        this.checkbox_use_merged_reads = mainGUI.getCheckbox_use_merged_reads();
        this.checkbos_ssLibs_protocol = mainGUI.getCheckbos_ssLibs_protocol();
        this.textfield_title = mainGUI.getTextfield_title();
        //this.checkbox_dynamic_y_axis_height = mainGUI.get??;
        this.textfield_y_axis_height = mainGUI.getTextfield_y_axis_height();
        this.progressBar = mainGUI.getProgressBar();

        this.btn_leftpane_identityDist = mainGUI.getBtn_leftpane_identityDist();
        this.btn_leftpane_info = mainGUI.getBtn_leftpane_info();
        this.btn_leftpane_damageProfile = mainGUI.getBtn_leftpane_damageProfile();
        this.btn_leftpane_lengthDist = mainGUI.getBtn_leftpane_lengthDist();

        addListener();



    }

    private void addListener() {
        btn_inputfile.setOnAction(e -> {

            BamFileChooser fqfc = new BamFileChooser(communicator);
            Tooltip tooltip_input = new Tooltip(communicator.getInput());
            setTooltipDelay(tooltip_input);
            btn_inputfile.setTooltip(tooltip_input);

            if (checkIfInputWasSelected()) {
                btn_run.setDisable(false);

            } else {
                btn_run.setDisable(true);
            }

        });


        btn_reference.setOnAction(e -> {

            ReferenceFileChooser rfc = new ReferenceFileChooser(communicator);
            Tooltip tooltip_ref = new Tooltip(communicator.getReference());
            setTooltipDelay(tooltip_ref);
            btn_reference.setTooltip(tooltip_ref);

            if (checkIfInputWasSelected()) {
                btn_run.setDisable(false);
            } else {
                btn_run.setDisable(true);
            }

        });



        btn_output.setOnAction(e -> {

            OutputDirChooser rfc = new OutputDirChooser(communicator);
            Tooltip tooltip_output = new Tooltip(communicator.getOutfolder());
            setTooltipDelay(tooltip_output);
            btn_output.setTooltip(tooltip_output);

            if (checkIfInputWasSelected()) {
                btn_run.setDisable(false);

            } else {
                btn_run.setDisable(true);
            }

        });




        btn_run.setOnAction(e -> {

            RuntimeEstimator runtimeEstimator = new RuntimeEstimator(communicator.getInput());
            long estimatedRuntimeInSeconds = runtimeEstimator.getEstimatedRuntimeInSeconds();
            String text_estimatedRuntime;

            if(estimatedRuntimeInSeconds > 60) {
                long minutes = estimatedRuntimeInSeconds / 60;
                long seconds = estimatedRuntimeInSeconds % 60;
                text_estimatedRuntime = "Estimated Runtime: " + minutes + " minutes, and " + seconds + " seconds.";
            } else {
                if(estimatedRuntimeInSeconds == 0 ){
                    text_estimatedRuntime = "Estimated Runtime: Insignificant";
                } else {
                    text_estimatedRuntime = "Estimated Runtime: " + estimatedRuntimeInSeconds + " seconds.";
                }

            }

            AbstractDialogue runtimeInfoDialogue = new AbstractDialogue("Runtime information", "This gives you an estimate of the runtime. For large files with a long runtime,\nit's recommended to use the command line version of DamageProfiler.");
            Button btn_start = new Button("Proceed");
            Button btn_cancel = new Button("Cancel");

            int row = runtimeInfoDialogue.getRow();
            runtimeInfoDialogue.getGridPane().add(new Label("Number of reads: " + runtimeEstimator.getNumberOfRecords()), 0, ++row, 2,1);
            runtimeInfoDialogue.getGridPane().add(new Label(text_estimatedRuntime), 0, ++row, 2,1);
            runtimeInfoDialogue.getGridPane().add(btn_cancel, 0, ++row, 1,1);
            runtimeInfoDialogue.getGridPane().add(btn_start, 1, row, 1,1);

            runtimeInfoDialogue.show();

            btn_start.setOnAction(e_start -> {

                runtimeInfoDialogue.close();

                // set all user options
                communicator.setLength(Integer.parseInt(textfield_length.getText()));
                communicator.setThreshold(Integer.parseInt(textfield_threshold.getText()));
                communicator.setSsLibsProtocolUsed(checkbos_ssLibs_protocol.isSelected());

                if(textfield_specie.getText().equals(""))
                    communicator.setSpecies_ref_identifier(null);
                else
                    communicator.setSpecies_ref_identifier(textfield_specie.getText());

                //            if(!checkbox_dynamic_y_axis_height.isSelected()){
                //                try {
                //                    communicator.setyAxis_damageplot(Double.parseDouble(textfield_y_axis_height.getText()));
                //                } catch (Exception ex){
                //                    System.out.println("Height value not valid.");
                //                }
                //            }


                if(!textfield_title.getText().equals("")){
                    communicator.setTitle_plots(textfield_title.getText());
                }


                try {
                    // add progress indicator
                    progressBar.setProgress(0);
                    startCalculuations = startCalculations(communicator, runtimeEstimator);
                    progressBar.progressProperty().unbind();
                    progressBar.progressProperty().bind(startCalculuations.progressProperty());

                    startCalculuations.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                            (EventHandler<WorkerStateEvent>) t -> {
                                if(starter.isCalculationsDone()){
                                    // replace config with result GUI
                                    btn_leftpane_lengthDist.setDisable(false);
                                    btn_leftpane_identityDist.setDisable(false);
                                    btn_leftpane_damageProfile.setDisable(false);
                                    generateDamageProfile();
                                }
                            });

                    new Thread(startCalculuations).start();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });

            btn_cancel.setOnAction(e_cancel -> {

                runtimeInfoDialogue.close();

            });
        });


        btn_specieList.setOnAction(e -> {

            SpeciesListFileChooser slfc = new SpeciesListFileChooser(communicator);
            if (checkIfInputWasSelected()) {
                btn_specieList.setDisable(false);
            } else {
                btn_specieList.setDisable(true);
            }


        });


        btn_leftpane_damageProfile.setOnAction(e -> {
            if(starter.isCalculationsDone()){
                if(plotAlreadyGenerated()){
                    // show plot
                } else {
                    // generate plot
                    generateDamageProfile();

                }
            } else {
                mainGUI.getRoot().setCenter(null);
            }
        });


        btn_leftpane_identityDist.setOnAction(e -> {
            if(starter.isCalculationsDone()){
                if(plotAlreadyGenerated()){
                    // show plot
                } else {
                    // generate plot
                    generateIdentityDist();

                }
            } else {
                mainGUI.getRoot().setCenter(null);            }
        });

        btn_leftpane_lengthDist.setOnAction(e -> {
            if(starter.isCalculationsDone()){
                if(plotAlreadyGenerated()){
                    // show plot
                } else {
                    // generate plot
                    generateLengthDist();

                }
            } else {
                mainGUI.getRoot().setCenter(null);
            }
        });


    }

    private void generateLengthDist() {
        LengthDistPlot lengthDistPlot = new LengthDistPlot();
        mainGUI.getRoot().setCenter(lengthDistPlot.getBc());

    }

    private void generateIdentityDist() {

        IdentityHistPlot identityHistPlot = new IdentityHistPlot();
        mainGUI.getRoot().setCenter(identityHistPlot.getRoot());

    }

    private void generateDamageProfile() {
        DamagePlot damagePlot = new DamagePlot();
        mainGUI.getRoot().setCenter(damagePlot.getLineChart());
    }

    // todo
    private boolean plotAlreadyGenerated() {
        return false;
    }


    public Task startCalculations(Communicator communicator, RuntimeEstimator runtimeEstimator) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                starter.start(communicator, runtimeEstimator);
                return true;
            }
        };
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



    private static void setTooltipDelay(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
