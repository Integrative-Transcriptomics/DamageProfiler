package org.damageprofiler.services;

public enum Version {
  VERSION("1.1");

  private final String value;

  Version(final String s) {
    this.value = s;
  }

  public String getValue() {
    return value;
  }
}
