package com.compare.core;

/**
 * a Help is any type of object that can be targetted by a system "help" command.
 * These objects are required to provide some information in the form of a
 * getDescription() method
 *
 * @author luke
 */
public interface HelpTarget {
    /**
     * returns a description of the object
     *
     * @return a description of the object
     */
    public String getDescription();
}
