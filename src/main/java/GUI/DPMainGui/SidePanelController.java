package GUI.DPMainGui;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class SidePanelController implements Initializable {


    @FXML
    private JFXButton b1;
    @FXML
    private JFXButton b2;
    @FXML
    private JFXButton b3;
    @FXML
    private JFXButton exit;

    private ContentUpdater callback;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setCallback(ContentUpdater callback) {
        this.callback = callback;
    }

    @FXML
    private void updatePanelContent(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();
        System.out.println(btn.getText());
        switch (btn.getText()) {
            case "Damage Profile":
                callback.updateDamagePlot();
                break;
            case "Length Distribution":
                callback.updateLengthDistribution();
                break;
            case "Identity Histogram":
                callback.updateIdentityHistogram();
                break;
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }

}
