package org.damageprofiler.IO;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.URL;

public class LogClass {

    public LogClass(){}


    /**
     * Set up logger properties.
     * Use file logger.properties for changes.
     */

    public void setUp(){
        URL url = this.getClass().getResource("/logger/logger.properties");
        PropertyConfigurator.configure(url);
    }

    /**
     * Return logger for specific Class
     * @param c
     * @return
     */

    public Logger getLogger(Class c){
        // creates a custom logger and log messages
        return Logger.getLogger(c);
    }


    /**
     * Update logger configuration.
     * i.e. set user specific file path
     * @param logFile
     */

    public void updateLog4jConfiguration(String logFile) {
        System.setProperty("logfile.name", logFile);
    }

}
