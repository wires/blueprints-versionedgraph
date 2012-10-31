package com.tinkerpop.blueprints.versioned;

import java.util.HashMap;
import java.util.Map;

/**
 * Construct using {@link VersionedGraph#createVersionedSubset(Object, long, long)}
 */
public class VersionedSubset
{
    final ConsistentView view;
    final long version;
    final Object id;

    // all vertices created by us (edges always attached to these, so no need to keep track)
    final Map<Object,VersionedVertex> ownedVx = new HashMap<Object, VersionedVertex>();

    VersionedSubset(ConsistentView view, Object id, long version)
    {
        this.view = view;
        this.id = id;
        this.version = version;
    }

    public SymbolicVertex getVertex(Object id) throws NotSymbolicException
    {
        final VersionedVertex v = ownedVx.get(id);
        if(v != null)
            throw new NotSymbolicException(v);


    }

    /**
     * Get a VersionedVertex by id.
     *
     * You can only get a versioned view on a vertex if you created it before in this instance of {@link VersionedSubset}.
     *
     * You probably shouldn't use this method and just keep the object you created with {@link VersionedSubset#addVertex(Object id)}
     *
     * @param id
     * @return
     * @throws NotVersionedException
     */
    public VersionedVertex getVersionedVertex(Object id) throws NotVersionedException
    {
        final VersionedVertex v = ownedVx.get(id);
        if(v == null)
            throw new NotVersionedException(id);

        return v;
    }


    public VersionedVertex addVertex(Object id)
    {
        final VersionedVertex vx;

        ownedVx.put(id, vx);
        return vx;
    }

    // TODO VersionedEdge
    void addEdge(SymbolicVertex a, SymbolicVertex b);
    void addEdge(SymbolicVertex a, VersionedVertex b);
    void addEdge(VersionedVertex b, SymbolicVertex a);
    void addEdge(VersionedVertex a, VersionedVertex b);

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
    public long commit()
    {
        return graph.commit(this);
    }

    void rollback()
    {
        // TODO je weet toch
    }
}
