package com.tinkerpop.blueprints.versioned;

/**
 *
 */
public interface VersionedVertex
{
    VersionedSubset owner();
    SymbolicVertex symbolicVertex();
    Object id();
}
