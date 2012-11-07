package com.tinkerpop.blueprints.versioned.lowlevel;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.versioned.impl.gc.Labels;

enum Namespace { SUBSET, VERTEX, EDGE };

/**
 * Scoped and versioned identifiers
 */
class ID
{
    final Object id;
    final long version;
    final Namespace ns;

    final ID symbolic;

    ID(Namespace ns, Object id, long version)
    {
        this.ns = ns;
        this.id = id;
        this.version = version;

        if(version > 0)
            this.symbolic = new ID(ns, id, 0);
        else
            this.symbolic = this;
    }

    public String toString()
    {
        final StringBuilder s = new StringBuilder();
        s.append(ns);
        s.append("::");
        s.append(id);
        s.append(':');
        s.append(version);
        return s.toString();
    }
}

/**
 * Pair of symbolic and versioned vertex with and edge in between
 */
class LLSubset
{
    final ID ID;
    final Vertex versionedVertex;
    final Vertex symbolicVertex;
    final Edge versionOfEdge;

    LLSubset(ID ID, Vertex v, Vertex s, Edge e)
    {
        this.ID = ID;
        this.versionedVertex = v;
        this.symbolicVertex = s;
        this.versionOfEdge = e;
    }
}

/**
 * Square containing two SVPairs and edges s->s, v->v
 */
class LLVertex extends LLSubset
{
    final LLSubset owner;
    final Edge symbolicOwnershipEdge;
    final Edge versionedOwnershipEdge;

    LLVertex(LLSubset owner, ID id, Vertex v, Vertex s, Edge e, Edge sOwn, Edge vOwn)
    {
        super(id, v, s, e);
        this.owner = owner;
        this.symbolicOwnershipEdge = sOwn;
        this.versionedOwnershipEdge = vOwn;
    }
}

/**
 * Edge structure
 */
class LLEdge extends LLVertex
{
    final Edge fromVertex;
    final Edge toVertex;

    LLEdge(LLSubset owner, ID id, Vertex v, Vertex s, Edge e, Edge sOwn, Edge vOwn, Edge fromVertex, Edge toVertex)
    {
        super(owner, id, v, s, e, sOwn, vOwn);
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
    }
}



/**
 * Create and find low level structures in the graph.
 */
public class LLManager
{
    final Graph G;

    public LLManager(Graph G)
    {
        this.G = G;
    }

    public LLSubset createVSS(Object id, long version)
    {
        if(version < 1)
            throw new RuntimeException("Cannot create version < 1");

        final ID ID = new ID(Namespace.SUBSET, id, version);

        // TODO transactional symbolic node creation!! IMPORTANT

        // symbolic vertex
        final Vertex s = G.addVertex(ID.symbolic);

        // versioned vertex
        final Vertex v = G.addVertex(ID);

        // the edge that indicates that v is a version of s
        final Edge versionOf = G.addEdge(null, v, s, Labels.VERSION_OF);

        return new LLSubset(ID, v, s, versionOf);
    }

    public LLVertex createVV(LLSubset owner, Object id)
    {
        // version is coupled to owner version
        final ID vertexID = new ID(Namespace.VERTEX, id, owner.ID.version);

        // TODO refactor out?
        final Vertex s = G.addVertex(vertexID.symbolic);
        final Vertex v = G.addVertex(vertexID);
        final Edge versionOf = G.addEdge(null, v, s, Labels.VERSION_OF);

        // TODO check id is not already owned by someone else!! IMPORTANT
        //  horizontal edges, indicating ownership
        final Edge symbolicOwnership = G.addEdge(null, owner.symbolicVertex, s, Labels.OWNS);
        final Edge versionedOwnership = G.addEdge(null, owner.versionedVertex, v, Labels.OWNS);

        //LLSubset owner, ID id, Vertex v, Vertex s, Edge e, Edge sOwn, Edge vOwn
        return new LLVertex(owner, vertexID, v, s, versionOf, symbolicOwnership, versionedOwnership);
    }

    public LLEdge createVE(LLSubset owner, Object id, Vertex tail, Vertex head, String label)
    {
        final ID edgeID = new ID(Namespace.EDGE, id, owner.ID.version);

        // TODO refactor out?
        final Vertex s = G.addVertex(edgeID.symbolic);
        final Vertex v = G.addVertex(edgeID);
        final Edge versionOf = G.addEdge(null, v, s, Labels.VERSION_OF);

        // TODO check id is not already owned by someone else!! IMPORTANT
        //  horizontal edges, indicating ownership
        final Edge symbolicOwnership = G.addEdge(null, owner.symbolicVertex, s, Labels.OWNS);
        final Edge versionedOwnership = G.addEdge(null, owner.versionedVertex, v, Labels.OWNS);

        // the edges tail --> v --> head
        final Edge in  = G.addEdge(null, tail, v, label);
        final Edge out = G.addEdge(null, v, head, label);

        return new LLEdge(owner, edgeID, v, s, versionOf, symbolicOwnership, versionedOwnership, in, out);
    }
}
