package com.tinkerpop.blueprints.versioned;

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
