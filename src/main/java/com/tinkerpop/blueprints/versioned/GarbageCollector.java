package com.tinkerpop.blueprints.versioned;

import com.tinkerpop.blueprints.versioned.exceptions.*;

/**
 * Responsible for tracking unused versions.
 */
public interface GarbageCollector
{
    void lock(long tick) throws VersionNoLongerAvailableException;

    boolean release(long tick);
}
