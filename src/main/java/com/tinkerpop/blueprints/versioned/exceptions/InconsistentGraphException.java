package com.tinkerpop.blueprints.versioned.exceptions;

/**
 * Thrown when some condition no longer holds for the graph
 */
public class InconsistentGraphException extends RuntimeException
{
    public InconsistentGraphException(String s)
    {
        super(s);
    }
}
