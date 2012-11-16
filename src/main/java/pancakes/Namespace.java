package pancakes;

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