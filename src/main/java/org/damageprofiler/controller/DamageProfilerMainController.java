package org.damageprofiler.controller;

import org.damageprofiler.GUI.*;
import org.damageprofiler.GUI.Dialogues.AdvancedPlottingOptionsDialogue;
import org.damageprofiler.GUI.Dialogues.HelpDialogue;
import org.damageprofiler.GUI.Dialogues.RunInfoDialogue;
import org.damageprofiler.GUI.Dialogues.RuntimeEstimatorDialogue;
import org.damageprofiler.calculations.RuntimeEstimator;
import org.damageprofiler.calculations.StartCalculations;
import org.damageprofiler.IO.Communicator;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;

public class DamageProfilerMainController {

    private final Button btn_leftpane_run_config;
    private final Button btn_leftpane_identityDist;
    private final Button btn_leftpane_damageProfile;
    private final Button btn_leftpane_lengthDist;
    private final ProgressBarController progressBarController;
    private final Button btn_estimate_runtime;
    private final Button btn_help;
    private final HelpDialogue help_dialogue;
    private final AdvancedPlottingOptionsDialogue advancedPlottingOptionsDialogue;
    private final PlottingSettingController plottingSettingController;
    private RunInfoDialogue runInfoDialogue;
    private Communicator communicator;
    private Button btn_inputfile;
    private Button btn_reference;
    private Button btn_output;
    private Button btn_run;
    private Button btn_speciesList;
    private TextField textfield_threshold;
    private TextField textfield_length;
    private TextField textfield_species;
    private CheckBox checkbox_use_merged_reads;
    private CheckBox checkbox_ssLibs_protocol;
    private TextField textfield_title;
    private TextField textfield_y_axis_height;
    private StartCalculations starter = new StartCalculations();
    private DamageProfilerMainGUI mainGUI;
    private RuntimeEstimatorDialogue runtimeInfoDialogue;
    /**
     * Constructor
     * @param damageProfilerMainGUI
     * @param progressBarController
     * @param plottingSettingController
     */
    public DamageProfilerMainController(DamageProfilerMainGUI damageProfilerMainGUI,
                                        ProgressBarController progressBarController,
                                        PlottingSettingController plottingSettingController){

        this.mainGUI = damageProfilerMainGUI;
        this.progressBarController = progressBarController;
        this.communicator = mainGUI.getCommunicator();
        runInfoDialogue = new RunInfoDialogue("Run configuration", communicator);
        starter.setVERSION(damageProfilerMainGUI.getVersion());
        this.help_dialogue = new HelpDialogue();

        this.btn_inputfile = mainGUI.getConfig_dialogue().getBtn_inputfile();
        this.btn_reference = mainGUI.getConfig_dialogue().getBtn_reference();
        this.btn_output = mainGUI.getConfig_dialogue().getBtn_output();
        this.btn_run = mainGUI.getConfig_dialogue().getBtn_run();
        this.btn_estimate_runtime = mainGUI.getConfig_dialogue().getBtn_estimate_runtime();
        this.btn_speciesList = mainGUI.getConfig_dialogue().getBtn_speciesList();
        this.btn_leftpane_identityDist = mainGUI.getBtn_leftpane_identityDist();
        this.btn_leftpane_run_config = mainGUI.getBtn_leftpane_info();
        this.btn_help = mainGUI.getBtn_help();
        this.btn_leftpane_damageProfile = mainGUI.getBtn_leftpane_damageProfile();
        this.btn_leftpane_lengthDist = mainGUI.getBtn_leftpane_lengthDist();

        this.textfield_threshold = mainGUI.getConfig_dialogue().getTextfield_threshold();
        this.textfield_length = mainGUI.getConfig_dialogue().getTextfield_length();
        this.textfield_species = mainGUI.getConfig_dialogue().getTextfield_species();
        this.textfield_title = mainGUI.getConfig_dialogue().getTextfield_title();
        this.textfield_y_axis_height = mainGUI.getConfig_dialogue().getTextfield_y_axis_height();

        this.checkbox_use_merged_reads = mainGUI.getConfig_dialogue().getCheckbox_use_merged_reads();
        this.checkbox_ssLibs_protocol = mainGUI.getConfig_dialogue().getCheckbox_ssLibs_protocol();


        // attributes of advanced plotting settings
        this.plottingSettingController = plottingSettingController;
        this.advancedPlottingOptionsDialogue = this.mainGUI.getConfig_dialogue().getAdvancedPlottingOptionsDialogue();
        this.plottingSettingController.addListener(this.advancedPlottingOptionsDialogue);


        runtimeInfoDialogue = new RuntimeEstimatorDialogue("Runtime information",
                "This gives you an estimate of the runtime. For large files with a long runtime,\n" +
                        "it's recommended to use the command line version of DamageProfiler.");

        setColorsPlotting();
        addListener();

    }

