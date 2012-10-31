package com.tinkerpop.blueprints.Attic;

import com.tinkerpop.blueprints.*;

/**
 * stuff
 */
public class ManagedGraph
{
    final protected Object id;
    final protected long version;
    final protected Graph graph;

    public ManagedGraph(Graph graph, Object id, long version)
    {
        this.id = id;
        this.version = version;
        this.graph = graph;
    }

    private <T extends Element> T claim(T element)
    {
        Util.setOwner(element, id);
        Util.setVersion(element, version);

        return element;
    }

    public Vertex addVertex(Object id)
    {
        // add symbolic vertex
        Vertex symbolic = graph.getVertex(id);

        // cannot create this vertex if somebody else already owns it
        if (symbolic != null && ! id.equals(Util.getOwner(symbolic)))
            throw new RuntimeException("0wned already");

        if (symbolic == null)
            symbolic = graph.addVertex(id);

        // vertex claimed
        Util.setOwner(symbolic, id);


        // add versioned edge
        final Vertex versioned = claim(graph.addVertex(null));
        Util.setIdentifier(versioned, id);

        // add "is_version_off" indicator
        final Edge e = claim(graph.addEdge(null, versioned, symbolic, Util.VERSION_EDGE_LABEL));
        // TODO onthouden dat je straks nog een edge toevoegen

        return versioned;
    }

    public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex, String label)
    {
        return claim(graph.addEdge(id, outVertex, inVertex, label));
    }

    public Vertex getVertex(Object id)
    {
        // always return the symbolic version
        return graph.getVertex(id);
    }

    public Vertex getOrCreateSymbolicVertex(Object id)
    {
        // Create unowned symbolic vertex
        return graph.addVertex(id);
    }

    // dereference symbolic vertex to latest version
    public Vertex getLatestVersion(Object id)
    {
        final Vertex v = graph.getVertex(id);

        Vertex max = null;

        for(Edge e : v.getEdges(Direction.IN, Util.VERSION_EDGE_LABEL))
        {
            final Vertex w = e.getVertex(Direction.OUT);

            if(max == null)
            {
                max = w;
                continue;
            }

            if(Util.getVersion(max) < Util.getVersion(w))
                max = w;
        }

        return max;
    }
}
