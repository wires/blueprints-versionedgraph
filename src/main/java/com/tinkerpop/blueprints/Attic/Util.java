package com.tinkerpop.blueprints.Attic;

import com.tinkerpop.blueprints.Element;

public class Util
{
    final static private String OWNER = "_owner";
    final static private String VERSION = "_version";
    final static private String IDENTIFIER = "_identifier";

    final static public String VERSION_EDGE_LABEL = "_isVersionOf";

    public static long getVersion(Element e)
    {
        return (Long)e.getProperty(VERSION);
    }

    public static Object getOwner(Element e)
    {
        return e.getProperty(OWNER);
    }

    public static void setOwner(Element e, Object owner)
    {
        e.setProperty(OWNER, owner);
    }

    public static void setVersion(Element e, long version)
    {
        e.setProperty(VERSION, version);
    }

    public static void setIdentifier(Element e, Object id)
    {
        e.setProperty(IDENTIFIER, id);
    }
}
