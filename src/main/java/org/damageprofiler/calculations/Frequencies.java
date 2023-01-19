package org.damageprofiler.calculations;

import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.util.SequenceUtil;
import java.util.stream.IntStream;
import org.apache.log4j.Logger;

public class Frequencies {
  private final Logger logger;
  private double[] countA_forward_5;
  private double[] countC_forward_5;
  private double[] countG_forward_5;
  private double[] countT_forward_5;
  private double[] countS_forward_5;
  private double[] count0_forward_5;

  private double[] countA_forward_3;
  private double[] countC_forward_3;
  private double[] countG_forward_3;
  private double[] countT_forward_3;
  private double[] countS_forward_3;
  private double[] count0_forward_3;

  private double[] countA_reverse_5;
  private double[] countC_reverse_5;
  private double[] countG_reverse_5;
  private double[] countT_reverse_5;
  private double[] countS_reverse_5;
  private double[] count0_reverse_5;

  private double[] countA_reverse_3;
  private double[] countC_reverse_3;
  private double[] countG_reverse_3;
  private double[] countT_reverse_3;
  private double[] countS_reverse_3;
  private double[] count0_reverse_3;

  private double[] countA_ref_forward_5;
  private double[] countC_ref_forward_5;
  private double[] countG_ref_forward_5;
  private double[] countT_ref_forward_5;

  private double[] countA_ref_forward_3;
  private double[] countC_ref_forward_3;
  private double[] countG_ref_forward_3;
  private double[] countT_ref_forward_3;

  private double[] countA_ref_reverse_5;
  private double[] countC_ref_reverse_5;
  private double[] countG_ref_reverse_5;
  private double[] countT_ref_reverse_5;

  private double[] countA_ref_reverse_3;
  private double[] countC_ref_reverse_3;
  private double[] countG_ref_reverse_3;
  private double[] countT_ref_reverse_3;

  private double[] count0_ref_forward_3;
  private double[] count0_ref_reverse_3;

  private double[] count0_ref_forward_5;
  private double[] count0_ref_reverse_5;

  // deletions
  private double[] count_forward_A_0_5;
  private double[] count_forward_C_0_5;
  private double[] count_forward_G_0_5;
  private double[] count_forward_T_0_5;
  private double[] count_reverse_A_0_5;
  private double[] count_reverse_C_0_5;
  private double[] count_reverse_G_0_5;
  private double[] count_reverse_T_0_5;

  private double[] count_forward_A_0_3;
  private double[] count_forward_C_0_3;
  private double[] count_forward_G_0_3;
  private double[] count_forward_T_0_3;
  private double[] count_reverse_A_0_3;
  private double[] count_reverse_C_0_3;
  private double[] count_reverse_G_0_3;
  private double[] count_reverse_T_0_3;

  // insertions
  private double[] count_forward_0_A_5;
  private double[] count_forward_0_C_5;
  private double[] count_forward_0_G_5;
  private double[] count_forward_0_T_5;
  private double[] count_reverse_0_A_5;
  private double[] count_reverse_0_C_5;
  private double[] count_reverse_0_G_5;
  private double[] count_reverse_0_T_5;

  private double[] count_forward_0_A_3;
  private double[] count_forward_0_C_3;
  private double[] count_forward_0_G_3;
  private double[] count_forward_0_T_3;
  private double[] count_reverse_0_A_3;
  private double[] count_reverse_0_C_3;
  private double[] count_reverse_0_G_3;
  private double[] count_reverse_0_T_3;

  private double[] count_forward_A_C_5;
  private double[] count_forward_A_G_5;
  private double[] count_forward_A_T_5;

  private double[] count_forward_A_C_3;
  private double[] count_forward_A_G_3;
  private double[] count_forward_A_T_3;

  //

  private double[] count_forward_C_A_5;
  private double[] count_forward_C_G_5;
  private double[] count_forward_C_T_5;

  private double[] count_forward_C_A_3;
  private double[] count_forward_C_G_3;
  private double[] count_forward_C_T_3;

  //

  private double[] count_forward_G_A_5;
  private double[] count_forward_G_C_5;
  private double[] count_forward_G_T_5;

  private double[] count_forward_G_A_3;
  private double[] count_forward_G_C_3;
  private double[] count_forward_G_T_3;

  //

  private double[] count_forward_T_A_5;
  private double[] count_forward_T_C_5;
  private double[] count_forward_T_G_5;

  private double[] count_forward_T_A_3;
  private double[] count_forward_T_C_3;
  private double[] count_forward_T_G_3;

  //

  private double[] count_reverse_A_C_5;
  private double[] count_reverse_A_G_5;
  private double[] count_reverse_A_T_5;

  private double[] count_reverse_A_C_3;
  private double[] count_reverse_A_G_3;
  private double[] count_reverse_A_T_3;

  //

  private double[] count_reverse_C_A_5;
  private double[] count_reverse_C_G_5;
  private double[] count_reverse_C_T_5;

  private double[] count_reverse_C_A_3;
  private double[] count_reverse_C_G_3;
  private double[] count_reverse_C_T_3;

  //

  private double[] count_reverse_G_A_5;
  private double[] count_reverse_G_C_5;
  private double[] count_reverse_G_T_5;

  private double[] count_reverse_G_A_3;
  private double[] count_reverse_G_C_3;
  private double[] count_reverse_G_T_3;

  //

  private double[] count_reverse_T_A_5;
  private double[] count_reverse_T_C_5;
  private double[] count_reverse_T_G_5;

  private double[] count_reverse_T_A_3;
  private double[] count_reverse_T_C_3;
  private double[] count_reverse_T_G_3;

  //

  private double[] count_A_0_5_norm;
  private double[] count_A_0_3_norm;
  private double[] count_C_0_5_norm;
  private double[] count_C_0_3_norm;
  private double[] count_G_0_5_norm;
  private double[] count_G_0_3_norm;
  private double[] count_T_0_5_norm;
  private double[] count_T_0_3_norm;

  private double[] count_0_A_5_norm;
  private double[] count_0_A_3_norm;
  private double[] count_0_C_5_norm;
  private double[] count_0_C_3_norm;
  private double[] count_0_G_5_norm;
  private double[] count_0_G_3_norm;
  private double[] count_0_T_5_norm;
  private double[] count_0_T_3_norm;

  private double[] count_C_T_5_norm;
  private double[] count_C_T_3_norm;

  private double[] count_G_A_5_norm;
  private double[] count_G_A_3_norm;

  private double[] count_A_C_5_norm;
  private double[] count_A_C_3_norm;

  private double[] count_A_G_5_norm;
  private double[] count_A_G_3_norm;

  private double[] count_A_T_5_norm;
  private double[] count_A_T_3_norm;

  private double[] count_C_A_5_norm;
  private double[] count_C_A_3_norm;

