package org.damageprofiler.io;

import org.damageprofiler.io.pdfoutput.Histogram;
import org.damageprofiler.io.pdfoutput.LinePlot;
import org.damageprofiler.calculations.DamageProfiler;
import org.damageprofiler.calculations.Frequencies;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.paint.Color;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;

import java.awt.*;
import java.awt.Rectangle;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.awt.geom.Rectangle2D;

/**
 * Created by neukamm on 7/8/15.
 */

public class OutputGenerator {

    private final boolean ssLibProtocolUsed;
    private final int numberOfUsedReads;
    private final double height;
    private final Logger LOG;
    private final String outpath;
    private final Frequencies frequencies;
    private final DamageProfiler damageProfiler;
    private final int numberOfRecords;
    private final String title;
    private double max_length;
    private double min_length;
    private String species;
    private final int threshold;
    private final int length;
    private final String input;
    private final HashMap<String,Object> json_map = new HashMap<>();
    private JFreeChart chart_DP_5prime;
    private JFreeChart chart_DP_3prime;
    private JFreeChart length_chart_all;
    private JFreeChart length_chart_separated;
    private JFreeChart editDist_chart;

    private final Color color_DP_C_to_T;
    private final Color color_DP_G_to_A;
    private final Color color_DP_insertions;
    private final Color color_DP_deletions;
    private final Color color_DP_other;
    private double mean_length_dist;
    private double median_length_dist;
    private double std_length_dist;


    public OutputGenerator(String outputFolder, DamageProfiler damageProfiler, String species, int threshold,
                           int length, double height,  String input, Logger log, boolean ssLibProtocolUsed,
                           Color color_DP_C_to_T, Color color_DP_deletions, Color color_DP_G_to_A,
                           Color color_DP_insertions, Color color_DP_other, int numberOfRecords,
                           String title) {

        this.outpath = outputFolder;
        this.frequencies = damageProfiler.getFrequencies();
        this.numberOfUsedReads = damageProfiler.getNumberOfUsedReads();
        this.damageProfiler = damageProfiler;
        this.threshold = threshold;
        this.length = length;
        this.height = height;
        this.input = input;
        this.LOG = log;
        this.numberOfRecords = numberOfRecords;
        this.ssLibProtocolUsed = ssLibProtocolUsed;

        this.color_DP_C_to_T = color_DP_C_to_T;
        this.color_DP_deletions = color_DP_deletions;
        this.color_DP_G_to_A = color_DP_G_to_A;
        this.color_DP_other = color_DP_other;
        this.color_DP_insertions = color_DP_insertions;
        this.title = title;

        // set tax id if specified by user
        if(species != null && !species.equals("")){
            this.species = species;
        }

        LOG.info("Start writing output files:");
    }

    /**
     * writes all generated output statistics to YAML output file
     * YAML has several key,value pairs (HashMaps) that all contain information created in DamageProfiler
     * sample_name = the name of the sample
     *
     *
     * @throws IOException
     */
    public void writeJSON(String version) throws IOException {

        LOG.info("\tdmgprof.json");

        //Add Sample Name to yaml
        String sampleName = input.split("/")[input.split("/").length-1];


        //Add Metadata to JSON output
        HashMap<String, Object> meta_map = new HashMap<>();
        meta_map.put("sample_name", sampleName);
        meta_map.put("tool_name", "DamageProfiler");
        meta_map.put("version", version);

        json_map.put("metadata", meta_map);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json = gson.toJson(json_map);

        FileWriter fw = new FileWriter(this.outpath + "/dmgprof.json");
        fw.write(json);
        fw.flush();
        fw.close();

    }

    /**
     * create tab-separated txt file with all read length, sorted according strand direction
     *
     * @throws IOException
     */
    public void writeLengthDistribution() throws IOException{

        LOG.info("\tlgdistribution.txt");

        BufferedWriter lgdist = new BufferedWriter(new FileWriter(this.outpath + "/lgdistribution.txt"));
        HashMap<Integer, Integer> map_forward = damageProfiler.getLength_distribution_map_forward();
        HashMap<Integer, Integer> map_reverse = damageProfiler.getLength_distribution_map_reverse();

        lgdist.write("# table produced by DamageProfiler\n");
        lgdist.write("# using mapped file " + input + "\n");
        lgdist.write("# Sample ID: " + input.split("/")[input.split("/").length-1] + "\n");
        lgdist.write("# Std: strand of reads\n");
        lgdist.write("Std\tLength\tOccurrences\n");

        // fill file

        List<Integer> key_list = new ArrayList<>(map_forward.keySet());
        Collections.sort(key_list);

        HashMap<Integer,Integer> yaml_dump_fw = new HashMap<>();

        if(key_list.size()>0){
            min_length = key_list.get(0);
            max_length = key_list.get(key_list.size()-1);

            for(int key : key_list){
                lgdist.write("+\t" + key + "\t" + map_forward.get(key) + "\n");
                yaml_dump_fw.put(key, map_forward.get(key));
            }
        }
        json_map.put("lendist_fw",yaml_dump_fw);

        key_list.clear();
        key_list.addAll(map_reverse.keySet());
        Collections.sort(key_list);

        HashMap<Integer,Integer> yaml_dump_rv = new HashMap<>();


        if(key_list.size()>0){
            if(key_list.get(0) < min_length){
                min_length = key_list.get(0);
            }
            if(key_list.get(key_list.size()-1) > max_length){
                max_length = key_list.get(key_list.size()-1);

            }
            for(int key : key_list){
                lgdist.write("-\t" + key + "\t" + map_reverse.get(key) + "\n");
                yaml_dump_rv.put(key, map_reverse.get(key));
            }

        }

        json_map.put("lendist_rv", yaml_dump_rv);
        lgdist.close();
    }


