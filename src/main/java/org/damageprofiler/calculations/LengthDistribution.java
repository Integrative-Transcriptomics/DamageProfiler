package org.damageprofiler.calculations;

import htsjdk.samtools.SAMRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Created by neukamm on 31.05.16. */
public class LengthDistribution {

  private HashMap<Integer, Integer> length_distribution_map_forward;
  private HashMap<Integer, Integer> length_distribution_map_reverse;
  private HashMap<Integer, Integer> length_distribution_map;
  private List<Integer> length_forward;
  private List<Integer> length_reverse;
  private List<Integer> length_all;

  public LengthDistribution() {
  }

  public void init() {

    length_distribution_map_forward = new HashMap<>();
    length_distribution_map_reverse = new HashMap<>();
    length_distribution_map = new HashMap<>();
    length_forward = new ArrayList<>();
    length_reverse = new ArrayList<>();
    length_all = new ArrayList<>();
  }

  public void fillDistributionTable(final SAMRecord record, final String record_aligned) {

    // int record_length = record.getReadLength();
    final int record_length = record_aligned.length();
    length_all.add(record_length);

    // check if record is on forward or reverse strand
    if (record.getReadNegativeStrandFlag()) {
      if (!length_distribution_map_reverse.containsKey(record_length)) {
        length_distribution_map_reverse.put(record_length, 1);
      } else {
        final int count = length_distribution_map_reverse.get(record_length);
        length_distribution_map_reverse.put(record_length, count + 1);
      }
      length_reverse.add(record_length);

    } else {
      if (!length_distribution_map_forward.containsKey(record_length)) {
        length_distribution_map_forward.put(record_length, 1);
      } else {
        final int count = length_distribution_map_forward.get(record_length);
        length_distribution_map_forward.put(record_length, count + 1);
      }
      length_forward.add(record_length);
    }

    if (!length_distribution_map.containsKey(record_length)) {
      length_distribution_map.put(record_length, 1);
    } else {
      final int count = length_distribution_map.get(record_length);
      length_distribution_map.put(record_length, count + 1);
    }
  }

  /*
         Getter
  */

  public HashMap<Integer, Integer> getLength_distribution_map_forward() {
    return length_distribution_map_forward;
  }

  public HashMap<Integer, Integer> getLength_distribution_map_reverse() {
    return length_distribution_map_reverse;
  }

  public List<Integer> getLength_forward() {
    return length_forward;
  }

  public List<Integer> getLength_reverse() {
    return length_reverse;
  }

  public List<Integer> getLength_all() {
    return length_all;
  }
}
