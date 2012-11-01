package com.tinkerpop.blueprints.versioned;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class GarbageCollector
{
    /**
     * The ticks locked by a view
     */
    final protected ConcurrentSkipListMap<Long, AtomicLong> locks = new ConcurrentSkipListMap<Long, AtomicLong>();


    private long update(long tick, boolean increment)
    {
        // get the refcounter
        AtomicLong rc = locks.get(tick);

        // there is none, so we add one
        if(rc == null)
        {
            // initialize new reference counter
            final AtomicLong refCounter = new AtomicLong();

            // if someone put one in the meantime, rc != null
            rc = locks.putIfAbsent(tick, refCounter);

            if(rc == null)
                rc = refCounter;
        }

        //  now that we have a reference counter, update it
        if (increment)
            return rc.incrementAndGet();

        // TODO safely remove rc from locks
        return rc.decrementAndGet();
    }

    public void lock(long tick) throws VersionNoLongerAvailableException
    {
        // TODO check boundaries
        update(tick, true);
    }

    /**
     *
     *
     * @param tick
     * @return true of the {@link GarbageCollector#gcUpperbound()} value has changed
     */
    public boolean release(long tick)
    {
        final long rc = update(tick, false);
        if (rc == 0)
        {
            // TODO concurrently set the GC upperbound.. grr ?
        }
    }

    // values up to this tick can be GC'd
    public long gcUpperbound()
    {
        // TODO ja, ok, maar nu ook er voor zorgen dat locking tijdens GC goed gaat
        // dwz. Zodra GC begint, markeer
        try
        {
            return locks.firstKey();
        }
        catch (NoSuchElementException e)
        {
            return Long.MAX_VALUE;
        }
    }

    // times as lock as this tick can be locked
    public long lockLowerbound()
    {
    }
}
