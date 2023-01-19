package org.damageprofiler.io;

import htsjdk.samtools.reference.ReferenceSequence;
import htsjdk.samtools.reference.ReferenceSequenceFile;
import htsjdk.samtools.reference.ReferenceSequenceFileFactory;
import java.io.File;
import java.util.HashMap;

/** Created by peltzer on 25/06/15. */
public class FastACacher {
  private final HashMap<String, byte[]> data = new HashMap<>();

  public FastACacher(final File f) {
    final ReferenceSequenceFile refSeq = ReferenceSequenceFileFactory.getReferenceSequenceFile(f);

    while (true) {
      final ReferenceSequence rs = refSeq.nextSequence();
      if (rs == null) {
        break;
      } else {
        data.put(getKeyName(rs.getName()), rs.getBases());
      }
    }
  }

  public HashMap<String, byte[]> getData() {
    return data;
  }

  public String getKeyName(final String reference_name) {
    String name = reference_name;
    final String[] reference_name_splitted = reference_name.split("ref");

    if (reference_name_splitted.length > 1) {
      name = reference_name_splitted[1].substring(1, reference_name_splitted[1].length() - 1);
    }
    return name;
  }
}
