package org.damageprofiler.calculations;

import org.apache.commons.text.similarity.HammingDistance;

public class Functions {

  public int getHammingDistance(final String sequence1, final String sequence2) {
    final HammingDistance hammingDistance = new HammingDistance();
    return hammingDistance.apply(sequence1, sequence2);
  }
}
