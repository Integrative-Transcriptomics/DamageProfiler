package org.damageprofiler.io;

import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogClass {

  public LogClass() {}

  public void setUp() {
    final URL url = this.getClass().getResource("/logger/logger.properties");
    PropertyConfigurator.configure(url);
  }

  public Logger getLogger(final Class c) {
    // creates a custom logger and log messages
    return Logger.getLogger(c);
  }

  public void updateLog4jConfiguration(final String logFile) {
    System.setProperty("logfile.name", logFile);
  }
}
