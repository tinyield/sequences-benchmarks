package com.github.tiniyield.sequences.benchmarks.odd.lines;

import com.github.tiniyield.sequences.benchmarks.common.WeatherDataSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class QueryNrOfDistinctTemperaturesTest {

    private final int expected = 18;
    private final WeatherDataSource src = new WeatherDataSource();
    private QueryNrOfDistinctTemperatures instance;

    @BeforeMethod
    public void setup() {
        instance = new QueryNrOfDistinctTemperatures();
    }

    @Test
    public void testStream() {
        assertEquals(instance.nrOfTempsStream(src), expected);
    }

    @Test
    public void testSek() {
        assertEquals(instance.nrOfTempsSek(src), expected);
    }

    @Test
    public void testEclipse() {
        assertEquals(instance.nrOfTempsEclipse(src), expected);
    }

    @Test
    public void testKotlinYield() {
        assertEquals(instance.nrOfTempsKotlinYield(src), expected);
    }

    @Test
    public void testKotlin() {
        assertEquals(instance.nrOfTempsKotlin(src), expected);
    }

    @Test
    public void testVavr() {
        assertEquals(instance.nrOfTempsVavr(src), expected);
    }

    @Test
    public void testJool() {
        assertEquals(instance.nrOfTempsJool(src), expected);
    }

    @Test
    public void testJayield() {
        assertEquals(instance.nrOfTempsJayield(src), expected);
    }

    @Test
    public void testStreamEx() {
        assertEquals(instance.nrOfTempsStreamEx(src), expected);
    }
}