  private double[] count_C_G_5_norm;
  private double[] count_C_G_3_norm;

  private double[] count_G_C_5_norm;
  private double[] count_G_C_3_norm;

  private double[] count_G_T_5_norm;
  private double[] count_G_T_3_norm;

  private double[] count_T_A_5_norm;
  private double[] count_T_A_3_norm;

  private double[] count_T_C_5_norm;
  private double[] count_T_C_3_norm;

  private double[] count_T_G_5_norm;
  private double[] count_T_G_3_norm;

  private int length;
  private final int threshold;

  public Frequencies(final int length, final int threshold, final Logger logger) {
    this.length = length;
    this.threshold = threshold;
    this.logger = logger;
    init();
  }

  private void init() {

    countA_forward_5 = new double[this.length];
    countC_forward_5 = new double[this.length];
    countG_forward_5 = new double[this.length];
    countT_forward_5 = new double[this.length];
    countS_forward_5 = new double[this.length];

    countA_forward_3 = new double[this.length]; // base frequency is counted reverse direction
    countC_forward_3 = new double[this.length]; // base frequency is counted reverse direction
    countG_forward_3 = new double[this.length]; // base frequency is counted reverse direction
    countT_forward_3 = new double[this.length]; // base frequency is counted reverse direction
    countS_forward_3 = new double[this.length];

    count0_forward_3 = new double[this.length];
    count0_reverse_3 = new double[this.length];
    count0_forward_5 = new double[this.length];
    count0_reverse_5 = new double[this.length];

    countA_reverse_5 = new double[this.length];
    countC_reverse_5 = new double[this.length];
    countG_reverse_5 = new double[this.length];
    countT_reverse_5 = new double[this.length];
    countS_reverse_5 = new double[this.length];

    countA_reverse_3 = new double[this.length]; // base frequency is counted reverse direction
    countC_reverse_3 = new double[this.length]; // base frequency is counted reverse direction
    countG_reverse_3 = new double[this.length]; // base frequency is counted reverse direction
    countT_reverse_3 = new double[this.length]; // base frequency is counted reverse direction
    countS_reverse_3 = new double[this.length];

    // deletions
    count_forward_A_0_5 = new double[this.length];
    count_forward_C_0_5 = new double[this.length];
    count_forward_G_0_5 = new double[this.length];
    count_forward_T_0_5 = new double[this.length];
    count_reverse_A_0_5 = new double[this.length];
    count_reverse_C_0_5 = new double[this.length];
    count_reverse_G_0_5 = new double[this.length];
    count_reverse_T_0_5 = new double[this.length];

    count_forward_A_0_3 = new double[this.length];
    count_forward_C_0_3 = new double[this.length];
    count_forward_G_0_3 = new double[this.length];
    count_forward_T_0_3 = new double[this.length];
    count_reverse_A_0_3 = new double[this.length];
    count_reverse_C_0_3 = new double[this.length];
    count_reverse_G_0_3 = new double[this.length];
    count_reverse_T_0_3 = new double[this.length];

    // insertions
    count_forward_0_A_5 = new double[this.length];
    count_forward_0_C_5 = new double[this.length];
    count_forward_0_G_5 = new double[this.length];
    count_forward_0_T_5 = new double[this.length];
    count_reverse_0_A_5 = new double[this.length];
    count_reverse_0_C_5 = new double[this.length];
    count_reverse_0_G_5 = new double[this.length];
    count_reverse_0_T_5 = new double[this.length];

    count_forward_0_A_3 = new double[this.length];
    count_forward_0_C_3 = new double[this.length];
    count_forward_0_G_3 = new double[this.length];
    count_forward_0_T_3 = new double[this.length];
    count_reverse_0_A_3 = new double[this.length];
    count_reverse_0_C_3 = new double[this.length];
    count_reverse_0_G_3 = new double[this.length];
    count_reverse_0_T_3 = new double[this.length];

    countA_ref_forward_5 = new double[this.length];
    countC_ref_forward_5 = new double[this.length];
    countG_ref_forward_5 = new double[this.length];
    countT_ref_forward_5 = new double[this.length];

    countA_ref_forward_3 = new double[this.length];
    countC_ref_forward_3 = new double[this.length];
    countG_ref_forward_3 = new double[this.length];
    countT_ref_forward_3 = new double[this.length];

    countA_ref_reverse_5 = new double[this.length];
    countC_ref_reverse_5 = new double[this.length];
    countG_ref_reverse_5 = new double[this.length];
    countT_ref_reverse_5 = new double[this.length];

    countA_ref_reverse_3 = new double[this.length];
    countC_ref_reverse_3 = new double[this.length];
    countG_ref_reverse_3 = new double[this.length];
    countT_ref_reverse_3 = new double[this.length];

    count0_ref_forward_3 = new double[this.length];
    count0_ref_reverse_3 = new double[this.length];
    count0_ref_forward_5 = new double[this.length];
    count0_ref_reverse_5 = new double[this.length];

    //

    count_A_0_5_norm = new double[this.length];
    count_A_0_3_norm = new double[this.length];
    count_C_0_5_norm = new double[this.length];
    count_C_0_3_norm = new double[this.length];
    count_G_0_5_norm = new double[this.length];
    count_G_0_3_norm = new double[this.length];
    count_T_0_5_norm = new double[this.length];
    count_T_0_3_norm = new double[this.length];

    count_0_A_5_norm = new double[this.length];
    count_0_A_3_norm = new double[this.length];
    count_0_C_5_norm = new double[this.length];
    count_0_C_3_norm = new double[this.length];
    count_0_G_5_norm = new double[this.length];
    count_0_G_3_norm = new double[this.length];
    count_0_T_5_norm = new double[this.length];
    count_0_T_3_norm = new double[this.length];

    count_C_T_5_norm = new double[this.length];
    count_C_T_3_norm = new double[this.length];

    count_G_A_5_norm = new double[this.length];
    count_G_A_3_norm = new double[this.length];

    count_A_C_5_norm = new double[this.length];
    count_A_C_3_norm = new double[this.length];

    count_A_G_5_norm = new double[this.length];
    count_A_G_3_norm = new double[this.length];

    count_A_T_5_norm = new double[this.length];
    count_A_T_3_norm = new double[this.length];

    count_C_A_5_norm = new double[this.length];
    count_C_A_3_norm = new double[this.length];

    count_C_G_5_norm = new double[this.length];
    count_C_G_3_norm = new double[this.length];

    count_G_C_5_norm = new double[this.length];
    count_G_C_3_norm = new double[this.length];

    count_G_T_5_norm = new double[this.length];
    count_G_T_3_norm = new double[this.length];

    count_T_A_5_norm = new double[this.length];
    count_T_A_3_norm = new double[this.length];

    count_T_C_5_norm = new double[this.length];
    count_T_C_3_norm = new double[this.length];

    count_T_G_5_norm = new double[this.length];
    count_T_G_3_norm = new double[this.length];

    //

    count_forward_A_C_5 = new double[this.length];
    count_forward_A_G_5 = new double[this.length];
    count_forward_A_T_5 = new double[this.length];

    count_forward_A_C_3 = new double[this.length];
    count_forward_A_G_3 = new double[this.length];
    count_forward_A_T_3 = new double[this.length];

    //

    count_forward_C_A_5 = new double[this.length];
    count_forward_C_G_5 = new double[this.length];
    count_forward_C_T_5 = new double[this.length];

    count_forward_C_A_3 = new double[this.length];
    count_forward_C_G_3 = new double[this.length];
    count_forward_C_T_3 = new double[this.length];

    //

    count_forward_G_A_5 = new double[this.length];
    count_forward_G_C_5 = new double[this.length];
    count_forward_G_T_5 = new double[this.length];

    count_forward_G_A_3 = new double[this.length];
    count_forward_G_C_3 = new double[this.length];
    count_forward_G_T_3 = new double[this.length];

    //

    count_forward_T_A_5 = new double[this.length];
    count_forward_T_C_5 = new double[this.length];
    count_forward_T_G_5 = new double[this.length];

    count_forward_T_A_3 = new double[this.length];
    count_forward_T_C_3 = new double[this.length];
    count_forward_T_G_3 = new double[this.length];

    //

    count_reverse_A_C_5 = new double[this.length];
    count_reverse_A_G_5 = new double[this.length];
    count_reverse_A_T_5 = new double[this.length];

    count_reverse_A_C_3 = new double[this.length];
    count_reverse_A_G_3 = new double[this.length];
    count_reverse_A_T_3 = new double[this.length];

    //

    count_reverse_C_A_5 = new double[this.length];
    count_reverse_C_G_5 = new double[this.length];
    count_reverse_C_T_5 = new double[this.length];

    count_reverse_C_A_3 = new double[this.length];
    count_reverse_C_G_3 = new double[this.length];
    count_reverse_C_T_3 = new double[this.length];

    //

    count_reverse_G_A_5 = new double[this.length];
    count_reverse_G_C_5 = new double[this.length];
    count_reverse_G_T_5 = new double[this.length];

    count_reverse_G_A_3 = new double[this.length];
    count_reverse_G_C_3 = new double[this.length];
    count_reverse_G_T_3 = new double[this.length];

    //

    count_reverse_T_A_5 = new double[this.length];
    count_reverse_T_C_5 = new double[this.length];
    count_reverse_T_G_5 = new double[this.length];

    count_reverse_T_A_3 = new double[this.length];
    count_reverse_T_C_3 = new double[this.length];
    count_reverse_T_G_3 = new double[this.length];
  }

