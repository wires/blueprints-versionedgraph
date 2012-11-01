package com.tinkerpop.blueprints.versioned;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.tinkerpop.blueprints.*;

import static org.fest.assertions.Assertions.assertThat;

public class MyFirstTest {

    private VersionedGraph graph;

    @Before
    public void setup() {
        graph = new VersionedGraph(new TransactionalGraph() {

            @Override
            public void stopTransaction(Conclusion conclusion) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Features getFeatures() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Vertex addVertex(Object id) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Vertex getVertex(Object id) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void removeVertex(Vertex vertex) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Iterable<Vertex> getVertices() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Iterable<Vertex> getVertices(String key, Object value) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex, String label) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Edge getEdge(Object id) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void removeEdge(Edge edge) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Iterable<Edge> getEdges() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Iterable<Edge> getEdges(String key, Object value) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void shutdown() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    @Ignore
    @Test
    public void myFirstTest() {
        assertThat(graph).isInstanceOf(VersionedGraph.class);
    }
}