package com.tinkerpop.blueprints.versioned.impl;

import com.tinkerpop.blueprints.versioned.*;
import com.tinkerpop.blueprints.versioned.exceptions.NotSymbolicException;
import com.tinkerpop.blueprints.versioned.exceptions.NotVersionedException;

import java.util.HashMap;
import java.util.Map;

public class VersionedSubsetImpl implements VersionedSubset
{
    final ConsistentView view;
    final long version;
    final Object id;

    // all vertices created by us (edges always attached to these, so no need to keep track)
    final Map<Object,VersionedVertex> ownedVx = new HashMap<Object, VersionedVertex>();

    VersionedSubsetImpl(ConsistentView view, Object id, long version)
    {
        this.view = view;
        this.id = id;
        this.version = version;
    }

    /** {@inheritDoc} */
    @Override public SymbolicVertex getVertex(Object id) throws NotSymbolicException
    {
        final VersionedVertex v = ownedVx.get(id);
        if(v != null)
            throw new NotSymbolicException(v);
    }

    /** {@inheritDoc} */
    @Override public VersionedVertex getVersionedVertex(Object id) throws NotVersionedException
    {
        final VersionedVertex v = ownedVx.get(id);
        if(v == null)
            throw new NotVersionedException(id);

        return v;
    }

    @Override public VersionedVertex addVertex(Object id)
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

    @Override public long commit()
    {
        return graph.commit(this);
    }

    void rollback()
    {
        // TODO je weet toch
    }
}