  public void count(final SAMRecord record, final String record_aligned, final String ref_aligned) {

    // check whether record is plus of minus strand
    if (record.getReadNegativeStrandFlag()) {

      // build reverse complement of read
      final char[] record_char = SequenceUtil.reverseComplement(record_aligned).toCharArray();
      final char[] record_char_reverse = new char[record_char.length];
      int i = record_char.length - 1;
      while (i >= 0) {
        record_char_reverse[record_char.length - 1 - i] = record_char[i];
        i--;
      }

      // build reverse complement of reference
      final char[] ref_char = SequenceUtil.reverseComplement(ref_aligned).toCharArray();
      final char[] ref_char_reverse = new char[ref_char.length];
      i = ref_char.length - 1;
      while (i >= 0) {
        ref_char_reverse[ref_char.length - 1 - i] = ref_char[i];
        i--;
      }

      // count from 5'end
      countBaseFrequency(
          record_char,
          this.length,
          countA_reverse_5,
          countC_reverse_5,
          countG_reverse_5,
          countT_reverse_5,
          count0_reverse_5,
          countS_reverse_5);
      // count from 3'end
      countBaseFrequency(
          record_char_reverse,
          this.length,
          countA_reverse_3,
          countC_reverse_3,
          countG_reverse_3,
          countT_reverse_3,
          count0_reverse_3,
          countS_reverse_3);

      // count from 5'end
      countBaseFrequency(
          ref_char,
          this.length,
          countA_ref_reverse_5,
          countC_ref_reverse_5,
          countG_ref_reverse_5,
          countT_ref_reverse_5,
          count0_ref_reverse_5,
          null);
      // count from 3'end
      countBaseFrequency(
          ref_char_reverse,
          this.length,
          countA_ref_reverse_3,
          countC_ref_reverse_3,
          countG_ref_reverse_3,
          countT_ref_reverse_3,
          count0_ref_reverse_3,
          null);

    } else {

      final char[] record_char = record_aligned.toCharArray();
      final char[] record_char_reverse = new char[record_char.length];
      int i = record_char.length - 1;
      while (i >= 0) {
        record_char_reverse[record_char.length - 1 - i] = record_char[i];
        i--;
      }

      final char[] ref_char = ref_aligned.toCharArray();
      final char[] ref_char_reverse = new char[ref_char.length];
      i = ref_char.length - 1;
      while (i >= 0) {
        ref_char_reverse[ref_char.length - 1 - i] = ref_char[i];
        i--;
      }

      // read
      // count from 5'end
      countBaseFrequency(
          record_char,
          this.length,
          countA_forward_5,
          countC_forward_5,
          countG_forward_5,
          countT_forward_5,
          count0_forward_5,
          countS_forward_5);
      // count from 3'end
      countBaseFrequency(
          record_char_reverse,
          this.length,
          countA_forward_3,
          countC_forward_3,
          countG_forward_3,
          countT_forward_3,
          count0_forward_3,
          countS_forward_3);

      // reference
      // count from 5'end
      countBaseFrequency(
          ref_char,
          this.length,
          countA_ref_forward_5,
          countC_ref_forward_5,
          countG_ref_forward_5,
          countT_ref_forward_5,
          count0_ref_forward_5,
          null);
      // count from 3'end
      countBaseFrequency(
          ref_char_reverse,
          this.length,
          countA_ref_forward_3,
          countC_ref_forward_3,
          countG_ref_forward_3,
          countT_ref_forward_3,
          count0_ref_forward_3,
          null);
    }
  }