    private void setColorsPlotting() {

        communicator.setColor_DP_C_to_T(this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_C_to_T().getValue());
        communicator.setColor_DP_G_to_A(this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_G_to_A().getValue());
        communicator.setColor_DP_insertions(this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_insertions().getValue());
        communicator.setColor_DP_deletions(this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_deletions().getValue());
        communicator.setColor_DP_other(this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_others().getValue());

    }

    private void addListener() {

        btn_inputfile.setOnAction(e -> {

            BamFileChooser fqfc = new BamFileChooser(communicator);
            if (communicator.getInput() != null){
                Tooltip tooltip_input = new Tooltip(communicator.getInput());
                //setTooltipDelay(tooltip_input);
                btn_inputfile.setTooltip(tooltip_input);

                String filepath = communicator.getInput().substring(0, communicator.getInput().lastIndexOf('.'));
                String filename = filepath.split("/")[filepath.split("/").length-1];
                mainGUI.getConfig_dialogue().setTextfield_title(filename);

                if (checkIfInputWasSelected()) {
                    btn_run.setDisable(false);
                    btn_estimate_runtime.setDisable(false);

                } else {
                    btn_run.setDisable(true);
                    btn_estimate_runtime.setDisable(true);
                }
            }


        });

        btn_help.setOnAction(e -> {
            mainGUI.getRoot().setCenter(new ScrollPane(this.help_dialogue.getGridPane()));
        });

        btn_reference.setOnAction(e -> {

            ReferenceFileChooser rfc = new ReferenceFileChooser(communicator);
            Tooltip tooltip_ref = new Tooltip(communicator.getReference());
            //setTooltipDelay(tooltip_ref);
            btn_reference.setTooltip(tooltip_ref);

            if (checkIfInputWasSelected()) {
                btn_run.setDisable(false);
                btn_estimate_runtime.setDisable(false);
            } else {
                btn_run.setDisable(true);
                btn_estimate_runtime.setDisable(true);
            }

        });



        btn_output.setOnAction(e -> {

            OutputDirChooser rfc = new OutputDirChooser(communicator);
            Tooltip tooltip_output = new Tooltip(communicator.getOutfolder());
            //setTooltipDelay(tooltip_output);
            btn_output.setTooltip(tooltip_output);

            if (checkIfInputWasSelected()) {
                btn_run.setDisable(false);
                btn_estimate_runtime.setDisable(false);

            } else {
                btn_run.setDisable(true);
                btn_estimate_runtime.setDisable(true);
            }

        });



        btn_estimate_runtime.setOnAction(e -> {

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

            runtimeInfoDialogue.setNumberOfRecords(runtimeEstimator.getNumberOfRecords());
            runtimeInfoDialogue.setResultText(text_estimatedRuntime);
            runtimeInfoDialogue.addComponents();
            runtimeInfoDialogue.show();
        });

        runtimeInfoDialogue.getBtn_proceed().setOnAction(e_start -> {
            runtimeInfoDialogue.close();
            runDamageProfiler();
        });

        runtimeInfoDialogue.getBtn_cancel().setOnAction(e_cancel -> runtimeInfoDialogue.close());

        btn_run.setOnAction(e -> {
            runDamageProfiler();
        });

        btn_speciesList.setOnAction(e -> {

            SpeciesListFileChooser slfc = new SpeciesListFileChooser(communicator);
            if (checkIfInputWasSelected()) {
                btn_speciesList.setDisable(false);
            } else {
                btn_speciesList.setDisable(true);
            }

        });

//        btn_loadSpecies.setOnAction(e -> {
//            LoadSpeciesDialogue loadSpeciesDialogue = new LoadSpeciesDialogue(communicator.getInput());
//
//
//        });


        btn_leftpane_damageProfile.setOnAction(e -> {
            if(starter.isCalculationsDone()){
                // generate plot
                generateDamageProfile();
            }
        });


        btn_leftpane_identityDist.setOnAction(e -> {

            if(starter.isCalculationsDone()){
                // generate plot
                generateIdentityDist();
            }

        });

        btn_leftpane_lengthDist.setOnAction(e -> {


            if(starter.isCalculationsDone()){
                // generate plot
                generateLengthDist();
            }

        });

        btn_leftpane_run_config.setOnAction(e -> {
            if(starter.isCalculationsDone()){
                mainGUI.getRoot().setCenter(runInfoDialogue.getGridPane());
                // show info (parameter / input / output / ...)
                // ask for new configuration
            } else {
                mainGUI.getRoot().setCenter(mainGUI.getConfig_dialogue().getConfig_gridpane());
            }
        });

        runInfoDialogue.getBtn_new_config().setOnAction(e -> {
            mainGUI.getRoot().setCenter(mainGUI.getConfig_dialogue().getConfig_gridpane());
            clear();
        });


    }

