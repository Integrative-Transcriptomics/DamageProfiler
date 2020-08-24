package org.damageprofiler.calculations;

import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.ValidationStringency;

import java.io.File;
import java.text.DecimalFormat;

public class RuntimeEstimator {

    private SamReader input;
    private long estimatedNumberOfRecords;
    private double timePer100000RecordsInSeconds = 1.5;
    private long estimatedRuntimeInSeconds;
    private double megabytes;

    public RuntimeEstimator(String inputfile){
        input = SamReaderFactory.make().enable(SamReaderFactory.Option.DONT_MEMORY_MAP_INDEX).
                validationStringency(ValidationStringency.LENIENT).open(new File(inputfile));

        estimate(inputfile);

    }

    /**
     * Estimate runtime based on the time needed in previous runs and the number of reads in the
     * current input file.
     *
     * @param inputfile
     */
    public void estimate(String inputfile) {

        double bytes = new File (inputfile).length();
        megabytes = (bytes / 1000 / 1000);


        if(inputfile.endsWith("bam")){
            double sizeSamRecordInBytes = 80;
            this.estimatedNumberOfRecords = (long) (bytes/sizeSamRecordInBytes);
            DecimalFormat df = new DecimalFormat("###,###,###");
            //System.out.println("Estimated number of records to process: " + df.format(estimatedNumberOfRecords));
            //System.out.println("Estimated number of records: " + this.estimatedNumberOfRecords);
            estimatedRuntimeInSeconds = (long) (this.estimatedNumberOfRecords /100000 * timePer100000RecordsInSeconds);

            if(estimatedRuntimeInSeconds > 60) {
                long minutes = estimatedRuntimeInSeconds / 60;
                long seconds = estimatedRuntimeInSeconds % 60;
                System.out.println("Estimated Runtime: " + minutes + " minutes.");
            } else {
                System.out.println("Estimated Runtime: " + estimatedRuntimeInSeconds + " seconds.");
            }
        }


    }

    public long getEstimatedRuntimeInSeconds() {
        return estimatedRuntimeInSeconds;
    }

    public int getEstimatedNumberOfRecords() {
        return (int) estimatedNumberOfRecords;
    }

    public String getFileSize() {
        DecimalFormat df = new DecimalFormat("###.##");
        return String.valueOf(df.format(megabytes));
    }
}
