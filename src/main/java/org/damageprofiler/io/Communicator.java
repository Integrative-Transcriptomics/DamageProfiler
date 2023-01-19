package org.damageprofiler.io;

import javafx.scene.paint.Color;

/** Created by neukamm on 11.11.2016. */
public class Communicator {

  // file options
  private String input;
  private String reference = "";
  private String outfolder;
  private String specieslist_filepath = null;

  // damage calculation
  private int threshold = 25;

  private double yAxis_damageplot = 0.4;
  private int length = 100;
  private boolean use_merged_and_mapped_reads = false;
  private boolean ssLibsProtocolUsed = false;

  // specie filtering
  private String species_ref_identifier = null;

  // plot settings
  private String title_plots;
  private Color color_DP_C_to_T = Color.RED;
  private Color color_DP_G_to_A = Color.BLUE;
  private Color color_DP_insertions = Color.valueOf("FF00FF");
  private Color color_DP_deletions = Color.GREEN;
  private Color color_DP_other = Color.GREY;

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public String getOutfolder() {
    return outfolder;
  }

  public void setOutfolder(String outfolder) {
    this.outfolder = outfolder;
  }

  public int getThreshold() {
    return threshold;
  }

  public void setThreshold(int threshold) {
    this.threshold = threshold;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public String getSpecies_ref_identifier() {
    return species_ref_identifier;
  }

  public void setSpecies_ref_identifier(String species_ref_identifier) {
    this.species_ref_identifier = species_ref_identifier;
  }

  public boolean isUse_merged_and_mapped_reads() {
    return use_merged_and_mapped_reads;
  }

  public void setUse_merged_and_mapped_reads(boolean use_merged_and_mapped_reads) {
    this.use_merged_and_mapped_reads = use_merged_and_mapped_reads;
  }

  public boolean isSsLibsProtocolUsed() {
    return ssLibsProtocolUsed;
  }

  public void setSsLibsProtocolUsed(boolean ssLibsProtocolUsed) {
    this.ssLibsProtocolUsed = ssLibsProtocolUsed;
  }

  public void setyAxis_damageplot(double yAxis_damageplot) {
    this.yAxis_damageplot = yAxis_damageplot;
  }

  public double getyAxis_damageplot() {
    return yAxis_damageplot;
  }

  public String getTitle_plots() {
    return title_plots;
  }

  public void setTitle_plots(String title_plots) {
    this.title_plots = title_plots;
  }

  public String getSpecieslist_filepath() {
    return specieslist_filepath;
  }

  public void setSpecieslist_filepath(String specieslist_filepath) {
    this.specieslist_filepath = specieslist_filepath;
  }

  public void setColor_DP_C_to_T(Color color_c_to_t) {
    this.color_DP_C_to_T = color_c_to_t;
  }

  public void setColor_DP_G_to_A(Color color_g_to_a) {
    this.color_DP_G_to_A = color_g_to_a;
  }

  public void setColor_DP_insertions(Color color_insertions) {
    this.color_DP_insertions = color_insertions;
  }

  public void setColor_DP_deletions(Color color_deletions) {
    this.color_DP_deletions = color_deletions;
  }

  public void setColor_DP_other(Color color_other) {
    this.color_DP_other = color_other;
  }

  public Color getColor_DP_C_to_T() {
    return color_DP_C_to_T;
  }

  public Color getColor_DP_G_to_A() {
    return color_DP_G_to_A;
  }

  public Color getColor_DP_insertions() {
    return color_DP_insertions;
  }

  public Color getColor_DP_deletions() {
    return color_DP_deletions;
  }

  public Color getColor_DP_other() {
    return color_DP_other;
  }
}