  public void calculateMisincorporations(
      final SAMRecord record, final String record_aligned, final String reference_aligned) {

    // count from 3' end

    // get 5'end up to set threshold
    char[] rec_char_5_length = record_aligned.toCharArray();
    char[] ref_char_5_length = reference_aligned.toCharArray();

    // get 3'end up to set threshold
    char[] rec_char_3_length = new StringBuilder(record_aligned).reverse().toString().toCharArray();
    char[] ref_char_3_length =
        new StringBuilder(reference_aligned).reverse().toString().toCharArray();

    if (!record.getReadNegativeStrandFlag()) {
      // forward strand
      comparePos(
          rec_char_3_length,
          ref_char_3_length,
          // count misincorporations
          this.count_forward_A_C_3,
          this.count_forward_A_G_3,
          this.count_forward_A_T_3,
          this.count_forward_C_A_3,
          this.count_forward_C_G_3,
          this.count_forward_C_T_3,
          this.count_forward_G_A_3,
          this.count_forward_G_C_3,
          this.count_forward_G_T_3,
          this.count_forward_T_A_3,
          this.count_forward_T_C_3,
          this.count_forward_T_G_3,
          this.count_forward_A_0_3,
          this.count_forward_C_0_3,
          this.count_forward_G_0_3,
          this.count_forward_T_0_3,
          this.count_forward_0_A_3,
          this.count_forward_0_C_3,
          this.count_forward_0_G_3,
          this.count_forward_0_T_3);

      comparePos(
          rec_char_5_length,
          ref_char_5_length,
          // count misincorporations
          this.count_forward_A_C_5,
          this.count_forward_A_G_5,
          this.count_forward_A_T_5,
          this.count_forward_C_A_5,
          this.count_forward_C_G_5,
          this.count_forward_C_T_5,
          this.count_forward_G_A_5,
          this.count_forward_G_C_5,
          this.count_forward_G_T_5,
          this.count_forward_T_A_5,
          this.count_forward_T_C_5,
          this.count_forward_T_G_5,
          this.count_forward_A_0_5,
          this.count_forward_C_0_5,
          this.count_forward_G_0_5,
          this.count_forward_T_0_5,
          this.count_forward_0_A_5,
          this.count_forward_0_C_5,
          this.count_forward_0_G_5,
          this.count_forward_0_T_5);

    } else {
      // negative strand

      // build reverse complement of read

      // get 5'end up to set threshold
      rec_char_5_length =
          SequenceUtil.reverseComplement(String.valueOf(rec_char_5_length)).toCharArray();
      ref_char_5_length =
          SequenceUtil.reverseComplement(String.valueOf(ref_char_5_length)).toCharArray();

      // get 3'end up to set threshold
      rec_char_3_length =
          SequenceUtil.reverseComplement(String.valueOf(rec_char_3_length)).toCharArray();
      ref_char_3_length =
          SequenceUtil.reverseComplement(String.valueOf(ref_char_3_length)).toCharArray();

      comparePos(
          rec_char_3_length,
          ref_char_3_length,
          // count misincorporations
          this.count_reverse_A_C_3,
          this.count_reverse_A_G_3,
          this.count_reverse_A_T_3,
          this.count_reverse_C_A_3,
          this.count_reverse_C_G_3,
          this.count_reverse_C_T_3,
          this.count_reverse_G_A_3,
          this.count_reverse_G_C_3,
          this.count_reverse_G_T_3,
          this.count_reverse_T_A_3,
          this.count_reverse_T_C_3,
          this.count_reverse_T_G_3,
          this.count_reverse_A_0_3,
          this.count_reverse_C_0_3,
          this.count_reverse_G_0_3,
          this.count_reverse_T_0_3,
          this.count_reverse_0_A_3,
          this.count_reverse_0_C_3,
          this.count_reverse_0_G_3,
          this.count_reverse_0_T_3);
      comparePos(
          rec_char_5_length,
          ref_char_5_length,
          // count misincorporations
          this.count_reverse_A_C_5,
          this.count_reverse_A_G_5,
          this.count_reverse_A_T_5,
          this.count_reverse_C_A_5,
          this.count_reverse_C_G_5,
          this.count_reverse_C_T_5,
          this.count_reverse_G_A_5,
          this.count_reverse_G_C_5,
          this.count_reverse_G_T_5,
          this.count_reverse_T_A_5,
          this.count_reverse_T_C_5,
          this.count_reverse_T_G_5,
          this.count_reverse_A_0_5,
          this.count_reverse_C_0_5,
          this.count_reverse_G_0_5,
          this.count_reverse_T_0_5,
          this.count_reverse_0_A_5,
          this.count_reverse_0_C_5,
          this.count_reverse_0_G_5,
          this.count_reverse_0_T_5);
    }
  }

  private void comparePos(
      final char[] seq,
      final char[] ref,
      final double[] a_c,
      final double[] a_g,
      final double[] a_t,
      final double[] c_a,
      final double[] c_g,
      final double[] c_t,
      final double[] g_a,
      final double[] g_c,
      final double[] g_t,
      final double[] t_a,
      final double[] t_c,
      final double[] t_g,
      final double[] a_o,
      final double[] c_o,
      final double[] g_o,
      final double[] t_o,
      final double[] o_a,
      final double[] o_c,
      final double[] o_g,
      final double[] o_t) {

    if (seq.length < this.length) {
      this.length = seq.length;
    }

    for (int i = 0; i < this.length; i++) {
      final char current_record = seq[i];
      final char current_ref = ref[i];

      switch (current_ref) {
        case 'A':
          switch (current_record) {
            case 'C':
              a_c[i]++;
              break;
            case 'G':
              a_g[i]++;
              break;
            case 'T':
              a_t[i]++;
              break;
            case '-':
              a_o[i]++;
              break;
          }
          break;

        case 'C':
          switch (current_record) {
            case 'A':
              c_a[i]++;
              break;
            case 'G':
              c_g[i]++;
              break;
            case 'T':
              c_t[i]++;
              break;
            case '-':
              c_o[i]++;
              break;
          }
          break;

        case 'G':
          switch (current_record) {
            case 'A':
              g_a[i]++;
              break;
            case 'C':
              g_c[i]++;
              break;
            case 'T':
              g_t[i]++;
              break;
            case '-':
              g_o[i]++;
              break;
          }
          break;

        case 'T':
          switch (current_record) {
            case 'A':
              t_a[i]++;
              break;
            case 'C':
              t_c[i]++;
              break;
            case 'G':
              t_g[i]++;
              break;
            case '-':
              t_o[i]++;
              break;
          }
          break;

        case '-':
          switch (current_record) {
            case 'A':
              o_a[i]++;
              break;
            case 'C':
              o_c[i]++;
              break;
            case 'G':
              o_g[i]++;
              break;
            case '-':
              o_t[i]++;
              break;
          }
          break;
      }
    }
    this.length = 100;
  }

  private void countBaseFrequency(
      final char[] rec,
      final int length,
      final double[] countA,
      final double[] countC,
      final double[] countG,
      final double[] countT,
      final double[] count0,
      final double[] countS) {
    int position = 0;
    while (position < length && position < rec.length) {
      switch (rec[position]) {
        case 'A':
          countA[position]++;
          break;
        case 'C':
          countC[position]++;
          break;
        case 'G':
          countG[position]++;
          break;
        case 'T':
          countT[position]++;
          break;
        case '-':
          count0[position]++;
          break;
        case 'S':
          if (countS != null) countS[position]++;
          break;
      }
      position += 1;
    }
  }

