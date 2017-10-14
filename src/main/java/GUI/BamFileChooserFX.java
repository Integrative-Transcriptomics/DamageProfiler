package GUI;

import IO.Communicator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BamFileChooserFX {

    private FileChooser fileChooser = new FileChooser();

    public BamFileChooserFX(Communicator c){

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("BAM", "*.bam"),
                new FileChooser.ExtensionFilter("SAM", "*.sam")
        );


        List<File> f = fileChooser.showOpenMultipleDialog(new Stage());

        ArrayList<String> files = new ArrayList<String>();

        try{

            for (int i = 0; i < f.size(); i++){
                files.add(f.get(i).getAbsolutePath());
            }

            c.setInput(files.get(0));
            System.out.println(files.get(0));

        } catch (Exception e){

        }


        }

}
