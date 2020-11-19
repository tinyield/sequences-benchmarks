package com.github.tiniyield.sequences.benchmarks.odd.lines;

import com.github.tiniyield.sequences.benchmarks.common.WeatherDataSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class QueryMaxTemperatureTest {

    private QueryMaxTemperature instance;

    @BeforeMethod
    public void setup() {
        instance = new QueryMaxTemperature();
    }

    @Test
    public void testSameOutput() {
        WeatherDataSource src = new WeatherDataSource();
        int expected = 27;
        assertEquals(instance.maxTempStream(src),expected);
        assertEquals(instance.maxTempStreamEx(src),expected);
        assertEquals(instance.maxTempJayield(src),expected);
        assertEquals(instance.maxTempJool(src),expected);
        assertEquals(instance.maxTempVavr(src),expected);
        assertEquals(instance.maxTempKotlin(src),expected);
        assertEquals(instance.maxTempKotlinYield(src),expected);
        assertEquals(instance.maxTempEclipse(src),expected);
    }

}
