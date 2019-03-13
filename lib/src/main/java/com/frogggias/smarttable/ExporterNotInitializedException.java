package com.frogggias.smarttable;

/**
 * @author Maroš Šeleng
 */
public class ExporterNotInitializedException extends RuntimeException {
    public ExporterNotInitializedException() {
        super("You need to initialize the exporter first.");
    }
}
