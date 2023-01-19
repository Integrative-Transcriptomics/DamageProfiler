package org.damageprofiler.calculations;

import java.io.File;

public class RuntimeEstimator {

  private long estimatedNumberOfRecords;
  private long estimatedRuntimeInSeconds;
  private double megabytes;

  public RuntimeEstimator(final String inputfile) {
    estimate(inputfile);
  }

  private void estimate(final String inputfile) {

    final double bytes = new File(inputfile).length();
    megabytes = (bytes / 1000 / 1000);

    if (inputfile.endsWith("bam")) {
      final double sizeSamRecordInBytes = 80;
      this.estimatedNumberOfRecords = (long) (bytes / sizeSamRecordInBytes);
      final double TIME_FOR_10000_RECORDS_IN_SECONDS = 1.5;
      estimatedRuntimeInSeconds =
          (long) (this.estimatedNumberOfRecords / 100000 * TIME_FOR_10000_RECORDS_IN_SECONDS);
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
