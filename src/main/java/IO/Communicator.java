package IO;

/**
 * Created by neukamm on 11.11.2016.
 */
public class Communicator {

    // file options
    private String input;
    private String reference="";
    private String outfolder;

    // damage calculation
    private int threshold = 25;
    private int length = 100;
    private boolean use_merged_and_mapped_reads = true;

    // specie filtering
    private String rname = null;
    private String specie_name;

    // plot settings
    private String title_plots;



    // options recsaling
    private boolean perform_rescaling = false;

    private int rand = 30;          // Number of random starting points for the likelihood optimization
    private int burn = 10000;       // Number of burnin iterations
    private int adjust = 10;        // Number of adjust proposal variance parameters iterations
    private int iter = 50000;       // Number of final MCMC iterations
    private boolean forward = false;  // Using only the 5' end of the seqs
    private boolean reverse = false;  // Using only the 3' end of the seqs
    private boolean var_disp = false; // Variable dispersion in the overhangs
    private boolean jukes_cantor = false;  // Use Jukes Cantor instead of HKY85
    private boolean diff_hangs = false; // The overhangs are different for 5' and 3'
    private int nu_samples = 0;
    private boolean fix_nicks = false; // Fix the nick frequency vector (Only C.T from the 5' end and G.A from the 3' end)
    private boolean use_raw_nick_freq = false; // Use the raw nick frequency vector without smoothing
    private boolean single_stranded = false;  // Single stranded protocol
    private int seq_length = 12; // how long sequence to use from each side
    private int minQual = 0;


    public Communicator(){

    }

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

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getSpecie_name() {
        return specie_name;
    }

    public void setSpecie_name(String specie_name) {
        this.specie_name = specie_name;
    }

    public boolean isUse_merged_and_mapped_reads() {
        return use_merged_and_mapped_reads;
    }

    public void setUse_merged_and_mapped_reads(boolean use_merged_and_mapped_reads) {
        this.use_merged_and_mapped_reads = use_merged_and_mapped_reads;
    }

    public boolean isPerform_rescaling() {
        return perform_rescaling;
    }

    public void setPerform_rescaling(boolean perform_rescaling) {
        this.perform_rescaling = perform_rescaling;
    }

    public String getTitle_plots() {
        return title_plots;
    }

    public void setTitle_plots(String title_plots) {
        this.title_plots = title_plots;
    }

    public int getRand() {
        return rand;
    }

    public void setRand(int rand) {
        this.rand = rand;
    }

    public int getBurn() {
        return burn;
    }

    public void setBurn(int burn) {
        this.burn = burn;
    }

    public int getAdjust() {
        return adjust;
    }

    public void setAdjust(int adjust) {
        this.adjust = adjust;
    }

    public int getIter() {
        return iter;
    }

    public void setIter(int iter) {
        this.iter = iter;
    }

    public boolean isForward() {
        return forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public boolean isVar_disp() {
        return var_disp;
    }

    public void setVar_disp(boolean var_disp) {
        this.var_disp = var_disp;
    }

    public boolean isJukes_cantor() {
        return jukes_cantor;
    }

    public void setJukes_cantor(boolean jukes_cantor) {
        this.jukes_cantor = jukes_cantor;
    }

    public boolean isDiff_hangs() {
        return diff_hangs;
    }

    public void setDiff_hangs(boolean diff_hangs) {
        this.diff_hangs = diff_hangs;
    }

    public int getNu_samples() {
        return nu_samples;
    }

    public void setNu_samples(int nu_samples) {
        this.nu_samples = nu_samples;
    }

    public boolean isFix_nicks() {
        return fix_nicks;
    }

    public void setFix_nicks(boolean fix_nicks) {
        this.fix_nicks = fix_nicks;
    }

    public boolean isUse_raw_nick_freq() {
        return use_raw_nick_freq;
    }

    public void setUse_raw_nick_freq(boolean use_raw_nick_freq) {
        this.use_raw_nick_freq = use_raw_nick_freq;
    }

    public boolean isSingle_stranded() {
        return single_stranded;
    }

    public void setSingle_stranded(boolean single_stranded) {
        this.single_stranded = single_stranded;
    }

    public int getSeq_length() {
        return seq_length;
    }

    public void setSeq_length(int seq_length) {
        this.seq_length = seq_length;
    }

    public int getMinQual() {
        return minQual;
    }

    public void setMinQual(int minQual) {
        this.minQual = minQual;
    }
}
