package com.tinkerpop.blueprints.versioned.lowlevel;


import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.versioned.*;
import com.tinkerpop.blueprints.versioned.exceptions.VersionNoLongerAvailableException;
import com.tinkerpop.blueprints.versioned.impl.VersionedGraphImpl;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.fest.assertions.Assertions.assertThat;

class TransactionFaker implements TransactionalGraph
{
    final Graph graph;

    public TransactionFaker(Graph graph)
    {
        this.graph = graph;
    }

    @Override
    public void stopTransaction(Conclusion conclusion)
    {}

    @Override
    public Features getFeatures()
    {
        return graph.getFeatures();
    }

    @Override
    public Vertex addVertex(Object id)
    {
        return graph.addVertex(id);
    }

    @Override
    public Vertex getVertex(Object id)
    {
        return graph.getVertex(id);
    }

    @Override
    public void removeVertex(Vertex vertex)
    {
        graph.removeVertex(vertex);
    }

    @Override
    public Iterable<Vertex> getVertices()
    {
        return graph.getVertices();
    }

    @Override
    public Iterable<Vertex> getVertices(String key, Object value)
    {
        return graph.getVertices(key, value);
    }

    @Override
    public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex, String label)
    {
        return graph.addEdge(id, outVertex, inVertex, label);
    }

    @Override
    public Edge getEdge(Object id)
    {
        return graph.getEdge(id);
    }

    @Override
    public void removeEdge(Edge edge)
    {
        graph.removeEdge(edge);
    }

    @Override
    public Iterable<Edge> getEdges()
    {
        return graph.getEdges();
    }

    @Override
    public Iterable<Edge> getEdges(String key, Object value)
    {
        return graph.getEdges(key, value);
    }

    @Override
    public void shutdown()
    {
        graph.shutdown();
    }
}

public class APITest
{
    TransactionalGraph tg;
    VersionedGraph vg;

    @BeforeTest
    public void setup() {
        tg = new TransactionFaker(new TinkerGraph());
        vg = new VersionedGraphImpl(tg);
    }

    @Test
    public void test() throws VersionNoLongerAvailableException
    {
        final VersionedSubset vss = vg.createVersionedSubset("A", 1);

        final VersionedVertex x = vss.addVertex("x");
        x.setProperty("foo-x", "bar");

        final VersionedVertex y = vss.addVertex("x");
        x.setProperty("foo-y", "baz");

        final long t = vss.commit();

        final VersionedSubset vss2 = vg.createVersionedSubset("A", 2);

        final VersionedVertex x2 = vss2.addVertex("x");
        x.setProperty("foo-x2", "bar2");

        final VersionedVertex y2 = vss2.addVertex("x");
        x.setProperty("foo-y2", "baz2");

        final long t2 = vss.commit();

        final ConsistentView cv1 = vg.createConsistentView(t);
        final ConsistentView cv2 = vg.createConsistentView(t);

        assertThat(cv1.getVertex("x").vertex().getProperty("foo-x")).isEqualTo("bar");
        assertThat(cv1.getVertex("y").vertex().getProperty("foo-y")).isEqualTo("baz");
        
        assertThat(cv1.getVertex("x").vertex().getProperty("foo-x2")).isEqualTo("bar2");
        assertThat(cv1.getVertex("y").vertex().getProperty("foo-y2")).isEqualTo("baz2");
    }
}
