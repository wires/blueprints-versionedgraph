package com.tinkerpop.blueprints.versioned.lowlevel;

import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class LLTest
{
    TinkerGraph graph;
    LLManager ll;

    @BeforeTest
    public void setup() {
        graph = new TinkerGraph();
        ll = new LLManager(graph);
    }

    @Test
    public void subsetTest()
    {
        final String identifier = "A";
        final long version = 1;

        final LLSubset subset = ll.createVSS(identifier, version);

        //assertThat(subset.symbolicVertex.getId()).isInstanceOf(ID.class);
        //assertThat(subset.versionedVertex.getId()).isInstanceOf(ID.class);

        final ID si = ID.fromString(subset.symbolicVertex.getId().toString());
        assertThat(si.id).isEqualTo(identifier);
        assertThat(si.ns).isSameAs(Namespace.SUBSET);
        assertThat(si.version).isZero();
        assertThat(si.symbolic).isSameAs(si);

        final ID vi = ID.fromString(subset.versionedVertex.getId().toString());
        assertThat(vi.id).isEqualTo(identifier);
        assertThat(vi.ns).isSameAs(Namespace.SUBSET);
        assertThat(vi.version).isEqualTo(version);

        assertThat(vi.symbolic.id).isEqualTo(si.id);
        assertThat(vi.symbolic.ns).isEqualTo(si.ns);
        assertThat(vi.symbolic.version).isEqualTo(si.version);

        final LLSubset s = LLSubset.find(graph, vi);
        // assertThat(subset.equals(s)).isTrue();
    }

    @Test
    public void subsetUpdateTest()
    {
        final String identifier = "A";

        final LLSubset subset_v1 = ll.createVSS(identifier, 1);
        final LLSubset subset_v2 = ll.createVSS(identifier, 2);

        final LLVertex x_v1 = ll.createVV(subset_v1, "x");
        final LLVertex y_v2 = ll.createVV(subset_v2, "x");

        final ID x_v1_ID = new ID(Namespace.VERTEX, identifier, 1);
        final LLSubset x__v1 = LLSubset.find(graph, x_v1_ID);

        assertThat(x__v1.versionedVertex.getId()).isEqualTo(x_v1_ID.toString());

        final ID x_v2_ID = new ID(Namespace.VERTEX, identifier, 2);
        final LLSubset x__v2 = LLSubset.find(graph, x_v2_ID);

        assertThat(x__v2.versionedVertex.getId()).isEqualTo(x_v2_ID.toString());
    }
}