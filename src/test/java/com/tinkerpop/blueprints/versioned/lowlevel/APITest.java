package com.tinkerpop.blueprints.versioned.lowlevel;


import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.versioned.*;
import com.tinkerpop.blueprints.versioned.exceptions.VersionNoLongerAvailableException;
import com.tinkerpop.blueprints.versioned.impl.VersionedGraphImpl;
import org.fest.assertions.api.Assertions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

import static com.tinkerpop.blueprints.versioned.lowlevel.Util.*;
import static org.fest.assertions.api.Assertions.assertThat;


public class APITest
{
    final Random r = new Random();

    TransactionalGraph tg;
    VersionedGraph vg;

    @BeforeTest
    public void setup() {
        tg = newGraph();
        vg = new VersionedGraphImpl(tg);
    }

    VersionedSubset createSubset(String name, long version)
    {
        // create a subset
        final VersionedSubset vss = vg.createVersionedSubset(name, version);

        // create two vertices with some random properties associated with them
        final VersionedVertex x = rndProps(vss.addVertex("x"));
        final VersionedVertex y = rndProps(vss.addVertex("y"));

        return vss;
    }

    final Function<VersionedVertex, Object> extractIds = new Function<VersionedVertex, Object>()
        {
            @Override
            public Object apply(VersionedVertex input)
            {
                return input.id();
            }
        };

    final Predicate<VersionedVertex> hasId(final String id) {
        return new Predicate<VersionedVertex>() {
            public boolean apply(VersionedVertex input) {
                return id.equals(input.id());
            }
        };
    }

    @Test
    public void testAddingSubset() throws VersionNoLongerAvailableException
    {
        // create a subset
        final VersionedSubset vss = createSubset("A", 1);

        // commit the subset
        final long t = vss.commit();

        // create a view
        final ConsistentView cv = vg.createConsistentView(t);

        assertThat(Iterables.transform(vss.vertices(), extractIds)).containsOnly("x", "y");

        assertThat(Iterables.filter(vss.vertices(), hasId("x"))).hasSize(1);
        assertThat(Iterables.filter(vss.vertices(), hasId("y"))).hasSize(1);

        final VersionedVertex x = Iterables.filter(vss.vertices(), hasId("x")).iterator().next();
        final VersionedVertex y = Iterables.filter(vss.vertices(), hasId("y")).iterator().next();

        assertThat(equalProperties(x, cv.getVertex("x"))).isTrue();
        assertThat(equalProperties(y, cv.getVertex("y"))).isTrue();
    }


    @Test
    public void testAddingSubsets() throws VersionNoLongerAvailableException
    {
        Map<VersionedSubset, ConsistentView> m = new HashMap<VersionedSubset, ConsistentView>();

        for(int i = 1; i < 11; i++)
        {
            // create a subset
            final VersionedSubset vss = createSubset("A", i);

            // commit the subset
            final long t = vss.commit();

            // create a view
            final ConsistentView cv = vg.createConsistentView(t);

            m.put(vss, cv);
        }

        for(Map.Entry<VersionedSubset, ConsistentView> e : m.entrySet())
        {
            final VersionedSubset vss = e.getKey();
            final ConsistentView cv = e.getValue();

            assertThat(Iterables.transform(vss.vertices(), extractIds)).containsOnly("x", "y");

            assertThat(Iterables.filter(vss.vertices(), hasId("x"))).hasSize(1);
            assertThat(Iterables.filter(vss.vertices(), hasId("y"))).hasSize(1);

            final VersionedVertex x = Iterables.filter(vss.vertices(), hasId("x")).iterator().next();
            final VersionedVertex y = Iterables.filter(vss.vertices(), hasId("y")).iterator().next();

            assertThat(equalProperties(x, cv.getVertex("x"))).isTrue();
            assertThat(equalProperties(y, cv.getVertex("y"))).isTrue();
        }
    }


    @Test
    public void testje()
    {
        final ArrayList<Koekje> koekjes = new ArrayList<Koekje>();
        koekjes.add(new Koekje("van"));
        koekjes.add(new Koekje("eigen"));
        koekjes.add(new Koekje("deeg"));

        Assertions.assertThat(Assertions.extractProperty("naam", String.class).from(koekjes)).containsOnly("van", "eigen", "deeg");
    }
}