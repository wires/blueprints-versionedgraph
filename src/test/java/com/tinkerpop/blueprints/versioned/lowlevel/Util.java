package com.tinkerpop.blueprints.versioned.lowlevel;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import java.util.Random;

public class Util
{
    static final Random r = new Random();

    /** Create {@link TinkerGraph} as {@link TransactionalGraph} with empty stopTransaction method. */
    static TransactionalGraph newGraph()
    {
        return new TransactionFaker(new TinkerGraph());
    }

    /** {@code b} has the same value for all keys of {@code a} */
    static boolean propertiesSubset(Element a, Element b)
    {
        for(String k : a.getPropertyKeys())
            if(!a.getProperty(k).equals(b.getProperty(k)))
                return false;

        return true;
    }

    /** {@code a} and {@code b} are equal */
    static boolean equalProperties(Element a, Element b)
    {
        return propertiesSubset(a, b) && propertiesSubset(b, a);
    }

    /** assign 10 random properties to {@code e} */
    static <T extends Element> T rndProps(T e)
    {
        for(int i = 0; i < 10; i++)
        {
            final String k = Double.toHexString(r.nextDouble());
            final String v = Double.toHexString(r.nextDouble());
            e.setProperty(k, v);
        }

        return e;
    }
}


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

