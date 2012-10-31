package com.tinkerpop.blueprints.versioned;

/**
 *
 */
public class ConsistentView
{
    final protected VersionedGraph graph;

    // we are currently viewing the graph up to this transaction
    protected long tick;

    ConsistentView(VersionedGraph graph, long tick)
    {
        this.tick = tick;
        this.graph = graph;
    }

    /**
     * Refresh the view to consider the latest transactions as well.
     *
     * Don't do this while performing a traversal on this view.
     *
     */
    public void refresh()
    {
        final long t = graph.tick();
        graph.release(t);
        tick = t;
    }

    void release()
    {
        graph.release(tick);
    }

    /**
     * Get a SymblicVertex, with the properties of the latest (relative to our view) vertex.
     *
     */
    public SymbolicVertex getVertex(Object id)
    {
        graph.latestVertex(tick);
    }
}
