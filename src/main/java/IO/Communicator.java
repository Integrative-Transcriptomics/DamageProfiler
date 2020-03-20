package IO;

/**
 * Created by neukamm on 11.11.2016.
 */
public class Communicator {

    // file options
    private String input;
    private String reference="";
    private String outfolder;
    private String specieslist_filepath = null;

    // damage calculation
    private int threshold = 25;

    private double yAxis_damageplot=0.4;
    private double xaxis_histo_id_min =-1;
    private double xaxis_histo_id_max =-1;
    private double xaxis_histo_length_min=-1;
    private double xaxis_histo_length_max=-1;
    private int length = 100;
    private boolean use_merged_and_mapped_reads = false;
    private boolean use_all_reads = false;
    private boolean ssLibsProtocolUsed = false;

    // specie filtering
    private String species_ref_identifier = null;

    // plot settings
    private String title_plots;



    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getOutfolder() {
        return outfolder;
    }

    public void setOutfolder(String outfolder) {
        this.outfolder = outfolder;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSpecies_ref_identifier() {
        return species_ref_identifier;
    }

    public void setSpecies_ref_identifier(String species_ref_identifier) {
        this.species_ref_identifier = species_ref_identifier;
    }

    public boolean isUse_merged_and_mapped_reads() {
        return use_merged_and_mapped_reads;
    }

    public void setUse_merged_and_mapped_reads(boolean use_merged_and_mapped_reads) {
        this.use_merged_and_mapped_reads = use_merged_and_mapped_reads;
    }

    public boolean isUse_all_reads() {
        return use_all_reads;
    }

    public void setUse_all_reads(boolean use_all_reads) {
        this.use_all_reads = use_all_reads;
    }

    public boolean isSsLibsProtocolUsed() {
        return ssLibsProtocolUsed;
    }

    public void setSsLibsProtocolUsed(boolean ssLibsProtocolUsed) {
        this.ssLibsProtocolUsed = ssLibsProtocolUsed;
    }

    public void setyAxis_damageplot(double yAxis_damageplot) {
        this.yAxis_damageplot = yAxis_damageplot;
    }

    public double getyAxis_damageplot() {
        return yAxis_damageplot;
    }

    public String getTitle_plots() {
        return title_plots;
    }

    public void setTitle_plots(String title_plots) {
        this.title_plots = title_plots;
    }

    public String getSpecieslist_filepath() {
        return specieslist_filepath;
    }

    public void setSpecieslist_filepath(String specieslist_filepath) {
        this.specieslist_filepath = specieslist_filepath;
    }


    public double getXaxis_histo_id_min() {
        return xaxis_histo_id_min;
    }

    public void setXaxis_histo_id_min(double xaxis_histo_id_min) {
        this.xaxis_histo_id_min = xaxis_histo_id_min;
    }

    public double getXaxis_histo_id_max() {
        return xaxis_histo_id_max;
    }

    public void setXaxis_histo_id_max(double xaxis_histo_id_max) {
        this.xaxis_histo_id_max = xaxis_histo_id_max;
    }

    public double getXaxis_histo_length_min() {
        return xaxis_histo_length_min;
    }

    public void setXaxis_histo_length_min(double xaxis_histo_length_min) {
        this.xaxis_histo_length_min = xaxis_histo_length_min;
    }

    public double getXaxis_histo_length_max() {
        return xaxis_histo_length_max;
    }

    public void setXaxis_histo_length_max(double xaxis_histo_length_max) {
        this.xaxis_histo_length_max = xaxis_histo_length_max;
    }
}
