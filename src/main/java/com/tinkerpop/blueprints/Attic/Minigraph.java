package com.tinkerpop.blueprints.Attic;


// identify edge by (tail,label,head)
class E
{
    public final String tail;
    public final String label;
    public final String head;

    public E(String tail, String label, String head)
    {
        this.tail = tail;
        this.label = label;
        this.head = head;
    }
}

// minimal interface to build graphs
public interface Minigraph
{
    void addEdge(E edge);
    void addEdgeProperty(E edge, String key, Object value);

    void addNode(String node);
    void addNodeProperty(String node, String key, Object value);
}
