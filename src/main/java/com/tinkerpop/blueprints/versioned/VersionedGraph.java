package com.tinkerpop.blueprints.versioned;

import com.tinkerpop.blueprints.versioned.exceptions.VersionNoLongerAvailableException;

/**
 * Created with IntelliJ IDEA.
 * User: wires
 * Date: 11/1/12
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface VersionedGraph
{
    /**
     * Create a versioned subset
     *
     * @param id
     * @param version
     * @param t
     * @return
     */
    VersionedSubset createVersionedSubset(Object id, long version, long t) throws VersionNoLongerAvailableException;

    /**
     * Create consistent view at time {@code t}
     *
     * @param t time (in ticks)
     * @return
     */
    ConsistentView createConsistentView(long t) throws VersionNoLongerAvailableException;

    /**
     *  Current tick (transactions counter)
     */
    long tick();
}
