/**
 * Gives a versioned view of a blueprints {@link Graph}.
 *
 *
 * The principle behind all this can be summarized as follows:
 *
 * <ul>
 *     <li>Every subset has an identifier and version</li>
 *     <li>Components can only be created by subsets, and once created</li>
 *     <li>Once committed to the graph, a subset can no longer be changed</li>
 *     <li>To update a subset, you create a new subset with the same identifier and a different version</li>
 *     <li>Components created in this subset will update previously created components</li>
 *     <li>When you "get" a component, we consider this a reference to a component outside the subset</li>
 * </ul>
 *
 *
 * Typically this is used as follows:
 *
 * <pre>
 *     // obtain a blueprints graph and wrap it
 *     final TransactionalGraph G = ...
 *     final VersionedGraph vg = new VersionedGraphImpl(G);
 * </pre>
 *
 * We can now obtain a versioned subset. This is a scope in which versioned graph components (vertices, edges) can
 * be created. There is no other way to create components in the graph.
 *
 * Every vertex created using {@link VersionedSubset#addVertex(Object)} will have the type {@link VersionedVertex}
 * and it will be owned (claimed) by the subset. Ownership means that no other subset can create a vertex with the
 * same identifier. The only way to update that vertex is by creating a new VersionedSubset with the same identifier
 * and a newer version. Th
 *
 * Another way to obtain a vertex is by referring (using the <code>id</code>) to an existing vertex, <i>outside</i>
 * the subset. Since we don't own such components, {@link VersionedSubset#getVertex(Object)} returns a
 * {@link SymbolicVertex}.
 *
 * <pre>
 *     final VersionedSubset ss = vg.createVersionedSubset("A", 1);
 *
 *     // create two vertices that we own
 *     final VersionedVertex a = ss.addVertex("a");
 *     final VersionedVertex b = ss.addVertex("b");
 *
 *     // properties are versioned
 *     a.setProperty("voodoo", 1234);
 * </pre>
 *
 *  We can also refer to nodes outside our subset, these will be symbolic (ie. have no version or data) and are
 *  created on demand.
 *
 * <pre>
 *     final SymbolicVertex c = ss.getVertex("c");
 * </pre>
 *
 *  A versioned subset can later claim that node (by adding a vertex with that id).
 *  Then the symbolic vertex will then be resolved to that versioned vertex.
 *
 * <pre>
 *     final VersionedSubset ssB = vg.createVersionedSubset("B", 1);
 *     final VersionedVertex c_ = ssB.addVertex("C");
 *     c_.setProperty("bananas", 4);
 *     c_.setProperty("cookies", 6);
 *
 *     // when we get the latest version of c we will find c_
 *     final VersionedVertex vc = c.getLatestVersion(Integer.MAX_VALUE);
 *     assertEquals(vc.getProperty("banana"), 4);
 *     assertEquals(vc.owner().id(), "B");
 *     assertEquals(vc.owner().version(), 1);
 * </pre>
 *
 * We can also create edges, they will be owned and versioned. Again, the same principle as with vertices: if
 * we <code>getEdge</code> an edge, it will be symbolic.  Versioned edges
 * can be created between versioned and symbolic vertices,
 *
 * <pre>
 *     // but their types will depend on the type over head/tail vertice:
 *
 *     // 1). between two symbolic vertices you get a symbolic edge
 *     final VersionedEdgeSS se = ss.addEdge("w", c, c, "label1");
 *
 *     // 2a). between a symbolic and versioned vertex, you get a half "versioned" edge:
 *     final VersionedEdgeVS hve = ss.addEdge("x", c, a, "label2a");
 *     final VersionedVertex a_  = hve.getVersionedHead();
 *
 *      // 2b).
 *     final VersionedEdgeSV tve = ss.addEdge("y", b, c, "label2b");
 *     final VersionedVertex b_  = hve.getVersionedTail();
 *
 *     // 3). between two versioned vertices, you get a versioned edge
 *     final VersionedEdgeVV vv = ss.addEdge("z", a, b, "label3");
 * </pre>
 *
 * Etcetera.
 *
 */

package com.tinkerpop.blueprints.versioned;