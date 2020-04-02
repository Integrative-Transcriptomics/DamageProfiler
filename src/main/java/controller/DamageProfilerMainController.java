package controller;

import GUI.*;
import GUI.Dialogues.AdvancedPlottingOptionsDialogue;
import GUI.Dialogues.HelpDialogue;
import GUI.Dialogues.RunInfoDialogue;
import GUI.Dialogues.RuntimeEstimatorDialogue;
import IO.Communicator;
import calculations.RuntimeEstimator;
import calculations.StartCalculations;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;

import java.lang.reflect.Field;

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
    private RunInfoDialogue runInfoDialogue;
    private Communicator communicator;
    private Button btn_inputfile;
    private Button btn_reference;
    private Button btn_output;
    private Button btn_run;
    private Button btn_specieList;
    private TextField textfield_threshold;
    private TextField textfield_length;
    private TextField textfield_specie;
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
     */
    public DamageProfilerMainController(DamageProfilerMainGUI damageProfilerMainGUI, ProgressBarController progressBarController){

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
        this.btn_specieList = mainGUI.getConfig_dialogue().getBtn_specieList();
        this.btn_leftpane_identityDist = mainGUI.getBtn_leftpane_identityDist();
        this.btn_leftpane_run_config = mainGUI.getBtn_leftpane_info();
        this.btn_help = mainGUI.getBtn_help();
        this.btn_leftpane_damageProfile = mainGUI.getBtn_leftpane_damageProfile();
        this.btn_leftpane_lengthDist = mainGUI.getBtn_leftpane_lengthDist();

        this.textfield_threshold = mainGUI.getConfig_dialogue().getTextfield_threshold();
        this.textfield_length = mainGUI.getConfig_dialogue().getTextfield_length();
        this.textfield_specie = mainGUI.getConfig_dialogue().getTextfield_specie();
        this.textfield_title = mainGUI.getConfig_dialogue().getTextfield_title();
        this.textfield_y_axis_height = mainGUI.getConfig_dialogue().getTextfield_y_axis_height();

        this.checkbox_use_merged_reads = mainGUI.getConfig_dialogue().getCheckbox_use_merged_reads();
        this.checkbox_ssLibs_protocol = mainGUI.getConfig_dialogue().getCheckbox_ssLibs_protocol();


        // attributes of advanced plotting settings
        this.advancedPlottingOptionsDialogue = this.mainGUI.getConfig_dialogue().getAdvancedPlottingOptionsDialogue();


        runtimeInfoDialogue = new RuntimeEstimatorDialogue("Runtime information",
                "This gives you an estimate of the runtime. For large files with a long runtime,\n" +
                        "it's recommended to use the command line version of DamageProfiler.");

        addListener();

        addListenerAdvPlotting();
    }

    private void addListenerAdvPlotting() {

        Color color_c_to_t = this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_C_to_T().getValue();
        Color color_g_to_a = this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_G_to_A().getValue();
        Color color_insertions = this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_insertions().getValue();
        Color color_deletions = this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_deletions().getValue();
        Color color_other = this.advancedPlottingOptionsDialogue.getTabAdvancedSettingsDamagePlot().getColorPicker_others().getValue();

        // set colors in communicator
        communicator.setColor_DP_C_to_T(color_c_to_t);
        communicator.setColor_DP_G_to_A(color_g_to_a);
        communicator.setColor_DP_insertions(color_insertions);
        communicator.setColor_DP_deletions(color_deletions);
        communicator.setColor_DP_other(color_other);

    }

    private void addListener() {
        btn_inputfile.setOnAction(e -> {

            BamFileChooser fqfc = new BamFileChooser(communicator);
            Tooltip tooltip_input = new Tooltip(communicator.getInput());
            setTooltipDelay(tooltip_input);
            btn_inputfile.setTooltip(tooltip_input);

            if (checkIfInputWasSelected()) {
                btn_run.setDisable(false);
                btn_estimate_runtime.setDisable(false);

            } else {
                btn_run.setDisable(true);
                btn_estimate_runtime.setDisable(true);
            }

        });

        btn_help.setOnAction(e -> {
            mainGUI.getRoot().setCenter(new ScrollPane(this.help_dialogue.getGridPane()));
        });

        btn_reference.setOnAction(e -> {

            ReferenceFileChooser rfc = new ReferenceFileChooser(communicator);
            Tooltip tooltip_ref = new Tooltip(communicator.getReference());
            setTooltipDelay(tooltip_ref);
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
            setTooltipDelay(tooltip_output);
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

        if(textfield_specie.getText().equals(""))
            communicator.setSpecies_ref_identifier(null);
        else
            communicator.setSpecies_ref_identifier(textfield_specie.getText());

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
                // replace config with result GUI
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


    /**
     * This method overrides the default tooltip delay and sets it to 0 seconds. So the tooptip
     * pops up immediately when hovering over the item.
     *
     * @param tooltip
     */
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