    public void writeDNAcomp_genome() throws IOException{

        LOG.info("\tDNA_comp_genome.txt");

        BufferedWriter file_dna_comp = new BufferedWriter(new FileWriter(outpath + File.separator + "DNA_comp_genome.txt"));

        file_dna_comp.write("# table produced by DamageProfiler\n");
        file_dna_comp.write("# using mapped file " + input + "\n");
        file_dna_comp.write("# Sample ID: " + input.split("/")[input.split("/").length-1] + "\n");
        file_dna_comp.write("DNA base frequencies Sample\n");
        file_dna_comp.write("A\tC\tG\tT\n");
        double totalSample = frequencies.getCountAllBasesSample();
        double a_percentage_sample = frequencies.getCountA_sample() / totalSample;
        double c_percentage_sample = frequencies.getCountC_sample() / totalSample;
        double g_percentage_sample = frequencies.getCountG_sample() / totalSample;
        double t_percentage_sample = frequencies.getCountT_sample() / totalSample;

        file_dna_comp.write(a_percentage_sample + "\t" + c_percentage_sample+ "\t" + g_percentage_sample + "\t" + t_percentage_sample + "\n");


        file_dna_comp.write("\nDNA base frequencies Reference\n");
        file_dna_comp.write("A\tC\tG\tT\n");
        double totalRef = frequencies.getCountAllBasesRef();
        double a_percentage_ref = frequencies.getCountA_ref() / totalRef;
        double c_percentage_ref = frequencies.getCountC_ref() / totalRef;
        double g_percentage_ref = frequencies.getCountG_ref() / totalRef;
        double t_percentage_ref = frequencies.getCountT_ref() / totalRef;

        file_dna_comp.write(a_percentage_ref + "\t" + c_percentage_ref+ "\t" + g_percentage_ref + "\t" + t_percentage_ref + "\n");

        file_dna_comp.close();

    }


    public void writeDNAComp(Frequencies frequencies, List<String> referenceName) throws IOException{

        LOG.info("\tDNA_composition_sample.txt");

        BufferedWriter freq_file_sample = new BufferedWriter(new FileWriter(outpath + File.separator + "DNA_composition_sample.txt"));

        String ref = "";
        if(referenceName.size() == 1)
            ref = referenceName.get(0);

        /*
         fill now line per line
         Scheme:

               End	Std pos	A	C	G	T	Total
               3p    +
               ....
               3p    -
               ....
               5p    +
               ....
               5p    -
               ....

          */

        freq_file_sample.write("# table produced by DamageProfiler\n");
        freq_file_sample.write("# using mapped file " + input + "\n");
        freq_file_sample.write("# Sample ID: " + input.split("/")[input.split("/").length-1] + "\n");

        // write header
        freq_file_sample.write("Chr\tEnd\tStd\tPos\tA\tC\tG\tT\tTotal\n");

        // fill '3p +'
        for(int i = 0; i < this.length; i++){
            double sum=frequencies.getCount_total_forward_3()[i];

            freq_file_sample.write(ref+"\t3p\t+\t"+(i+1)+"\t"
                    +(int)frequencies.getCountA_forward_3()[i]+"\t"+(int)frequencies.getCountC_forward_3()[i]+"\t"
                    +(int)frequencies.getCountG_forward_3()[i]+"\t"+(int)frequencies.getCountT_forward_3()[i]+"\t"
                    +(int)sum+"\n"
            );

        }

        // fill '3p -'
        for(int i = 0; i < this.length; i++){
            double sum = frequencies.getCount_total_reverse_3()[i];

            freq_file_sample.write(ref+"\t3p\t-\t"+(i+1)+"\t"
                    +(int)frequencies.getCountA_reverse_3()[i]+"\t"+(int)frequencies.getCountC_reverse_3()[i]+"\t"
                    +(int)frequencies.getCountG_reverse_3()[i]+"\t"+(int)frequencies.getCountT_reverse_3()[i]+"\t"
                    +(int)sum+"\n"
            );
        }


        // fill '5p +'
        for(int i = 0; i < this.length; i++){
            double sum = frequencies.getCount_total_forward_5()[i];

            freq_file_sample.write(ref+"\t5p\t+\t"+(i+1)+"\t"
                    +(int)frequencies.getCountA_forward_5()[i]+"\t"+(int)frequencies.getCountC_forward_5()[i]+"\t"
                    +(int)frequencies.getCountG_forward_5()[i]+"\t"+(int)frequencies.getCountT_forward_5()[i]+"\t"
                    +(int)sum+"\n"
            );
        }

        // fill '5p -'
        for(int i = 0; i < this.length; i++){
            double sum = frequencies.getCount_total_reverse_5()[i];

            freq_file_sample.write(ref+"\t5p\t-\t"+(i+1)+"\t"
                    +(int)frequencies.getCountA_reverse_5()[i]+"\t"+(int)frequencies.getCountC_reverse_5()[i]+"\t"
                    +(int)frequencies.getCountG_reverse_5()[i]+"\t"+(int)frequencies.getCountT_reverse_5()[i]+"\t"
                    +(int)sum+"\n");
        }
        freq_file_sample.close();
    }

