package com.github.tiniyield.sequences.benchmarks.find;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FindFixedIndexBenchmarkTest {


    private final String expected = "1";
    private FindFixedIndexBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new FindFixedIndexBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.init();
    }

    @Test
    public void testStream() {
        assertThat(instance.stream()).isEqualTo(expected);
    }

    @Test
    public void testStreamEx() {
        assertThat(instance.streamEx()).isEqualTo(expected);
    }

    @Test
    public void testJayield() {
        assertThat(instance.jayield()).isEqualTo(expected);
    }

    @Test
    public void testJool() {
        assertThat(instance.jool()).isEqualTo(expected);
    }

    @Test
    public void testVavr() {
        assertThat(instance.vavr()).isEqualTo(expected);
    }

    @Test
    public void testProtonpack() {
        assertThat(instance.protonpack()).isEqualTo(expected);
    }

    @Test
    public void testGuava() {
        assertThat(instance.guava()).isEqualTo(expected);
    }

    @Test
    public void testZipline() {
        assertThat(instance.zipline()).isEqualTo(expected);
    }

    @Test
    public void testKotlin() {
        assertThat(instance.kotlin()).isEqualTo(expected);
    }

    @Test
    public void testJkotlin() {
        assertThat(instance.jkotlin()).isEqualTo(expected);
    }

    @Test
    public void testEclipse() {
        assertThat(instance.eclipse()).isEqualTo(expected);
    }

    @Test
    public void testSek() {
        assertThat(instance.sek()).isEqualTo(expected);
    }
}
