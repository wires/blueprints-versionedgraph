package com.tinkerpop.blueprints.versioned;

import com.tinkerpop.blueprints.versioned.exceptions.NotSymbolicException;
import com.tinkerpop.blueprints.versioned.exceptions.NotVersionedException;

/**
 *
 */
public interface VersionedSubset
{
    SymbolicVertex getVertex(Object id) throws NotSymbolicException;

    /**
     * Get a VersionedVertex by id.
     *
     * You can only get a versioned view on a vertex if you created it before in this instance of {@link com.tinkerpop.blueprints.versioned.VersionedSubset}.
     *
     * You probably shouldn't use this method and just keep the object you created with {@link com.tinkerpop.blueprints.versioned.VersionedSubset#addVertex(Object id)}
     *
     * @param id
     * @return
     * @throws com.tinkerpop.blueprints.versioned.exceptions.NotVersionedException
     */
    VersionedVertex getVersionedVertex(Object id) throws NotVersionedException;

    VersionedVertex addVertex(Object id);

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
}
