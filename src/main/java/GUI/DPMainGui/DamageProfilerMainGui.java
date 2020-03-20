package GUI.DPMainGui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class DamageProfilerMainGui {

    private Stage stage;

    public DamageProfilerMainGui() throws IOException {

        Parent root = FXMLLoader.load(this.getClass().getResource("/fxml/main_view.fxml"));
        stage = new Stage();
        stage.setTitle("Damage Profiler Results");
        stage.setScene(new Scene(root, 1000, 800));
        stage.show();

    }

}


