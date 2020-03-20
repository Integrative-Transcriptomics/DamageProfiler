package GUI.DPMainGui;

import GUI.Plots.DamagePlot;
import com.jfoenix.controls.JFXDrawer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable, ContentUpdater {


    @FXML
    private JFXDrawer drawer;

    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sidepanel.fxml"));
            VBox box = loader.load();
            SidePanelController controller = loader.getController();
            controller.setCallback(this);
            drawer.setSidePane(box);
            drawer.open();
        } catch (IOException ex) {
            Logger.getLogger(DamageProfilerMainGui.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void updateDamagePlot() {
        DamagePlot damagePlot = new DamagePlot();
    }

    @Override
    public void updateLengthDistribution() {

    }

    @Override
    public void updateIdentityHistogram() {

    }
}