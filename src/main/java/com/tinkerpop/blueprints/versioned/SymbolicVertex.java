package com.tinkerpop.blueprints.versioned;

import com.tinkerpop.blueprints.Vertex;

public interface SymbolicVertex
{
    Object id();

    /**
     * Immutable vertex.
     *
     * The version of the data depends on which {@link ConsistentView} is used to obtain
     * the SymbolicVertex.
     */
    Vertex vertex();
}