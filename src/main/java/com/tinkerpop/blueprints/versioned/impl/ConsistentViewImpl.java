package com.tinkerpop.blueprints.versioned.impl;

import com.tinkerpop.blueprints.versioned.ConsistentView;
import com.tinkerpop.blueprints.versioned.SymbolicVertex;

public class ConsistentViewImpl implements ConsistentView
{
    final protected VersionedGraphImpl graph;

    // we are currently viewing the graph up to this transaction
    protected long tick;

    ConsistentViewImpl(VersionedGraphImpl graph, long tick)
    {
        this.tick = tick;
        this.graph = graph;
    }

    @Override public void refresh()
    {
        final long t = graph.tick();
        graph.release(t);
        tick = t;
    }

    void release()
    {
        graph.release(tick);
    }

    @Override public SymbolicVertex getVertex(Object id)
    {
        return null;
    }
}
