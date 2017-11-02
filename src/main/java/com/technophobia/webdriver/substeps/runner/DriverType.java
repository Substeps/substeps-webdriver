package com.technophobia.webdriver.substeps.runner;

/**
 * Interface to be called to determine if a specific driver is visual or not
 */
public interface DriverType {
    boolean isVisual();
}