  /** normalize the counted number of bases per position */
  public void normalizeValues() {
    for (int i = 0; i < this.threshold; i++) {

      final int[] countC_ref_3 =
          IntStream.range(0, this.getCountC_ref_forward_3().length)
              .map(
                  k ->
                      (int) this.getCountC_ref_forward_3()[k]
                          + (int) this.getCountC_ref_reverse_3()[k])
              .toArray();
      final int[] countC_ref_5 =
          IntStream.range(0, this.getCountC_ref_forward_5().length)
              .map(
                  k ->
                      (int) this.getCountC_ref_forward_5()[k]
                          + (int) this.getCountC_ref_reverse_5()[k])
              .toArray();

      final int[] countG_ref_3 =
          IntStream.range(0, this.getCountG_ref_forward_3().length)
              .map(
                  k ->
                      (int) this.getCountG_ref_forward_3()[k]
                          + (int) this.getCountG_ref_reverse_3()[k])
              .toArray();
      final int[] countG_ref_5 =
          IntStream.range(0, this.getCountG_ref_forward_5().length)
              .map(
                  k ->
                      (int) this.getCountG_ref_forward_5()[k]
                          + (int) this.getCountG_ref_reverse_5()[k])
              .toArray();

      final int[] countA_ref_5 =
          IntStream.range(0, this.getCountA_ref_forward_5().length)
              .map(
                  k ->
                      (int) this.getCountA_ref_forward_5()[k]
                          + (int) this.getCountA_ref_reverse_5()[k])
              .toArray();
      final int[] countA_ref_3 =
          IntStream.range(0, this.getCountA_ref_forward_3().length)
              .map(
                  k ->
                      (int) this.getCountA_ref_forward_3()[k]
                          + (int) this.getCountA_ref_reverse_3()[k])
              .toArray();

      final int[] countT_ref_5 =
          IntStream.range(0, this.getCountT_ref_forward_5().length)
              .map(
                  k ->
                      (int) this.getCountT_ref_forward_5()[k]
                          + (int) this.getCountT_ref_reverse_5()[k])
              .toArray();
      final int[] countT_ref_3 =
          IntStream.range(0, this.getCountT_ref_forward_3().length)
              .map(
                  k ->
                      (int) this.getCountT_ref_forward_3()[k]
                          + (int) this.getCountT_ref_reverse_3()[k])
              .toArray();

      // normalize mismatches

      this.count_A_C_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_A_C_3[i] + count_reverse_A_C_3[i], countA_ref_3[i]);
      this.count_A_C_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_A_C_5[i] + count_reverse_A_C_5[i], countA_ref_5[i]);

