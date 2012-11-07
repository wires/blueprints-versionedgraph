package com.tinkerpop.blueprints.versioned.impl;

import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.versioned.*;
import com.tinkerpop.blueprints.versioned.exceptions.VersionNoLongerAvailableException;
import com.tinkerpop.blueprints.versioned.impl.gc.DummyGC;

import java.util.concurrent.atomic.AtomicLong;


/**
 * Gives a versioned view on a {@link com.tinkerpop.blueprints.TransactionalGraph}.
 *
 */
public class VersionedGraphImpl implements VersionedGraph
{
    final protected TransactionalGraph graph;
    final protected AtomicLong tx = new AtomicLong();
    final protected GarbageCollector gc = new DummyGC();

    public VersionedGraphImpl(TransactionalGraph graph)
    {
        this.graph = graph;
    }

    @Override public VersionedSubset createVersionedSubset(Object id, long version, long t) throws VersionNoLongerAvailableException
    {
        return new VersionedSubset(createConsistentView(t), id, version);
    }


    @Override public ConsistentView createConsistentView(long t) throws VersionNoLongerAvailableException
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
     * Get latest version of the vertex, considering transactions up to and including {@code tick}
     */
    void latestVertex(Object id, long tick)
    {

    }

    @Override public long tick()
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