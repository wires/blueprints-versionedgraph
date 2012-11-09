Provide "functional view" of a Graph.

# What?

Two goals:

* Instead of allowing arbitrary modification of the graph, we want to restrict
  this to versioned subsets.

* We want to give a "consistent" view on the graph, while avoiding global write
  locks.

## Versioned Subsets

![VersionedSubsets](https://github.com/0x01/blueprints-versionedgraph/raw/master/doc/versions.png)

Each subset is assigned an ID and version. Multiple versions of the same ID
will co-exist in the graph. Until the subset is committed it is not visible in
the graph. Once committed to the graph, a subset can no longer be modified.
Instead, a new version for the same ID should be created and appended to the
graph.

## Consistent Views

Want want to modify our graph and maintain a consistent view of it.
Consistency here means that we can get a 'snapshot view' of the graph at a
certain time. All modifications done to the graph after this time are not
visibile to the view. A view is a bit like a lock, it prevents old versions
from being garbage collected. So you need to actively `release()` the view,
or you can `refresh()` it to some newer timestamp.


## Symbolic and versioned vertices

![SymbolicVersioned](https://github.com/0x01/blueprints-versionedgraph/raw/master/doc/graph1.png)

Each vertex in the unversioned graph has an id.

This is no different in the versioned graph, except that now we have
multiple versions of a vertex. So we distinguish two kinds of vertices:
/symbolic/ and /versioned/.

When you create a vertex through a VersionedSubset it is always a
VersionedVertex:

    final VersionedVertex v = subset.addVertex("x")
    assertTrue(v.getId().equals("x"))
    assertTrue(v.getVersion() == subset.getVersion())

The VersionedVertex is stored in the underlying graph as a regular
vertex with as id a tuple (id,version), so `Pair<Object,Long>`.

Linking between VersionedVertices withing a VersionedSubset is not
problem. If you want to link to a vertex outside the VSS you need to
link to a symbolic vertex


# How?

## Basic structures

![VersionedSubsets](https://github.com/0x01/blueprints-versionedgraph/raw/master/doc/svpair.png)

Pair of a symbolic and versioned component with `versionOf` edge
inbetween: `SVPair`

![VersionedSubsets](https://github.com/0x01/blueprints-versionedgraph/raw/master/doc/svsquare.png)

Two pairs, `SVSquare`, horizontal edges indicating ownership.

![VersionedSubsets](https://github.com/0x01/blueprints-versionedgraph/raw/master/doc/svleggedsquare.png)

Special square used by edges, it has two legs.

## Some more talk

We view a graph as the union of subsets.

The only way to modify the graph is through the adding, updating or removal of subsets.

Subsets are identified using an id (Object) and a version (long)

Vertices and edges created through the subset are owned by the subsets.


Subgraphs are versioned and traversals always find the latest version of a subgraph.

Vertices created by a subgraph are owned by the subgraph. Another subgraph cannot create a vertex with the same ID.

When we create a vertex with id=V and properties P in the subgraph we actually create two vertices:

 - one vertex S called the "symbol", which has id=V and no properties. this symbol vertex is also
   marked as belonging to the graph

 - another vertex K, which holds all the properties P and has id=(V, subgraph.version)

 - we create and edge between S and K indication that K is a version of S.

Edges created in the subgraph always run between a versioned node and a symbol node.

All the elements from a subgraph are tagged as belonging to the subgraph.
