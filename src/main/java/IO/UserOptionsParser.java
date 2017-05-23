package IO;

import org.apache.commons.cli.*;

/**
 * Created by neukamm on 01.08.16.
 */
public class UserOptionsParser {

    private static final String CLASS_NAME = "User option parser";
    private String[] args;
    private Communicator communicator;


    public UserOptionsParser(String[] args, Communicator c){
        this.args = args;
        this.communicator = c;
        parse();
    }

    private void parse(){

        // create command line parameters
        Options helpOptions = new Options();
        helpOptions.addOption("h", "help", false, "show this help page");
        Options options = new Options();
        options.addOption("h", "help", false, "show this help page");
        options.addOption(OptionBuilder.withLongOpt("input")
                .withArgName("INPUT")
                .withDescription("The input sam/bam file")
                .isRequired()
                .hasArg()
                .create("i"));
        options.addOption(OptionBuilder.withLongOpt("reference")
                .withArgName("REFERENCE")
                .withDescription("The reference file")
                .hasArg()
                .create("r"));
        options.addOption(OptionBuilder.withLongOpt("output")
                .withArgName("OUTPUT")
                .withDescription("The output folder")
                .isRequired()
                .hasArg()
                .create("o"));
        options.addOption(OptionBuilder.withLongOpt("threshold")
                .withArgName("THRESHOLD")
                .withDescription("Number of bases which are considered for plotting nucleotide misincorporations")
                .hasArg()
                .create("t"));
        options.addOption(OptionBuilder.withLongOpt("specie")
                .withArgName("SPECIE")
                .withDescription("RNAME flag SAM record (Reference sequence name)")
                .hasArg()
                .create("s"));
        options.addOption(OptionBuilder.withLongOpt("length")
                .withArgName("LENGTH")
                .withDescription("Number of bases which are considered for frequency computations")
                .hasArg()
                .create("l"));

        options.addOption(OptionBuilder.withLongOpt("title")
                .withArgName("TITLE")
                .withDescription("Title used for all plots. Default: filepath of input SAM/BAM file.")
                .hasArg()
                .create("title"));


        Option mapped_and_merged = new Option("mapped", "all_mapped_reads", false,
                "Use all mapped reads to calculate damage plots. Default: false ");
        options.addOption(mapped_and_merged);


        // recsaling options
        Option rescaling = new Option("rescale", "Rescaling_misincorporations", false,
                "Correct the damaged bases and generate new BAM file.");
        options.addOption(rescaling);
        options.addOption(OptionBuilder.withLongOpt("rand")
                .withArgName("RAND")
                .withDescription("Number of random starting points for the likelihood optimization")
                .hasArg()
                .create("rand"));
        options.addOption(OptionBuilder.withLongOpt("burn")
                .withArgName("BURN")
                .withDescription("Number of burnin iterations ")
                .hasArg()
                .create("burn"));
        options.addOption(OptionBuilder.withLongOpt("adjust")
                .withArgName("ADJUST")
                .withDescription("Number of adjust proposal variance parameters iterations.")
                .hasArg()
                .create("adjust"));
        options.addOption(OptionBuilder.withLongOpt("iter")
                .withArgName("Iterations")
                .withDescription("Number of final MCMC iterations")
                .hasArg()
                .create("iter"));
        options.addOption(OptionBuilder.withLongOpt("Q")
                .withArgName("MINQUALITY")
                .withDescription("Minimal base quality Phred score considered")
                .hasArg()
                .create("Q"));
        options.addOption(OptionBuilder.withLongOpt("seq_length")
                .withArgName("SEQ_LENGTH")
                .withDescription("How long sequence to use from each side by considering the damage probabilities. Default: 12")
                .hasArg()
                .create("seq_length"));

        Option only_forward = new Option("forward", "only_forward", false,
                "Using only forward reads (5' end). Default: false");
        options.addOption(only_forward);

        Option only_reverse = new Option("reverse", "only_reverse", false,
                "Using only reverse reads (3' end). Default: false");
        options.addOption(only_reverse);

        Option var_disp_opt = new Option("var_disp", "var_disp", false,
                "Variable dispersion in the overhangs . Default: false");
        options.addOption(var_disp_opt);

        Option jukes_cantor_opt = new Option("jukes_cantor", "jukes_cantor", false,
                "Use Jukes Cantor transition matrix instead of HKY85. Default: false");
        options.addOption(jukes_cantor_opt);

        Option diff_hangs_opt = new Option("diff_hangs", "diff_hangs", false,
                "The overhangs are different for 5' and 3'. Default: false");
        options.addOption(diff_hangs_opt);

        Option fix_nicks_opt = new Option("fix_nicks", "fix_nicks", false,
                "Fix the nick frequency vector (Only C>T from the 5' end and G>A from the 3' end). Default: false");
        options.addOption(fix_nicks_opt);

        Option use_raw_nick_freq_opt = new Option("use_raw_nick_freq", "use_raw_nick_freq", false,
                "Use the raw nick frequency vector without smoothing. Default: false");
        options.addOption(use_raw_nick_freq_opt);

        Option single_stranded_opt = new Option("single_stranded", "single_stranded", false,
                "Use Single stranded protocol. Default: false");
        options.addOption(single_stranded_opt);



        HelpFormatter helpformatter = new HelpFormatter();
        CommandLineParser parser = new BasicParser();

        if(args.length < 2){
            helpformatter.printHelp(CLASS_NAME, options);
            System.exit(0);
        }

        try {
            CommandLine cmd = parser.parse(helpOptions, args);
            if (cmd.hasOption('h')) {
                helpformatter.printHelp(CLASS_NAME, options);
                System.exit(0);
            }
        } catch (ParseException e1) {
        }


        try {
            CommandLine cmd = parser.parse(options, args);

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
                communicator.setRname(cmd.getOptionValue('s'));
            }
            if (cmd.hasOption('l')) {
                communicator.setLength(Integer.parseInt(cmd.getOptionValue('l')));
            }
            if(cmd.hasOption("all_mapped_reads")) {
                communicator.setUse_merged_and_mapped_reads(false);
            }

            // Plotting

            if(cmd.hasOption("title")) {
                communicator.setTitle_plots(cmd.getOptionValue("title"));
            }
            if (cmd.hasOption('t')) {
                communicator.setThreshold(Integer.parseInt(cmd.getOptionValue('t')));
            }


            // options for rescaling

            if(cmd.hasOption("Rescaling_misincorporations")) {
                communicator.setPerform_rescaling(true);
            }

            if(cmd.hasOption("rand")) {
                communicator.setRand(Integer.parseInt(cmd.getOptionValue("rand")));
            }
            if(cmd.hasOption("burn")) {
                communicator.setBurn(Integer.parseInt(cmd.getOptionValue("burn")));
            }
            if(cmd.hasOption("adjust")) {
                communicator.setAdjust(Integer.parseInt(cmd.getOptionValue("adjust")));
            }
            if(cmd.hasOption("iter")) {
                communicator.setIter(Integer.parseInt(cmd.getOptionValue("iter")));
            }
            if(cmd.hasOption("only_forward")) {
                communicator.setForward(true);
            }
            if(cmd.hasOption("only_reverse")) {
                communicator.setReverse(true);
            }
            if(cmd.hasOption("var_disp")) {
                communicator.setVar_disp(true);
            }
            if(cmd.hasOption("jukes_cantor")) {
                communicator.setJukes_cantor(true);
            }
            if(cmd.hasOption("diff_hangs")) {
                communicator.setDiff_hangs(true);
            }
            if(cmd.hasOption("fix_nicks")) {
                communicator.setFix_nicks(true);
            }
            if(cmd.hasOption("use_raw_nick_freq")) {
                communicator.setUse_raw_nick_freq(true);
            }
            if(cmd.hasOption("single_stranded")) {
                communicator.setSingle_stranded(false);
            }
            if(cmd.hasOption("seq_length")) {
                communicator.setSeq_length(Integer.parseInt(cmd.getOptionValue("seq_length")));
            }
            if(cmd.hasOption('Q')) {
                communicator.setMinQual(Integer.parseInt(cmd.getOptionValue('Q')));
            }


        } catch (ParseException e) {
            helpformatter.printHelp(CLASS_NAME, options);
            System.err.println(e.getMessage());
            System.exit(0);
        }

    }
}