    private void clear() {
        btn_leftpane_lengthDist.setDisable(true);
        btn_leftpane_identityDist.setDisable(true);
        btn_leftpane_damageProfile.setDisable(true);
        starter.setCalculationsDone(false);
        btn_inputfile.setTooltip(null);
        btn_output.setTooltip(null);
        btn_reference.setTooltip(null);
    }

    private void runDamageProfiler() {

        // set all user options
        communicator.setLength(Integer.parseInt(textfield_length.getText()));
        communicator.setThreshold(Integer.parseInt(textfield_threshold.getText()));
        communicator.setSsLibsProtocolUsed(checkbox_ssLibs_protocol.isSelected());
        communicator.setUse_merged_and_mapped_reads(checkbox_use_merged_reads.isSelected());
        communicator.setyAxis_damageplot(Double.parseDouble(textfield_y_axis_height.getText()));
        communicator.setTitle_plots(textfield_title.getText());

        // set colors
        communicator.setColor_DP_C_to_T(this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_C_to_T().getValue());
        communicator.setColor_DP_G_to_A(this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_G_to_A().getValue());
        communicator.setColor_DP_insertions(this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_insertions().getValue());
        communicator.setColor_DP_deletions(this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_deletions().getValue());
        communicator.setColor_DP_other(this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_others().getValue());


        if(textfield_species.getText().equals(""))
            communicator.setSpecies_ref_identifier(null);
        else
            communicator.setSpecies_ref_identifier(textfield_species.getText());

        if(!textfield_title.getText().equals("")){
            communicator.setTitle_plots(textfield_title.getText());
        }


        try {
            // add progress indicator
            Task startCalculuations = new Task() {
                @Override
                protected Object call() throws Exception {
                    starter.start(communicator);
                    runInfoDialogue.updateParameters();
                    return true;
                }
            };

            progressBarController.activate(startCalculuations);

            startCalculuations.setOnSucceeded((EventHandler<Event>) event -> {
                // replace config with result org.damageprofiler.GUI
                btn_leftpane_lengthDist.setDisable(false);
                btn_leftpane_identityDist.setDisable(false);
                btn_leftpane_damageProfile.setDisable(false);
                generateDamageProfile();
                progressBarController.stop();
            });

            new Thread(startCalculuations).start();

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    /**
     * The following methods generate the result plots after successful damage profile calculations.
     *    todo: if plot already created, just reload
     */

    private void generateLengthDist() {

        JFreeChart[] lengthCharts = starter.getOutputGenerator().getLengthDistPlots();

        TabPane tabPane_lengthDist = new TabPane();
        Tab allData = new Tab("All data");
        Tab splitData = new Tab("Forward vs. Reverse");

        ChartViewer viewerLengthAll = new ChartViewer(lengthCharts[0]);
        ChartViewer viewerLengthSep = new ChartViewer(lengthCharts[1]);

        // disable zoom on x-axis
        viewerLengthAll.getCanvas().setDomainZoomable(false);
        viewerLengthAll.getCanvas().setDomainZoomable(false);

        allData.setContent(viewerLengthAll);
        splitData.setContent(viewerLengthSep);

        tabPane_lengthDist.getTabs().addAll(allData, splitData);

        mainGUI.getRoot().setCenter(tabPane_lengthDist);

    }

    private void generateIdentityDist() {
        ChartViewer viewerEditDistance = new ChartViewer(starter.getOutputGenerator().getEditDist_chart());
        mainGUI.getRoot().setCenter(viewerEditDistance);

    }

    private void generateDamageProfile() {

        JFreeChart[] dpCharts = starter.getOutputGenerator().getDP_chart();
        if(dpCharts.length==1){
            ChartViewer viewer5prime = new ChartViewer(dpCharts[0]);
            viewer5prime.getCanvas().setDomainZoomable(false);
            mainGUI.getRoot().setCenter(viewer5prime);
        } else if(dpCharts.length == 2){

            TabPane tabPane_damagePlot = new TabPane();
            Tab fivePrime = new Tab("5'end");
            Tab threePrime = new Tab("3'end");

            ChartViewer viewer5prime = new ChartViewer(dpCharts[0]);
            ChartViewer viewer3prime = new ChartViewer(dpCharts[1]);

            // disable zoom on x-axis
            viewer5prime.getCanvas().setDomainZoomable(false);
            viewer3prime.getCanvas().setDomainZoomable(false);

            fivePrime.setContent(viewer5prime);
            threePrime.setContent(viewer3prime);

            tabPane_damagePlot.getTabs().addAll(fivePrime, threePrime);

            mainGUI.getRoot().setCenter(tabPane_damagePlot);
        }
    }


    /**
     * This method checks if all mandatory fields are set. Otherwise, it's not possible to run the tool.
     * @return
     */
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
