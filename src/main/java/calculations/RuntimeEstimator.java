package calculations;

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

        estimate();

    }

    /**
     * Estimate runtime based on the time needed in previous runs and the number of reads in the
     * current input file.
     *
     */
    public void estimate() {
        // estimate runtime:
        numberOfRecords = input.iterator().toList().size();
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
