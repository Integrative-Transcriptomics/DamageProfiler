package org.damageprofiler.io;

import javafx.scene.paint.Color;
import org.apache.commons.cli.*;


/**
 * Created by neukamm on 01.08.16.
 */
public class UserOptionsParser {

    private static final String CLASS_NAME = "User option parser";
    private final String version;
    private final String[] args;
    private final Communicator communicator;


    public UserOptionsParser(String[] args, Communicator c, String version){
        this.args = args;
        this.communicator = c;
        this.version = version;
        parse();
    }

    private void parse() {

        // create command line parameters
        Options helpOptions = new Options();
        helpOptions.addOption("h", "help", false, "show this help page");

        Options options = new Options();

        options.addOption("h", false, "Shows this help page.");

        options.addOption(new Option("version", false,
                "Shows the version of DamageProfiler."));

        options.addOption(Option.builder("i")
                .argName("INPUT")
                .desc("The input sam/bam/cram file.")
                .required()
                .hasArg()
                .build());

        options.addOption(Option.builder("r")
                .argName("REFERENCE")
                .desc("The reference file (fasta format).")
                .hasArg()
                .build());

        options.addOption(Option.builder("o")
                .argName("OUTPUT")
                .desc("The output folder.")
                .required()
                .hasArg()
                .build());

        options.addOption(Option.builder("t")
                .argName("THRESHOLD")
                .desc("DamagePlot: Number of bases which are considered for plotting nucleotide misincorporations. Default: 25")
                .hasArg()
                .build());

        options.addOption(Option.builder("s")
                .argName("SPECIES")
                .desc("Reference sequence name (Reference NAME flag of SAM record). For more details see Documentation.")
                .hasArg()
                .build());

        options.addOption(Option.builder("sf")
                .argName("SPECIES FILE")
                .desc("List of species reference names (Reference NAME flag of SAM record). For more details see Documentation.")
                .hasArg()
                .build());

        options.addOption(Option.builder("l")
                .argName("LENGTH")
                .desc("Number of bases which are considered for frequency computations. Default: 100.")
                .hasArg()
                .build());


        /*
                plotting options
         */

        options.addOption(Option.builder("title")
                .argName("TITLE")
                .desc("Title used for all plots. Default: input filename.")
                .hasArg()
                .build());

        // damage plot

        options.addOption(Option.builder("yaxis_dp_max")
                .argName("MAX_VALUE")
                .desc("DamagePlot: Maximal y-axis value.")
                .hasArg()
                .build());

        options.addOption(Option.builder("color_c_t")
                .argName("COLOR_C_T")
                .desc("DamagePlot: Color (HEX code) for C to T misincoporation frequency.")
                .hasArg()
                .build());

        options.addOption(Option.builder("color_g_a")
                .argName("COLOR_G_A")
                .desc("DamagePlot: Color (HEX code) for G to A misincoporation frequency.")
                .hasArg()
                .build());

        options.addOption(Option.builder("color_insertions")
                .argName("COLOR_C_T")
                .desc("DamagePlot: Color (HEX code) for base insertions.")
                .hasArg()
                .build());

        options.addOption(Option.builder("color_deletions")
                .argName("COLOR_DELETIONS")
                .desc("DamagePlot: Color (HEX code) for base deletions.")
                .hasArg()
                .build());

        options.addOption(Option.builder("color_other")
                .argName("COLOR_OTHER")
                .desc("DamagePlot: Color (HEX code) for other bases different to reference.")
                .hasArg()
                .build());


        // others

        options.addOption(Option.builder("only_merged")
                .desc("Use only mapped and merged (in case of paired-end sequencing) reads to calculate the damage plot " +
                        "instead of using all mapped reads. The SAM/BAM entry must start with 'M_', otherwise " +
                        "it will be skipped. Default: false ")
                .build());

        options.addOption(Option.builder("sslib")
                .desc("Single-stranded library protocol was used. Default: false")
                .build());



        String header = "\nDetailed description:\n\n";

        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(null);
        formatter.setWidth(130);
        CommandLineParser parser = new DefaultParser();


// ---------------------------------------------------------------------------------------


        try {

            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption('h')) {
                formatter.printHelp("DamageProfiler", header, options, null, true);
                System.exit(0);
            }

            if(cmd.hasOption("version")) {
                System.out.print("DamageProfiler v" + this.version + "\n");
                System.exit(0);
            }

            // input files

            if (cmd.hasOption('i')) {
                communicator.setInput(cmd.getOptionValue('i'));
            }
            if (cmd.hasOption('r')) {
                communicator.setReference(cmd.getOptionValue('r'));
            }
            if (cmd.hasOption('o')) {
                communicator.setOutfolder(cmd.getOptionValue('o'));
            }

            // damage calculation

            if (cmd.hasOption('s')) {
                communicator.setSpecies_ref_identifier(cmd.getOptionValue('s'));
            }
            if (cmd.hasOption("sf")) {
                communicator.setSpecieslist_filepath(cmd.getOptionValue("sf"));
            }
            if (cmd.hasOption('l')) {
                communicator.setLength(Integer.parseInt(cmd.getOptionValue('l')));
            }
            if(cmd.hasOption("only_merged")) {
                communicator.setUse_merged_and_mapped_reads(true);
            }

            if(cmd.hasOption("sslib")) {
                communicator.setSsLibsProtocolUsed(true);
            }

            // Plotting

            if(cmd.hasOption("title")) {
                communicator.setTitle_plots(cmd.getOptionValue("title"));
            }

            if(cmd.hasOption("yaxis_dp_max")) {
                communicator.setyAxis_damageplot(Double.parseDouble(cmd.getOptionValue("yaxis_dp_max")));
            }


            if (cmd.hasOption('t')) {
                communicator.setThreshold(Integer.parseInt(cmd.getOptionValue('t')));
            }

            if(cmd.hasOption("color_c_t")){
                communicator.setColor_DP_C_to_T( Color.web(cmd.getOptionValue("color_c_t")));
            }

            if(cmd.hasOption("color_g_a")){
                communicator.setColor_DP_G_to_A(Color.web(cmd.getOptionValue("color_g_a")));
            }

            if(cmd.hasOption("color_insertions")){
                communicator.setColor_DP_insertions(Color.web(cmd.getOptionValue("color_insertions")));
            }

            if(cmd.hasOption("color_deletions")){
                communicator.setColor_DP_deletions(Color.web(cmd.getOptionValue("color_deletions")));
            }

            if(cmd.hasOption("color_other")){
                communicator.setColor_DP_other(Color.web(cmd.getOptionValue("color_other")));
            }

        } catch (ParseException e) {
            formatter.printHelp(CLASS_NAME, options);
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }
}