    public void writeFrequenciesReference(Frequencies frequencies, List<String> referenceName) throws IOException{
        LOG.info("\tmisincorporation.txt");

        BufferedWriter freq_file_ref = new BufferedWriter(new FileWriter(outpath + File.separator + "misincorporation.txt"));

        String ref="";
        if(referenceName.size() == 1)
            ref = referenceName.get(0);

        freq_file_ref.write("# table produced by DamageProfiler\n");
        freq_file_ref.write("# using mapped file " + input + "\n");
        freq_file_ref.write("# Sample ID: " + input.split("/")[input.split("/").length-1] + "\n");

        /*
         fill now line per line
         Scheme:

               End	Std pos	A	C	G	T	Total	G>A	C>T	A>G	T>C	A>C	A>T	C>G	C>A	T>G	T>A	G>C	G>T
               3p    +
               ....
               3p    -
               ....
               5p    +
               ....
               5p    -
               ....

          */

        // write header
        freq_file_ref.write("Chr\tEnd\tStd\tPos\tA\tC\tG\tT\tTotal\tG>A\tC>T\tA>G\tT>C\tA>C\tA>T\tC>G\tC>A\tT>G\tT>A\tG>C\tG>T\tA>-\tT>-\tC>-\tG>-\t->A\t->T\t->C\t->G\tS\n");

        // fill '3p +'
        for(int i = 0; i < this.length; i++){
            double sum=frequencies.getCountA_ref_forward_3()[i]+frequencies.getCountC_ref_forward_3()[i]+
                    frequencies.getCountG_ref_forward_3()[i]+frequencies.getCountT_ref_forward_3()[i];

            if(this.numberOfUsedReads>0){
                freq_file_ref.write(ref+"\t3p\t+\t"+(i+1)+"\t"
                        +frequencies.getCountA_ref_forward_3()[i]+"\t"+frequencies.getCountC_ref_forward_3()[i]+"\t"
                        +frequencies.getCountG_ref_forward_3()[i]+"\t"+frequencies.getCountT_ref_forward_3()[i]+"\t"
                        +sum+"\t"
                        +frequencies.getCount_forward_G_A_3()[i]+"\t"+frequencies.getCount_forward_C_T_3()[i]+"\t"
                        +frequencies.getCount_forward_A_G_3()[i]+"\t"+frequencies.getCount_forward_T_C_3()[i]+"\t"
                        +frequencies.getCount_forward_A_C_3()[i]+"\t"+frequencies.getCount_forward_A_T_3()[i]+"\t"
                        +frequencies.getCount_forward_C_G_3()[i]+"\t"+frequencies.getCount_forward_C_A_3()[i]+"\t"
                        +frequencies.getCount_forward_T_G_3()[i]+"\t"+frequencies.getCount_forward_T_A_3()[i]+"\t"
                        +frequencies.getCount_forward_G_C_3()[i]+"\t"+frequencies.getCount_forward_G_T_3()[i]+"\t"
                        +frequencies.getCount_forward_A_0_3()[i]+"\t"+frequencies.getCount_forward_T_0_3()[i]+"\t"
                        +frequencies.getCount_forward_C_0_3()[i]+"\t"+frequencies.getCount_forward_G_0_3()[i]+"\t"
                        +frequencies.getCount_forward_0_A_3()[i]+"\t"+frequencies.getCount_forward_0_T_3()[i]+"\t"
                        +frequencies.getCount_forward_0_C_3()[i]+"\t"+frequencies.getCount_forward_0_G_3()[i]+"\t"
                        +frequencies.getCountS_forward_3()[i]+"\n"
                );
            }
        }

        // fill '3p -'
        for(int i = 0; i < this.length; i++){
            double sum = frequencies.getCountA_ref_reverse_3()[i]+frequencies.getCountC_ref_reverse_3()[i]+
                    frequencies.getCountG_ref_reverse_3()[i]+frequencies.getCountT_ref_reverse_3()[i];

            if(this.numberOfUsedReads>0){
                freq_file_ref.write(ref + "\t3p\t-\t"+(i+1)+"\t"
                        +frequencies.getCountA_ref_reverse_3()[i]+"\t"+frequencies.getCountC_ref_reverse_3()[i]+"\t"
                        +frequencies.getCountG_ref_reverse_3()[i]+"\t"+frequencies.getCountT_ref_reverse_3()[i]+"\t"
                        +sum+"\t"
                        +frequencies.getCount_reverse_G_A_3()[i]+"\t"+frequencies.getCount_reverse_C_T_3()[i]+"\t"
                        +frequencies.getCount_reverse_A_G_3()[i]+"\t"+frequencies.getCount_reverse_T_C_3()[i]+"\t"
                        +frequencies.getCount_reverse_A_C_3()[i]+"\t"+frequencies.getCount_reverse_A_T_3()[i]+"\t"
                        +frequencies.getCount_reverse_C_G_3()[i]+"\t"+frequencies.getCount_reverse_C_A_3()[i]+"\t"
                        +frequencies.getCount_reverse_T_G_3()[i]+"\t"+frequencies.getCount_reverse_T_A_3()[i]+"\t"
                        +frequencies.getCount_reverse_G_C_3()[i]+"\t"+frequencies.getCount_reverse_G_T_3()[i]+"\t"
                        +frequencies.getCount_reverse_A_0_3()[i]+"\t"+frequencies.getCount_reverse_T_0_3()[i]+"\t"
                        +frequencies.getCount_reverse_C_0_3()[i]+"\t"+frequencies.getCount_reverse_G_0_3()[i]+"\t"
                        +frequencies.getCount_reverse_0_A_3()[i]+"\t"+frequencies.getCount_reverse_0_T_3()[i]+"\t"
                        +frequencies.getCount_reverse_0_C_3()[i]+"\t"+frequencies.getCount_reverse_0_G_3()[i]+"\t"
                        +frequencies.getCountS_reverse_3()[i]+"\n"
                );
            }
        }



        // fill '5p +'
        for(int i = 0; i < this.length; i++){
            double sum = frequencies.getCountA_ref_forward_5()[i]+frequencies.getCountC_ref_forward_5()[i]+
                    frequencies.getCountG_ref_forward_5()[i]+frequencies.getCountT_ref_forward_5()[i];

            if(this.numberOfUsedReads>0){
                freq_file_ref.write(ref+"\t5p\t+\t"+(i+1)+"\t"
                        +frequencies.getCountA_ref_forward_5()[i]+"\t"+frequencies.getCountC_ref_forward_5()[i]+"\t"
                        +frequencies.getCountG_ref_forward_5()[i]+"\t"+frequencies.getCountT_ref_forward_5()[i]+"\t"
                        +sum+"\t"
                        +frequencies.getCount_forward_G_A_5()[i]+"\t"+frequencies.getCount_forward_C_T_5()[i]+"\t"
                        +frequencies.getCount_forward_A_G_5()[i]+"\t"+frequencies.getCount_forward_T_C_5()[i]+"\t"
                        +frequencies.getCount_forward_A_C_5()[i]+"\t"+frequencies.getCount_forward_A_T_5()[i]+"\t"
                        +frequencies.getCount_forward_C_G_5()[i]+"\t"+frequencies.getCount_forward_C_A_5()[i]+"\t"
                        +frequencies.getCount_forward_T_G_5()[i]+"\t"+frequencies.getCount_forward_T_A_5()[i]+"\t"
                        +frequencies.getCount_forward_G_C_5()[i]+"\t"+frequencies.getCount_forward_G_T_5()[i]+"\t"
                        +frequencies.getCount_forward_A_0_5()[i]+"\t"+frequencies.getCount_forward_T_0_5()[i]+"\t"
                        +frequencies.getCount_forward_C_0_5()[i]+"\t"+frequencies.getCount_forward_G_0_5()[i]+"\t"
                        +frequencies.getCount_forward_0_A_5()[i]+"\t"+frequencies.getCount_forward_0_T_5()[i]+"\t"
                        +frequencies.getCount_forward_0_C_5()[i]+"\t"+frequencies.getCount_forward_0_G_5()[i]+"\t"
                        +frequencies.getCountS_forward_5()[i]+"\n"

                );
            }
        }

        // fill '5p -'
        for(int i = 0; i < this.length; i++){
            double sum = frequencies.getCountA_ref_reverse_5()[i]+frequencies.getCountC_ref_reverse_5()[i]+
                    frequencies.getCountG_ref_reverse_5()[i]+frequencies.getCountT_ref_reverse_5()[i];

            if(this.numberOfUsedReads>0){
                freq_file_ref.write(ref+"\t5p\t-\t"+(i+1)+"\t"
                        +frequencies.getCountA_ref_reverse_5()[i]+"\t"+frequencies.getCountC_ref_reverse_5()[i]+"\t"
                        +frequencies.getCountG_ref_reverse_5()[i]+"\t"+frequencies.getCountT_ref_reverse_5()[i]+"\t"
                        +sum+"\t"
                        +frequencies.getCount_reverse_G_A_5()[i]+"\t"+frequencies.getCount_reverse_C_T_5()[i]+"\t"
                        +frequencies.getCount_reverse_A_G_5()[i]+"\t"+frequencies.getCount_reverse_T_C_5()[i]+"\t"
                        +frequencies.getCount_reverse_A_C_5()[i]+"\t"+frequencies.getCount_reverse_A_T_5()[i]+"\t"
                        +frequencies.getCount_reverse_C_G_5()[i]+"\t"+frequencies.getCount_reverse_C_A_5()[i]+"\t"
                        +frequencies.getCount_reverse_T_G_5()[i]+"\t"+frequencies.getCount_reverse_T_A_5()[i]+"\t"
                        +frequencies.getCount_reverse_G_C_5()[i]+"\t"+frequencies.getCount_reverse_G_T_5()[i]+"\t"
                        +frequencies.getCount_reverse_A_0_5()[i]+"\t"+frequencies.getCount_reverse_T_0_5()[i]+"\t"
                        +frequencies.getCount_reverse_C_0_5()[i]+"\t"+frequencies.getCount_reverse_G_0_5()[i]+"\t"
                        +frequencies.getCount_reverse_0_A_5()[i]+"\t"+frequencies.getCount_reverse_0_T_5()[i]+"\t"
                        +frequencies.getCount_reverse_0_C_5()[i]+"\t"+frequencies.getCount_reverse_0_G_5()[i]+"\t"
                        +frequencies.getCountS_reverse_5()[i]+"\n"

                );
            }
        }
        freq_file_ref.close();

    }


