package org.damageprofiler.GUI.Dialogues;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HelpDialogue {

    private final GridPane gridPane;
    private Hyperlink link;

    public HelpDialogue(){

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(15,15,15,15));

        fill();
    }

    /**
     * Fill dialogue with help content
     */
    private void fill() {
        Label label_title = new Label("Welcome to DamageProfiler help page");
        label_title.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        Label label_usage = new Label("Usage:\n");
        label_usage.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        Label label_help = new Label();
        label_help.setText("DamageProfiler [-h] [-version] [-i <INPUT>] [-r <REFERENCE>] [-o <OUTPUT>] [-t <THRESHOLD>]\n[-s <SPECIES>] [-sf <SPECIESLIST>] " +
        "[-l <LENGTH>] [-title <TITLE>] [-yaxis_dp_max <MAX_VALUE>]\n[-xaxis_id_min <MIN_VALUE>] [-xaxis_id_max <MAX_VALUE>] [-xaxis_length_min <MIN_VALUE>]\n" +
                "[-xaxis_length_max <MAX_VALUE>] [-only_merged] [-ssLib]" +
                "\n\n" );

        Label label_details = new Label("Detailed description:\n");
        label_details.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        link = new Hyperlink();
        link.setText("Documentation");
        link.setOnAction(event -> new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("http://damageprofiler.readthedocs.io/en/latest/"));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        }).start());

        Label label_helppage = new Label(" -h\t\t\t\t\t\t\tShows this help page.\n" +
                " -version\t\t\t\t\t\tShows the version of DamageProfiler.\n" +
                " -i <INPUT>\t\t\t\t\tREQUIRED: The input sam/bam/cram file.\n" +
                " -r <REFERENCE>\t\t\t\tThe reference file (fasta format).\n" +
                " -o <OUTPUT>\t\t\t\t\tREQUIRED: The output folder.\n" +
                " -t <THRESHOLD>\t\t\t\tDamagePlot: Number of bases which are considered for plotting\n\t\t\t\t\t\t\tnucleotide misincorporations. Default: 25\n" +
                " -s <SPECIES>\t\t\t\t\tReference sequence name (RNAME flag of SAM record).\n\t\t\t\t\t\t\tFor more details see Documentation.\n" +
                " -sf <SPECIES LIST>\t\t\t\tList with species for which damage profile has to be calculated.\n\t\t\t\t\t\t\tFor more details see Documentation.\n" +
                " -l <LENGTH>\t\t\t\t\tNumber of bases which are considered for frequency\n\t\t\t\t\t\t\tcomputations. Default: 100.\n" +
                " -title <TITLE>\t\t\t\t\tTitle used for all plots. Default: input filename.\n" +
                " -yaxis_dp_max <MAX_VALUE>\tDamagePlot: Maximal y-axis value.\n" +
                " -xaxis_id_min <MIN_VALUE>\t\tIdentity Distribution: Minimal value x-axis.\n" +
                " -xaxis_id_max <MAX_VALUE>\tIdentity Distribution: Maximal value x-axis.\n" +
                " -xaxis_length_min <MIN_VALUE>\tLength Distribution: Minimal value x-axis.\n" +
                " -xaxis_length_max <MAX_VALUE>\tLength Distribution: Maximal value x-axis.\n" +
                " -only_merged\t\t\t\t\tUse only mapped and merged reads to calculate damage plot\n\t\t\t\t\t\t\tinstead of using all mapped reads.\n\t\t\t\t\t\t\tThe SAM/BAM entry must start with 'M_', otherwise  it will\n\t\t\t\t\t\t\tbe skipped. Default: false\n" +
                " -ssLib\t\t\t\t\t\tSingle-stranded library protocol was used. Default: false");

        int row=0;

        gridPane.add(label_title,0,row,2,1);
        gridPane.add(new Label("If you need more information, please check the documentation or contact the developers."),0, ++row,2,1);
        gridPane.add(link,0, ++row,2,1);
        gridPane.add(new Separator(),0, ++row,2,1);

        gridPane.add(label_usage,0, ++row,2,1);
        gridPane.add(label_help,0, ++row,2,1);
        gridPane.add(label_details,0, ++row,2,1);
        gridPane.add(label_helppage, 0,++row, 2,1);

    }


    // Getter

    public GridPane getGridPane() {
        return this.gridPane;
    }
}
