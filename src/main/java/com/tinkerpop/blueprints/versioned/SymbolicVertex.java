package com.tinkerpop.blueprints.versioned;

import com.tinkerpop.blueprints.Element;

/**
 * Immutable vertex.
 *
 * The version of the data depends on which {@link ConsistentView} is used to obtain
 * the SymbolicVertex.
 *
 * {@inheritDoc}
 */
public interface SymbolicVertex extends Element
{
    Object id();
}