package com.github.tiniyield.sequences.benchmarks.odd.lines;

import com.github.tiniyield.sequences.benchmarks.common.WeatherDataSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class QueryNrOfDistinctTemperaturesTest {
    private QueryNrOfDistinctTemperatures instance;

    @BeforeMethod
    public void setup() {
        instance = new QueryNrOfDistinctTemperatures();
    }

    @Test
    public void testSameOutput() {
        WeatherDataSource src = new WeatherDataSource();
        int expected = 18;
        assertEquals(instance.nrOfTempsStream(src),expected);
        assertEquals(instance.nrOfTempsStreamEx(src),expected);
        assertEquals(instance.nrOfTempsJayield(src),expected);
        assertEquals(instance.nrOfTempsJool(src),expected);
        assertEquals(instance.nrOfTempsVavr(src),expected);
        assertEquals(instance.nrOfTempsKotlin(src),expected);
        assertEquals(instance.nrOfTempsKotlinYield(src),expected);
        assertEquals(instance.nrOfTempsEclipse(src),expected);
    }
}
