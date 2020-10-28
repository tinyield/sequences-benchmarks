package com.github.tiniyield.sequences.benchmarks.flatmap;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class FlatMapAndReduceBenchmarkTest {
    private FlatMapAndReduceBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new FlatMapAndReduceBenchmark();
        instance.COLLECTION_SIZE = 3;
        instance.setup();
    }


    @Test
    public void testSameOutput() {
        Integer expected = 3;
        assertEquals(expected, instance.sumStream());
        assertEquals(expected, instance.sumStreamEx());
        assertEquals(expected, instance.sumQuery());
        assertEquals(expected, instance.sumJool());
        assertEquals(expected, instance.sumVavr());
        assertEquals(expected, instance.sumKotlin());
        assertEquals(expected, instance.sumJKotlin());
    }

}