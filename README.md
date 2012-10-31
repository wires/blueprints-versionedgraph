Provide "functional view" of a Graph.

# What?

Two goals:

* Instead of allowing arbitrary modification of the graph, we want to restrict
  this to versioned subsets.

* We want to give a "consistent" view on the graph, while avoiding global write
  locks.

## Versioned Subsets

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

# How?

	TODO update the stuff below

We view a graph as the union of subgraphs.

The only way to modify the graph is through the adding, updating or removal of subgraphs.

Subgraphs are identified using an id (String) and a version (long).

Subgraphs are versioned and traversals always find the latest version of a subgraph.

Vertices created by a subgraph are owned by the subgraph. Another subgraph cannot create a vertex with the same ID.

When we create a vertex with id=V and properties P in the subgraph we actually create two vertices:

 - one vertex S called the "symbol", which has id=V and no properties. this symbol vertex is also
   marked as belonging to the graph

 - another vertex K, which holds all the properties P and has id=(V, subgraph.version)

 - we create and edge between S and K indication that K is a version of S.

Edges created in the subgraph always run between a versioned node and a symbol node.

All the elements from a subgraph are tagged as belonging to the subgraph.
