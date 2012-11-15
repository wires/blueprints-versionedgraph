package com.tinkerpop.blueprints.versioned;

import com.tinkerpop.blueprints.Element;

public interface VersionedVertex extends Element
{
    VersionedSubset owner();
    Object id();

    SymbolicVertex symbolicVertex();
}
