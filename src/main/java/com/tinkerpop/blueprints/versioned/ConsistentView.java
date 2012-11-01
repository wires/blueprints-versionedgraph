package com.tinkerpop.blueprints.versioned;

/**
 * Curser onto the graph, gives a consistent view.
 */
public interface ConsistentView
{
    /**
     * Refresh the view to consider the latest transactions as well.
     *
     * Don't do this while performing a traversal on this view.
     *
     */
    void refresh();

    /**
     * Get a SymblicVertex, with the properties of the latest (relative to our view) vertex.
     *
     */
    SymbolicVertex getVertex(Object id);
}
