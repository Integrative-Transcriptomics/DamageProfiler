package org.damageprofiler.gui.dialogues;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.text.DecimalFormat;

public class RuntimeEstimatorDialogue extends AbstractApplication {

    private final Button btn_proceed;
    private final Button btn_cancel;
    private String numberOfRecords;
    private String text_estimatedRuntime;
    private String fileSize;

    public RuntimeEstimatorDialogue(String header, String message) {
        super(header, message);
        btn_proceed = new Button("Proceed");
        btn_cancel = new Button("Cancel");
    }



    public void addComponents(){


        this.gridPane.add(new Label("File size:\t\t\t" + fileSize + " mb"), 0,++row, 2,1);
        this.gridPane.add(new Label("Number of reads:\t" + numberOfRecords), 0, ++row, 2,1);
        this.gridPane.add(new Label(text_estimatedRuntime), 0, ++row, 2,1);
        this.gridPane.add(btn_cancel, 0, ++row, 1,1);
        this.gridPane.add(btn_proceed, 1, row, 1,1);



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
