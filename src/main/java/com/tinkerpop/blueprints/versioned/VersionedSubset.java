package com.tinkerpop.blueprints.versioned;

import com.tinkerpop.blueprints.versioned.exceptions.NotVersionedException;

/**
 *
 */
public interface VersionedSubset
{
    /**
     * Create a vertex owned by this subset.
     *
     * @param id
     * @return
     */
    VersionedVertex addVertex(Object id);

    /**
     * Get a VersionedVertex by {@code id}.
     *
     * You can only get a versioned view on a vertex if you created it before in this instance of
     * {@link com.tinkerpop.blueprints.versioned.VersionedSubset}.
     *
     * You probably shouldn't use this method and just keep the object you created with
     * {@link com.tinkerpop.blueprints.versioned.VersionedSubset#addVertex(Object id)}
     *
     * @param id
     * @return
     * @throws com.tinkerpop.blueprints.versioned.exceptions.NotVersionedException
     *
     */
    VersionedVertex getVersionedVertex(Object id) throws NotVersionedException;

    Iterable<VersionedVertex> vertices();
    Iterable<VersionedEdge> edges();


    /**
     * Commit this subset to the graph.
     *
     * You can no longer mutate this subset after committing.
     * TODO implement this ^^
     *
     * @return The tick at which this transaction was recorded. You can use this to
     *   get a consistent view involving everything up to this transaction only.
     *
     * TODO don't leak ticks? ie. add a method: ConsistentView commitAndView()
     * TODO check and bail out concurrent commits, ie. let VersionedGraph maintain MVCC counters
     *
     */
    long commit();

    void rollback();
}
