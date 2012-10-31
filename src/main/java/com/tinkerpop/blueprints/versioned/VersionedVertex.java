package com.tinkerpop.blueprints.versioned;

/**
 *
 */
public interface VersionedVertex
{
    Object id();
    VersionedSubset owner();
}
