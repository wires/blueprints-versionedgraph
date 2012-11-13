Provide "functional view" of a Graph.

# What?

Two goals:

* We want to give a immutable, consistent view on the graph, while
  avoiding global write locks. This means that writes can still occur
  and the view should not notice these changes.
  
* Thus, instead of allowing arbitrary modification of the graph, we
  want to restrict this to the transactional appending of immutable
  versioned subsets. In a sense, localizing the changes.

## Say what?

We introduce a `TransactionalGraph` wrapper, `VersionedGraph`. All
modifications on the underlying graph happen through the wrapper.
	
	final TransactionalGraph tg = new Neo4jGraph('/tmp/db');
	final VersionedGraph vg = new VersionedGraph(tg);

	// create a subset with the name "A"
	final VersionedSubset ss = vg.createVersionedSubset("A", 1);

	// create a vertex
	final VersionedVertex vv = ss.addVertex("x");
	vv.setProperty("koekje", 123);

	ss.commit();

The code above creates a subset with the name "A" and adds a vertex
"x" with data `koekje=123` to revision 1 of the subset.

The subset "A" now owns the symbol "x" as a vertex identifier and no
other subset can create a vertex with this name.

This is also where the distinction `SymbolicVertex` and
`VersionedVertex` comes from. A symbol is a identifier that refers to
all versions of a component. A VersionedComponent refers to a specific
version of a component.

VersionedComponents only make sense within the owning subset of that
particular version.

`SymbolicVertices` are used to refer to components from other subsets.

	final VersionedSubset ss2 = vg.createVersionedSubset("B", 2)
	final SymbolicVertex sv = ss2.getVertex("x")
	final VersionedVertex vv = ss2.addVertex("y");
	final TailVersionedEdge ve = ss2.addEdge(sv, vv, "lala");

This is summarized in the following picture:

![VersionedSubsets](https://github.com/0x01/blueprints-versionedgraph/raw/master/doc/versions.png)

On the left we see a graph assembled from various subsets.

On the right we see a single versioned subset. Each versioned subset
has two parts:

* the "symbolic" part, drawn in orange in the picture. This part keeps
  track of the ownership of names and connects the various versions.
  The ID of this part is the ID of the subset, so here it's `"A"`.

* the "version" part, drawn in black. Here the components and their
  properties are stored. Each version has as it's ID a 2-tuple of the
  ID of the subset and the version. In the picture: `"A",1`, `"A",2`,
  etc.

The left part of the picture also indicates what `ConsistentView`s
give you, namely a view on the graph, such that during the lifetime of
the view, every time you resolve a name (symbol) to a version, you
will resolve to the same version.

So one way to picture this is by taking a "slice" of the graph,
picking one version of each subset.

(Note that, internally this is implemented using a global transaction
counter and then ignoring transactions occuring after the view was
constructed.)

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
visibile to the view.

A view is a bit like a lock, it prevents old versions from being
garbage collected. So you need to actively `release()` the view, or
you can `refresh()` it to some newer timestamp.

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

We define a mapping between a `VersionedGraph` and a regular
graph. Then, a `ConsistentView` behaves pretty much like a regular
graph, except that you cannot modify it.

## Basic structures

![VersionedSubsets](https://github.com/0x01/blueprints-versionedgraph/raw/master/doc/svpair.png)

Pair of a symbolic and versioned component with `versionOf` edge
inbetween: `SVPair`

![VersionedSubsets](https://github.com/0x01/blueprints-versionedgraph/raw/master/doc/svsquare.png)

Two pairs, `SVSquare`, horizontal edges indicating ownership.

![VersionedSubsets](https://github.com/0x01/blueprints-versionedgraph/raw/master/doc/svleggedsquare.png)

Special square used by edges, it has two legs.

## Versioned and scoped identifiers

Identifiers are used to refer to and find objects in the graph.  Since
we add some structure to the graph, this is reflected in the
identifiers used. Each component in the versioned graph is given as
identifier an instance of `ID`.

`ID` essentially is a 3-tuple `<Namespace,Identifier,Version>`.

We distinguish three identifiers scopes in a `VersionedGraph`:

* Subsets
* Vertices
* Edges

This means, that you can use the same identifier for a subset and a
vertex without worrying about collisions.

In addition, we store a version, with `version == 0` indicating that
this identifier belongs to the symbolic component.

## Symbolic components


## Building docs

UML in /doc/

	mvn planetuml:generate

or
	mvn site:site


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
