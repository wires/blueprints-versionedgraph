package pancakes;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.versioned.impl.gc.Labels;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class PropertyBasedBlueprintsWrapper implements API
{
    final TransactionalGraph G;

    class Pair
    {
        final ID id;
        final Vertex s, v;
        final Edge e;

        public Pair(ID id)
        {
            this.id = id;
            s = Util.createOrGetVertex(G, id.symbolic);
            v = Util.createOrGetVertex(G, id);
            e = Util.createOrGetEdgeBetween(G, Labels.VERSION_OF, s, v);
        }
    }

    class Square
    {
        final Pair owner;
        final Pair ownee;
        final Edge sOwn;
        final Edge vOwn;

        Square(Pair owner, Pair ownee)
        {
            this.owner = owner;
            this.ownee = ownee;
            sOwn = Util.createOrGetEdgeBetween(G, Labels.OWNS, owner.s, ownee.s);
            vOwn = Util.createOrGetEdgeBetween(G, Labels.OWNS, owner.v, ownee.v);
        }
    }

    class Hair
    {
        final Square edge;
        final Edge h, t;

        Hair(Square edge, Vertex head, Vertex tail)
        {
            this.edge = edge;
            h = Util.createOrGetEdgeBetween(G, Labels.EDGE, head, edge.ownee.v);
            t = Util.createOrGetEdgeBetween(G, Labels.EDGE, edge.ownee.v, tail);
        }
    }

    class View implements view
    {
        final protected long t;

        View(long t)
        {
            this.t = t;
        }

        @Override
        public long timestamp()
        {
            return t;
        }
    }

    class SS implements subset
    {
        final protected ID ID;
        final Pair pair;

        public SS(Object id, long version)
        {
            this.ID = new ID(Namespace.SUBSET, id, version);
            this.pair = new Pair(ID);
        }

        @Override
        public Object id()
        {
            return ID.id;
        }

        @Override
        public long version()
        {
            return ID.version;
        }
    }

    class V implements vertex {

        @Override
        public subset owner()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getProperty(String key)
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Set<String> getPropertyKeys()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setProperty(String key, Object value)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object removeProperty(String key)
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getId()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    class E implements edge {

        @Override
        public Object getProperty(String key)
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Set<String> getPropertyKeys()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setProperty(String key, Object value)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object removeProperty(String key)
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getId()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public subset owner()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public vertex head()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public vertex tail()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String label()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    public PropertyBasedBlueprintsWrapper(TransactionalGraph G)
    {
        this.G = G;
    }

    final AtomicLong txCounter = new AtomicLong(0);

    @Override
    public subset addSubset(Object id, long version)
    {
        return new SS(id, version);
    }

    @Override
    public subset findSubset(Object id, long version)
    {
        return new SS(id, version);
    }

    @Override
    public void removeSubset(Object id, long version)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public vertex addVertex(subset ss, Object id)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public edge addEdge(subset ss, Object id, vertex head, vertex tail, String label)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public view createView(long tx)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void releaseView(view v)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public vertex getVertex(view v, Object id)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public edge getEdge(view v, Object id)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
