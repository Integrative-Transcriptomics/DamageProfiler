package org.damageprofiler.io;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;

public class MetagenomicOutput {
  private final Logger logger;

  public MetagenomicOutput(final Logger logger) {
    this.logger = logger;
  }

  public void generate(
      final String output_folder,
      final HashMap<String, List<JFreeChart>> species_output_summary,
      final String sample_name,
      final HashMap<String, Integer> mapped_reads,
      final String[] specieslist)
      throws FileNotFoundException, DocumentException {

    // step 1
    final Document document = new Document(PageSize.A4);

    // step 2
    final PdfWriter writer =
        PdfWriter.getInstance(
            document,
            new FileOutputStream(output_folder + File.separator + sample_name + "_summary.pdf"));
    logger.info("Write metagenomic summary file " + sample_name + "_summary.pdf\n\n");
    // step 3
    document.open();

    final Font fontbold = FontFactory.getFont("Calibri", 24, Font.BOLD);

    final Paragraph para = new Paragraph();
    final Chunk c_title =
        new Chunk("\n\n\n\nSummary of damage patterns\n\n" + sample_name, fontbold);

    final Phrase p1 = new Phrase(c_title);
    para.add(p1);
    para.setAlignment(1);

    // list all species that were considered
    final Paragraph para_species = new Paragraph();
    para_species.setSpacingBefore(50);
    para_species.setSpacingAfter(50);
    final StringBuilder species_string = new StringBuilder();
    for (final String s : specieslist) species_string.append(s).append("\n\t- ");

    final Phrase phrase_species =
        new Phrase(
            "Considered species:\n\t-" + species_string.substring(0, species_string.length() - 1),
            FontFactory.getFont("Calibri", 18));

    para_species.add(phrase_species);
    para_species.setAlignment(1);
    phrase_species.setMultipliedLeading((float) 3);

    document.add(para);
    document.add(para_species);
    document.setMargins(50, 50, 50, 50);
    document.addTitle("Summary_damage_patterns");

    document.newPage();

    final PdfContentByte cb = writer.getDirectContent();
    final float height = PageSize.A4.getHeight() * (float) 0.25;
    final float width = PageSize.A4.getWidth() / 2;

    for (final String species : species_output_summary.keySet()) {
      final Paragraph para2 = new Paragraph();
      final Phrase p2 = new Phrase("Results for " + species, FontFactory.getFont("Calibri", 18));
      p2.setMultipliedLeading((float) 2);
      final Phrase p3 =
          new Phrase(
              "\n\nNumber of mapped reads: " + mapped_reads.get(species),
              FontFactory.getFont("Calibri", 16));
      p3.setMultipliedLeading((float) 2);
      para2.add(p2);
      para2.add(p3);
      document.add(para2);

      for (int i = 0; i < species_output_summary.get(species).size(); i++) {
        final JFreeChart chart = species_output_summary.get(species).get(i);
        final PdfTemplate plot = cb.createTemplate(width, height);
        final Graphics2D g2d = new PdfGraphics2D(plot, width - 25, height - 25);
        final Rectangle2D r2d = new Rectangle2D.Double(0, 0, width - 25, height - 25);
        chart.draw(g2d, r2d);
        g2d.dispose();

        if (i == 0) {
          cb.addTemplate(plot, 25, 400);
        } else if (i == 1) {
          cb.addTemplate(plot, width, 400);
        } else if (i == 2) {
          cb.addTemplate(plot, 25, 400 - height);
        } else if (i == 3) {
          cb.addTemplate(plot, width, 400 - height);
        }
      }
      document.newPage();
    }
    // step 5
    document.close();
  }
}
