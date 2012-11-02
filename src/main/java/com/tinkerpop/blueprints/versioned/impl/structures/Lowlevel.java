package com.tinkerpop.blueprints.versioned.impl.structures;

import com.tinkerpop.blueprints.versioned.impl.gc.Labels;

// various "low-level" structures in the graph
public class Lowlevel
{
    
}

class ID
{
    // different identifier scopes
    enum Namespace { SUBSET, VERTEX, EDGE };

    final Object id;
    final long version;
    final Namespace ns;

    ID(Namespace ns, Object id, long version) {
        this.ns = ns;
        this.id = id;
        this.version = version;
    }
}

class SVPair
{
    final ID I;
    final Vertex v;
    final Vertex s;
    final Edge e;

    SVPair(Graph G, ID id)
    {
        I = id;

        s = G.addVertex(I.id());   // symbolic vertex
        v = G.addVertex(I);        // versioned vertex

        // indicating v <--[versionOf]-- s
        // TODO check edge orientation
        e = G.addEdge(null, v, s, Labels.ELABEL_VERSIONOF);
    }
}

class VSS extends SVPair {
    VSS(Graph G, Object id, long version) {
        super(new ID(SUBSET, id, version);
    }
}

class VV extends SVPair {
    final Edge ownedBy;
    VV(Graph G, VSS owner, Object id) {
        super(new ID(VERTEX, id, owner.I.version));

        // own the symbol
        // TODO check if not already owned
        G.addEdge(null, this.s, owner.s, Labels.ELABEL_OWNEDBY);

        // own the versioned edge
        G.addEdge(null, this.v, owner.v, Labels.ELABEL_OWNEDBY);
    }
}

class VE extends SVPair {

}