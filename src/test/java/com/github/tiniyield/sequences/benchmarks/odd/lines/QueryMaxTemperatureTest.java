package com.github.tiniyield.sequences.benchmarks.odd.lines;

import com.github.tiniyield.sequences.benchmarks.common.WeatherDataSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class QueryMaxTemperatureTest {

    private final int expected = 29;
    private final WeatherDataSource src = new WeatherDataSource();
    private QueryMaxTemperature instance;

    @BeforeMethod
    public void setup() {
        instance = new QueryMaxTemperature();
    }

    @Test
    public void testStream() {
        assertEquals(instance.maxTempStream(src), expected);
    }

    @Test
    public void testSek() {
        assertEquals(instance.maxTempSek(src), expected);
    }

    @Test
    public void testEclipse() {
        assertEquals(instance.maxTempEclipse(src), expected);
    }

    @Test
    public void testKotlinYield() {
        assertEquals(instance.maxTempKotlinYield(src), expected);
    }

    @Test
    public void testKotlin() {
        assertEquals(instance.maxTempKotlin(src), expected);
    }

    @Test
    public void testVavr() {
        assertEquals(instance.maxTempVavr(src), expected);
    }

    @Test
    public void testJool() {
        assertEquals(instance.maxTempJool(src), expected);
    }

    @Test
    public void testJayield() {
        assertEquals(instance.maxTempJayield(src), expected);
    }

    @Test
    public void testStreamEx() {
        assertEquals(instance.maxTempStreamEx(src), expected);
    }

}