      this.count_A_G_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_A_G_3[i] + count_reverse_A_G_3[i], countA_ref_3[i]);
      this.count_A_G_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_A_G_5[i] + count_reverse_A_G_5[i], countA_ref_5[i]);

      this.count_A_T_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_A_T_3[i] + count_reverse_A_T_3[i], countA_ref_3[i]);
      this.count_A_T_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_A_T_5[i] + count_reverse_A_T_5[i], countA_ref_5[i]);

      //

      this.count_C_A_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_C_A_3[i] + count_reverse_C_A_3[i], countC_ref_3[i]);
      this.count_C_A_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_C_A_5[i] + count_reverse_C_A_5[i], countC_ref_5[i]);

      this.count_C_G_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_C_G_3[i] + count_reverse_C_G_3[i], countC_ref_3[i]);
      this.count_C_G_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_C_G_5[i] + count_reverse_C_G_5[i], countC_ref_5[i]);

      this.count_C_T_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_C_T_3[i] + count_reverse_C_T_3[i], countC_ref_3[i]);
      this.count_C_T_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_C_T_5[i] + count_reverse_C_T_5[i], countC_ref_5[i]);

      //

      this.count_G_A_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_G_A_3[i] + count_reverse_G_A_3[i], countG_ref_3[i]);
      this.count_G_A_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_G_A_5[i] + count_reverse_G_A_5[i], countG_ref_5[i]);

      this.count_G_C_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_G_C_3[i] + count_reverse_G_C_3[i], countG_ref_3[i]);
      this.count_G_C_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_G_C_5[i] + count_reverse_G_C_5[i], countG_ref_5[i]);

      this.count_G_T_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_G_T_3[i] + count_reverse_G_T_3[i], countG_ref_3[i]);
      this.count_G_T_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_G_T_5[i] + count_reverse_G_T_5[i], countG_ref_5[i]);

      //

      this.count_T_A_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_T_A_3[i] + count_reverse_T_A_3[i], countT_ref_3[i]);
      this.count_T_A_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_T_A_5[i] + count_reverse_T_A_5[i], countT_ref_5[i]);

      this.count_T_C_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_T_C_3[i] + count_reverse_T_C_3[i], countT_ref_3[i]);
      this.count_T_C_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_T_C_5[i] + count_reverse_T_C_5[i], countT_ref_5[i]);

      this.count_T_G_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_T_G_3[i] + count_reverse_T_G_3[i], countT_ref_3[i]);
      this.count_T_G_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_T_G_5[i] + count_reverse_T_G_5[i], countT_ref_5[i]);

      //

      final double sum_ref_5 =
          (double) countT_ref_5[i]
              + (double) countG_ref_5[i]
              + (double) countC_ref_5[i]
              + (double) countA_ref_5[i];
      this.count_A_0_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_A_0_5[i] + count_reverse_A_0_5[i], sum_ref_5);
      this.count_C_0_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_C_0_5[i] + count_reverse_C_0_5[i], sum_ref_5);
      this.count_G_0_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_G_0_5[i] + count_reverse_G_0_5[i], sum_ref_5);
      this.count_T_0_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_T_0_5[i] + count_reverse_T_0_5[i], sum_ref_5);

      final double sum_ref_3 =
          (double) countT_ref_3[i]
              + (double) countG_ref_3[i]
              + (double) countC_ref_3[i]
              + (double) countA_ref_3[i];
      this.count_A_0_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_A_0_3[i] + count_reverse_A_0_3[i], sum_ref_3);
      this.count_C_0_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_C_0_3[i] + count_reverse_C_0_3[i], sum_ref_3);
      this.count_G_0_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_G_0_3[i] + count_reverse_G_0_3[i], sum_ref_3);
      this.count_T_0_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_T_0_3[i] + count_reverse_T_0_3[i], sum_ref_3);

      this.count_0_A_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_0_A_5[i] + count_reverse_0_A_5[i], sum_ref_5);
      this.count_0_C_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_0_C_5[i] + count_reverse_C_0_5[i], sum_ref_5);
      this.count_0_G_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_0_G_5[i] + count_reverse_G_0_5[i], sum_ref_5);
      this.count_0_T_5_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_0_T_5[i] + count_reverse_T_0_5[i], sum_ref_5);

      this.count_0_A_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_0_A_3[i] + count_reverse_0_A_3[i], sum_ref_3);
      this.count_0_C_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_0_C_3[i] + count_reverse_0_C_3[i], sum_ref_3);
      this.count_0_G_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_0_G_3[i] + count_reverse_0_G_3[i], sum_ref_3);
      this.count_0_T_3_norm[i] =
          normalizeBaseOccurrenceForOnePosition(
              count_forward_0_T_3[i] + count_reverse_0_T_3[i], sum_ref_3);
    }

    logger.info("\tBase frequencies are normalized\n");
  }

  private double normalizeBaseOccurrenceForOnePosition(
      final double count_mis, final double count_ref) {

    // !! Number of reference bases may not be zero !!
    if (count_ref == 0.0) {
      return 0.0;
    } else {
      return (count_mis / count_ref);
    }
  }

  /*
   * Getter
   */

  public double[] getCount_A_C_5_norm() {
    return count_A_C_5_norm;
  }

  public double[] getCount_A_C_3_norm() {
    return count_A_C_3_norm;
  }

  public double[] getCount_A_G_5_norm() {
    return count_A_G_5_norm;
  }

  public double[] getCount_A_G_3_norm() {
    return count_A_G_3_norm;
  }

  public double[] getCount_A_T_5_norm() {
    return count_A_T_5_norm;
  }

  public double[] getCount_A_T_3_norm() {
    return count_A_T_3_norm;
  }

  public double[] getCount_C_A_5_norm() {
    return count_C_A_5_norm;
  }

  public double[] getCount_C_A_3_norm() {
    return count_C_A_3_norm;
  }

  public double[] getCount_C_G_5_norm() {
    return count_C_G_5_norm;
  }

  public double[] getCount_C_G_3_norm() {
    return count_C_G_3_norm;
  }

  public double[] getCount_G_C_5_norm() {
    return count_G_C_5_norm;
  }

  public double[] getCount_G_C_3_norm() {
    return count_G_C_3_norm;
  }

  public double[] getCount_G_T_5_norm() {
    return count_G_T_5_norm;
  }

  public double[] getCount_G_T_3_norm() {
    return count_G_T_3_norm;
  }

  public double[] getCount_T_A_5_norm() {
    return count_T_A_5_norm;
  }

  public double[] getCount_T_A_3_norm() {
    return count_T_A_3_norm;
  }

  public double[] getCount_T_C_5_norm() {
    return count_T_C_5_norm;
  }

  public double[] getCount_T_C_3_norm() {
    return count_T_C_3_norm;
  }

  public double[] getCount_T_G_5_norm() {
    return count_T_G_5_norm;
  }

  public double[] getCount_T_G_3_norm() {
    return count_T_G_3_norm;
  }

  public double[] getCount_C_T_5_norm() {
    return count_C_T_5_norm;
  }

  public double[] getCount_C_T_3_norm() {
    return count_C_T_3_norm;
  }

  public double[] getCount_G_A_5_norm() {
    return count_G_A_5_norm;
  }

  public double[] getCount_G_A_3_norm() {
    return count_G_A_3_norm;
  }

  public double[] getCount_forward_A_C_5() {
    return count_forward_A_C_5;
  }

  public double[] getCount_forward_A_G_5() {
    return count_forward_A_G_5;
  }

  public double[] getCount_forward_A_T_5() {
    return count_forward_A_T_5;
  }

  public double[] getCount_forward_A_C_3() {
    return count_forward_A_C_3;
  }

  public double[] getCount_forward_A_G_3() {
    return count_forward_A_G_3;
  }

  public double[] getCount_forward_A_T_3() {
    return count_forward_A_T_3;
  }

  public double[] getCount_forward_C_A_5() {
    return count_forward_C_A_5;
  }

  public double[] getCount_forward_C_G_5() {
    return count_forward_C_G_5;
  }

  public double[] getCount_forward_C_T_5() {
    return count_forward_C_T_5;
  }

  public double[] getCount_forward_C_A_3() {
    return count_forward_C_A_3;
  }

  public double[] getCount_forward_C_G_3() {
    return count_forward_C_G_3;
  }

  public double[] getCount_forward_C_T_3() {
    return count_forward_C_T_3;
  }

  public double[] getCount_forward_G_A_5() {
    return count_forward_G_A_5;
  }

  public double[] getCount_forward_G_C_5() {
    return count_forward_G_C_5;
  }

  public double[] getCount_forward_G_T_5() {
    return count_forward_G_T_5;
  }

  public double[] getCount_forward_G_A_3() {
    return count_forward_G_A_3;
  }

  public double[] getCount_forward_G_C_3() {
    return count_forward_G_C_3;
  }

  public double[] getCount_forward_G_T_3() {
    return count_forward_G_T_3;
  }

  public double[] getCount_forward_T_A_5() {
    return count_forward_T_A_5;
  }

  public double[] getCount_forward_T_C_5() {
    return count_forward_T_C_5;
  }

  public double[] getCount_forward_T_G_5() {
    return count_forward_T_G_5;
  }

  public double[] getCount_forward_T_A_3() {
    return count_forward_T_A_3;
  }

  public double[] getCount_forward_T_C_3() {
    return count_forward_T_C_3;
  }

  public double[] getCount_forward_T_G_3() {
    return count_forward_T_G_3;
  }

  public double[] getCount_reverse_A_C_5() {
    return count_reverse_A_C_5;
  }

  public double[] getCount_reverse_A_G_5() {
    return count_reverse_A_G_5;
  }

  public double[] getCount_reverse_A_T_5() {
    return count_reverse_A_T_5;
  }

  public double[] getCount_reverse_A_C_3() {
    return count_reverse_A_C_3;
  }

  public double[] getCount_reverse_A_G_3() {
    return count_reverse_A_G_3;
  }

  public double[] getCount_reverse_A_T_3() {
    return count_reverse_A_T_3;
  }

  public double[] getCount_reverse_C_A_5() {
    return count_reverse_C_A_5;
  }

  public double[] getCount_reverse_C_G_5() {
    return count_reverse_C_G_5;
  }

  public double[] getCount_reverse_C_T_5() {
    return count_reverse_C_T_5;
  }

  public double[] getCount_reverse_C_A_3() {
    return count_reverse_C_A_3;
  }

  public double[] getCount_reverse_C_G_3() {
    return count_reverse_C_G_3;
  }

  public double[] getCount_reverse_C_T_3() {
    return count_reverse_C_T_3;
  }

  public double[] getCount_reverse_G_A_5() {
    return count_reverse_G_A_5;
  }

  public double[] getCount_reverse_G_C_5() {
    return count_reverse_G_C_5;
  }

  public double[] getCount_reverse_G_T_5() {
    return count_reverse_G_T_5;
  }

  public double[] getCount_reverse_G_A_3() {
    return count_reverse_G_A_3;
  }

  public double[] getCount_reverse_G_C_3() {
    return count_reverse_G_C_3;
  }

  public double[] getCount_reverse_G_T_3() {
    return count_reverse_G_T_3;
  }

  public double[] getCount_reverse_T_A_5() {
    return count_reverse_T_A_5;
  }

  public double[] getCount_reverse_T_C_5() {
    return count_reverse_T_C_5;
  }

  public double[] getCount_reverse_T_G_5() {
    return count_reverse_T_G_5;
  }

  public double[] getCount_reverse_T_A_3() {
    return count_reverse_T_A_3;
  }

  public double[] getCount_reverse_T_C_3() {
    return count_reverse_T_C_3;
  }

  public double[] getCount_reverse_T_G_3() {
    return count_reverse_T_G_3;
  }

  public double[] getCountA_ref_forward_5() {
    return countA_ref_forward_5;
  }

  public double[] getCountC_ref_forward_5() {
    return countC_ref_forward_5;
  }

  public double[] getCountG_ref_forward_5() {
    return countG_ref_forward_5;
  }

  public double[] getCountT_ref_forward_5() {
    return countT_ref_forward_5;
  }

  public double[] getCountA_ref_forward_3() {
    return countA_ref_forward_3;
  }

  public double[] getCountC_ref_forward_3() {
    return countC_ref_forward_3;
  }

  public double[] getCountG_ref_forward_3() {
    return countG_ref_forward_3;
  }

  public double[] getCountT_ref_forward_3() {
    return countT_ref_forward_3;
  }

  public double[] getCountA_ref_reverse_5() {
    return countA_ref_reverse_5;
  }

  public double[] getCountC_ref_reverse_5() {
    return countC_ref_reverse_5;
  }

  public double[] getCountG_ref_reverse_5() {
    return countG_ref_reverse_5;
  }

  public double[] getCountT_ref_reverse_5() {
    return countT_ref_reverse_5;
  }

  public double[] getCountA_ref_reverse_3() {
    return countA_ref_reverse_3;
  }

  public double[] getCountC_ref_reverse_3() {
    return countC_ref_reverse_3;
  }

  public double[] getCountG_ref_reverse_3() {
    return countG_ref_reverse_3;
  }

  public double[] getCountT_ref_reverse_3() {
    return countT_ref_reverse_3;
  }

  public double[] getCount_forward_A_0_5() {
    return count_forward_A_0_5;
  }

  public double[] getCount_forward_C_0_5() {
    return count_forward_C_0_5;
  }

  public double[] getCount_forward_G_0_5() {
    return count_forward_G_0_5;
  }

  public double[] getCount_forward_T_0_5() {
    return count_forward_T_0_5;
  }

  public double[] getCount_reverse_A_0_5() {
    return count_reverse_A_0_5;
  }

  public double[] getCount_reverse_C_0_5() {
    return count_reverse_C_0_5;
  }

  public double[] getCount_reverse_G_0_5() {
    return count_reverse_G_0_5;
  }

  public double[] getCount_reverse_T_0_5() {
    return count_reverse_T_0_5;
  }

  public double[] getCount_forward_A_0_3() {
    return count_forward_A_0_3;
  }

  public double[] getCount_forward_C_0_3() {
    return count_forward_C_0_3;
  }

  public double[] getCount_forward_G_0_3() {
    return count_forward_G_0_3;
  }

  public double[] getCount_forward_T_0_3() {
    return count_forward_T_0_3;
  }

  public double[] getCount_reverse_A_0_3() {
    return count_reverse_A_0_3;
  }

  public double[] getCount_reverse_C_0_3() {
    return count_reverse_C_0_3;
  }

  public double[] getCount_reverse_G_0_3() {
    return count_reverse_G_0_3;
  }

  public double[] getCount_reverse_T_0_3() {
    return count_reverse_T_0_3;
  }

  public double[] getCount_forward_0_A_5() {
    return count_forward_0_A_5;
  }

  public double[] getCount_forward_0_C_5() {
    return count_forward_0_C_5;
  }

  public double[] getCount_forward_0_G_5() {
    return count_forward_0_G_5;
  }

  public double[] getCount_forward_0_T_5() {
    return count_forward_0_T_5;
  }

  public double[] getCount_reverse_0_A_5() {
    return count_reverse_0_A_5;
  }

  public double[] getCount_reverse_0_C_5() {
    return count_reverse_0_C_5;
  }

  public double[] getCount_reverse_0_G_5() {
    return count_reverse_0_G_5;
  }

  public double[] getCount_reverse_0_T_5() {
    return count_reverse_0_T_5;
  }

  public double[] getCount_forward_0_A_3() {
    return count_forward_0_A_3;
  }

  public double[] getCount_forward_0_C_3() {
    return count_forward_0_C_3;
  }

  public double[] getCount_forward_0_G_3() {
    return count_forward_0_G_3;
  }

  public double[] getCount_forward_0_T_3() {
    return count_forward_0_T_3;
  }

  public double[] getCount_reverse_0_A_3() {
    return count_reverse_0_A_3;
  }

  public double[] getCount_reverse_0_C_3() {
    return count_reverse_0_C_3;
  }

  public double[] getCount_reverse_0_G_3() {
    return count_reverse_0_G_3;
  }

  public double[] getCount_reverse_0_T_3() {
    return count_reverse_0_T_3;
  }

  public double[] getCount_A_0_5_norm() {
    return count_A_0_5_norm;
  }

  public double[] getCount_A_0_3_norm() {
    return count_A_0_3_norm;
  }

  public double[] getCount_C_0_5_norm() {
    return count_C_0_5_norm;
  }

  public double[] getCount_C_0_3_norm() {
    return count_C_0_3_norm;
  }

  public double[] getCount_G_0_5_norm() {
    return count_G_0_5_norm;
  }

  public double[] getCount_G_0_3_norm() {
    return count_G_0_3_norm;
  }

  public double[] getCount_T_0_5_norm() {
    return count_T_0_5_norm;
  }

  public double[] getCount_T_0_3_norm() {
    return count_T_0_3_norm;
  }

  public double[] getCount_0_A_5_norm() {
    return count_0_A_5_norm;
  }

  public double[] getCount_0_A_3_norm() {
    return count_0_A_3_norm;
  }

  public double[] getCount_0_C_5_norm() {
    return count_0_C_5_norm;
  }

  public double[] getCount_0_C_3_norm() {
    return count_0_C_3_norm;
  }

  public double[] getCount_0_G_5_norm() {
    return count_0_G_5_norm;
  }

  public double[] getCount_0_G_3_norm() {
    return count_0_G_3_norm;
  }

  public double[] getCount_0_T_5_norm() {
    return count_0_T_5_norm;
  }

  public double[] getCount_0_T_3_norm() {
    return count_0_T_3_norm;
  }

  public double[] getCountA_forward_5() {
    return countA_forward_5;
  }

  public double[] getCountC_forward_5() {
    return countC_forward_5;
  }

  public double[] getCountG_forward_5() {
    return countG_forward_5;
  }

  public double[] getCountT_forward_5() {
    return countT_forward_5;
  }

  public double[] getCountA_forward_3() {
    return countA_forward_3;
  }

  public double[] getCountC_forward_3() {
    return countC_forward_3;
  }

  public double[] getCountG_forward_3() {
    return countG_forward_3;
  }

  public double[] getCountT_forward_3() {
    return countT_forward_3;
  }

  public double[] getCountA_reverse_5() {
    return countA_reverse_5;
  }

  public double[] getCountC_reverse_5() {
    return countC_reverse_5;
  }

  public double[] getCountG_reverse_5() {
    return countG_reverse_5;
  }

  public double[] getCountT_reverse_5() {
    return countT_reverse_5;
  }

  public double[] getCountA_reverse_3() {
    return countA_reverse_3;
  }

  public double[] getCountC_reverse_3() {
    return countC_reverse_3;
  }

  public double[] getCountG_reverse_3() {
    return countG_reverse_3;
  }

  public double[] getCountT_reverse_3() {
    return countT_reverse_3;
  }

  public double[] getCount_total_forward_3() {
    final double[] total_forward_3 = new double[this.length];
    for (int i = 0; i < this.length; i++) {
      total_forward_3[i] =
          countA_forward_3[i] + countC_forward_3[i] + countG_forward_3[i] + countT_forward_3[i];
    }

    return total_forward_3;
  }

  public double[] getCount_total_reverse_3() {
    final double[] total_reverse_3 = new double[this.length];
    for (int i = 0; i < this.length; i++) {
      total_reverse_3[i] =
          countA_reverse_3[i] + countC_reverse_3[i] + countG_reverse_3[i] + countT_reverse_3[i];
    }

    return total_reverse_3;
  }

  public double[] getCount_total_reverse_5() {
    final double[] total_reverse_5 = new double[this.length];
    for (int i = 0; i < this.length; i++) {
      total_reverse_5[i] =
          countA_reverse_5[i] + countC_reverse_5[i] + countG_reverse_5[i] + countT_reverse_5[i];
    }

    return total_reverse_5;
  }

  public double[] getCount_total_forward_5() {
    final double[] total_forward_5 = new double[this.length];
    for (int i = 0; i < this.length; i++) {
      total_forward_5[i] =
          countA_forward_5[i] + countC_forward_5[i] + countG_forward_5[i] + countT_forward_5[i];
    }

    return total_forward_5;
  }

  public double getCountAllBasesSample() {
    double sum = 0.0;

    for (int i = 0; i < this.length; i++) {
      sum +=
          getCount_total_forward_5()[i]
              + getCount_total_forward_3()[i]
              + getCount_total_reverse_3()[i]
              + getCount_total_reverse_5()[i];
    }
    return sum;
  }

  public double getCountAllBasesRef() {
    double sum = 0.0;

    for (int i = 0; i < this.length; i++) {
      sum +=
          countA_ref_forward_3[i]
              + countC_ref_forward_3[i]
              + countG_ref_forward_3[i]
              + countT_ref_forward_3[i]
              + countA_ref_forward_5[i]
              + countC_ref_forward_5[i]
              + countG_ref_forward_5[i]
              + countT_ref_forward_5[i]
              + countA_ref_reverse_3[i]
              + countC_ref_reverse_3[i]
              + countG_ref_reverse_3[i]
              + countT_ref_reverse_3[i]
              + countA_ref_reverse_5[i]
              + countC_ref_reverse_5[i]
              + countG_ref_reverse_5[i]
              + countT_ref_reverse_5[i];
    }
    return sum;
  }

  public double getCountA_sample() {
    double countA = 0.0;
    for (int i = 0; i < this.length; i++) {
      countA +=
          this.countA_forward_3[i]
              + this.countA_forward_5[i]
              + this.countA_reverse_3[i]
              + this.countA_reverse_5[i];
    }
    return countA;
  }

  public double getCountC_sample() {
    double count = 0.0;
    for (int i = 0; i < this.length; i++) {
      count +=
          this.countC_forward_3[i]
              + this.countC_forward_5[i]
              + this.countC_reverse_3[i]
              + this.countC_reverse_5[i];
    }
    return count;
  }

  public double getCountG_sample() {
    double count = 0.0;
    for (int i = 0; i < this.length; i++) {
      count +=
          this.countG_forward_3[i]
              + this.countG_forward_5[i]
              + this.countG_reverse_3[i]
              + this.countG_reverse_5[i];
    }
    return count;
  }

  public double getCountT_sample() {
    double count = 0.0;
    for (int i = 0; i < this.length; i++) {
      count +=
          this.countT_forward_3[i]
              + this.countT_forward_5[i]
              + this.countT_reverse_3[i]
              + this.countT_reverse_5[i];
    }
    return count;
  }

  public double getCountA_ref() {
    double countA = 0.0;
    for (int i = 0; i < this.length; i++) {
      countA +=
          this.countA_ref_forward_3[i]
              + this.countA_ref_forward_5[i]
              + this.countA_ref_reverse_3[i]
              + this.countA_ref_reverse_5[i];
    }
    return countA;
  }

  public double getCountC_ref() {
    double count = 0.0;
    for (int i = 0; i < this.length; i++) {
      count +=
          this.countC_ref_forward_3[i]
              + this.countC_ref_forward_5[i]
              + this.countC_ref_reverse_3[i]
              + this.countC_ref_reverse_5[i];
    }
    return count;
  }

  public double getCountG_ref() {
    double count = 0.0;
    for (int i = 0; i < this.length; i++) {
      count +=
          this.countG_ref_forward_3[i]
              + this.countG_ref_forward_5[i]
              + this.countG_ref_reverse_3[i]
              + this.countG_ref_reverse_5[i];
    }
    return count;
  }

  public double getCountT_ref() {
    double count = 0.0;
    for (int i = 0; i < this.length; i++) {
      count +=
          this.countT_ref_forward_3[i]
              + this.countT_ref_forward_5[i]
              + this.countT_ref_reverse_3[i]
              + this.countT_ref_reverse_5[i];
    }
    return count;
  }

  public double[] getCountS_forward_5() {
    return countS_forward_5;
  }

  public double[] getCountS_forward_3() {
    return countS_forward_3;
  }

  public double[] getCountS_reverse_5() {
    return countS_reverse_5;
  }

  public double[] getCountS_reverse_3() {
    return countS_reverse_3;
  }
}
