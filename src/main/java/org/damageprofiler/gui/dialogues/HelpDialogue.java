package org.damageprofiler.gui.dialogues;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HelpDialogue {

  private final GridPane gridPane;

  public HelpDialogue() {

    gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(15, 15, 15, 15));

    fill();
  }

  private void fill() {
    Label label_title = new Label("Welcome to DamageProfiler help page");
    label_title.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

    Label label_usage = new Label("Usage:\n");
    label_usage.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

    Label label_help = new Label();
    label_help.setText(
        "usage: DamageProfiler [-h] [-version] [-i <INPUT>] [-r <REFERENCE>] [-o <OUTPUT>] [-t <THRESHOLD>] [-s <SPECIES>]\n\t  "
            + "[-sf <SPECIES LIST>] [-l <LENGTH>] [-title <TITLE>] [-yaxis_dp_max <MAX_VALUE>] [-color_c_t <COLOR_C_T>]\n\t  "
            + "[-color_g_a <COLOR_G_A>] [-color_insertions <COLOR_C_T>] [-color_deletions <COLOR_DELETIONS>]\n\t  "
            + "[-color_other <COLOR_OTHER>] [-only_merged] [-sslib]\n\n");

    Label label_details = new Label("Detailed description:\n");
    label_details.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

    Hyperlink link = new Hyperlink();
    link.setText("Documentation");
    link.setOnAction(
        event ->
            new Thread(
                    () -> {
                      try {
                        Desktop.getDesktop()
                            .browse(new URI("http://damageprofiler.readthedocs.io/en/latest/"));
                      } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                      }
                    })
                .start());

    Label label_helppage =
        new Label(
            "-h\t\t\t\t\t\t\t\tShows this help page.\n"
                + " -version\t\t\t\t\t\t\tShows the version of DamageProfiler.\n"
                + " -i <INPUT>\t\t\t\t\t\tThe input sam/bam/cram file.\n"
                + " -r <REFERENCE>\t\t\t\t\tThe reference file (fasta format).\n"
                + " -o <OUTPUT>\t\t\t\t\t\tThe output folder.\n"
                + " -t <THRESHOLD>\t\t\t\t\tDamagePlot: Number of bases which are considered for plotting\n\t\t\t\t\t\t\t\tnucleotide misincorporations. Default: 25\n"
                + " -s <SPECIES>\t\t\t\t\t\tReference sequence name (Reference NAME flag of SAM record).\n\t\t\t\t\t\t\t\tFor more details see Documentation.\n"
                + " -sf <SPECIES LIST>\t\t\t\t\tList with species for which damage profile has to be calculated.\n\t\t\t\t\t\t\t\tFor more details see Documentation.\n"
                + " -l <LENGTH>\t\t\t\t\t\tNumber of bases which are considered for frequency computations.\n\t\t\t\t\t\t\t\tDefault: 100.\n"
                + " -title <TITLE>\t\t\t\t\t\tTitle used for all plots. Default: input filename.\n"
                + " -yaxis_dp_max <MAX_VALUE>\t\tDamagePlot: Maximal y-axis value.\n"
                + " -color_c_t <COLOR_C_T>\t\t\tDamagePlot: Color for C to T misincoporation frequency.\n"
                + " -color_g_a <COLOR_G_A>\t\t\tDamagePlot: Color for G to A misincoporation frequency.\n"
                + " -color_insertions <COLOR_C_T>\t\tDamagePlot: Color for base insertions.\n"
                + " -color_deletions <COLOR_DELETIONS>\tDamagePlot: Color for base deletions.\n"
                + " -color_other <COLOR_OTHER>\t\tDamagePlot: Color for other bases different to reference.\n"
                + " -only_merged\t\t\t\t\t\tUse only mapped and merged (in case of paired-end sequencing)\n\t\t\t\t\t\t\t\treads to calculate damage plot\n"
                + " \t\t\t\t\t\t\t\tinstead of using all mapped reads. The SAM/BAM entry must start\n\t\t\t\t\t\t\t\twith 'M_', otherwise it will\n"
                + "\t\t\t\t\t\t\t\tbe skipped. Default: false\n"
                + " -sslib\t\t\t\t\t\t\tSingle-stranded library protocol was used. Default: false\n");

    int row = 0;

    gridPane.add(label_title, 0, row, 2, 1);
    gridPane.add(
        new Label(
            "If you need more information, please check the documentation or contact the developers."),
        0,
        ++row,
        2,
        1);
    gridPane.add(link, 0, ++row, 2, 1);
    gridPane.add(new Separator(), 0, ++row, 2, 1);

    gridPane.add(label_usage, 0, ++row, 2, 1);
    gridPane.add(label_help, 0, ++row, 2, 1);
    gridPane.add(label_details, 0, ++row, 2, 1);
    gridPane.add(label_helppage, 0, ++row, 2, 1);
  }

  // Getter

  public GridPane getGridPane() {
    return this.gridPane;
  }
}
