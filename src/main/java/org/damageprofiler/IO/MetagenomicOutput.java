package org.damageprofiler.IO;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

public class MetagenomicOutput {

    public void generate(String output_folder, HashMap<String, List<JFreeChart>> species_output_summary, String sample_name)
            throws FileNotFoundException, DocumentException {

        // step 1
        Document document = new Document(PageSize.A4);

        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(output_folder + File.separator + sample_name +"_summary.pdf"));
        // step 3
        document.open();

        Font fontbold = FontFactory.getFont("Times-Roman", 24, Font.BOLD);

        Paragraph para = new Paragraph();
        Chunk c_title = new Chunk("\n\n\n\nSummary of damage patterns\n\n" + sample_name, fontbold);

        Phrase p1 = new Phrase(c_title);
        para.add(p1);
        para.setAlignment(1);

        document.add(para);
        document.addTitle("Summary_damage_patterns");



        document.newPage();

        PdfContentByte cb = writer.getDirectContent();
        float height = PageSize.A4.getHeight() * (float)0.25;
        float width = PageSize.A4.getWidth() / 2;

        for(String species : species_output_summary.keySet()){
            int ypos = 20;
            Paragraph para2 = new Paragraph();
            Phrase p2 = new Phrase("Results for species: " + species);
            p2.setFont(FontFactory.getFont("Times-Roman", 14));
            para2.add(p2);
            document.add(para2);

            double xpos = 0;
            for(int i = 0; i < species_output_summary.get(species).size(); i++){
                JFreeChart chart = species_output_summary.get(species).get(i);
                PdfTemplate plot = cb.createTemplate(width, height);
                Graphics2D g2d = new PdfGraphics2D(plot, width, height);
                Rectangle2D r2d = new Rectangle2D.Double(0, 0, width, height);
                chart.draw(g2d, r2d);
                g2d.dispose();


                if(i==0){
                    cb.addTemplate(plot, xpos, 500);
                } else if(i==1){
                    cb.addTemplate(plot, width, 500);
                } else if(i==2){
                    cb.addTemplate(plot, xpos, 500-height);
                } else if(i==3){
                    cb.addTemplate(plot, width, 500-height);
                }

                ypos += height;

            }
            document.newPage();
        }
        // step 5
        document.close();
    }
}
