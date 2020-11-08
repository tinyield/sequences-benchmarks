package com.github.tiniyield.sequences.benchmarks.odd.lines;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class QueryNrOfDistinctTemperaturesTest {
    private QueryNrOfDistinctTemperatures instance;

    @BeforeMethod
    public void setup() {
        instance = new QueryNrOfDistinctTemperatures();
    }

    @Test
    public void testSameOutput() {
        DataSource src = new DataSource();
        int expected = 18;
        assertEquals(instance.nrOfTempsStream(src),expected);
        assertEquals(instance.nrOfTempsStreamEx(src),expected);
        assertEquals(instance.nrOfTempsJayield(src),expected);
        assertEquals(instance.nrOfTempsJool(src),expected);
        assertEquals(instance.nrOfTempsVavr(src),expected);
        assertEquals(instance.nrOfTempsKotlin(src),expected);
    }
}
