package com.tinkerpop.blueprints.versioned.exceptions;

/**
 *
 */
public class MaxTriesExceededException extends RuntimeException
{
    public MaxTriesExceededException(String action)
    {
        super("Maximum tries exceeded while " + action);
    }
}
