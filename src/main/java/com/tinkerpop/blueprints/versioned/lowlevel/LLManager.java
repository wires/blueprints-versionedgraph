package com.tinkerpop.blueprints.versioned.lowlevel;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.versioned.impl.gc.Labels;

enum Namespace {
    SUBSET, VERTEX, EDGE;

    public String shortName() {
        return name().toLowerCase().substring(0, 1);
    }

    public static Namespace fromShortName(String shortName)
    {
        for(Namespace ns : Namespace.values())
            if(ns.shortName().equals(shortName))
                return ns;

        throw new RuntimeException("No namespace for " + shortName);
    }
}

/**
 * Scoped and versioned identifiers
 */
class ID
{
    final static String SEPARATOR = "@@@";

    final Object id;
    final long version;
    final Namespace ns;

    final ID symbolic;

    ID(Namespace ns, Object id, long version)
    {
        if(version < 0)
            throw new RuntimeException("Cannot create version < 0");

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
        s.append(ns.shortName());
        s.append(SEPARATOR);
        s.append(id);
        s.append(SEPARATOR);
        s.append(version);

        return s.toString();
    }

    public static ID fromString(String s)
    {
        final String[] ss = s.split(SEPARATOR);

        if(ss.length != 3)
            throw new RuntimeException("Failed to parse identifier");

        final Namespace ns = Namespace.fromShortName(ss[0]);
        final Object id = ss[1];
        final long version = Long.valueOf(ss[2]);

        return new ID(ns, id, version);
    }
}

class EdgeID
{
    final ID head;
    final ID tail;

    EdgeID(ID head, ID tail)
    {
        this.head = head;
        this.tail = tail;
    }

    public String toString()
    {
        final StringBuilder s = new StringBuilder();
        s.append(head);
        s.append("<--");
        s.append(tail);
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

    static LLSubset create(Graph G, ID ID)
    {
        // TODO transactional symbolic node creation!! IMPORTANT

        // symbolic vertex
        final Vertex s = G.addVertex(ID.symbolic);

        // versioned vertex
        final Vertex v = G.addVertex(ID);

        // the edge that indicates that v is a version of s
        final Edge versionOf = G.addEdge(new EdgeID(ID, ID.symbolic), v, s, Labels.VERSION_OF);

        return new LLSubset(ID, v, s, versionOf);
    }

    /**
     *
     * @param G
     * @param ID
     *
     * @return null if the subset could not be found in the graph
     */
    static LLSubset find(Graph G, ID ID)
    {
        final Vertex s = G.getVertex(ID.symbolic);
        final Vertex v = G.getVertex(ID);
        final Edge e = G.getEdge(new EdgeID(ID,  ID.symbolic));

        if(s == null || v == null || e == null)
            return null;

        return new LLSubset(ID, v, s, e);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;

        if(!(obj instanceof LLSubset))
            return false;

        final LLSubset other = (LLSubset)obj;

        if(!ID.equals(other.ID))
            return false;

        if(!versionedVertex.equals(other.versionedVertex))
            return false;

        if(!symbolicVertex.equals(other.symbolicVertex))
            return false;

        if(!versionOfEdge.equals(other.versionOfEdge))
            return false;

        return true;
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

    static LLVertex create(Graph G, LLSubset owner, ID ID)
    {
        final LLSubset vertexPair = LLSubset.create(G, ID);

        // TODO check id is not already owned by someone else!! IMPORTANT
        //  horizontal edges, indicating ownership
        final Edge symbolicOwnership = G.addEdge(null, owner.symbolicVertex, vertexPair.symbolicVertex, Labels.OWNS);
        final Edge versionedOwnership = G.addEdge(null, owner.versionedVertex, vertexPair.versionedVertex, Labels.OWNS);

        //LLSubset owner, ID id, Vertex v, Vertex s, Edge e, Edge sOwn, Edge vOwn
        return new LLVertex(owner, ID, vertexPair.versionedVertex, vertexPair.symbolicVertex, vertexPair.versionOfEdge, symbolicOwnership, versionedOwnership);
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

    static LLEdge create(Graph G, LLSubset owner, ID ID, Vertex tail, Vertex head, String label)
    {
        final LLVertex edgePair = LLVertex.create(G, owner, ID);

        // the edges tail --> v --> head
        final Edge in  = G.addEdge(null, tail, edgePair.versionedVertex, label);
        final Edge out = G.addEdge(null, edgePair.versionedVertex, head, label);

        return new LLEdge(owner, ID, edgePair.versionedVertex, edgePair.symbolicVertex, edgePair.versionOfEdge, edgePair.symbolicOwnershipEdge, edgePair.versionedOwnershipEdge, in, out);
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
        return LLSubset.create(G, new ID(Namespace.SUBSET, id, version));
    }

    public LLVertex createVV(LLSubset owner, Object id)
    {
        // version is coupled to owner version
        return LLVertex.create(G, owner, new ID(Namespace.VERTEX, id, owner.ID.version));
    }

    public LLEdge createVE(LLSubset owner, Object id, Vertex tail, Vertex head, String label)
    {
        return LLEdge.create(G, owner, new ID(Namespace.EDGE, id, owner.ID.version), tail, head, label);
    }
}