    /**
     * plot length distribution as histogram.
     * first histogram: distribution over all reads lengths
     * second histogram: length distribution of forward and reverse strand in one plot
     *
     * @param length_all
     * @param length_forward
     * @param length_reverse
     * @throws IOException
     * @throws DocumentException
     */
    public void plotLengthHistogram(List<Integer> length_all, List<Integer> length_forward, List<Integer> length_reverse) throws  IOException, DocumentException {

        Histogram hist_all = new Histogram(LOG);
        hist_all.addData(length_all);
        HistogramDataset dataset_all = hist_all.createDataset(new String[]{"all reads"}, max_length);
        length_chart_all = hist_all.createChart(dataset_all, "", "Read length",
                "Occurrences", false);

        Histogram hist_separated = new Histogram(LOG);
        if(length_forward.size()>0){
            hist_separated.addData(length_forward);
        }
        if(length_reverse.size()>0){
            hist_separated.addData(length_reverse);
        }
        HistogramDataset dataset_separated = hist_separated.createDataset(new String[]{"+ strand", "- strand"}, max_length);
        length_chart_separated = hist_separated.createChart(dataset_separated, "",
                "Read length", "Occurrences", true);

        LegendTitle lt = length_chart_separated.getLegend();
        lt.setPosition(RectangleEdge.RIGHT);

        LOG.info("\tLength_plot.pdf, Length_plot_combined_data.svg, and Length_plot_forward_reverse_separated.svg");

        createPdf("/Length_plot.pdf", new JFreeChart[]{length_chart_all, length_chart_separated});
        createSVG("/Length_plot_combined_data.svg", length_chart_all);
        createSVG("/Length_plot_forward_reverse_separated.svg", length_chart_separated);


    }

    /**
     * plots histogram of the identity distribution of the reads
     *
     * @param distances
     * @param title
     * @throws DocumentException
     * @throws IOException
     */
    public void plotEditDistanceHistogram(List<Integer> distances, String title) throws DocumentException, IOException{
        Histogram hist_all = new Histogram(LOG);
        hist_all.addData(distances);

        HistogramDataset dataset = hist_all.createDataset(new String[]{title}, 100);
        editDist_chart = hist_all.createChart(dataset,  "", "Edit distance", "Occurrences",false);

        LOG.info("\tedit_distance.pdf and edit_distance.svg");

        createPdf("/edit_distance.pdf", new JFreeChart[]{editDist_chart});
        createSVG("/edit_distance.svg", editDist_chart);

    }


