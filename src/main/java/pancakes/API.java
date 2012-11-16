package pancakes;

import com.tinkerpop.blueprints.Element;

interface vertex extends Element
{
    subset owner();
}

interface edge extends Element
{
    subset owner();
    vertex head();
    vertex tail();
    String label();
}

interface view
{
    long timestamp();
}

interface subset
{
    Object id();
    long version();
}


public interface API
{
    // add find remove subsets
    subset addSubset   (Object id, long version);
    subset findSubset  (Object id, long version);
    void   removeSubset(Object id, long version);

    // add components inside/outside the subset

    /**
     * Mutable vertex
     *
     * @param ss
     * @param id
     *
     * @return mutable vertex
     */
    vertex addVertex (subset ss, Object id);
    edge   addEdge   (subset ss, Object id, vertex head, vertex tail, String label);

    // create a view
    view createView  (long tx);
    void releaseView (view v);

    /**
     * Immutable vertex, data resolved to latest version up to view transaction count.
     *
     * @throws NotSymbolicException if the id you specified has just been created with addVertex(..)
     *
     * @param v resolve the vertex version relative to this view
     * @param id
     * @return
     */
    vertex getVertex (view v, Object id);

    /**
     * Immutable edge
     *
     * @param v
     * @param id
     * @return
     */
    edge getEdge(view v, Object id);
}
