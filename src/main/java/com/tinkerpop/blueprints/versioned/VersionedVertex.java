package com.tinkerpop.blueprints.versioned;

import com.tinkerpop.blueprints.Element;

public interface VersionedVertex extends Element
{
    VersionedSubset owner();
    SymbolicVertex symbolicVertex();
    Object id();
}
