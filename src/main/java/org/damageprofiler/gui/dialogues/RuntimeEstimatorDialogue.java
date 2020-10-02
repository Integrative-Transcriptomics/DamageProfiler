package org.damageprofiler.gui.dialogues;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.text.DecimalFormat;

public class RuntimeEstimatorDialogue extends AbstractApplication {

    private final Label l_est_runtime;
    private final Label l_number_of_reads;
    private final Label l_file_size;
    private String numberOfRecords="";
    private String text_estimatedRuntime="";
    private String fileSize="";
    private Button btn_proceed;
    private Button btn_cancel;

    public RuntimeEstimatorDialogue(String header, String message) {
        super(header, message);
        btn_proceed = new Button("Proceed");
        btn_cancel = new Button("Cancel");
        l_file_size = new Label();
        l_number_of_reads = new Label();
        l_est_runtime = new Label();
    }



    public void addComponents(){
        l_file_size.setText("File size:\t\t\t" + fileSize + " mb");
        l_number_of_reads.setText("Number of reads:\t" + numberOfRecords);
        l_est_runtime.setText(text_estimatedRuntime);
        this.gridPane.add(l_file_size, 0,2, 2,1);
        this.gridPane.add(l_number_of_reads, 0, 3, 2,1);
        this.gridPane.add(l_est_runtime, 0, 4, 2,1);
        this.gridPane.add(btn_cancel, 0, 5, 1,1);
        this.gridPane.add(btn_proceed, 1, 5, 1,1);
    }

    public void update(){
        l_file_size.setText("File size:\t\t\t" + fileSize + " mb");
        l_number_of_reads.setText("Number of reads:\t" + numberOfRecords);
        l_est_runtime.setText(text_estimatedRuntime);
    }


    /*
            Setter and Getter
     */
    public void setNumberOfRecords(int numberOfRecords) {

        DecimalFormat df = new DecimalFormat("###,###");
        this.numberOfRecords = df.format(numberOfRecords);
    }
    public void setResultText(String text_estimatedRuntime) {
        this.text_estimatedRuntime = text_estimatedRuntime;
    }
    public void setFileSize(String fileSize) { this.fileSize = fileSize; }
    public Button getBtn_proceed() {
        return btn_proceed;
    }
    public Button getBtn_cancel() {
        return btn_cancel;
    }

}