    /**
     * write percentage of misincorporations per read position
     *
     * @param gToA_reverse
     * @param cToT
     * @throws IOException
     */
    public void writeDamageFiles(double[] gToA_reverse, double[] cToT) throws IOException{

        LOG.info("\t3pGtoA_freq.txt and 5pCtoT_freq.txt");

        BufferedWriter writer3Prime= new BufferedWriter(new FileWriter(this.outpath + "/3pGtoA_freq.txt"));
        BufferedWriter writer5Prime = new BufferedWriter(new FileWriter(this.outpath + "/5pCtoT_freq.txt"));

        //Add stuff to json output file
        json_map.put("dmg_5p",getSubArray(cToT, this.threshold));

        // 3p end always included in json file, as it's needed for EAGER2.0 report
        json_map.put("dmg_3p",getSubArray(gToA_reverse,this.threshold));

        writer3Prime.write("# table produced by DamageProfiler\n");
        writer3Prime.write("# using mapped file " + input + "\n");
        writer3Prime.write("# Sample ID: " + input.split("/")[input.split("/").length-1] + "\n");
        writeDamagePattern("pos\t3pG>A\n", getSubArray(gToA_reverse, this.threshold), writer3Prime);
        writer3Prime.close();


        writer5Prime.write("# table produced by DamageProfiler\n");
        writer5Prime.write("# using mapped file " + input + "\n");
        writer5Prime.write("# Sample ID: " + input.split("/")[input.split("/").length-1] + "\n");
        writeDamagePattern("pos\t5pC>T\n", getSubArray(cToT, this.threshold), writer5Prime);
        writer5Prime.close();

    }

    /**
     * Helper function for writeDamageFiles() that iterates over the values and writes it directly to the file.
     *
     * @param title Header line
     * @param values base substitution frequency
     * @param writer BufferedWriter for file
     * @throws IOException File Exception
     */
    private void writeDamagePattern(String title, double[] values, BufferedWriter writer) throws IOException{

        writer.write(title);
        for(int i = 0; i < values.length; i++){
            double d = values[i];
            BigDecimal bd = BigDecimal.valueOf(d);
            if(i!=values.length-1) {
                writer.write(i+1 + "\t" + bd.toPlainString() + "\n");
            } else {
                writer.write(i+1 + "\t" + bd.toPlainString());
            }
        }

    }

    /**
     * Method to calculate some statistics
     *  - mean of length dist
     *  - std of length dist
     *  - median of length dist
     */
    public void computeSummaryMetrics(){
        HashMap<Integer,Integer> forwardMap = damageProfiler.getLength_distribution_map_forward(); // key = length, value = occurrences
        HashMap<Integer, Integer>reverseMap = damageProfiler.getLength_distribution_map_reverse();

        //Create ArrayList<Integer> of read lengths
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();

        for (int key : forwardMap.keySet()){
            int occurences = forwardMap.get(key);
            for (int i = 0; i <= occurences; i++) {
                descriptiveStatistics.addValue(key);
            }
        }

        for (int key : reverseMap.keySet()){
            int occurences = reverseMap.get(key);
            for (int i = 0; i <= occurences; i++) {
                descriptiveStatistics.addValue(key);
            }
        }

        mean_length_dist = descriptiveStatistics.getMean();
        std_length_dist = descriptiveStatistics.getStandardDeviation();
        median_length_dist = descriptiveStatistics.getPercentile(50);

        HashMap<String, Double> summ_stats = new HashMap<>();

        summ_stats.put("mean_readlength", mean_length_dist);
        summ_stats.put("std", std_length_dist);
        summ_stats.put("median", median_length_dist);

        json_map.put("summary_stats", summ_stats);

    }


