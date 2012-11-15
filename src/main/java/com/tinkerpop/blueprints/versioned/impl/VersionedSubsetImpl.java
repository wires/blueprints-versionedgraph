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

    public SymbolicVertex getVertex(Object id) throws NotSymbolicException
    {
        final VersionedVertex v = ownedVx.get(id);
        if(v != null)
            throw new NotSymbolicException(v);

        return null; // TODO implement
    }

    public VersionedVertex getVersionedVertex(Object id) throws NotVersionedException
    {
        final VersionedVertex v = ownedVx.get(id);
        if(v == null)
            throw new NotVersionedException(id);

        return v;
    }

    @Override
    public Iterable<VersionedVertex> vertices()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Iterable<VersionedEdge> edges()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /** {@inheritDoc} */
    @Override
    public VersionedVertex addVertex(Object id)
    {
        return null;
    }

    @Override
    public long commit()
    {
        return 0;
    }

    @Override
    public void rollback()
    {
        // TODO je weet toch
    }
}
