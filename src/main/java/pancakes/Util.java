package pancakes;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.versioned.exceptions.InconsistentGraphException;
import com.tinkerpop.blueprints.versioned.exceptions.MaxTriesExceededException;

/**
 *
 */
public class Util
{
    public static final int MAX_TRIES = 10;

    // manage symbolic vertices
    public static Vertex createOrGetVertex(Graph G, ID id)
    {
        for(int i = 0; i < MAX_TRIES; i++)
        {
            // look for the vertex
            Vertex s = G.getVertex(id);

            // found it
            if(s != null)
                return s;

            // it's not there
            try
            {
                // create it
                return G.addVertex(id);
            }
            catch(IllegalArgumentException e)
            {
                // someone created in the meantime, try to get it again
                continue;
            }
        }

        throw new MaxTriesExceededException("Max tries exceeded while creating symbolic vertex with id " + id);
    }

    public static Iterable<Edge> getEdgesBetween(Graph G, String label, Vertex head, final Vertex tail)
    {
        final Predicate<Edge> hasThatTail = new Predicate<Edge>() {
                public boolean apply(Edge input) {
                    return input.getVertex(Direction.OUT) == tail;
                }
            };

        return Iterables.filter(head.getEdges(Direction.IN, label), hasThatTail);
    }

    public static Edge createOrGetEdgeBetween(Graph G, String label, Vertex head, Vertex tail)
    {
        Edge created = null;

        for(int i = 0; i < MAX_TRIES; i++)
        {
            // find our edge based on head, tail and label
            final Iterable<Edge> edges = getEdgesBetween(G, label, head, tail);
            final int size = Iterables.size(edges);

            // found it
            if(size == 1)
                return edges.iterator().next();

            // create it
            if(size == 0)
            {
                // TODO add concurrency check
                return G.addEdge(null, tail, head, label);
            }

            if(size > 1)
                throw new InconsistentGraphException("More than one edge with label " + label + " between " + head + " and " + tail);
        }
    }
}
