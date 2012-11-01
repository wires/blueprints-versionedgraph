package com.tinkerpop.blueprints.versioned;

import com.tinkerpop.blueprints.versioned.exceptions.VersionNoLongerAvailableException;

/**
 * Responsible for tracking unused versions.
 */
public interface GarbageCollector
{
    void lock(long tick) throws VersionNoLongerAvailableExceptioen;

    boolean release(long tick);
}
