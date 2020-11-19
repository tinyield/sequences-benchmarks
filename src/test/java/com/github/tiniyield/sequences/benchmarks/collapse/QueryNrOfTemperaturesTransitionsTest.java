package com.github.tiniyield.sequences.benchmarks.collapse;

import com.github.tiniyield.sequences.benchmarks.common.WeatherDataSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class QueryNrOfTemperaturesTransitionsTest {


    private QueryNrOfTemperaturesTransitions instance;

    @BeforeMethod
    public void setup() {
        instance = new QueryNrOfTemperaturesTransitions();
    }

    @Test
    public void testSameOutput() {
        WeatherDataSource src = new WeatherDataSource();
        int expected = 79;
        assertEquals(instance.nrOfTransitionsStream(src),expected);
        assertEquals(instance.nrOfTransitionsStreamEx(src),expected);
        assertEquals(instance.nrOfTransitionsJayield(src),expected);
        assertEquals(instance.nrOfTransitionsJool(src),expected);
        assertEquals(instance.nrOfTransitionsVavr(src),expected);
        assertEquals(instance.nrOfTransitionsKotlin(src),expected);
        assertEquals(instance.nrOfTransitionsKotlinYield(src),expected);
        assertEquals(instance.nrOfTransitionsEclipse(src),expected);
    }

}
