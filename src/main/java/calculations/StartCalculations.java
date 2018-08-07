package calculations;

import IO.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.*;


/**
 * Created by neukamm on 11.11.2016.
 */
public class StartCalculations {

    private final String VERSION;
    private LogClass logClass;
    private long currtime_prior_execution;  // start time to get overall runtime
    private boolean calculationsDone = true;
    private Logger LOG;
    private List<String> rnameList = null;

    public StartCalculations(String version){
        VERSION = version;
    }

    public void start(Communicator c) throws Exception {

        currtime_prior_execution = System.currentTimeMillis();

        String input = c.getInput();
        String outfolder = c.getOutfolder();
        String reference = c.getReference();
        String rname = c.getRname();
        int length = c.getLength();
        int threshold = c.getThreshold();
        double height = c.getyAxis();
        boolean use_only_merged_reads = !c.isUse_merged_and_mapped_reads();


        SpeciesListParser speciesListParser = new SpeciesListParser(
                c.getSpeciesListFile(),
                LOG
        );

        List<String> species_name_list = new ArrayList<>();

        if (c.getSpeciesListFile() == null && c.getRname() != null){
            this.rnameList = new ArrayList<>();
            rnameList.add(rname);
            species_name_list.add(speciesListParser.getSingleSpecie(rname));

        } else if (c.getSpeciesListFile() != null && c.getRname() == null) {
            this.rnameList = new ArrayList<>();
            rnameList.addAll(speciesListParser.getList());

            for(String s : rnameList)
                species_name_list.add(speciesListParser.getSingleSpecie(s));
        }


        // run for each species in list
        if(rnameList != null) {
            for (int i = 0; i < rnameList.size(); i++) {
                String specie_input_string = rnameList.get(i);
                String specie_name = species_name_list.get(i);

                String inputfileNameWithOutExtension;
                if (c.getTitle_plots() == null) {
                    inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
                } else {
                    inputfileNameWithOutExtension = c.getTitle_plots();
                }

                String ref = specie_input_string.split("\\|")[0];
                String output_folder = createOutputFolder(
                        outfolder,
                        inputfileNameWithOutExtension.split("/")[inputfileNameWithOutExtension.split("/").length - 1] + File.separator + ref + "_" + specie_name);


                // init Logger
                logClass = new LogClass();
                logClass.updateLog4jConfiguration(output_folder + "/DamageProfiler_" + ref + "_" + specie_name +".log");
                logClass.setUp();

                LOG = logClass.getLogger(this.getClass());
                System.out.println("DamageProfiler v" + VERSION);
                LOG.info("DamageProfiler v" + VERSION);
                LOG.info("Calculate damage profile for species " + specie_name);



                // decompress input file if necessary
                if (input.endsWith(".gz")) {
                    Unzip unzip = new Unzip(LOG);
                    input = unzip.decompress(input);
                }

                // create new output folder
                File file = new File(input);
                // log settings
                LOG.info("Analysis of file (-i):" + file + "\n"
                        + "Output folder (-o):" + output_folder + "\n"
                        + "Reference (-r, optional) :" + reference + "\n"
                        + "Specie (-s, optional):" + specie_input_string + "\n"
                        + "Species list (-sf, optional):" + c.getSpeciesListFile() + "\n"
                        + "Length (-l): " + length + "\n"
                        + "Threshold (-t): " + threshold + "\n"
                        + "Height yaxis (-yaxis): " + height);


                // start DamageProfiler
                DamageProfiler damageProfiler = new DamageProfiler(
                        file,
                        new File(reference),
                        threshold,
                        length,
                        specie_input_string,
                        LOG
                );
                damageProfiler.extractSAMRecords(use_only_merged_reads);

                if (damageProfiler.getNumberOfUsedReads() != 0) {
                    // generate output files if more that 0 reads were processed
                    OutputGenerator outputGenerator = new OutputGenerator(
                            output_folder,
                            damageProfiler,
                            //specie_name,
                            specie_input_string,
                            threshold,
                            length,
                            height,
                            input,
                            LOG
                    );
                    outputGenerator.writeLengthDistribution();
                    outputGenerator.writeDamageFiles(
                            damageProfiler.getFrequencies().getCount_G_A_3_norm(),
                            damageProfiler.getFrequencies().getCount_C_T_5_norm()
                    );
                    outputGenerator.writeFrequenciesReference(damageProfiler.getFrequencies());
                    outputGenerator.writeDNAComp(damageProfiler.getFrequencies());
                    outputGenerator.writeDNAcomp_genome(damageProfiler.getFrequencies());
                    outputGenerator.writeMisincorporations(
                            damageProfiler.getFrequencies(),
                            threshold
                    );


                    // create DamagePlots of 3' and 5' ends
                    outputGenerator.plotMisincorporations(inputfileNameWithOutExtension);
                    outputGenerator.plotLengthHistogram(
                            damageProfiler.getLength_all(),
                            damageProfiler.getLength_forward(),
                            damageProfiler.getLength_reverse(),
                            inputfileNameWithOutExtension
                    );
                    outputGenerator.plotIdentitiyHistogram(
                            damageProfiler.getIdentity(),
                            "Identity distribution",
                            inputfileNameWithOutExtension
                    );

                    LOG.info("Output files generated");

                } else {
                    LOG.warn("No reads processed. Can't create any output");
                }
            }

        } else {

                String inputfileNameWithOutExtension;
                if (c.getTitle_plots() == null) {
                    inputfileNameWithOutExtension = input.substring(0, input.lastIndexOf('.'));
                } else {
                    inputfileNameWithOutExtension = c.getTitle_plots();
                }

                String output_folder = createOutputFolder(
                        outfolder,
                        inputfileNameWithOutExtension.split("/")[inputfileNameWithOutExtension.split("/").length - 1]);


                // init Logger
                logClass = new LogClass();
                logClass.updateLog4jConfiguration(output_folder + "/DamageProfiler.log");
                logClass.setUp();
                LOG = logClass.getLogger(this.getClass());
                System.out.println("DamageProfiler v" + VERSION);
                LOG.info("DamageProfiler v" + VERSION);


                // decompress input file if necessary
                if (input.endsWith(".gz")) {
                    Unzip unzip = new Unzip(LOG);
                    input = unzip.decompress(input);
                }

                // create new output folder
                File file = new File(input);
                // log settings
                LOG.info("Analysis of file (-i):" + file + "\n"
                        + "Output folder (-o):" + output_folder + "\n"
                        + "Reference (-r, optional) :" + reference + "\n"
                        + "Specie (-s, optional):" + rnameList + "\n"
                        + "Species list (-sf, optional):" + c.getSpeciesListFile() + "\n"
                        + "Length (-l): " + length + "\n"
                        + "Threshold (-t): " + threshold + "\n"
                        + "Height yaxis (-yaxis): " + height);


                // start DamageProfiler
                DamageProfiler damageProfiler = new DamageProfiler(
                        file,
                        new File(reference),
                        threshold,
                        length,
                        null,
                        LOG
                );

                damageProfiler.extractSAMRecords(use_only_merged_reads);

                speciesListParser.setLOG(LOG);

                if (damageProfiler.getNumberOfUsedReads() != 0) {
                    // generate output files if more that 0 reads were processed
                    String spe = null;
                    if(damageProfiler.getSpecie_ref_for_output().size() == 1){
                        spe = speciesListParser.getSingleSpecie(damageProfiler.getSpecie_ref_for_output().iterator().next());
                    }
                    OutputGenerator outputGenerator = new OutputGenerator(
                            output_folder,
                            damageProfiler,
                            spe,
                            threshold,
                            length,
                            height,
                            input,
                            LOG
                    );
                    outputGenerator.writeLengthDistribution();
                    outputGenerator.writeDamageFiles(
                            damageProfiler.getFrequencies().getCount_G_A_3_norm(),
                            damageProfiler.getFrequencies().getCount_C_T_5_norm()
                    );
                    outputGenerator.writeFrequenciesReference(damageProfiler.getFrequencies());
                    outputGenerator.writeDNAComp(damageProfiler.getFrequencies());
                    outputGenerator.writeDNAcomp_genome(damageProfiler.getFrequencies());
                    outputGenerator.writeMisincorporations(
                            damageProfiler.getFrequencies(),
                            threshold
                    );


                    // create DamagePlots of 3' and 5' ends
                    outputGenerator.plotMisincorporations(inputfileNameWithOutExtension);
                    outputGenerator.plotLengthHistogram(
                            damageProfiler.getLength_all(),
                            damageProfiler.getLength_forward(),
                            damageProfiler.getLength_reverse(),
                            inputfileNameWithOutExtension
                    );
                    outputGenerator.plotIdentitiyHistogram(
                            damageProfiler.getIdentity(),
                            "Identity distribution",
                            inputfileNameWithOutExtension
                    );

                    LOG.info("Output files generated");

                } else {
                    LOG.warn("No reads processed. Can't create any output");
                }

        }

        // print runtime
        long currtime_post_execution = System.currentTimeMillis();
        long diff = currtime_post_execution - currtime_prior_execution;
        long runtime_s = diff / 1000;
        if(runtime_s > 60) {
            long minutes = runtime_s / 60;
            long seconds = runtime_s % 60;
            LOG.info("Runtime of Module was: " + minutes + " minutes, and " + seconds + " seconds.");
        } else {
            LOG.info("Runtime of Module was: " + runtime_s + " seconds.");
        }

        calculationsDone=true;


    }


    /**
     * create output folder.
     * Save all files in subfolder, which has the same name as the input file
     * (without extension)
     *
     * @param path
     * @throws IOException
     */
    private static String createOutputFolder(String path, String inputfileNameWithOutExtension) {

        // use Pattern.quote(File.separator) to split file path
        File f = new File(path + File.separator + inputfileNameWithOutExtension);

        // create new output directory
        if (!f.isDirectory()) {
            f.mkdirs();
        }

        return f.getAbsolutePath();
    }


    public boolean isCalculationsDone() {
        return calculationsDone;
    }
}
