package com.tinkerpop.blueprints.versioned.impl.gc;

import com.tinkerpop.blueprints.versioned.GarbageCollector;
import com.tinkerpop.blueprints.versioned.exceptions.VersionNoLongerAvailableException;

public class DummyGC implements GarbageCollector
{
    @Override
    public boolean release(long tick)
    {
        return false;
    }

    @Override
    public void lock(long tick) throws VersionNoLongerAvailableException
    {
    }
}
