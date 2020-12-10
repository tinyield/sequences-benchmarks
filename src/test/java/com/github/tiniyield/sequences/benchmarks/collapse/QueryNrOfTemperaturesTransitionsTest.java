package com.github.tiniyield.sequences.benchmarks.collapse;

import com.github.tiniyield.sequences.benchmarks.common.WeatherDataSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class QueryNrOfTemperaturesTransitionsTest {


    private final int expected = 28;
    private final WeatherDataSource src = new WeatherDataSource();
    private QueryNrOfTemperaturesTransitions instance;

    @BeforeMethod
    public void setup() {
        instance = new QueryNrOfTemperaturesTransitions();
    }

    @Test
    public void testStream() {
        assertEquals(instance.nrOfTransitionsStream(src), expected);
    }

    @Test
    public void testSek() {
        assertEquals(instance.nrOfTransitionsSek(src), expected);
    }

    @Test
    public void testEclipse() {
        assertEquals(instance.nrOfTransitionsEclipse(src), expected);
    }

    @Test
    public void testKotlinYield() {
        assertEquals(instance.nrOfTransitionsKotlinYield(src), expected);
    }

    @Test
    public void testKotlin() {
        assertEquals(instance.nrOfTransitionsKotlin(src), expected);
    }

    @Test
    public void testVavr() {
        assertEquals(instance.nrOfTransitionsVavr(src), expected);
    }

    @Test
    public void testJool() {
        assertEquals(instance.nrOfTransitionsJool(src), expected);
    }

    @Test
    public void testJayield() {
        assertEquals(instance.nrOfTransitionsJayield(src), expected);
    }

    @Test
    public void testStreamEx() {
        assertEquals(instance.nrOfTransitionsStreamEx(src), expected);
    }

}
