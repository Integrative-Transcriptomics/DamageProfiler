package org.damageprofiler.calculations;

import java.io.File;

public class RuntimeEstimator {

  private long estimatedNumberOfRecords;
  private final double timePer100000RecordsInSeconds = 1.5;
  private long estimatedRuntimeInSeconds;
  private double megabytes;

  public RuntimeEstimator(String inputfile) {
    estimate(inputfile);
  }

  /**
   * Estimate runtime based on the time needed in previous runs and the number of reads in the
   * current input file.
   *
   * @param inputfile
   */
  private void estimate(String inputfile) {

    double bytes = new File(inputfile).length();
    megabytes = (bytes / 1000 / 1000);

    if (inputfile.endsWith("bam")) {
      double sizeSamRecordInBytes = 80;
      this.estimatedNumberOfRecords = (long) (bytes / sizeSamRecordInBytes);
      estimatedRuntimeInSeconds =
          (long) (this.estimatedNumberOfRecords / 100000 * timePer100000RecordsInSeconds);
    }
  }

  public long getEstimatedRuntimeInSeconds() {
    return estimatedRuntimeInSeconds;
  }

  public int getEstimatedNumberOfRecords() {
    return (int) estimatedNumberOfRecords;
  }

  public double getMegabytes() {
    return megabytes;
  }
}
