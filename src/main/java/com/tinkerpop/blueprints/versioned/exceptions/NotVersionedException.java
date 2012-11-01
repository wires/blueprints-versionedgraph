package com.tinkerpop.blueprints.versioned.exceptions;

/**
 *
 */
public class NotVersionedException extends Exception
{
    public NotVersionedException(Object id)
    {
        super("Vertex with id " + id + " is not versioned");
    }
}
