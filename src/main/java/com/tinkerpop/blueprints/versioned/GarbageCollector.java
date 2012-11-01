package com.tinkerpop.blueprints.versioned;

/**
 * Created with IntelliJ IDEA.
 * User: wires
 * Date: 11/1/12
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GarbageCollector
{
    void lock(long tick) throws VersionNoLongerAvailableException;

    boolean release(long tick);
}
