package GUI;

import IO.Communicator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BamFileChooser {

    private FileChooser fileChooser = new FileChooser();

    public BamFileChooser(Communicator c){

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
            System.out.println("Input file: " + files.get(0));

        } catch (Exception e){
            System.err.println(e);

        }


        }

}
