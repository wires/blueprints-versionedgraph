package com.tinkerpop.blueprints.versioned;

import com.tinkerpop.blueprints.TransactionalGraph;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Gives a versioned view on a {@link TransactionalGraph}.
 *
 */
public class VersionedGraph
{
    final protected TransactionalGraph graph;

    final protected AtomicLong tx = new AtomicLong();

    final protected GarbageCollector gc = new DummyGC();

    public VersionedGraph(TransactionalGraph graph)
    {
        this.graph = graph;
    }

    /**
     * Create a versioned subset
     *
     * @param id
     * @param version
     * @param t
     * @return
     */
    public VersionedSubset createVersionedSubset(Object id, long version, long t) throws VersionNoLongerAvailableException
    {
        return new VersionedSubset(createConsistentView(t), id, version);
    }


    /**
     * Create consistent view at time {@code t}
     *
     * @param t time (in ticks)
     * @return
     */
    public ConsistentView createConsistentView(long t) throws VersionNoLongerAvailableException
    {
        gc.lock(t);

        try
        {
            // create new view
            return new ConsistentView(this, t);
        }
        catch (RuntimeException e)
        {
            // should this fail, release the lock
            gc.release(t);
            throw e;
        }
    }


    /**
     *  Current tick (transactions counter)
     */
    public long tick()
    {
        return tx.longValue();
    }


    long commit(VersionedSubset vss)
    {
        final long t = tx.incrementAndGet();

        // write nodes


        return t;
    }

    long refresh(long oldTick)
    {
        gc.release(oldTick);
        final long t = tick();
        try
        {
            gc.lock(t);
        }
        catch (VersionNoLongerAvailableException e)
        {
            // TODO this shouldn't happen...
        }
        return t;
    }

    void release(long tick)
    {
        gc.release(tick);
    }
}