    /**
     * Method to write overall base misincorporations from 5' and 3' end, respectively.
     *
     * @param frequencies
     * @param threshold
     * @throws IOException
     */
    public void writeMisincorporations(Frequencies frequencies, int threshold) throws IOException {
        LOG.info("\t5p_freq_misincorporations.txt and 3p_freq_misincorporations.txt");
        Locale.setDefault(Locale.US);

        BufferedWriter writer5PrimeAll = new BufferedWriter(new FileWriter(this.outpath + "/5p_freq_misincorporations.txt"));

        writer5PrimeAll.write("# table produced by DamageProfiler\n");
        writer5PrimeAll.write("# using mapped file " + input + "\n");
        writer5PrimeAll.write("# Sample ID: " + input.split("/")[input.split("/").length-1] + "\n");

           /*

                        5 prime end
         */

        // write header
        writer5PrimeAll.write("Pos\tC>T\tG>A\tA>C\tA>G\tA>T\tC>A\tC>G\tG>C\tG>T\tT>A\tT>C\tT>G\t" +
                "->ACGT\tACGT>-\n");

        // red
        double[] C_T_5_norm = frequencies.getCount_C_T_5_norm();
        // blue
        double[] G_A_5_norm = frequencies.getCount_G_A_5_norm();

        // purple (insertions)
        double[] insertions_mean5 = getMean(frequencies.getCount_0_A_5_norm(),frequencies.getCount_0_C_5_norm(),
                frequencies.getCount_0_G_5_norm(),frequencies.getCount_0_T_5_norm());

        // green (deletions)
        // plot only mean of deletions
        double[] deletions_mean5 = getMean(frequencies.getCount_A_0_5_norm(), frequencies.getCount_C_0_5_norm() ,
                frequencies.getCount_G_0_5_norm(), frequencies.getCount_T_0_5_norm());

        // gray
        double[] A_C_5_norm = frequencies.getCount_A_C_5_norm();
        double[] A_G_5_norm = frequencies.getCount_A_G_5_norm();
        double[] A_T_5_norm = frequencies.getCount_A_T_5_norm();
        double[] C_A_5_norm = frequencies.getCount_C_A_5_norm();
        double[] C_G_5_norm = frequencies.getCount_C_G_5_norm();
        double[] G_C_5_norm = frequencies.getCount_G_C_5_norm();
        double[] G_T_5_norm = frequencies.getCount_G_T_5_norm();
        double[] T_A_5_norm = frequencies.getCount_T_A_5_norm();
        double[] T_C_5_norm = frequencies.getCount_T_C_5_norm();
        double[] T_G_5_norm = frequencies.getCount_T_G_5_norm();


        for(int pos = 0; pos < threshold; pos++){
            writer5PrimeAll.write(pos + "\t" +
                    String.format("%.6f", C_T_5_norm[pos]) + "\t" +
                    String.format("%.6f",G_A_5_norm[pos]) + "\t" +
                    String.format("%.6f",A_C_5_norm[pos]) + "\t" +
                    String.format("%.6f",A_G_5_norm[pos]) + "\t" +
                    String.format("%.6f",A_T_5_norm[pos]) + "\t" +
                    String.format("%.6f", C_A_5_norm[pos]) + "\t" +
                    String.format("%.6f",C_G_5_norm[pos]) + "\t" +
                    String.format("%.6f",G_C_5_norm[pos]) + "\t" +
                    String.format("%.6f",G_T_5_norm[pos]) + "\t" +
                    String.format("%.6f",T_A_5_norm[pos]) + "\t" +
                    String.format("%.6f",T_C_5_norm[pos]) + "\t" +
                    String.format("%.6f",T_G_5_norm[pos] )+ "\t" +
                    String.format("%.6f",insertions_mean5[pos]) + "\t" +
                    String.format("%.6f",deletions_mean5[pos])+ "\n");

        }
        writer5PrimeAll.close();

        /*

                        3 prime end
         */

        BufferedWriter writer3PrimeAll = new BufferedWriter(new FileWriter(this.outpath + "/3p_freq_misincorporations.txt"));
        writer3PrimeAll.write("# table produced by DamageProfiler\n");
        writer3PrimeAll.write("# using mapped file " + input + "\n");
        writer3PrimeAll.write("# Sample ID: " + input.split("/")[input.split("/").length-1] + "\n");


        double[] three_A_to_C_reverse = getSubArray(frequencies.getCount_A_C_3_norm(),threshold);
        double[] three_A_to_G_reverse = getSubArray(frequencies.getCount_A_G_3_norm(),threshold);
        double[] three_A_to_T_reverse = getSubArray(frequencies.getCount_A_T_3_norm(),threshold);

        double[] three_C_to_A_reverse = getSubArray(frequencies.getCount_C_A_3_norm(), threshold);
        double[] three_C_to_G_reverse = getSubArray(frequencies.getCount_C_G_3_norm(), threshold);
        double[] three_C_to_T_reverse = getSubArray(frequencies.getCount_C_T_3_norm(), threshold);

        double[] three_G_to_A_reverse = getSubArray(frequencies.getCount_G_A_3_norm(),threshold);
        double[] three_G_to_C_reverse = getSubArray(frequencies.getCount_G_C_3_norm(),threshold);
        double[] three_G_to_T_reverse = getSubArray(frequencies.getCount_G_T_3_norm(),threshold);

        double[] three_T_to_A_reverse = getSubArray(frequencies.getCount_T_A_3_norm(),threshold);
        double[] three_T_to_C_reverse = getSubArray(frequencies.getCount_T_C_3_norm(),threshold);
        double[] three_T_to_G_reverse = getSubArray(frequencies.getCount_T_G_3_norm(),threshold);



        double[] insertions_mean_3 = getMean(frequencies.getCount_0_A_3_norm(),frequencies.getCount_0_C_3_norm(),
                frequencies.getCount_0_G_3_norm(),frequencies.getCount_0_T_3_norm());

        // green (deletions)
        double[] deletions_mean_3 = getMean(frequencies.getCount_A_0_3_norm(), frequencies.getCount_C_0_3_norm() ,
                frequencies.getCount_G_0_3_norm(), frequencies.getCount_T_0_3_norm());
        // write header
        writer3PrimeAll.write("Pos\tC>T\tG>A\tA>C\tA>G\tA>T\tC>A\tC>G\tG>C\tG>T\tT>A\tT>C\tT>G\t" +
                "->ACGT\tACGT>-\n");

        for(int pos = threshold-1; pos >=0; pos--){

            writer3PrimeAll.write(pos + "\t" +
                    String.format("%.6f", three_C_to_T_reverse[pos]) + "\t" +
                    String.format("%.6f", three_G_to_A_reverse[pos])+ "\t" +
                    String.format("%.6f", three_A_to_C_reverse[pos]) + "\t" +
                    String.format("%.6f", three_A_to_G_reverse[pos]) + "\t" +
                    String.format("%.6f", three_A_to_T_reverse[pos]) + "\t" +
                    String.format("%.6f", three_C_to_A_reverse[pos]) + "\t" +
                    String.format("%.6f", three_C_to_G_reverse[pos]) + "\t" +
                    String.format("%.6f", three_G_to_C_reverse[pos]) + "\t" +
                    String.format("%.6f", three_G_to_T_reverse[pos]) + "\t" +
                    String.format("%.6f", three_T_to_A_reverse[pos]) + "\t" +
                    String.format("%.6f", three_T_to_C_reverse[pos]) + "\t" +
                    String.format("%.6f", three_T_to_G_reverse[pos]) + "\t" +
                    String.format("%.6f", insertions_mean_3[pos]) + "\t" +
                    String.format("%.6f", deletions_mean_3[pos]) + "\n");

        }

        writer3PrimeAll.close();



    }


    /**
     * Method to write edit distance.
     *
     * @param editDistances
     * @throws IOException
     */
    public void writeEditDistance(List<Integer> editDistances) throws IOException {
        LOG.info("\teditDistance.txt\n");
        Collections.sort(editDistances);
        Set<Integer> distances_sorted = new HashSet<>(editDistances);
        HashMap<Double, Integer> edit_occurrences_map = new HashMap<>();
        for(double dist : distances_sorted){
            int occurrences = Collections.frequency(editDistances, dist);
            edit_occurrences_map.put(dist, occurrences);

        }

        BufferedWriter writerEditDistance = new BufferedWriter(new FileWriter(this.outpath + "/editDistance.txt"));
        writerEditDistance.write("#Edit distances for file:" + input + "\n");
        writerEditDistance.write("Edit distance\tOccurrences\n");

        for(double dist : edit_occurrences_map.keySet())
            writerEditDistance.write(dist + "\t" + edit_occurrences_map.get(dist) + "\n");

        writerEditDistance.close();
    }

    /**
     * create damage plot
     *
     * @throws IOException
     * @throws DocumentException
     */

