package com.tinkerpop.blueprints.versioned.impl.gc;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Reference counter to keep track of views and their ticks.
 *
 * The natural ordering is defined by the tick times
 *
 */
public final class RefCounter implements Comparable<RefCounter>
{
    // the tick we are reference counting
    final private long t;

    // the reference counter
    final private AtomicLong refs = new AtomicLong(0);

    RefCounter(long t)
    {
        this.t = t;
    }

    long increment()
    {
        return refs.incrementAndGet();
    }

    long decrement()
    {
        return refs.decrementAndGet();
    }

    @Override
    public int compareTo(RefCounter o)
    {
        return Long.compare(t, o.t);
    }
}