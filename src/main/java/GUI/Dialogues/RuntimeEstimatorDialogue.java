package GUI.Dialogues;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RuntimeEstimatorDialogue extends AbstractApplication {

    private final Button btn_proceed;
    private final Button btn_cancel;
    private int numberOfRecords;
    private String text_estimatedRuntime;

    public RuntimeEstimatorDialogue(String header, String message) {
        super(header, message);


        btn_proceed = new Button("Proceed");
        btn_cancel = new Button("Cancel");


    }

    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public void setResultText(String text_estimatedRuntime) {
        this.text_estimatedRuntime = text_estimatedRuntime;
    }

    public void addComponents(){
        this.gridPane.add(new Label("Number of reads: " + numberOfRecords), 0, ++row, 2,1);
        this.gridPane.add(new Label(text_estimatedRuntime), 0, ++row, 2,1);
        this.gridPane.add(btn_cancel, 0, ++row, 1,1);
        this.gridPane.add(btn_proceed, 1, row, 1,1);


    }


    public Button getBtn_proceed() {
        return btn_proceed;
    }

    public Button getBtn_cancel() {
        return btn_cancel;
    }

}
