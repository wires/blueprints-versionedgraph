package com.tinkerpop.blueprints.versioned.lowlevel;

public class Koekje
{
    final public String naam;

    Koekje(String naam)
    {
        this.naam = naam;
    }

    public String getNaam()
    {
        return naam;
    }
}
