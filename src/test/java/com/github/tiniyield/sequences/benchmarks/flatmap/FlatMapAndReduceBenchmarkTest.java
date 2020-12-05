package com.github.tiniyield.sequences.benchmarks.flatmap;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class FlatMapAndReduceBenchmarkTest {
    private final Integer expected = 3;
    private FlatMapAndReduceBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new FlatMapAndReduceBenchmark();
        instance.COLLECTION_SIZE = 3;
        instance.setup();
    }


    @Test
    public void testStream() {
        assertEquals(expected, instance.stream());
    }

    @Test
    public void testSek() {
        assertEquals(expected, instance.sek());
    }

    @Test
    public void testEclipse() {
        assertEquals(expected, instance.eclipse());
    }

    @Test
    public void testJKotlin() {
        assertEquals(expected, instance.jkotlin());
    }

    @Test
    public void testKotlin() {
        assertEquals(expected, instance.kotlin());
    }

    @Test
    public void testVavr() {
        assertEquals(expected, instance.vavr());
    }

    @Test
    public void testJool() {
        assertEquals(expected, instance.jool());
    }

    @Test
    public void testJayield() {
        assertEquals(expected, instance.jayield());
    }

    @Test
    public void testStreamEx() {
        assertEquals(expected, instance.streamEx());
    }

}