    public void plotMisincorporations() throws IOException, DocumentException{

        double[] three_A_to_C_reverse = getSubArray(frequencies.getCount_A_C_3_norm(),threshold);
        double[] three_A_to_G_reverse = getSubArray(frequencies.getCount_A_G_3_norm(),threshold);
        double[] three_A_to_T_reverse = getSubArray(frequencies.getCount_A_T_3_norm(),threshold);

        double[] three_C_to_A_reverse = getSubArray(frequencies.getCount_C_A_3_norm(), threshold);
        double[] three_C_to_G_reverse = getSubArray(frequencies.getCount_C_G_3_norm(), threshold);
        double[] three_C_to_T_reverse = getSubArray(frequencies.getCount_C_T_3_norm(), threshold);

        double[] three_G_to_A_reverse = getSubArray(frequencies.getCount_G_A_3_norm(), threshold);
        double[] three_G_to_C_reverse = getSubArray(frequencies.getCount_G_C_3_norm(),threshold);
        double[] three_G_to_T_reverse = getSubArray(frequencies.getCount_G_T_3_norm(),threshold);

        double[] three_T_to_A_reverse = getSubArray(frequencies.getCount_T_A_3_norm(),threshold);
        double[] three_T_to_C_reverse = getSubArray(frequencies.getCount_T_C_3_norm(),threshold);
        double[] three_T_to_G_reverse = getSubArray(frequencies.getCount_T_G_3_norm(),threshold);

        three_A_to_C_reverse = reverseArray(three_A_to_C_reverse);
        three_A_to_G_reverse = reverseArray(three_A_to_G_reverse);
        three_A_to_T_reverse = reverseArray(three_A_to_T_reverse);

        three_C_to_A_reverse = reverseArray(three_C_to_A_reverse);
        three_C_to_G_reverse = reverseArray(three_C_to_G_reverse);
        three_C_to_T_reverse = reverseArray(three_C_to_T_reverse);

        three_G_to_A_reverse = reverseArray(three_G_to_A_reverse);
        three_G_to_C_reverse = reverseArray(three_G_to_C_reverse);
        three_G_to_T_reverse = reverseArray(three_G_to_T_reverse);

        three_T_to_A_reverse = reverseArray(three_T_to_A_reverse);
        three_T_to_C_reverse = reverseArray(three_T_to_C_reverse);
        three_T_to_G_reverse = reverseArray(three_T_to_G_reverse);

        LinePlot damagePlot_three;

        // create plots
        damagePlot_three = new LinePlot(threshold, height, LOG, color_DP_C_to_T, color_DP_G_to_A,
                color_DP_insertions, color_DP_deletions, color_DP_other);

        // three prime end
        // red
        damagePlot_three.addData(three_C_to_T_reverse, "3'C>T");
        // blue
        damagePlot_three.addData(three_G_to_A_reverse, "3'G>A");

        // purple (insertions)
        double[] insertions_mean_3 = getMean(frequencies.getCount_0_A_3_norm(),frequencies.getCount_0_C_3_norm(),
                frequencies.getCount_0_G_3_norm(),frequencies.getCount_0_T_3_norm());
        damagePlot_three.addData(insertions_mean_3, "insertions");

        // green (deletions)
        double[] deletions_mean_3 = getMean(frequencies.getCount_A_0_3_norm(), frequencies.getCount_C_0_3_norm() ,
                frequencies.getCount_G_0_3_norm(), frequencies.getCount_T_0_3_norm());
        damagePlot_three.addData(deletions_mean_3, "deletions");


        // gray
        damagePlot_three.addData(three_A_to_C_reverse, "others");
        damagePlot_three.addData(three_A_to_G_reverse, "3'A>G");
        damagePlot_three.addData(three_A_to_T_reverse, "3'A>T");
        damagePlot_three.addData(three_C_to_A_reverse, "3'C>A");
        damagePlot_three.addData(three_C_to_G_reverse, "3'C>G");
        damagePlot_three.addData(three_G_to_C_reverse, "3'G>C");
        damagePlot_three.addData(three_G_to_T_reverse, "3'G>T");
        damagePlot_three.addData(three_T_to_A_reverse, "3'T>A");
        damagePlot_three.addData(three_T_to_C_reverse, "3'T>C");
        damagePlot_three.addData(three_T_to_G_reverse, "3'T>G");



        LinePlot damagePlot_five  = new LinePlot(threshold, height, LOG, color_DP_C_to_T,
                color_DP_G_to_A, color_DP_insertions, color_DP_deletions, color_DP_other);

          /*
                add data to plots

                colour code:

                red     ->  C>T
                blue    ->  G>A
                green   ->  deletions
                purple  ->  insertions
                gray    ->  all others
          */



        // five prime end
        // red
        damagePlot_five.addData(frequencies.getCount_C_T_5_norm(), "5'C>T");
        // blue
        damagePlot_five.addData(frequencies.getCount_G_A_5_norm(), "5'G>A" );

        // purple (insertions)
        double[] insertions_mean = getMean(frequencies.getCount_0_A_5_norm(),frequencies.getCount_0_C_5_norm(),
                frequencies.getCount_0_G_5_norm(),frequencies.getCount_0_T_5_norm());
        damagePlot_five.addData(insertions_mean, "insertions");

        // green (deletions)
        // plot only mean of deletions
        double[] deletions_mean = getMean(frequencies.getCount_A_0_5_norm(), frequencies.getCount_C_0_5_norm() ,
                frequencies.getCount_G_0_5_norm(), frequencies.getCount_T_0_5_norm());
        damagePlot_five.addData(deletions_mean, "deletions");

        // gray
        damagePlot_five.addData(frequencies.getCount_A_C_5_norm(), "others");
        damagePlot_five.addData(frequencies.getCount_A_G_5_norm(), "5'A>G");
        damagePlot_five.addData(frequencies.getCount_A_T_5_norm(), "5'A>T");
        damagePlot_five.addData(frequencies.getCount_C_A_5_norm(), "5'C>A");
        damagePlot_five.addData(frequencies.getCount_C_G_5_norm(), "5'C>G");
        damagePlot_five.addData(frequencies.getCount_G_C_5_norm(), "5'G>C" );
        damagePlot_five.addData(frequencies.getCount_G_T_5_norm(), "5'G>T" );
        damagePlot_five.addData(frequencies.getCount_T_A_5_norm(), "5'T>A" );
        damagePlot_five.addData(frequencies.getCount_T_C_5_norm(), "5'T>C" );
        damagePlot_five.addData(frequencies.getCount_T_G_5_norm(), "5'T>G" );

        // create Dataset
        XYDataset dataset_five = damagePlot_five.createDataset();

        double ymax;

        // set equal y axis range
        ymax = Math.max(damagePlot_five.getY_max(), damagePlot_three.getY_max());


        JFreeChart[] charts;
        // create damage plot five prime
        chart_DP_5prime = damagePlot_five.createChart("5' end", dataset_five, ymax, threshold, ssLibProtocolUsed);
        XYDataset dataset_three = damagePlot_three.createDataset();
        // create damage plot three prime
        chart_DP_3prime = damagePlot_three.createChart("3' end", dataset_three, ymax, threshold, ssLibProtocolUsed);
        charts = new JFreeChart[]{chart_DP_5prime, chart_DP_3prime};

        LOG.info("\tDamagePlot_three_prime.svg, DamagePlot.pdf, and DamagePlot_five_prime.svg");
        createSVG("/DamagePlot_three_prime.svg", chart_DP_3prime);
        createPdf("/DamagePlot.pdf", charts);
        createSVG("/DamagePlot_five_prime.svg", chart_DP_5prime);

    }


