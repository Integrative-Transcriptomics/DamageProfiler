package org.damageprofiler.calculations;

import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.ValidationStringency;

import java.io.File;

public class RuntimeEstimator {

    private SamReader input;
    private long numberOfRecords;
    private double timePer100000RecordsInSeconds = 2;
    private long estimatedRuntimeInSeconds;

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
        // estimate runtime:

        double bytes = new File (inputfile).length();
        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);
        double gigabytes = (megabytes / 1024);

        double sizeSamRecordInBytes = 50;

        double estimatedNumberOfRecords = bytes/sizeSamRecordInBytes;
        System.out.println("Estimated number of records to process: " + estimatedNumberOfRecords);

//        numberOfRecords = input.iterator().toList().size();
        numberOfRecords = (long)estimatedNumberOfRecords;
        System.out.println("Number of records to process: " + numberOfRecords);

        estimatedRuntimeInSeconds = (long) (numberOfRecords/100000 * timePer100000RecordsInSeconds);

        if(estimatedRuntimeInSeconds > 60) {
            long minutes = estimatedRuntimeInSeconds / 60;
            long seconds = estimatedRuntimeInSeconds % 60;
            System.out.println("Estimated Runtime: " + minutes + " minutes, and " + seconds + " seconds.");
        } else {
            System.out.println("Estimated Runtime: " + estimatedRuntimeInSeconds + " seconds.");
        }

    }

    public long getEstimatedRuntimeInSeconds() {
        return estimatedRuntimeInSeconds;
    }

    public int getNumberOfRecords() {
        return (int)numberOfRecords;
    }
}
