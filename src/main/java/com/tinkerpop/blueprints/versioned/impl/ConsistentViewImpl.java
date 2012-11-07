package com.tinkerpop.blueprints.versioned.impl;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.versioned.ConsistentView;
import com.tinkerpop.blueprints.versioned.SymbolicVertex;
import com.tinkerpop.blueprints.versioned.VersionedGraph;

public class ConsistentViewImpl implements ConsistentView
{
    class SV implements SymbolicVertex
    {
        final private Object id;

        SV(Object id)
        {
            this.id = id;
        }

        @Override
        public Object id()
        {
            return id;
        }

        @Override
        public Vertex vertex()
        {
            graph.
        }
    }

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
        graph.latestVertex(tick);
    }
}