    /**
     * build reverse array
     * @param a
     * @return
     */

    private double[] reverseArray(double[] a){
        double[] a_rev = new double[a.length];
        int count = a.length;
        for(int i = 0; i < a.length; i++){
            a_rev[i] = a[count-1];
            count--;
        }

        return a_rev;

    }


    /**
     * calculates the mean per position of all input arrays
     *
     * @param a1
     * @param a2
     * @param a3
     * @param a4
     * @return
     */
    private double[] getMean(double[] a1,double[] a2,double[] a3,double[] a4){
        double[] mean = new double[a1.length];
        for(int i = 0; i < a1.length; i++){
            mean[i] = (a1[i] + a2[i] + a3[i] + a4[i]);
        }
        return mean;
    }

    /**
     * returns first n elements of an array
     *
     * @param array
     * @param n
     * @return
     */
    private double[] getSubArray(double[] array, int n){

        double[] out = new double[n];

        if (n >= 0) System.arraycopy(array, 0, out, 0, n);

        return out;

    }


    /**
     * Save images sc svg. This is only possible when using JFreeChart library.
     * @param filename
     * @param chart
     * @throws IOException
     */
    public void createSVG(String filename, JFreeChart chart) throws IOException {

        int height = (int)(PageSize.A4.getWidth() * (float)0.8);
        int width = (int)(PageSize.A4.getHeight() / 2);

        SVGGraphics2D g2 = new SVGGraphics2D(width, height);
        Rectangle r = new Rectangle(0, 0, width, height);
        chart.draw(g2, r);
        File f = new File(outpath + "/" + filename);
        SVGUtils.writeToSVG(f, g2.getSVGElement());

    }
    /**
     * Creates a PDF document.
     *
     * @param filename the path to the new PDF document
     * @throws DocumentException
     * @throws IOException
     *
     */
    public void createPdf(String filename, JFreeChart[] charts) throws IOException, DocumentException {
        // step 1
        Document document = new Document(PageSize.A4.rotate());

        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outpath + "/" + filename));
        // step 3
        document.open();

        // compute percentage of used reads
        double ratio_used_reads = damageProfiler.getNumberOfUsedReads() / (double)numberOfRecords;

        // draw text
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;

        String read_per;
        if(this.species == null){
            read_per = Chunk.NEWLINE + "Number of used reads: " + df.format(damageProfiler.getNumberOfUsedReads()) + " (" +
                    (double)(Math.round(ratio_used_reads*10000))/100 + "% of all input reads)";
        } else{
            read_per = Chunk.NEWLINE + "Number of used reads: " + df.format(damageProfiler.getNumberOfUsedReads()) + " (" +
                    (double)(Math.round(ratio_used_reads*10000))/100 + "% of all input reads) | Species: " + this.species;
        }

        Font fontbold = FontFactory.getFont("Times-Roman", 18, Font.BOLD);
        Font font = FontFactory.getFont("Times-Roman", 14);

        Paragraph para = new Paragraph();
        Chunk c_title = new Chunk(title, fontbold);
        Chunk c_underline = new Chunk(read_per, font);

        Phrase p1 = new Phrase(c_title);
        p1.add(c_underline);
        para.add(p1);

        document.add(para);

        PdfContentByte cb = writer.getDirectContent();
        float height = PageSize.A4.getWidth() * (float)0.8;
        float width = PageSize.A4.getHeight() / 2;

        // create plots, both three prime and five prime and add them to one PDF

        float xpos = 0;
        for(JFreeChart chart : charts){
            PdfTemplate plot = cb.createTemplate(width, height);
            Graphics2D g2d = new PdfGraphics2D(plot, width, height);
            Rectangle2D r2d = new Rectangle2D.Double(0, 0, width, height);
            chart.draw(g2d, r2d);
            g2d.dispose();
            cb.addTemplate(plot, xpos, 20);
            xpos += width;
        }

        // step 5
        document.close();
    }


    /*
            Getter
     */
    public JFreeChart[] getDP_chart() {
        if(chart_DP_3prime!=null){
            return new JFreeChart[]{chart_DP_5prime, chart_DP_3prime};
        } else {
            return new JFreeChart[]{chart_DP_5prime};
        }
    }

    public JFreeChart[] getLengthDistPlots() {
        return new JFreeChart[]{length_chart_all, length_chart_separated};

    }
    public JFreeChart getEditDist_chart() {
        return editDist_chart;
    }

    public JFreeChart getChart_DP_5prime() {
        return chart_DP_5prime;
    }

    public JFreeChart getChart_DP_3prime() {
        return chart_DP_3prime;
    }

    public JFreeChart getLength_chart_all() {
        return length_chart_all;
    }

    public JFreeChart getLength_chart_sep() {
        return length_chart_separated;
    }

    public double getMeanLength(){return mean_length_dist;}

    public double getMedian_length_dist() {
        return median_length_dist;
    }

    public double getStd_length_dist() {
        return std_length_dist;
    }
}
