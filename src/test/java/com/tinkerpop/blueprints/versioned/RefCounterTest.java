package com.tinkerpop.blueprints.versioned;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class RefCounterTest {

    private RefCounter refCounter;

    @Before
    public void setup() {
        refCounter = new RefCounter(1);
    }

    @Test
    public void testIncrement() {
        Long incrementedValue = refCounter.increment();
        assertThat(incrementedValue).isEqualTo(2L);
    }

    @Test
    public void testDecrement() {
        Long decrementedValue = refCounter.decrement();
        assertThat(decrementedValue).isEqualTo(0L);
    }
}
