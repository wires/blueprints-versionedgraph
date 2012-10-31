package com.tinkerpop.blueprints.versioned;

/**
 *
 */
public class NotSymbolicException extends Exception
{
    final VersionedVertex v;

    public NotSymbolicException(VersionedVertex v)
    {
        super("Vertex with id " + v.id() + " is not symbolic");
        this.v = v;
    }

    public VersionedVertex getVersionedVertex()
    {
        return v;
    }
}
